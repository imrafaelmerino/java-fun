package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
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

public final class JsIntegralParser extends AbstractParser

{
  @Override
  public JsBigInt value(final JsonReader<?> reader) throws JsParserException
  {
    try
    {

        return JsBigInt.of(MyNumberConverter.parseDecimal(reader)
                                            .toBigIntegerExact());

    }
    catch (ArithmeticException | IOException e)
    {
      throw new JsParserException(reader.newParseError("Integral number expected"));
    }
  }

  public JsBigInt valueSuchThat(final JsonReader<?> reader,
                                final Function<BigInteger, Optional<Error>> fn
                               ) throws JsParserException
  {
    try
    {
      final BigInteger value = MyNumberConverter.parseDecimal(reader)
                                                .toBigIntegerExact();
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsBigInt.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (IOException e)
    {
      throw new JsParserException(e);

    }

  }


}
