package com.dslplatform.json;

<<<<<<< HEAD:src/main/java/com/dslplatform/json/JsIntParser.java
=======
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
>>>>>>> feat: 🎸 new method toPrettyString:src/main/java/com/dslplatform/json/parsers/JsIntParser.java
import jsonvalues.JsInt;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntParser extends AbstractParser {
    @Override
    JsInt value(final JsonReader<?> reader){
        try {
            return JsInt.of(MyNumberConverter.deserializeInt(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsInt valueSuchThat(final JsonReader<?> reader,
                        final IntFunction<Optional<Error>> fn
                       ){
        try {
            final int             value  = MyNumberConverter.deserializeInt(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsInt.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
