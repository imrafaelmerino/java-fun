package com.dslplatform.json.derializers.types;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.util.Optional;
import java.util.function.IntFunction;

public final class JsIntDeserializer extends AbstractDeserializer
{
  @Override
  public JsInt value(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      return JsInt.of(MyNumberConverter.deserializeInt(reader));
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsInt valueSuchThat(final JsonReader<?> reader,
                             final IntFunction<Optional<Error>> fn
                            ) throws DeserializerException
  {
    try
    {
      final int value = MyNumberConverter.deserializeInt(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsInt.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final IntFunction<Optional<Error>> fn
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
