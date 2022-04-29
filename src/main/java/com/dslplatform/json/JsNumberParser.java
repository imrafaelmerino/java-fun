package com.dslplatform.json;

import jsonvalues.*;
import jsonvalues.spec.JsError;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

final class JsNumberParser extends AbstractParser {

    JsNumber valueSuchThat(final JsonReader<?> reader,
                           final Function<JsNumber, Optional<JsError>> fn
                          ){
        try {
            final JsNumber        value  = value(reader);
            final Optional<JsError> result = fn.apply(value);
            if (!result.isPresent()) return value;
            throw reader.newParseError(result.toString());
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }

    }

    @Override
    JsNumber value(final JsonReader<?> reader){
        final Number number;
        try {
            number = MyNumberConverter.deserializeNumber(reader);
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
        if (number instanceof Double) return JsDouble.of(((double) number));
        else if (number instanceof Long) {
            long n = (long) number;
            try {
                return JsInt.of(Math.toIntExact(n));
            } catch (ArithmeticException e) {
                return JsLong.of(n);
            }
        }
        else if (number instanceof BigDecimal)
            return JsBigDec.of(((BigDecimal) number));
        throw new JsParserException("internal error: not considered " + number.getClass());
    }


}
