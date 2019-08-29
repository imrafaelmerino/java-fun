package jsonvalues;

import jsonvalues.JsTokenizer.Token;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

import static jsonvalues.JsParser.Event.*;
import static jsonvalues.JsTokenizer.Token.*;

final class JsParser implements Closeable
{
    private static final JsBufferPool pool = new JsBufferPool();
    private final String EXPECTED_START_JSON_OR_VALUE_TOKENS = "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]";
    private final String EXPECTED_COMMA_TOKEN = "[COMMA]";
    private final String EXPECTED_COMMA_CLOSE_OBJECT_TOKENS = "[COMMA, CURLYCLOSE]";

    /**
     * An event from {@code MyJsParser}.
     */
    enum Event
    {
        /**
         * Start of a JSON array. The position of the parser is after '['.
         */
        START_ARRAY,
        /**
         * Start of a JSON object. The position of the parser is after '{'.
         */
        START_OBJECT,
        /**
         * Name in a name/value pair of a JSON object. The position of the parser
         * is after the key name. The method {@link #getString} returns the key
         * name.
         */
        KEY_NAME,
        /**
         * String value in a JSON array or object. The position of the parser is
         * after the string value. The method {@link #getString}
         * returns the string value.
         */
        VALUE_STRING,
        /**
         * Number value in a JSON array or object. The position of the parser is
         * after the number value. {@code MyJsParser} provides the following
         * methods to access the number value: {@link #getInt},
         * {@link #getLong}, and {@link #getBigDecimal}.
         */
        VALUE_NUMBER,
        /**
         * {@code true} value in a JSON array or object. The position of the
         * parser is after the {@code true} value.
         */
        VALUE_TRUE,
        /**
         * {@code false} value in a JSON array or object. The position of the
         * parser is after the {@code false} value.
         */
        VALUE_FALSE,
        /**
         * {@code null} value in a JSON array or object. The position of the
         * parser is after the {@code null} value.
         */
        VALUE_NULL,
        /**
         * End of a JSON object. The position of the parser is after '}'.
         */
        END_OBJECT,
        /**
         * End of a JSON array. The position of the parser is after ']'.
         */
        END_ARRAY,
        /**
         event associated to the tokens: comma, colon or eof.
         */
        NOTHING
    }

    private Context currentContext = new NoneContext();
    private Event currentEvent = NOTHING;
    private final Deque<Context> stack = new ConcurrentLinkedDeque<>();
    private final JsTokenizer tokenizer;

    private boolean isIntegralNumber()
    {
        return tokenizer.isIntegral();
    }

    private int getInt()
    {
        return tokenizer.getInt();
    }

    private boolean isDefinitelyInt()
    {
        return tokenizer.isDefinitelyInt();
    }

    private boolean isDefinitelyLong()
    {
        return tokenizer.isDefinitelyLong();
    }

    private long getLong()
    {
        return tokenizer.getLong();
    }

    private BigDecimal getBigDecimal()
    {
        return tokenizer.getBigDecimal();
    }

    private BigInteger getBigInteger()
    {
        return tokenizer.getBigInteger();
    }

    JsParser(Reader reader)
    {
        tokenizer = new JsTokenizer(Objects.requireNonNull(reader),
                                    pool
        );
    }

    String getString()
    {
        return tokenizer.getString();
    }

    JsStr getJsString()
    {
        return tokenizer.getJsString();
    }

    JsNumber getJsNumber()
    {
        if (isDefinitelyInt())
            return JsInt.of(getInt());
        if (isDefinitelyLong())
            return JsLong.of(getLong());
        if (isIntegralNumber())
            return JsBigInt.of(getBigInteger());
        return JsBigDec.of(getBigDecimal());
    }

    private JsCharLocation getLastCharLocation()
    {
        return tokenizer.getLastCharLocation();
    }

    Event next() throws MalformedJson
    {
        currentEvent = currentContext.getNextEvent();
        return currentEvent;
    }

    @Override
    @SuppressWarnings("squid:S00112") //unchecked exception is fine here, no need to create a dedicated exception, unrecoverable situation
    public void close()
    {
        try
        {
            tokenizer.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("I/O error while closing JSON tokenizer",
                                       e
            );
        }
    }

