package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
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

public final class JsLongParser extends AbstractParser
{

  @Override
  public JsLong value(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return JsLong.of(MyNumberConverter.parseLong(reader));
    }
    catch (IOException e)
    {
      throw new JsParserException(e);
    }
  }

  public JsLong valueSuchThat(final JsonReader<?> reader,
                              final LongFunction<Optional<Error>> fn
                             ) throws JsParserException
  {
    try
    {
      final long value = MyNumberConverter.parseLong(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsLong.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (IOException e)
    {
      throw new JsParserException(e);

    }

  }




}
