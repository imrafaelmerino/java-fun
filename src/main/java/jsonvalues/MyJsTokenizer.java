package jsonvalues;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static jsonvalues.MyJsParser.Event.*;
import static jsonvalues.MyJsTokenizer.Token.*;


final class MyJsTokenizer implements Closeable
{
    /**
     Table to look up hex ch -> value (for e.g HEX['F'] = 15, HEX['5'] = 5)
     */
    private static final int[] HEX = new int[128];

    static
    {
        Arrays.fill(HEX,
                    -1
                   );
        for (int i = '0'; i <= '9'; i++)
        {
            HEX[i] = i - '0';
        }
        for (int i = 'A'; i <= 'F'; i++)
        {
            HEX[i] = 10 + i - 'A';
        }
        for (int i = 'a'; i <= 'f'; i++)
        {
            HEX[i] = 10 + i - 'a';
        }
    }

    private static final int HEX_LENGTH = HEX.length;
    private final MyJsBufferPool bufferPool;
    private final Reader reader;

    /**
     Internal buffer that is used for parsing. It is also used
     for storing current string and number value token
     */
    private char[] buf;

    /**
     Indexes in buffer
     XXXssssssssssssXXXXXXXXXXXXXXXXXXXXXXrrrrrrrrrrrrrrXXXXXX
     ^           ^                     ^             ^
     |           |                     |             |
     storeBegin  storeEnd            readBegin      readEnd
     */
    private int readBegin;
    private int readEnd;
    private int storeBegin;
    private int storeEnd;
    /**
     line number of the current pointer of parsing char
     */
    private long lineNo = 1;
    /**
     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     ^
     |
     bufferOffset
     offset of the last \r\n or \n. will be used to calculate column number
     of a token or an error. This may be outside of the buffer.
     */
    private long lastLineOffset = 0;

    /**
     offset in the stream for the start of the buffer, will be used in
     calculating Location's stream offset, column no.
     */
    private long bufferOffset = 0;

    private boolean minus;
    private boolean fracOrExp;

    JsStr getJsString()
    {

        return JsStr.of(getString());

    }

    enum Token
    {
        CURLYOPEN(START_OBJECT,
                  false
        ),
        SQUAREOPEN(START_ARRAY,
                   false
        ),
        COLON(false),
        COMMA(false),
        STRING(VALUE_STRING,
               true
        ),
        NUMBER(VALUE_NUMBER,
               true
        ),
        TRUE(VALUE_TRUE,
             true
        ),
        FALSE(VALUE_FALSE,
              true
        ),
        NULL(VALUE_NULL,
             true
        ),
        CURLYCLOSE(END_OBJECT,
                   false
        ),
        SQUARECLOSE(END_ARRAY,
                    false
        ),
        EOF(false);
        private MyJsParser.Event event;
        private final boolean value;

        Token(MyJsParser.Event event,
              boolean value
             )
        {
            this.event = event;
            this.value = value;
        }

        Token(boolean value)
        {
            this.value = value;
            this.event = NOTHING;
        }

        MyJsParser.Event getEvent()
        {
            return event;
        }

        boolean isValue()
        {
            return value;
        }
    }

    MyJsTokenizer(final Reader reader,
                  final MyJsBufferPool bufferPool
                 )
    {
        this.reader = reader;
        this.bufferPool = bufferPool;
        buf = bufferPool.take();
    }

    private void readString() throws MalformedJson
    {
        // when inPlace is true, no need to copy chars
        boolean inPlace = true;
        storeBegin = storeEnd = readBegin;
        do
        {
            // Write unescaped char block within the current buffer
            if (inPlace)
            {
                int ch;
                while (readBegin < readEnd && ((ch = buf[readBegin]) >= 0x20) && ch != '\\')
                {
                    if (ch == '"')
                    {
                        storeEnd = readBegin++;
                        return;
                    }
                    readBegin++;
                }
                storeEnd = readBegin;
            }

            // string may be crossing buffer boundaries and may contain
            // escaped characters.
            int ch = read();
            if (ch >= 0x20 && ch != 0x22 && ch != 0x5c)
            {
                if (!inPlace) buf[storeEnd] = (char) ch;
                storeEnd++;
                continue;
            }
            switch (ch)
            {
                case '\\':
                    inPlace = false;        // Now onwards need to copy chars
                    unescape();
                    break;
                case '"':
                    return;
                default:
                    throw MalformedJson.unexpectedChar(ch,
                                                       getLastCharLocation()
                                                      );
            }
        } while (true);
    }

