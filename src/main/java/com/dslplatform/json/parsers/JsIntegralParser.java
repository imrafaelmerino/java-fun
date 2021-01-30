package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.NumberConverter;
import jsonvalues.JsBigInt;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

final class JsIntegralParser extends AbstractParser {
    @Override
    JsBigInt value(final JsonReader<?> reader) {
        try {

            return JsBigInt.of(NumberConverter.deserializeDecimal(reader)
                                              .toBigIntegerExact());

        } catch (ArithmeticException | IOException e) {
            throw new JsParserException(reader.newParseError("Integral number expected"));
        }
    }

    JsBigInt valueSuchThat(final JsonReader<?> reader,
                           final Function<BigInteger, Optional<Error>> fn
                          ) {
        try {
            final BigInteger value = NumberConverter.deserializeDecimal(reader)
                                                    .toBigIntegerExact();
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsBigInt.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
