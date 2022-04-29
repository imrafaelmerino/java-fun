package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.JsError;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class JsArrayOfValueParser extends JsArrayParser {
    private final JsValueParser parser;

    JsArrayOfValueParser(final JsValueParser parser) {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final Function<JsValue, Optional<JsError>> fn
                                   ){
        try {
            return reader.wasNull() ? JsNull.NULL : arrayEachSuchThat(reader,
                                                                      fn
                                                                     );
        } catch (ParsingException e) {
            throw new JsParserException(e.getMessage());

        }
    }


    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final Function<JsValue, Optional<JsError>> fn
                             ){
        try {
            if (ifIsEmptyArray(reader)) return EMPTY;
            JsArray buffer = EMPTY.append(parser.valueSuchThat(reader,
                                                               fn
                                                              ));
            while (reader.getNextToken() == ',') {
                reader.getNextToken();
                buffer = buffer.append(parser.valueSuchThat(reader,
                                                            fn
                                                           ));
            }
            reader.checkArrayEnd();
            return buffer;
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());

        }
    }


}
