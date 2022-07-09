package com.dslplatform.json;

import fun.tuple.Pair;
import jsonvalues.JsLong;
import jsonvalues.JsValue;
import jsonvalues.spec.ERROR_CODE;

import java.io.IOException;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsLongParser extends AbstractParser {

    @Override
    JsLong value(final JsonReader<?> reader) throws IOException {
        return JsLong.of(MyNumberConverter.deserializeLong(reader));
    }

    JsLong valueSuchThat(final JsonReader<?> reader,
                         final LongFunction<Optional<Pair<JsValue, ERROR_CODE>>> fn
    ) throws IOException {
            long value = MyNumberConverter.deserializeLong(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = fn.apply(value);
            if (!result.isPresent()) return JsLong.of(value);
            throw new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                        reader.getCurrentIndex());
    }

}
