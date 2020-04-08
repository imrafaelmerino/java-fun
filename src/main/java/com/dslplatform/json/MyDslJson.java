package com.dslplatform.json;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.derializers.specs.SpecDeserializer;
import com.dslplatform.json.serializers.JsArraySerializer;
import com.dslplatform.json.serializers.JsObjSerializer;
import com.dslplatform.json.serializers.JsValueSerializer;
import com.dslplatform.json.serializers.SerializerException;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.Json;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public final class MyDslJson<Object> extends DslJson<Object>
{
  public static final MyDslJson<java.lang.Object> INSTANCE = new MyDslJson<>();

  private MyDslJson()
  {
  }

  static
  {
    final JsValueSerializer valueSerializer = new JsValueSerializer();
    final JsonWriter.WriteObject<JsObj> objSerializer = new JsObjSerializer<>(valueSerializer);
    final JsonWriter.WriteObject<JsArray> arraySerializer = new JsArraySerializer<>(valueSerializer);
    valueSerializer.setArraySerializer(arraySerializer);
    valueSerializer.setObjectSerializer(objSerializer);
    INSTANCE.registerWriter(JsObj.class,
                            objSerializer
                           );

    INSTANCE.registerWriter(JsArray.class,
                            arraySerializer
                           );
  }

  private JsonReader<?> getReader(final byte[] bytes
                                 )
  {
    return localReader.get()
                      .process(bytes,
                               bytes.length
                              );
  }

  private JsonReader<?> getReader(final InputStream is) throws DeserializerException
  {
    try
    {
      return localReader.get()
                        .process(is);
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);

    }
  }

  public JsObj deserializeToJsObj(final byte[] bytes,
                                  final SpecDeserializer deserializer
                                 ) throws DeserializerException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsObj();
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);

    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final byte[] bytes,
                                      final SpecDeserializer deserializer
                                     ) throws DeserializerException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsArray();
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
    finally
    {
      reader.reset();
    }
  }

  public JsObj deserializeToJsObj(final InputStream is,
                                  final SpecDeserializer deserializer

                                 ) throws DeserializerException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsObj();
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final InputStream is,
                                      final SpecDeserializer deserializer
                                     ) throws SerializerException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsArray();
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
    finally
    {
      reader.reset();
    }
  }

  public byte[] serialize() throws SerializerException
  {
    try
    {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      INSTANCE.serialize(this,
                         outputStream
                        );
      outputStream.flush();
      return outputStream.toByteArray();
    }
    catch (IOException e)
    {
      throw new SerializerException(e);
    }
  }

  public void serialize(Json<?> json,
                        OutputStream ouputstream
                       ) throws SerializerException
  {
    try
    {
      super.serialize(json,
                requireNonNull(ouputstream)
               );
    }
    catch (IOException e)
    {
      throw new SerializerException(e);
    }

  }

  /** Returns the string representation of this Json
   * @param json json to be serialized
   * @return the string representation of this Json
   */
  public String serialize(Json<?> json)
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      MyDslJson.INSTANCE.serialize(json,
                                   baos
                                  );
      return baos.toString(StandardCharsets.UTF_8.name());
    }
    catch (IOException e)
    {
      throw new SerializerException(e);
    }
  }

  public String toPrettyString()
  {

    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      INSTANCE.serialize(this,
                         new MyPrettifyOutputStream(baos)
                        );
      return baos.toString(StandardCharsets.UTF_8.name());
    }
    catch (IOException e)
    {
      throw new SerializerException(e);
    }


  }

}
