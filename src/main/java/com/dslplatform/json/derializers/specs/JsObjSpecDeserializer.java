package com.dslplatform.json.derializers.specs;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.derializers.types.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.io.IOException;

public class JsObjSpecDeserializer extends AbstractJsObjDeserializer
{
  private final Map<String, SpecDeserializer> deserializers;
  protected final boolean strict;
  private final JsValueDeserializer valueDeserializer = new JsValueDeserializer();
  private final SpecDeserializer defaultDeserializer = valueDeserializer::value;

  public JsObjSpecDeserializer(boolean strict,
                               final Map<String, SpecDeserializer> deserializers
                              )
  {
    this.strict = strict;
    this.deserializers = deserializers;
  }

    @Override
    public JsObj value ( final JsonReader<?> reader) throws DeserializerException
    {
      try
      {
        if (isEmptyObj(reader)) return EMPTY_OBJ;
        String key = reader.readKey();
        if (strict && !deserializers.containsKey(key))
        {
          throw reader.newParseError("There is no spec defined for the key " + key);
        }
        HashMap<String, JsValue> map = EMPTY_MAP.put(key,
                                                     deserializers.getOrElse(key,
                                                                             defaultDeserializer
                                                                            )
                                                                  .read(reader
                                                                       )
                                                    );
        byte nextToken;
        while ((nextToken = reader.getNextToken()) == ',')
        {
          reader.getNextToken();
          key = reader.readKey();
          map = map.put(key,
                        deserializers.getOrElse(key,
                                                null
                                               )
                                     .read(reader)
                       );

        }
        if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
        return new JsObj(map);
      }
      catch (IOException e)
      {
        throw new DeserializerException(e);
      }
    }


  }