    private void unescape() throws MalformedJson
    {
        int ch = read();
        switch (ch)
        {
            case 'b':
                buf[storeEnd++] = '\b';
                break;
            case 't':
                buf[storeEnd++] = '\t';
                break;
            case 'n':
                buf[storeEnd++] = '\n';
                break;
            case 'f':
                buf[storeEnd++] = '\f';
                break;
            case 'r':
                buf[storeEnd++] = '\r';
                break;
            case '"':
            case '\\':
            case '/':
                buf[storeEnd++] = (char) ch;
                break;
            case 'u':
            {
                readUnicodeChars();
                break;
            }
            default:
                throw MalformedJson.unexpectedChar(ch,
                                                   getLastCharLocation()
                                                  );
        }
    }

    private void readUnicodeChars() throws MalformedJson
    {
        int unicode = 0;
        for (int i = 0; i < 4; i++)
        {
            int ch3 = read();
            int digit = (ch3 >= 0 && ch3 < HEX_LENGTH) ? HEX[ch3] : -1;
            if (digit < 0)
                throw MalformedJson.unexpectedChar(ch3,
                                                   getLastCharLocation()
                                                  );
            unicode = (unicode << 4) | digit;
        }
        buf[storeEnd++] = (char) unicode;
    }


    /**
     Reads a number char. If the char is within the buffer, directly
     reads from the buffer. Otherwise, uses read() which takes care
     of resizing, filling up the buf, adjusting the pointers
     @return a number char
     */
    private int readNumberChar()
    {
        if (readBegin < readEnd) return buf[readBegin++];
        storeEnd = readBegin;
        return read();
    }

    private void readNumber(int ch) throws MalformedJson
    {
        storeBegin = storeEnd = readBegin - 1;
        if (ch == '-') ch = readNegative();
        if (ch == '0') ch = readNumberChar();
        else ch = readAllNumberChar();
        if (ch == '.') ch = readFractional();
        if (ch == 'e' || ch == 'E') ch = readExponential();
        if (ch != -1)
        {
            readBegin--;
            storeEnd = readBegin;
        }
    }

    private int readAllNumberChar()
    {
        int ch;
        do
        {
            ch = readNumberChar();
        } while (ch >= '0' && ch <= '9');
        return ch;
    }

    private int readNegative() throws MalformedJson
    {
        this.minus = true;
        final int ch = readNumberChar();
        if (ch < '0' || ch > '9')
            throw MalformedJson.unexpectedChar(ch,
                                               getLastCharLocation()
                                              );
        return ch;
    }

    private int readExponential() throws MalformedJson
    {
        this.fracOrExp = true;
        int ch = readNumberChar();
        if (ch == '+' || ch == '-') ch = readNumberChar();
        int count;
        for (count = 0; ch >= '0' && ch <= '9'; count++)
        {
            ch = readNumberChar();
        }
        if (count == 0)
            throw MalformedJson.unexpectedChar(ch,
                                               getLastCharLocation()
                                              );
        return ch;
    }

    private int readFractional() throws MalformedJson
    {
        this.fracOrExp = true;
        int count = 0;
        int ch;
        do
        {
            ch = readNumberChar();
            count++;
        } while (ch >= '0' && ch <= '9');
        if (count == 1)
            throw MalformedJson.unexpectedChar(ch,
                                               getLastCharLocation()
                                              );
        return ch;
    }

