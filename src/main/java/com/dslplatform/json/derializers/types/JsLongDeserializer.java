package com.dslplatform.json.derializers.types;

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
  public JsLong value(final JsonReader<?> reader) throws IOException
  {
    return JsLong.of(MyNumberConverter.deserializeLong(reader));
  }

  public JsLong valueSuchThat(final JsonReader<?> reader,
                              final LongFunction<Optional<Error>> fn
                             ) throws IOException
  {
    final long value = MyNumberConverter.deserializeLong(reader);
    final Optional<Error> result = fn.apply(value);
    if (!result.isPresent()) return JsLong.of(value);
    throw reader.newParseError(result.toString());

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final LongFunction<Optional<Error>> fn
                                    ) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                          fn
                                                         );
  }


}
