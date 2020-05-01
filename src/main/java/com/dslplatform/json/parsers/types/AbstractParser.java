package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import io.vavr.collection.HashMap;
import jsonvalues.*;

public abstract class AbstractParser
{
  protected final static HashMap<String, JsValue> EMPTY_MAP = HashMap.empty();
  protected final static JsObj EMPTY_OBJ = JsObj.empty();

  public abstract JsValue value(final JsonReader<?> reader) throws JsParserException;

  public JsValue nullOrValue(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : value(reader);
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);
    }
  }


}
