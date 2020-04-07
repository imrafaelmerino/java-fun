package com.dslplatform.json.derializers.specs;

import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.derializers.ValueDeserializer;
import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import java.io.IOException;
import java.util.function.Function;

public final  class JsArraySpecDeserializer
{
    private final Vector<ValueDeserializer> deserializers;

    public JsArraySpecDeserializer(final Vector<ValueDeserializer> deserializers)
    {
        this.deserializers = deserializers;
    }

    public JsValue nullOrArray(final JsonReader<?> reader) throws DeserializerException
    {
      try
      {
        return reader.wasNull() ? JsNull.NULL : array(reader);
      }
      catch (ParsingException e)
      {
        throw new DeserializerException(e);
      }
    }


    public JsArray array(final JsonReader<?>reader) throws DeserializerException
    {
      try
      {
        if (reader.last() != '[') throw reader.newParseError("Expecting '[' for list start");
        reader.getNextToken();
        if (reader.last() == ']') return JsArray.empty();
        JsArray buffer = JsArray.empty();
        Integer i = 0;
        buffer = buffer.append(deserializers.apply(i).read(reader));
        while (reader.getNextToken() == ',')
        {
            i = i+ 1;
            reader.getNextToken();
            buffer = buffer.append(deserializers.apply(i).read(reader));

        }
        reader.checkArrayEnd();
        return buffer;
      }
      catch (IOException e)
      {
        throw new DeserializerException(e);
      }
    }



}
