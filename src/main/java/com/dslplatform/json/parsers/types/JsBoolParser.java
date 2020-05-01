package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

public final class JsBoolParser extends AbstractParser
{

  public JsBool value(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      if (reader.wasTrue()) return JsBool.TRUE;
      else if (reader.wasFalse()) return JsBool.FALSE;
      throw reader.newParseErrorAt("Found invalid boolean value",
                                   0
                                  );
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }
  }


  public JsBool True(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      if (reader.wasTrue()) return JsBool.TRUE;
      throw reader.newParseErrorAt("Found invalid boolean value. True was expected.",
                                   0
                                  );
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }
  }

  public JsValue nullOrTrue(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : True(reader);
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }
  }


  public JsBool False(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      if (reader.wasFalse()) return JsBool.FALSE;
      throw reader.newParseErrorAt("Found invalid boolean value. False was expected.",
                                   0
                                  );
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }
  }

  public JsValue nullOrFalse(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : False(reader);
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }
  }


}
