/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2018 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

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

/**
 Code from org.glassfish/javax.json to parse a string into Json tokens. It's been modified following some recommendations
 that I found in the Javadocs to improve the performance. I did some refactor, but I prefer not to do further
 modifications in this class, at least for the moment.
 open issue in GitHub (#52), to migrate all the modified code from the library to other repo and jar.
 */
final class JsParser implements Closeable
{
    private static final JsBufferPool pool = new JsBufferPool();
    private static final String EXPECTED_START_JSON_OR_VALUE_TOKENS = "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]";
    private static final String EXPECTED_COMMA_TOKEN = "[COMMA]";
    private static final String EXPECTED_COMMA_CLOSE_OBJECT_TOKENS = "[COMMA, CURLYCLOSE]";

    /**
     * An event from {@code MyJsParser}.
     */
    enum Event
    {
        /**
         * String value in a JSON array or object. The position of the parser is
         * after the string value. The method {@link #getString}
         * returns the string value.
         */
        VALUE_STRING(0),

        /**
         * Number value in a JSON array or object. The position of the parser is
         * after the number value. {@code MyJsParser} provides the following
         * methods to access the number value: {@link #getInt},
         * {@link #getLong}, and {@link #getBigDecimal}.
         */
        VALUE_NUMBER(1),

        /**
         * {@code false} value in a JSON array or object. The position of the
         * parser is after the {@code false} value.
         */
        VALUE_FALSE(2),

        /**
         * {@code true} value in a JSON array or object. The position of the
         * parser is after the {@code true} value.
         */
        VALUE_TRUE(3),

        /**
         * {@code null} value in a JSON array or object. The position of the
         * parser is after the {@code null} value.
         */
        VALUE_NULL(4),

        /**
         * Start of a JSON object. The position of the parser is after '{'.
         */
        START_OBJECT(5),

        /**
         * Start of a JSON array. The position of the parser is after '['.
         */
        START_ARRAY(6),

        /**
         * End of a JSON object. The position of the parser is after '}'.
         */
        END_OBJECT(7),
        /**
         * End of a JSON array. The position of the parser is after ']'.
         */
        END_ARRAY(8),
        /**
         * Name in a name/value pair of a JSON object. The position of the parser
         * is after the key name. The method {@link #getString} returns the key
         * name.
         */
        KEY_NAME(9),
        /**
         event associated to the tokens: comma, colon or eof.
         */
        NOTHING(10);

        final int code;

        Event(final int code)
        {
            this.code = code;
        }
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
            switch (currentEvent.code){
                case 9: {
                    token = tokenizer.matchColonToken();
                    break;
                }
                case 5:{
                    token = tokenizer.matchQuoteOrCloseObject();
                    break;
                }
                default: token = tokenizer.nextToken();
            }


            if (token == EOF) return throwUnexpectedEOFException(token);
            if (currentEvent == KEY_NAME) return nextValueOrJsonBeginning(tokenizer.nextToken(),
                                                                          EXPECTED_START_JSON_OR_VALUE_TOKENS
                                                                         );
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
            switch (token.getCode())
            {
                case 11:
                {
                    if (currentEvent == START_ARRAY)
                        throw expectedValueOrJsonBeginning(token,
                                                           EXPECTED_START_JSON_OR_VALUE_TOKENS
                                                          );

                    throw expectedValueOrJsonBeginning(token,
                                                       EXPECTED_COMMA_CLOSE_OBJECT_TOKENS
                                                      );
                }
                case 10:
                {
                    currentContext = stack.pop();
                    return END_ARRAY;
                }
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

    private Event nextValueOrJsonBeginning(final Token token,
                                           String expectedTokens
                                          ) throws MalformedJson
    {
        if (token.isValue()) return token.getEvent();
        switch (token.getCode())
        {
            case 0:
            {
                stack.push(currentContext);
                currentContext = new ObjectContext();
                return START_OBJECT;
            }
            case 1:
            {
                stack.push(currentContext);
                currentContext = new ArrayContext();
                return Event.START_ARRAY;
            }
        }
        throw expectedValueOrJsonBeginning(token,
                                           expectedTokens
                                          );
    }

}