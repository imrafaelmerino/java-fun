package com.dslplatform.json.derializers.specs;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.derializers.ValueDeserializer;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;
import java.io.IOException;
import java.util.function.Function;

public final  class JsObjSpecWithRequiredKeysDeserializer extends JsObjSpecDeserializer
{
    private final Vector<String> required;


    public JsObjSpecWithRequiredKeysDeserializer(final Vector<String> required,
                                                 final Map<String, ValueDeserializer> deserializers
                                                )
    {
        super(deserializers);
        this.required = required;
    }

    @Override
    public JsObj value(final JsonReader<?> reader) throws IOException
    {
        final JsObj obj = super.value(reader);
        final Iterator<String> iterator = required.iterator();
        while (iterator.hasNext())
        {
            final String key = iterator.next();
            if (!obj.containsPath(JsPath.fromKey(key))) throw reader.newParseError("Required key not found: " + key);
        }
        return obj;
    }


}
