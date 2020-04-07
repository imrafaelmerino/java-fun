package com.dslplatform.json.derializers.specs;
import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.derializers.ValueDeserializer;
import com.dslplatform.json.derializers.types.JsTypeDeserializer;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.io.IOException;
import java.util.function.Function;

public  class JsObjSpecDeserializer extends JsTypeDeserializer
{
    private final Map<String, ValueDeserializer> deserializers;

    public JsObjSpecDeserializer(final Map<String, ValueDeserializer> deserializers)
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
