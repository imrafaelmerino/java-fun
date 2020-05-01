package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyNumberConverter;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.util.Optional;
import java.util.function.IntFunction;

public final class JsIntParser extends AbstractParser
{
  @Override
  public JsInt value(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return JsInt.of(MyNumberConverter.parseInt(reader));
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);
    }
  }

  public JsInt valueSuchThat(final JsonReader<?> reader,
                             final IntFunction<Optional<Error>> fn
                            ) throws JsParserException
  {
    try
    {
      final int value = MyNumberConverter.parseInt(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsInt.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final IntFunction<Optional<Error>> fn
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
