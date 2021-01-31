package com.dslplatform.json;

<<<<<<< HEAD:src/main/java/com/dslplatform/json/JsDecimalParser.java
=======
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
>>>>>>> feat: 🎸 new method toPrettyString:src/main/java/com/dslplatform/json/parsers/JsDecimalParser.java
import jsonvalues.JsBigDec;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;


final class JsDecimalParser extends AbstractParser {

    @Override
    JsBigDec value(final JsonReader<?> reader) {
        try {
            return JsBigDec.of(MyNumberConverter.deserializeDecimal(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsBigDec valueSuchThat(final JsonReader<?> reader,
                           final Function<BigDecimal, Optional<Error>> fn
                          ) {
        try {
            final BigDecimal      value  = MyNumberConverter.deserializeDecimal(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsBigDec.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }


}
