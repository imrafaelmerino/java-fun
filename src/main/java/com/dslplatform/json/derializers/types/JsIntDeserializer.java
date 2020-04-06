package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.util.Optional;
import java.util.function.IntFunction;

public final class JsIntDeserializer extends JsTypeDeserializer
{
  @Override
  public JsInt value(final JsonReader<?> reader) throws ParsingException
  {
    return JsInt.of(MyNumberConverter.deserializeInt(reader));
  }

  public JsInt valueSuchThat(final JsonReader<?> reader,
                             final IntFunction<Optional<Error>> fn
                            ) throws ParsingException
  {
    final int value = MyNumberConverter.deserializeInt(reader);
    final Optional<Error> result = fn.apply(value);
    if (!result.isPresent()) return JsInt.of(value);
    throw reader.newParseError(result.toString());

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final IntFunction<Optional<Error>> fn
                                    ) throws ParsingException
  {
    return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                          fn
                                                         );
  }

}
