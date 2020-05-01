package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
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


public final class JsDecimalParser extends AbstractParser
{

  @Override
  public JsBigDec value(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return JsBigDec.of(MyNumberConverter.parseDecimal(reader));
    }
    catch (IOException e)
    {
      throw new JsParserException(e);
    }
  }

  public JsBigDec valueSuchThat(final JsonReader<?> reader,
                                final Function<BigDecimal, Optional<Error>> fn
                               ) throws JsParserException
  {
    try
    {
      final BigDecimal value = MyNumberConverter.parseDecimal(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsBigDec.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (IOException e)
    {
      throw new JsParserException(e);

    }
  }


}
