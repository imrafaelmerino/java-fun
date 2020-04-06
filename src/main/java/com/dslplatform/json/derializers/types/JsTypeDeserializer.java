package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import io.vavr.collection.HashMap;
import jsonvalues.*;
import java.io.IOException;

public abstract class JsTypeDeserializer
{
  protected final static HashMap<String, JsValue> EMPTY_MAP = HashMap.empty();
  protected final static JsObj EMPTY_OBJ = JsObj.empty();

  public abstract JsValue value(final JsonReader<?> reader) throws IOException;

  public JsValue nullOrValue(final JsonReader<?> reader) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : value(reader);
  }

  protected boolean isEmptyObj(final JsonReader<?> reader) throws IOException
  {
    if (reader.last() != '{') throw reader.newParseError("Expecting '{' for map start");
    byte nextToken = reader.getNextToken();
    return nextToken == '}';
  }

}
