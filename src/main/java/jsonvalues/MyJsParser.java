package jsonvalues;
import jsonvalues.MyJsTokenizer.Token;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Stack;

import static jsonvalues.MyJsParser.Event.*;
import static jsonvalues.MyJsTokenizer.Token.*;

class MyJsParser implements Closeable
{
    private static final MyJsBufferPool pool = new MyJsBufferPool();

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
    private final Stack<Context> stack = new Stack<>();
    private final MyJsTokenizer tokenizer;

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

    MyJsParser(Reader reader)
    {
        tokenizer = new MyJsTokenizer(Objects.requireNonNull(reader),
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

    MyJsCharLocation getLocation()
    {
        return tokenizer.getLocation();
    }

    private MyJsCharLocation getLastCharLocation()
    {
        return tokenizer.getLastCharLocation();
    }

    Event next() throws MalformedJson
    {
        return currentEvent = currentContext.getNextEvent();
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


    private abstract static class Context
    {
        Context next;

        abstract Event getNextEvent() throws MalformedJson;
    }

    private final class NoneContext extends Context
    {
        @Override
        Event getNextEvent() throws MalformedJson
        {
            return nextValueOrJsonBeginning(tokenizer.nextToken());
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

    private final class ObjectContext extends Context
    {
        private boolean firstValue = true;

        @Override
        Event getNextEvent() throws MalformedJson
        {
            Token token;
            if (currentEvent == KEY_NAME) token = tokenizer.matchColonToken();
            else if (currentEvent == Event.START_OBJECT) token = tokenizer.matchQuoteOrCloseObject();
            else token = tokenizer.nextToken();
            if (token == EOF) return throwUnexpectedEOFException(token);
            if (currentEvent == KEY_NAME) return nextValueOrJsonBeginning(tokenizer.nextToken());
            if (token == CURLYCLOSE)
            {
                currentContext = stack.pop();
                return Event.END_OBJECT;
            }
            if (firstValue) firstValue = false;
            else if (token != COMMA) throw expectedValueOrJsonBeginning(token,
                                                                        "[COMMA]"
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
                                                       "[COMMA, CURLYCLOSE]"
                                                      );
            }
        }
    }

    private final class ArrayContext extends Context
    {
        private boolean firstValue = true;

        @Override
        Event getNextEvent() throws MalformedJson
        {
            Token token = tokenizer.nextToken();
            if (token == EOF)
            {
                if (currentEvent == START_ARRAY)
                    throw expectedValueOrJsonBeginning(token,
                                                       "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]"
                                                      );

                throw expectedValueOrJsonBeginning(token,
                                                   "[COMMA, CURLYCLOSE]"
                                                  );
            }
            if (token == Token.SQUARECLOSE)
            {
                currentContext = stack.pop();
                return END_ARRAY;
            }
            if (firstValue) firstValue = false;
            else if (token != COMMA) throw expectedValueOrJsonBeginning(token,
                                                                        "[COMMA]"
                                                                       );
            else token = tokenizer.nextToken();

            return nextValueOrJsonBeginning(token);
        }


    }

    private Event nextValueOrJsonBeginning(final Token token) throws MalformedJson
    {
        if (token.isValue()) return token.getEvent();
        if (token == CURLYOPEN)
        {
            stack.push(currentContext);
            currentContext = new ObjectContext();
            return Event.START_OBJECT;
        }
        if (token == SQUAREOPEN)
        {
            stack.push(currentContext);
            currentContext = new ArrayContext();
            return Event.START_ARRAY;
        }
        throw expectedValueOrJsonBeginning(token,
                                           "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]"
                                          );
    }

}