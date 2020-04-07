package com.dslplatform.json.derializers.types;

import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsLong;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.Optional;

import jsonvalues.spec.Error;

import java.util.function.LongFunction;

public final class JsLongDeserializer extends JsTypeDeserializer
{

  @Override
  public JsLong value(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      return JsLong.of(MyNumberConverter.deserializeLong(reader));
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsLong valueSuchThat(final JsonReader<?> reader,
                              final LongFunction<Optional<Error>> fn
                             ) throws DeserializerException
  {
    try
    {
      final long value = MyNumberConverter.deserializeLong(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsLong.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);

    }

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final LongFunction<Optional<Error>> fn
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