    private interface Context
    {
        Event getNextEvent() throws MalformedJson;
    }


    private final class NoneContext implements Context
    {
        @Override
        public Event getNextEvent() throws MalformedJson
        {
            return nextValueOrJsonBeginning(tokenizer.nextToken(),
                                            "[CURLYOPEN, SQUAREOPEN]"
                                           );
        }
    }

    private MalformedJson expectedValueOrJsonBeginning(final Token token,
                                                       final String s
                                                      )
    {
        return MalformedJson.invalidToken(token,
                                          getLastCharLocation(),
                                          s
                                         );
    }

    private final class ObjectContext implements Context
    {
        private boolean firstValue = true;

        @Override
        public Event getNextEvent() throws MalformedJson
        {
            Token token;
            if (currentEvent == KEY_NAME) token = tokenizer.matchColonToken();
            else if (currentEvent == START_OBJECT) token = tokenizer.matchQuoteOrCloseObject();
            else token = tokenizer.nextToken();
            if (token == EOF) return throwUnexpectedEOFException(token);
            if (currentEvent == KEY_NAME) return nextValueOrJsonBeginning(tokenizer.nextToken() ,
                                                                          EXPECTED_START_JSON_OR_VALUE_TOKENS);
            if (token == CURLYCLOSE)
            {
                currentContext = stack.pop();
                return END_OBJECT;
            }
            if (firstValue) firstValue = false;
            else if (token != COMMA) throw expectedValueOrJsonBeginning(token,
                                                                        EXPECTED_COMMA_TOKEN
                                                                       );
            else token = tokenizer.nextToken();
            if (token == STRING) return KEY_NAME;
            throw expectedValueOrJsonBeginning(token,
                                               "[STRING]"
                                              );

        }

        private Event throwUnexpectedEOFException(final Token token) throws MalformedJson
        {
            switch (currentEvent)
            {
                case START_OBJECT:
                    throw expectedValueOrJsonBeginning(token,
                                                       "[STRING, CURLYCLOSE]"
                                                      );
                case KEY_NAME:
                    throw expectedValueOrJsonBeginning(token,
                                                       "[COLON]"
                                                      );
                default:
                    throw expectedValueOrJsonBeginning(token,
                                                       EXPECTED_COMMA_CLOSE_OBJECT_TOKENS
                                                      );
            }
        }
    }

    private final class ArrayContext implements Context
    {
        private boolean firstValue = true;

        @Override
        public Event getNextEvent() throws MalformedJson
        {
            Token token = tokenizer.nextToken();
            if (token == EOF)
            {
                if (currentEvent == START_ARRAY)
                    throw expectedValueOrJsonBeginning(token,
                                                       EXPECTED_START_JSON_OR_VALUE_TOKENS
                                                      );

                throw expectedValueOrJsonBeginning(token,
                                                   EXPECTED_COMMA_CLOSE_OBJECT_TOKENS
                                                  );
            }
            if (token == Token.SQUARECLOSE)
            {
                currentContext = stack.pop();
                return END_ARRAY;
            }
            if (firstValue) firstValue = false;
            else if (token != COMMA) throw expectedValueOrJsonBeginning(token,
                                                                        EXPECTED_COMMA_TOKEN
                                                                       );
            else token = tokenizer.nextToken();

            return nextValueOrJsonBeginning(token,
                                            EXPECTED_START_JSON_OR_VALUE_TOKENS
                                           );
        }


    }

    private Event nextValueOrJsonBeginning(final Token token,String expectedTokens) throws MalformedJson
    {
        if (token.isValue()) return token.getEvent();
        if (token == CURLYOPEN)
        {
            stack.push(currentContext);
            currentContext = new ObjectContext();
            return START_OBJECT;
        }
        if (token == SQUAREOPEN)
        {
            stack.push(currentContext);
            currentContext = new ArrayContext();
            return Event.START_ARRAY;
        }
        throw expectedValueOrJsonBeginning(token,
                                          expectedTokens
                                          );
    }

}