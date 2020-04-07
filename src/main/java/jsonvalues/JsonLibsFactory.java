package jsonvalues;

import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.MyDslJson;
import com.dslplatform.json.serializers.JsArraySerializer;
import com.dslplatform.json.serializers.JsObjSerializer;
import com.dslplatform.json.serializers.JsValueSerializer;
import com.fasterxml.jackson.core.JsonFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public class JsonLibsFactory
{
    public static final JsonFactory jackson = new JsonFactory();
    public static final MyDslJson<Object> dslJson = new MyDslJson<>();


  static {
    final JsValueSerializer valueSerializer = new JsValueSerializer();
    final JsonWriter.WriteObject<JsObj> objSerializer = new JsObjSerializer<>(valueSerializer);
    final JsonWriter.WriteObject<JsArray> arraySerializer = new JsArraySerializer<>(valueSerializer);
     valueSerializer.setArraySerializer(arraySerializer);
     valueSerializer.setObjectSerializer(objSerializer);
     dslJson.registerWriter(JsObj.class,
                            objSerializer
                            );

     dslJson.registerWriter(JsArray.class,
                            arraySerializer
                           );
  }

    /** Returns the string representation of this Json
     *
     * @return the string representation of this Json
     */
    static String toString(Json<?> json)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            dslJson.serialize(json,
                              baos
                             );
            return baos.toString(StandardCharsets.UTF_8.name());
        }
        catch (IOException e)
        {
            throw  InternalError.unexpectedErrorSerializingAJsonIntoString(e);
        }
    }

}
