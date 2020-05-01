package com.dslplatform.json.parsers.types;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;


import java.io.IOException;

public abstract class AbstractJsObjParser extends AbstractParser
{

  protected boolean isEmptyObj(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      if (reader.last() != '{') throw reader.newParseError("Expecting '{' for map start");
      byte nextToken = reader.getNextToken();
      return nextToken == '}';
    }
    catch (IOException e)
    {
      throw new JsParserException(e);
    }
  }

}
