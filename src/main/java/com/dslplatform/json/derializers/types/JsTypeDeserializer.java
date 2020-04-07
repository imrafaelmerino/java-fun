package com.dslplatform.json.derializers.types;

import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import io.vavr.collection.HashMap;
import jsonvalues.*;
import java.io.IOException;

public abstract class JsTypeDeserializer
{
  protected final static HashMap<String, JsValue> EMPTY_MAP = HashMap.empty();
  protected final static JsObj EMPTY_OBJ = JsObj.empty();

  public abstract JsValue value(final JsonReader<?> reader) throws DeserializerException;

  public JsValue nullOrValue(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : value(reader);
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }
  }

  protected boolean isEmptyObj(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      if (reader.last() != '{') throw reader.newParseError("Expecting '{' for map start");
      byte nextToken = reader.getNextToken();
      return nextToken == '}';
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
  }

}
