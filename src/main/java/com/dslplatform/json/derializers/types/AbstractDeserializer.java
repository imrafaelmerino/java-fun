package com.dslplatform.json.derializers.types;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import io.vavr.collection.HashMap;
import jsonvalues.*;

public abstract class AbstractDeserializer
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


}
