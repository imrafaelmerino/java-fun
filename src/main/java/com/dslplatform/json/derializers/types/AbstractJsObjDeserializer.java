package com.dslplatform.json.derializers.types;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;


import java.io.IOException;

public abstract class AbstractJsObjDeserializer extends AbstractDeserializer
{

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
