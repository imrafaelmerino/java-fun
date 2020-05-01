package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.NumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import jsonvalues.spec.Error;

public final class JsNumberParser extends AbstractParser
{

  @Override
  public JsNumber value(final JsonReader<?> reader) throws JsParserException
  {
    final Number number;
    try
    {
      number = NumberConverter.deserializeNumber(reader);
    }
    catch (IOException e)
    {
      throw new JsParserException(e);

    }
    if (number instanceof Double) return JsDouble.of(((double) number));
    else if (number instanceof Long) return JsLong.of(((long) number));
    else if (number instanceof BigDecimal) return JsBigDec.of(((BigDecimal) number));
    throw new JsParserException("internal error: not condisered " + number.getClass());
  }


  public JsNumber valueSuchThat(final JsonReader<?> reader,
                                final Function<JsNumber, Optional<Error>> fn
                               ) throws JsParserException
  {
    try
    {
      final JsNumber value = value(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return value;
      throw reader.newParseError(result.toString());
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final Function<JsNumber, Optional<Error>> fn
                                    ) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                            fn
                                                           );
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }
  }

}
