package com.dslplatform.json.derializers.specs;

import com.dslplatform.json.JsonReader;
import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;


import java.io.IOException;
import java.util.function.Function;

public final  class JsArraySpecDeserializer
{
    private final Vector<Function<JsonReader<?>, JsValue>> deserializers;

    public JsArraySpecDeserializer(final Vector<Function<JsonReader<?>, JsValue>> deserializers)
    {
        this.deserializers = deserializers;
    }

    public JsValue nullOrArray(final JsonReader<?> reader) throws IOException
    {
        return reader.wasNull() ? JsNull.NULL : array(reader);
    }


    public JsArray array(final JsonReader<?>reader) throws IOException
    {
        if (reader.last() != '[') throw reader.newParseError("Expecting '[' for list start");
        reader.getNextToken();
        if (reader.last() == ']') return JsArray.empty();
        JsArray buffer = JsArray.empty();
        Integer i = 0;
        buffer = buffer.append(deserializers.apply(i).apply(reader));
        while (reader.getNextToken() == ',')
        {
            i = i+ 1;
            reader.getNextToken();
            buffer = buffer.append(deserializers.apply(i).apply(reader));

        }
        reader.checkArrayEnd();
        return buffer;
    }



}
