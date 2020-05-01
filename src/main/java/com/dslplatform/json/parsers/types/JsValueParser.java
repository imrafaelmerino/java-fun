package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.parsers.types.arrays.JsArrayOfValueParser;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.spec.Error;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public final class JsValueParser extends AbstractParser
{
  private JsObjParser objDeserializer;

  private JsArrayOfValueParser arrayDeserializer;

  public void setNumberDeserializer(final JsNumberParser numberDeserializer)
  {
    this.numberDeserializer = numberDeserializer;
  }

  private JsNumberParser numberDeserializer;

  public void setObjDeserializer(final JsObjParser objDeserializer)
  {
    this.objDeserializer = objDeserializer;
  }

  public void setArrayDeserializer(final JsArrayOfValueParser arrayDeserializer)
  {
    this.arrayDeserializer = arrayDeserializer;
  }

  @Override
  public JsValue value(final JsonReader<?> reader) throws JsParserException
  {

    try
    {
      switch (reader.last())
      {
        case 't':
          if (!reader.wasTrue())
          {
            throw new JsParserException(reader.newParseErrorAt("Expecting 'true' for true constant",
                                                               0
                                                              ));
          }
          return JsBool.TRUE;
        case 'f':
          if (!reader.wasFalse())
          {
            throw new JsParserException(reader.newParseErrorAt("Expecting 'false' for false constant",
                                                               0
                                                              ));
          }
          return JsBool.FALSE;
        case '"':
          return JsStr.of(reader.readString());
        case '{':
          return objDeserializer.value(reader);
        case '[':
          return arrayDeserializer.array(reader);
        default:
          return numberDeserializer.value(reader);
      }
    }
    catch (IOException e)
    {
      throw new JsParserException(e);
    }
  }

  public JsValue valueSuchThat(final JsonReader<?> reader,
                               final Function<JsValue, Optional<Error>> fn

                              ) throws JsParserException
  {
    try
    {
      final JsValue value = value(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return value;
      throw reader.newParseError(result.toString());
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }

  }


}
