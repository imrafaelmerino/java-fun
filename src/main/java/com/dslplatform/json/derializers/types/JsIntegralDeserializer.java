package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsBigInt;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

public final class JsIntegralDeserializer extends JsTypeDeserializer

{
  @Override
  public JsBigInt value(final JsonReader<?> reader) throws ParsingException
  {
    try
    {
      return JsBigInt.of(MyNumberConverter.deserializeDecimal(reader)
                                          .toBigIntegerExact());
    }
    catch (ArithmeticException | IOException e)
    {
      throw reader.newParseError("Integral number expected");
    }
  }

  public JsBigInt valueSuchThat(final JsonReader<?> reader,
                                final Function<BigInteger, Optional<Error>> fn
                               ) throws IOException
  {
    final BigInteger value = MyNumberConverter.deserializeDecimal(reader)
                                              .toBigIntegerExact();
    final Optional<Error> result = fn.apply(value);
    if (!result.isPresent()) return JsBigInt.of(value);
    throw reader.newParseError(result.toString());

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final Function<BigInteger, Optional<Error>> fn
                                    ) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                          fn
                                                         );
  }
}
