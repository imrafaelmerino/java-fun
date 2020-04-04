package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsValue;


import java.io.IOException;
import java.util.Optional;
import java.util.function.IntFunction;

public final  class JsIntDeserializer extends JsTypeDeserializer
{
    @Override
    public JsInt value(final JsonReader<?> reader) throws IOException
    {
        return JsInt.of(MyNumberConverter.deserializeInt(reader));
    }

    public JsInt valueSuchThat(final JsonReader<?> reader,
                               final IntFunction<Optional<Error>> fn
                              ) throws IOException
    {
        final int value = MyNumberConverter.deserializeInt(reader);
        final Optional<Error> result = fn.apply(value);
        if (!result.isPresent()) return JsInt.of(value);
        throw reader.newParseError(result.toString());

    }

    public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                       final IntFunction<Optional<Error>> fn
                                      ) throws IOException
    {
        return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                              fn
                                                             );
    }

}
