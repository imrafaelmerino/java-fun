package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import io.vavr.collection.HashMap;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;


import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final  class JsObjDeserializer extends JsTypeDeserializer
{

    private final JsValueDeserializer valueDeserializer;

    public JsObjDeserializer(final JsValueDeserializer valueDeserializer)
    {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public JsObj value(final JsonReader<?> reader) throws IOException
    {
        if (isEmptyObj(reader)) return EMPTY_OBJ;

        String key = reader.readKey();
        HashMap<String, JsValue> map = EMPTY_MAP.put(key,
                                                     valueDeserializer.value(reader
                                                                                )
                                                        );
        byte nextToken;
        while ((nextToken = reader.getNextToken()) == ',')
        {
            reader.getNextToken();
            key = reader.readKey();
            map = map.put(key,
                              valueDeserializer.value(reader
                                                     )
                             );
        }
        if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
        return new JsObj(map);
    }

    public JsObj valueSuchThat(final JsonReader<?> reader,
                               final Function<JsObj, Optional<Error>> fn
                              ) throws IOException
    {
        final JsObj value = value(reader);
        final Optional<Error> result = fn.apply(value);
        if (!result.isPresent()) return value;
        throw reader.newParseError(result.toString());

    }

    public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                       final Function<JsObj,Optional<Error>> fn
                                      ) throws IOException
    {
        return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                              fn
                                                             );
    }
}
