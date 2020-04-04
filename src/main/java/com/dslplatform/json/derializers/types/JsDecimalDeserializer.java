package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import jsonvalues.JsBigDec;
import jsonvalues.JsNull;
import jsonvalues.JsValue;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;


public final  class JsDecimalDeserializer extends JsTypeDeserializer
{

    @Override
    public JsBigDec value(final JsonReader<?> reader) throws IOException
    {
        return JsBigDec.of(MyNumberConverter.deserializeDecimal(reader));
    }

    public JsBigDec valueSuchThat(final JsonReader<?> reader,
                                  final Function<BigDecimal, Optional<Error>> fn
                                 ) throws IOException
    {
        final BigDecimal value = MyNumberConverter.deserializeDecimal(reader);
        final Optional<Error> result = fn.apply(value);
        if (!result.isPresent()) return JsBigDec.of(value);
        throw reader.newParseError(result.toString());
    }

    public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                       final Function<BigDecimal, Optional<Error>> fn
                                      ) throws IOException
    {
        return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                              fn
                                                             );
    }
}
