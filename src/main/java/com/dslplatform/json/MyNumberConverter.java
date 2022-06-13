package com.dslplatform.json;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * Modified class from dsl-json library.
 * deserializeInt, deserializeLong and so long, convert string numbers into numbers:
 * "123" -> 123
 * which I consider it's a mistake. The change consists of throwing an error
 */

@SuppressWarnings("rawtypes")
abstract class MyNumberConverter {

    private final static int[] DIGITS = new int[1000];
    @SuppressWarnings("FloatingPointLiteralPrecision")

    private static final byte MINUS = '-';
    private static final byte[] MIN_INT = "-2147483648".getBytes(StandardCharsets.UTF_8);
    private static final byte[] MIN_LONG = "-9223372036854775808".getBytes(StandardCharsets.UTF_8);
    private static final BigDecimal BD_MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
    private static final BigDecimal BD_MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);

    static {
        for (int i = 0; i < DIGITS.length; i++) {
            DIGITS[i] = (i < 10 ?
                         (2 << 24) :
                         i < 100 ?
                         (1 << 24) :
                         0)
                    + (((i / 100) + '0') << 16)
                    + ((((i / 10) % 10) + '0') << 8)
                    + i % 10 + '0';
        }
    }

    private MyNumberConverter() {
    }

    static void numberException(final JsonReader reader,
                                final int start,
                                final int end,
                                String message) throws JsParserException {
        final int len = end - start;
        if (len > reader.maxNumberDigits) {
            throw new JsParserException(ParserErrors.TOO_MANY_DIGITS,reader.getCurrentIndex());
        }
        throw new JsParserException(message,reader.getCurrentIndex());

    }



    private static BigDecimal parseNumberGeneric(final char[] buf,
                                                 final int len,
                                                 final JsonReader reader) throws JsParserException {
        int end = len;
        while (end > 0 && Character.isWhitespace(buf[end - 1])) {
            end--;
        }
        if (end > reader.maxNumberDigits)
            throw new JsParserException(ParserErrors.TOO_MANY_DIGITS,reader.getCurrentIndex());

        final int offset = buf[0] == '-' ?
                           1 :
                           0;
        if (buf[offset] == '0' && end > offset + 1 && buf[offset + 1] >= '0' && buf[offset + 1] <= '9')
            throw new JsParserException(ParserErrors.LEADING_ZERO,reader.getCurrentIndex());

        try {
            return new BigDecimal(buf,
                                  0,
                                  end
            );
        } catch (NumberFormatException nfe) {
            throw new JsParserException(nfe,reader.getCurrentIndex());
        }
    }

    private static MyNumberConverter.NumberInfo readLongNumber(final JsonReader reader,
                                                               final int start) throws JsParserException,IOException {
        int len = reader.length() - start;
        char[] result = reader.prepareBuffer(start,
                                             len
        );
        while (reader.length() == reader.getCurrentIndex()) {
            if (reader.isEndOfStream()) break;
            reader.scanNumber(); // peek, do not read
            int end = reader.getCurrentIndex();
            int oldLen = len;
            len += end;
            if (len > reader.maxNumberDigits) {
                throw new JsParserException(ParserErrors.TOO_MANY_DIGITS,reader.getCurrentIndex());
            }
            char[] tmp = result;
            result = new char[len];
            System.arraycopy(tmp,
                             0,
                             result,
                             0,
                             oldLen
            );
            System.arraycopy(reader.prepareBuffer(0,
                                                  end
                             ),
                             0,
                             result,
                             oldLen,
                             end
            );
        }
        return new MyNumberConverter.NumberInfo(result,
                                                len
        );
    }


    public static void serialize(final int value,
                                 final JsonWriter sw) {
        final byte[] buf = sw.ensureCapacity(11);
        final int position = sw.size();
        int current = serialize(buf,
                                position,
                                value
        );
        sw.advance(current - position);
    }

    private static int serialize(final byte[] buf,
                                 int pos,
                                 final int value) {
        int i;
        if (value < 0) {
            if (value == Integer.MIN_VALUE) {
                for (int x = 0; x < MIN_INT.length; x++) {
                    buf[pos + x] = MIN_INT[x];
                }
                return pos + MIN_INT.length;
            }
            i = -value;
            buf[pos++] = MINUS;
        } else {
            i = value;
        }
        final int q1 = i / 1000;
        if (q1 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[i],
                                 pos
            );
            return pos;
        }
        final int r1 = i - q1 * 1000;
        final int q2 = q1 / 1000;
        if (q2 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[q1];
            int off = writeFirstBuf(buf,
                                    v2,
                                    pos
            );
            writeBuf(buf,
                     v1,
                     pos + off
            );
            return pos + 3 + off;
        }
        final int r2 = q1 - q2 * 1000;
        final int q3 = q2 / 1000;
        final int v1 = DIGITS[r1];
        final int v2 = DIGITS[r2];
        if (q3 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[q2],
                                 pos
            );
        } else {
            final int r3 = q2 - q3 * 1000;
            buf[pos++] = (byte) (q3 + '0');
            writeBuf(buf,
                     DIGITS[r3],
                     pos
            );
            pos += 3;
        }
        writeBuf(buf,
                 v2,
                 pos
        );
        writeBuf(buf,
                 v1,
                 pos + 3
        );
        return pos + 6;
    }

    public static int deserializeInt(final JsonReader reader) throws ParsingException {
        final int start = reader.scanNumber();
        final int end = reader.getCurrentIndex();
        final byte[] buf = reader.buffer;
        final byte ch = buf[start];
        if (ch == '-') {
            if (end > start + 2 && buf[start + 1] == '0' && buf[start + 2] >= '0' && buf[start + 2] <= '9') {
                numberException(reader,
                                start,
                                end,
                                ParserErrors.LEADING_ZERO
                );
            }
            return parseNegativeInt(buf,
                                    reader,
                                    start,
                                    end
            );
        } else {
            if (ch == '0' && end > start + 1 && buf[start + 1] >= '0' && buf[start + 1] <= '9') {
                numberException(reader,
                                start,
                                end,
                                ParserErrors.LEADING_ZERO
                );
            }
            return parsePositiveInt(buf,
                                    reader,
                                    start,
                                    end,
                                    0
            );
        }
    }

    private static int parsePositiveInt(final byte[] buf,
                                        final JsonReader reader,
                                        final int start,
                                        final int end,
                                        final int offset) throws ParsingException {
        int value = 0;
        int i = start + offset;
        if (i == end) numberException(reader,
                                      start,
                                      end,
                                      ParserErrors.DIGIT_NOT_FOUND
        );
        for (; i < end; i++) {
            final int ind = buf[i] - 48;
            if (ind < 0 || ind > 9) {
                if (i > start + offset && reader.allWhitespace(i,
                                                               end
                )) return value;
                else if (i == end - 1 && buf[i] == '.') numberException(reader,
                                                                        start,
                                                                        end,
                                                                        ParserErrors.NUMBER_ENDS_DOT
                );
                final BigDecimal v = parseNumberGeneric(reader.prepareBuffer(start,
                                                                             end - start
                                                        ),
                                                        end - start,
                                                        reader
                );
                if (v.scale() > 0) numberException(reader,
                                                   start,
                                                   end,
                                                   ParserErrors.EXPECTING_INT_DECIMAL_FOUND
                );
                return v.intValue();

            }
            value = (value << 3) + (value << 1) + ind;
            if (value < 0) {
                numberException(reader,
                                start,
                                end,
                                ParserErrors.INTEGER_OVERFLOW
                );
            }
        }
        return value;
    }

    private static int parseNegativeInt(final byte[] buf,
                                        final JsonReader reader,
                                        final int start,
                                        final int end) throws ParsingException {
        int value = 0;
        int i = start + 1;
        if (i == end) numberException(reader,
                                      start,
                                      end,
                                      ParserErrors.DIGIT_NOT_FOUND
        );
        for (; i < end; i++) {
            final int ind = buf[i] - 48;
            if (ind < 0 || ind > 9) {
                if (i > start + 1 && reader.allWhitespace(i,
                                                          end
                )) return value;
                else if (i == end - 1 && buf[i] == '.') numberException(reader,
                                                                        start,
                                                                        end,
                                                                        ParserErrors.NUMBER_ENDS_DOT
                );
                final BigDecimal v = parseNumberGeneric(reader.prepareBuffer(start,
                                                                             end - start
                                                        ),
                                                        end - start,
                                                        reader
                );
                if (v.scale() > 0) numberException(reader,
                                                   start,
                                                   end,
                                                   ParserErrors.EXPECTING_INT_DECIMAL_FOUND
                );
                return v.intValue();
            }
            value = (value << 3) + (value << 1) - ind;
            if (value > 0) {
                numberException(reader,
                                start,
                                end,
                                ParserErrors.INTEGER_OVERFLOW
                );
            }
        }
        return value;
    }

    private static int writeFirstBuf(final byte[] buf,
                                     final int v,
                                     int pos) {
        final int start = v >> 24;
        if (start == 0) {
            buf[pos++] = (byte) (v >> 16);
            buf[pos++] = (byte) (v >> 8);
        } else if (start == 1) {
            buf[pos++] = (byte) (v >> 8);
        }
        buf[pos] = (byte) v;
        return 3 - start;
    }

    private static void writeBuf(final byte[] buf,
                                 final int v,
                                 int pos) {
        buf[pos] = (byte) (v >> 16);
        buf[pos + 1] = (byte) (v >> 8);
        buf[pos + 2] = (byte) v;
    }

    public static void serialize(final long value,
                                 final JsonWriter sw) {
        final byte[] buf = sw.ensureCapacity(21);
        final int position = sw.size();
        int current = serialize(buf,
                                position,
                                value
        );
        sw.advance(current - position);
    }

    private static int serialize(final byte[] buf,
                                 int pos,
                                 final long value) {
        long i;
        if (value < 0) {
            if (value == Long.MIN_VALUE) {
                for (int x = 0; x < MIN_LONG.length; x++) {
                    buf[pos + x] = MIN_LONG[x];
                }
                return pos + MIN_LONG.length;
            }
            i = -value;
            buf[pos++] = MINUS;
        } else {
            i = value;
        }
        final long q1 = i / 1000;
        if (q1 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[(int) i],
                                 pos
            );
            return pos;
        }
        final int r1 = (int) (i - q1 * 1000);
        final long q2 = q1 / 1000;
        if (q2 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[(int) q1];
            int off = writeFirstBuf(buf,
                                    v2,
                                    pos
            );
            writeBuf(buf,
                     v1,
                     pos + off
            );
            return pos + 3 + off;
        }
        final int r2 = (int) (q1 - q2 * 1000);
        final long q3 = q2 / 1000;
        if (q3 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[r2];
            final int v3 = DIGITS[(int) q2];
            pos += writeFirstBuf(buf,
                                 v3,
                                 pos
            );
            writeBuf(buf,
                     v2,
                     pos
            );
            writeBuf(buf,
                     v1,
                     pos + 3
            );
            return pos + 6;
        }
        final int r3 = (int) (q2 - q3 * 1000);
        final int q4 = (int) (q3 / 1000);
        if (q4 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[r2];
            final int v3 = DIGITS[r3];
            final int v4 = DIGITS[(int) q3];
            pos += writeFirstBuf(buf,
                                 v4,
                                 pos
            );
            writeBuf(buf,
                     v3,
                     pos
            );
            writeBuf(buf,
                     v2,
                     pos + 3
            );
            writeBuf(buf,
                     v1,
                     pos + 6
            );
            return pos + 9;
        }
        final int r4 = (int) (q3 - q4 * 1000L);
        final int q5 = q4 / 1000;
        if (q5 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[r2];
            final int v3 = DIGITS[r3];
            final int v4 = DIGITS[r4];
            final int v5 = DIGITS[q4];
            pos += writeFirstBuf(buf,
                                 v5,
                                 pos
            );
            writeBuf(buf,
                     v4,
                     pos
            );
            writeBuf(buf,
                     v3,
                     pos + 3
            );
            writeBuf(buf,
                     v2,
                     pos + 6
            );
            writeBuf(buf,
                     v1,
                     pos + 9
            );
            return pos + 12;
        }
        final int r5 = q4 - q5 * 1000;
        final int q6 = q5 / 1000;
        final int v1 = DIGITS[r1];
        final int v2 = DIGITS[r2];
        final int v3 = DIGITS[r3];
        final int v4 = DIGITS[r4];
        final int v5 = DIGITS[r5];
        if (q6 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[q5],
                                 pos
            );
        } else {
            final int r6 = q5 - q6 * 1000;
            buf[pos++] = (byte) (q6 + '0');
            writeBuf(buf,
                     DIGITS[r6],
                     pos
            );
            pos += 3;
        }
        writeBuf(buf,
                 v5,
                 pos
        );
        writeBuf(buf,
                 v4,
                 pos + 3
        );
        writeBuf(buf,
                 v3,
                 pos + 6
        );
        writeBuf(buf,
                 v2,
                 pos + 9
        );
        writeBuf(buf,
                 v1,
                 pos + 12
        );
        return pos + 15;
    }

    public static long deserializeLong(final JsonReader reader) throws IOException {
        final int start = reader.scanNumber();
        final int end = reader.getCurrentIndex();
        final byte[] buf = reader.buffer;
        final byte ch = buf[start];
        int i = start;
        long value = 0;
        if (ch == '-') {
            i = start + 1;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          ParserErrors.DIGIT_NOT_FOUND
            );
            final boolean leadingZero = buf[i] == 48;
            for (; i < end; i++) {
                final int ind = buf[i] - 48;
                if (ind < 0 || ind > 9) {
                    if (leadingZero && i > start + 2) {
                        numberException(reader,
                                        start,
                                        end,
                                        ParserErrors.LEADING_ZERO
                        );
                    }
                    if (i > start + 1 && reader.allWhitespace(i,
                                                              end
                    )) return value;
                    return parseLongGeneric(reader,
                                            start,
                                            end
                    );
                }
                value = (value << 3) + (value << 1) - ind;
                if (value > 0) {
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.LONG_OVERFLOW
                    );
                }
            }
            if (leadingZero && i > start + 2) {
                numberException(reader,
                                start,
                                end,
                                ParserErrors.LEADING_ZERO
                );
            }
            return value;
        }
        if (i == end) numberException(reader,
                                      start,
                                      end,
                                      ParserErrors.DIGIT_NOT_FOUND
        );
        final boolean leadingZero = buf[i] == 48;
        for (; i < end; i++) {
            final int ind = buf[i] - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 1) {
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.LEADING_ZERO
                    );
                }
                if (ch == '+' && i > start + 1 && reader.allWhitespace(i,
                                                                       end
                )) return value;
                else if (ch != '+' && i > start && reader.allWhitespace(i,
                                                                        end
                )) return value;
                return parseLongGeneric(reader,
                                        start,
                                        end
                );
            }
            value = (value << 3) + (value << 1) + ind;
            if (value < 0) {
                numberException(reader,
                                start,
                                end,
                                ParserErrors.LONG_OVERFLOW
                );
            }
        }
        if (leadingZero && i > start + 1) {
            numberException(reader,
                            start,
                            end,
                            ParserErrors.LEADING_ZERO
            );
        }
        return value;
    }

    private static long parseLongGeneric(final JsonReader reader,
                                         final int start,
                                         final int end) throws IOException {
        final int len = end - start;
        final char[] buf = reader.prepareBuffer(start,
                                                len
        );
        if (len > 0 && buf[len - 1] == '.') numberException(reader,
                                                            start,
                                                            end,
                                                            ParserErrors.NUMBER_ENDS_DOT
        );
        final BigDecimal v = parseNumberGeneric(buf,
                                                len,
                                                reader
        );
        if (v.scale() > 0) numberException(reader,
                                           start,
                                           end,
                                           ParserErrors.EXPECTING_LONG_INSTEAD_OF_DECIMAL
        );
        return v.longValue();
    }

    public static void serialize(final BigDecimal value,
                                 final JsonWriter sw) {
        sw.writeAscii(value.toString());
    }

    public static BigDecimal deserializeDecimal(final JsonReader reader) throws IOException {
        final int start = reader.scanNumber();
        int end = reader.getCurrentIndex();
        if (end == reader.length()) {
            MyNumberConverter.NumberInfo info = readLongNumber(reader,
                                                               start
            );
            return parseNumberGeneric(info.buffer,
                                      info.length,
                                      reader
            );
        }
        int len = end - start;
        if (len > 18) {
            return parseNumberGeneric(reader.prepareBuffer(start,
                                                           len
                                      ),
                                      len,
                                      reader
            );
        }
        final byte[] buf = reader.buffer;
        final byte ch = buf[start];
        if (ch == '-') {
            return parseNegativeDecimal(buf,
                                        reader,
                                        start,
                                        end
            );
        }
        return parsePositiveDecimal(buf,
                                    reader,
                                    start,
                                    end
        );
    }

    private static BigDecimal parsePositiveDecimal(final byte[] buf,
                                                   final JsonReader reader,
                                                   final int start,
                                                   final int end) throws IOException {
        long value = 0;
        byte ch = ' ';
        int i = start;
        final boolean leadingZero = buf[start] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 1) {
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.LEADING_ZERO
                    );
                }
                if (i > start && reader.allWhitespace(i,
                                                      end
                )) return BigDecimal.valueOf(value);
                numberException(reader,
                                start,
                                end,
                                ParserErrors.UNKNOWN_DIGIT
                );
            }
            value = (value << 3) + (value << 1) + ind;
        }
        if (i == start) numberException(reader,
                                        start,
                                        end,
                                        ParserErrors.DIGIT_NOT_FOUND
        );
        else if (leadingZero && ch != '.' && i > start + 1) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            ParserErrors.LEADING_ZERO
        );
        else if (i == end) return BigDecimal.valueOf(value);
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          ParserErrors.NUMBER_ENDS_DOT
            );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                    )) return BigDecimal.valueOf(value,
                                                 i - dp
                    );
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.UNKNOWN_DIGIT
                    );
                }
                value = (value << 3) + (value << 1) + ind;
            }
            if (i == end) return BigDecimal.valueOf(value,
                                                    end - dp
            );
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                    );
                } else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                    );
                } else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                    );
                }
                return BigDecimal.valueOf(value,
                                          ep - dp - exp
                );
            }
            return BigDecimal.valueOf(value,
                                      end - dp
            );
        } else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                );
            } else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                );
            } else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                );
            }
            return BigDecimal.valueOf(value,
                                      -exp
            );
        }
        return BigDecimal.valueOf(value);
    }

    private static BigDecimal parseNegativeDecimal(final byte[] buf,
                                                   final JsonReader reader,
                                                   final int start,
                                                   final int end) throws IOException {
        long value = 0;
        byte ch = ' ';
        int i = start + 1;
        final boolean leadingZero = buf[start + 1] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 2) {
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.LEADING_ZERO
                    );
                }
                if (i > start + 1 && reader.allWhitespace(i,
                                                          end
                )) return BigDecimal.valueOf(value);
                numberException(reader,
                                start,
                                end,
                                ParserErrors.UNKNOWN_DIGIT
                );
            }
            value = (value << 3) + (value << 1) - ind;
        }
        if (i == start + 1) numberException(reader,
                                            start,
                                            end,
                                            ParserErrors.DIGIT_NOT_FOUND
        );
        else if (leadingZero && ch != '.' && i > start + 2) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            ParserErrors.LEADING_ZERO
        );
        else if (i == end) return BigDecimal.valueOf(value);
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          ParserErrors.NUMBER_ENDS_DOT
            );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                    )) return BigDecimal.valueOf(value,
                                                 i - dp
                    );
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.UNKNOWN_DIGIT
                    );
                }
                value = (value << 3) + (value << 1) - ind;
            }
            if (i == end) return BigDecimal.valueOf(value,
                                                    end - dp
            );
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                    );
                } else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                    );
                } else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                    );
                }
                return BigDecimal.valueOf(value,
                                          ep - dp - exp
                );
            }
            return BigDecimal.valueOf(value,
                                      end - dp
            );
        } else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                );
            } else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                );
            } else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                );
            }
            return BigDecimal.valueOf(value,
                                      -exp
            );
        }
        return BigDecimal.valueOf(value);
    }


    private static Number tryLongFromBigDecimal(final char[] buf,
                                                final int len,
                                                JsonReader reader) throws JsParserException {
        final BigDecimal num = parseNumberGeneric(buf,
                                                  len,
                                                  reader
        );
        if (num.scale() == 0 && num.precision() <= 19) {
            if (num.signum() == 1) {
                if (num.compareTo(BD_MAX_LONG) <= 0) {
                    return num.longValue();
                }
            } else if (num.compareTo(BD_MIN_LONG) >= 0) {
                return num.longValue();
            }
        }
        return num;
    }

    public static Number deserializeNumber(final JsonReader reader) throws IOException {
        final int start = reader.scanNumber();
        int end = reader.getCurrentIndex();
        if (end == reader.length()) {
            MyNumberConverter.NumberInfo info = readLongNumber(reader,
                                                               start
            );
            return tryLongFromBigDecimal(info.buffer,
                                         info.length,
                                         reader
            );
        }
        int len = end - start;
        if (len > 18) {
            return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                              len
                                         ),
                                         len,
                                         reader
            );
        }
        final byte[] buf = reader.buffer;
        final byte ch = buf[start];
        if (ch == '-') {
            return parseNegativeNumber(buf,
                                       reader,
                                       start,
                                       end
            );
        }
        return parsePositiveNumber(buf,
                                   reader,
                                   start,
                                   end
        );
    }

    private static Number parsePositiveNumber(final byte[] buf,
                                              final JsonReader reader,
                                              final int start,
                                              final int end) throws IOException {
        long value = 0;
        byte ch = ' ';
        int i = start;
        final boolean leadingZero = buf[start] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 1) {
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.LEADING_ZERO
                    );
                }
                if (i > start && reader.allWhitespace(i,
                                                      end
                )) return value;
                return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                  end - start
                                             ),
                                             end - start,
                                             reader
                );
            }
            value = (value << 3) + (value << 1) + ind;
        }
        if (i == start) numberException(reader,
                                        start,
                                        end,
                                        ParserErrors.DIGIT_NOT_FOUND
        );
        else if (leadingZero && ch != '.' && i > start + 1) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            ParserErrors.LEADING_ZERO
        );
        else if (i == end) return value;
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          ParserErrors.NUMBER_ENDS_DOT
            );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                    )) return BigDecimal.valueOf(value,
                                                 i - dp
                    );
                    return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                      end - start
                                                 ),
                                                 end - start,
                                                 reader
                    );
                }
                value = (value << 3) + (value << 1) + ind;
            }
            if (i == end)
                return BigDecimal.valueOf(value,
                                          end - dp);
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                    );
                } else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                    );
                } else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                    );
                }
                return BigDecimal.valueOf(value,
                                          ep - dp - exp);
            }
            return BigDecimal.valueOf(value,
                                      end - dp
            );
        } else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                );
            } else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                );
            } else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                );
            }
            return BigDecimal.valueOf(value,
                                      -exp
            );
        }
        return BigDecimal.valueOf(value);
    }

    private static Number parseNegativeNumber(final byte[] buf,
                                              final JsonReader reader,
                                              final int start,
                                              final int end) throws IOException {
        long value = 0;
        byte ch = ' ';
        int i = start + 1;
        final boolean leadingZero = buf[start + 1] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 2) {
                    numberException(reader,
                                    start,
                                    end,
                                    ParserErrors.LEADING_ZERO
                    );
                }
                if (i > start + 1 && reader.allWhitespace(i,
                                                          end
                )) return value;
                return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                  end - start
                                             ),
                                             end - start,
                                             reader
                );
            }
            value = (value << 3) + (value << 1) - ind;
        }
        if (i == start + 1) numberException(reader,
                                            start,
                                            end,
                                            ParserErrors.DIGIT_NOT_FOUND
        );
        else if (leadingZero && ch != '.' && i > start + 2) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            ParserErrors.LEADING_ZERO
        );
        else if (i == end) return value;
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          ParserErrors.NUMBER_ENDS_DOT
            );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                    )) return BigDecimal.valueOf(value,
                                                 i - dp
                    );
                    return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                      end - start
                                                 ),
                                                 end - start,
                                                 reader
                    );
                }
                value = (value << 3) + (value << 1) - ind;
            }
            if (i == end) return BigDecimal.valueOf(value,
                                                    end - dp
            );
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                    );
                } else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                    );
                } else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                    );
                }
                return BigDecimal.valueOf(value,
                                          ep - dp - exp
                );
            }
            return BigDecimal.valueOf(value,
                                      end - dp
            );
        } else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                );
            } else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                );
            } else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                );
            }
            return BigDecimal.valueOf(value,
                                      -exp
            );
        }
        return BigDecimal.valueOf(value);
    }

    private static class NumberInfo {
        final char[] buffer;
        final int length;

        NumberInfo(final char[] buffer,
                   final int length) {
            this.buffer = buffer;
            this.length = length;
        }
    }


}

