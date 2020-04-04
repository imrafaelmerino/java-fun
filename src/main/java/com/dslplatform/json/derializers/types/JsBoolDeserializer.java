package com.dslplatform.json.derializers.types;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.io.IOException;

public final  class JsBoolDeserializer extends JsTypeDeserializer
{

    public JsBool value(final JsonReader<?>reader) throws IOException
    {
        if (reader.wasTrue()) return JsBool.TRUE;
        else if (reader.wasFalse()) return JsBool.FALSE;
        throw reader.newParseErrorAt("Found invalid boolean value",
                                     0
                                    );
    }


    public JsBool True(final JsonReader<?>reader) throws IOException
    {
        if (reader.wasTrue()) return JsBool.TRUE;
        throw reader.newParseErrorAt("Found invalid boolean value. True was expected.",
                                     0
                                    );
    }

    public JsValue nullOrTrue(final JsonReader<?> reader) throws IOException
    {
        return reader.wasNull() ? JsNull.NULL : True(reader);
    }


    public JsBool False(final JsonReader<?>reader) throws IOException
    {
        if (reader.wasFalse()) return JsBool.FALSE;
        throw reader.newParseErrorAt("Found invalid boolean value. False was expected.",
                                     0
                                    );
    }

    public JsValue nullOrFalse(final JsonReader<?> reader) throws IOException
    {
        return reader.wasNull() ? JsNull.NULL : False(reader);
    }


}