    private void readTrue() throws MalformedJson
    {
        int ch1 = read();
        if (ch1 != 'r')
            throw MalformedJson.unexpectedChar(ch1,
                                               getLastCharLocation()
                                              );
        int ch2 = read();
        if (ch2 != 'u')
            throw MalformedJson.unexpectedChar(ch2,
                                               getLastCharLocation()
                                              );
        int ch3 = read();
        if (ch3 != 'e')
            throw MalformedJson.unexpectedChar(ch3,
                                               getLastCharLocation()
                                              );

    }

    private void readFalse() throws MalformedJson
    {
        int ch1 = read();
        if (ch1 != 'a')
            throw MalformedJson.expectedChar(ch1,
                                             getLastCharLocation(),
                                             'a'
                                            );
        int ch2 = read();
        if (ch2 != 'l')
            throw MalformedJson.expectedChar(ch2,
                                             getLastCharLocation(),
                                             'l'
                                            );
        int ch3 = read();
        if (ch3 != 's')
            throw MalformedJson.expectedChar(ch3,
                                             getLastCharLocation(),
                                             's'
                                            );
        int ch4 = read();
        if (ch4 != 'e')
            throw MalformedJson.expectedChar(ch4,
                                             getLastCharLocation(),
                                             'e'
                                            );
    }

    private void readNull() throws MalformedJson
    {
        int ch1 = read();
        if (ch1 != 'u')
            throw MalformedJson.expectedChar(ch1,
                                             getLastCharLocation(),
                                             'u'
                                            );
        int ch2 = read();
        if (ch2 != 'l')
            throw MalformedJson.expectedChar(ch2,
                                             getLastCharLocation(),
                                             'l'
                                            );
        int ch3 = read();
        if (ch3 != 'l')
            throw MalformedJson.expectedChar(ch3,
                                             getLastCharLocation(),
                                             'l'
                                            );
    }

    Token nextToken() throws MalformedJson
    {
        reset();
        int ch = read();
        ch = skipWhitespaces(ch);
        switch (ch)
        {
            case '"':
                readString();
                return STRING;
            case '{':
                return CURLYOPEN;
            case '[':
                return SQUAREOPEN;
            case ',':
                return COMMA;
            case 't':
                readTrue();
                return TRUE;
            case 'f':
                readFalse();
                return FALSE;
            case 'n':
                readNull();
                return NULL;
            case ']':
                return SQUARECLOSE;
            case '}':
                return CURLYCLOSE;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '-':
                readNumber(ch);
                return NUMBER;
            case -1:
                return EOF;
            default:
                throw MalformedJson.unexpectedChar(ch,
                                                   getLastCharLocation()
                                                  );
        }
    }

    Token matchColonToken() throws MalformedJson
    {
        reset();
        int ch = read();
        ch = skipWhitespaces(ch);
        if (ch != ':') throw MalformedJson.expectedChar(ch,
                                                        getLastCharLocation(),
                                                        ':'
                                                       );
        return Token.COLON;
    }

    Token matchQuoteOrCloseObject() throws MalformedJson
    {
        reset();
        int ch = read();
        ch = skipWhitespaces(ch);
        if (ch == '"')
        {
            readString();
            return STRING;
        }
        if (ch == '}') return CURLYCLOSE;
        throw MalformedJson.expectedChar(ch,
                                         getLastCharLocation(),
                                         '"',
                                         '}'
                                        );
    }

    private int skipWhitespaces(int ch)
    {
        while (ch == 0x20 || ch == 0x09 || ch == 0x0a || ch == 0x0d)
        {
            if (ch == '\r')
            {
                ++lineNo;
                ch = read();
                if (ch == '\n')
                {
                    lastLineOffset = bufferOffset + readBegin;
                } else
                {
                    lastLineOffset = bufferOffset + readBegin - 1;
                    continue;
                }
            } else if (ch == '\n')
            {
                ++lineNo;
                lastLineOffset = bufferOffset + readBegin;
            }
            ch = read();
        }
        return ch;
    }

    // Gives the location of the last char. Used for
    // ParsingException.getLocation
    MyJsCharLocation getLastCharLocation()
    {
        // Already read the char, so subtracting -1
        return new MyJsCharLocation(lineNo,
                                    bufferOffset + readBegin - lastLineOffset,
                                    bufferOffset + readBegin - 1
        );
    }

