package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.NumberConverter;
import jsonvalues.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;


public final  class JsNumberDeserializer extends JsTypeDeserializer
{

    @Override
    public JsNumber value(final JsonReader<?> reader) throws IOException
    {
        final Number number = NumberConverter.deserializeNumber(reader);
        if (number instanceof Double) return JsDouble.of(((double) number));
        else if (number instanceof Long) return JsLong.of(((long) number));
        else if (number instanceof BigDecimal) return JsBigDec.of(((BigDecimal) number));
        throw new RuntimeException("internal error: not condisered " + number.getClass());
    }


    public JsNumber valueSuchThat(final JsonReader<?> reader,
                                  final Function<JsNumber, Optional<Error>> fn
                                 ) throws IOException
    {
        final JsNumber value = value(reader);
        final Optional<Error> result = fn.apply(value);
        if (!result.isPresent()) return value;
        throw reader.newParseError(result.toString());

    }

    public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                       final Function<JsNumber, Optional<Error>> fn
                                      ) throws IOException
    {
        return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                              fn
                                                             );
    }

}
