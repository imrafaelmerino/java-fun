package com.dslplatform.json.derializers.specs;
import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.derializers.types.AbstractJsObjDeserializer;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.io.IOException;

public  class JsObjSpecDeserializer extends AbstractJsObjDeserializer
{
    private final Map<String, SpecDeserializer> deserializers;

    public JsObjSpecDeserializer(final Map<String, SpecDeserializer> deserializers)
    {
        this.deserializers = deserializers;
    }

    @Override
    public JsObj value(final JsonReader<?> reader) throws DeserializerException
    {
      try
      {
        if (isEmptyObj(reader)) return EMPTY_OBJ;
        String key = reader.readKey();
        HashMap<String, JsValue> map = EMPTY_MAP.put(key,
                                                         deserializers.apply(key)
                                                                      .read(reader
                                                                            )
                                                        );
        byte nextToken;
        while ((nextToken = reader.getNextToken()) == ',')
        {
            reader.getNextToken();
            key = reader.readKey();
            map = map.put(key,
                              deserializers.apply(key)
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