    // Gives the parser location. Used for MyJsParser.getLocation
    MyJsCharLocation getLocation()
    {
        return new MyJsCharLocation(lineNo,
                                    bufferOffset + readBegin - lastLineOffset + 1,
                                    bufferOffset + readBegin
        );
    }

    @SuppressWarnings("squid:S00112") //unchecked exception is fine here, no need to create a dedicated exception, unrecoverable situation
    private int read()
    {
        try
        {
            if (readBegin == readEnd)
            {
                int len = fillBuf();
                if (len == -1) return -1;
                assert len != 0;
                readBegin = storeEnd;
                readEnd = readBegin + len;
            }
            return buf[readBegin++];
        }
        catch (IOException ioe)
        {
            throw new RuntimeException("I/O error while parsing JSON",
                                       ioe
            );
        }
    }

    private int fillBuf() throws IOException
    {
        if (storeEnd != 0)
        {
            int storeLen = storeEnd - storeBegin;
            if (storeLen > 0)
            {
                // there is some store data
                if (storeLen == buf.length)
                {
                    // buffer is full, double the capacity
                    char[] doubleBuf = Arrays.copyOf(buf,
                                                     2 * buf.length
                                                    );
                    bufferPool.recycle(buf);
                    buf = doubleBuf;
                } else
                {
                    System.arraycopy(buf,
                                     storeBegin,
                                     buf,
                                     0,
                                     storeLen
                                    );
                    storeEnd = storeLen;
                    storeBegin = 0;
                    bufferOffset += readBegin - storeEnd;
                }
            } else
            {
                storeBegin = storeEnd = 0;
                bufferOffset += readBegin;
            }
        } else
        {
            bufferOffset += readBegin;
        }
        return reader.read(buf,
                           storeEnd,
                           buf.length - storeEnd
                          );
    }

    private void reset()
    {
        if (storeEnd != 0)
        {
            storeBegin = 0;
            storeEnd = 0;
            minus = false;
            fracOrExp = false;
        }
    }

    String getString()
    {
        return new String(buf,
                          storeBegin,
                          storeEnd - storeBegin
        );
    }

    BigDecimal getBigDecimal()
    {

        return new BigDecimal(buf,
                              storeBegin,
                              storeEnd - storeBegin
        );

    }

    BigInteger getBigInteger()
    {
        return getBigDecimal().toBigIntegerExact();

    }

    int getInt()
    {
        // no need to create BigDecimal for common integer values (1-9 digits)
        int storeLen = storeEnd - storeBegin;
        int num = 0;
        int i = minus ? 1 : 0;
        for (; i < storeLen; i++)
        {
            final char c = buf[storeBegin + i];
            num = num * 10 + (c - '0');
        }
        return minus ? -num : num;

    }

    long getLong()
    {
        // no need to create BigDecimal for common integer values (1-18 digits)
        int storeLen = storeEnd - storeBegin;
        long num = 0;
        int i = minus ? 1 : 0;
        for (; i < storeLen; i++)
        {
            num = num * 10 + (buf[storeBegin + i] - '0');
        }
        return minus ? -num : num;
    }


    /**
     returns true for common integer values (1-9 digits).
     So there are cases it will return false even though the number is int
     @return true for common integer values (1-9 digits).
     */
    boolean isDefinitelyInt()
    {
        int storeLen = storeEnd - storeBegin;
        return !fracOrExp && (storeLen <= 9 || (minus && storeLen <= 10));
    }

    /**
     returns true for common long values (1-18 digits).
     So there are cases it will return false even though the number is long
     @return true for common long values (1-18 digits)
     */
    boolean isDefinitelyLong()
    {
        int storeLen = storeEnd - storeBegin;
        return !fracOrExp && (storeLen <= 18 || (minus && storeLen <= 19));
    }

    boolean isIntegral()
    {
        return !fracOrExp || getBigDecimal().scale() == 0;
    }

    @Override
    public void close() throws IOException
    {
        reader.close();
        bufferPool.recycle(buf);
    }
}
