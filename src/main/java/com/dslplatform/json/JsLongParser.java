package com.dslplatform.json;

<<<<<<< HEAD:src/main/java/com/dslplatform/json/JsLongParser.java
=======
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
>>>>>>> feat: 🎸 new method toPrettyString:src/main/java/com/dslplatform/json/parsers/JsLongParser.java
import jsonvalues.JsLong;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsLongParser extends AbstractParser {

    @Override
    JsLong value(final JsonReader<?> reader) {
        try {
            return JsLong.of(MyNumberConverter.deserializeLong(reader));
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }

    JsLong valueSuchThat(final JsonReader<?> reader,
                         final LongFunction<Optional<Error>> fn
                        ) {
        try {
            final long            value  = MyNumberConverter.deserializeLong(reader);
            final Optional<Error> result = fn.apply(value);
            if (!result.isPresent()) return JsLong.of(value);
            throw reader.newParseError(result.toString());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }

    }


}
