package com.dslplatform.json.parsers.specs;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.parsers.types.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.io.IOException;

public class JsObjSpecParser extends AbstractJsObjParser
{
  private final Map<String, SpecParser> parsers;
  protected final boolean strict;
  private final JsValueParser valueParser = new JsValueParser();
  private final SpecParser defaultParser = valueParser::value;

  public JsObjSpecParser(boolean strict,
                         final Map<String, SpecParser> parsers
                        )
  {
    this.strict = strict;
    this.parsers = parsers;
  }

    @Override
    public JsObj value ( final JsonReader<?> reader) throws JsParserException
    {
      try
      {
        if (isEmptyObj(reader)) return EMPTY_OBJ;
        String key = reader.readKey();
        if (strict && !parsers.containsKey(key))
        {
          throw reader.newParseError("There is no spec defined for the key " + key);
        }
        HashMap<String, JsValue> map = EMPTY_MAP.put(key,
                                                     parsers.getOrElse(key,
                                                                       defaultParser
                                                                      )
                                                            .parse(reader
                                                                  )
                                                    );
        byte nextToken;
        while ((nextToken = reader.getNextToken()) == ',')
        {
          reader.getNextToken();
          key = reader.readKey();
          map = map.put(key,
                        parsers.getOrElse(key,
                                          null
                                         )
                               .parse(reader)
                       );

        }
        if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
        return new JsObj(map);
      }
      catch (IOException e)
      {
        throw new JsParserException(e);
      }
    }


  }
