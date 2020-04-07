package com.dslplatform.json.derializers.types;

import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsBigDec;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;


public final class JsDecimalDeserializer extends JsTypeDeserializer
{

  @Override
  public JsBigDec value(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      return JsBigDec.of(MyNumberConverter.deserializeDecimal(reader));
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsBigDec valueSuchThat(final JsonReader<?> reader,
                                final Function<BigDecimal, Optional<Error>> fn
                               ) throws DeserializerException
  {
    try
    {
      final BigDecimal value = MyNumberConverter.deserializeDecimal(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsBigDec.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);

    }
  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final Function<BigDecimal, Optional<Error>> fn
                                    ) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                            fn
                                                           );
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }
  }
}
