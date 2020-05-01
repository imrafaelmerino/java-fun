package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.StringConverter;
import jsonvalues.JsNull;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public final class JsStrParser extends AbstractParser
{
  @Override
  public JsStr value(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return JsStr.of(StringConverter.deserialize(reader));
    }
    catch (IOException e)
    {
      throw new JsParserException(e);

    }
  }


  public JsStr valueSuchThat(final JsonReader<?> reader,
                             final Function<String, Optional<Error>> fn
                            ) throws JsParserException
  {
    try
    {
      final String value = StringConverter.deserialize(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return JsStr.of(value);
      throw reader.newParseError(result.toString());
    }
    catch (IOException e)
    {
      throw new JsParserException(e);

    }

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final Function<String, Optional<Error>> fn
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
