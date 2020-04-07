package com.dslplatform.json;

import com.dslplatform.json.derializers.ValueDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsObj;

import java.io.IOException;
import java.io.InputStream;

public final class MyDslJson<Object> extends DslJson<Object>
{

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
                                  final ValueDeserializer deserializer
                                 ) throws DeserializerException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsObj();
    }
    catch (IOException e){
      throw new DeserializerException(e);

    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final byte[] bytes,
                                      final ValueDeserializer deserializer
                                     ) throws DeserializerException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsArray();
    }
    catch (IOException e){
      throw new DeserializerException(e);
    }
    finally
    {
      reader.reset();
    }
  }

  public JsObj deserializeToJsObj(final InputStream is,
                                  final ValueDeserializer deserializer

                                 ) throws DeserializerException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsObj();
    }
    catch (IOException e){
      throw new DeserializerException(e);
    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final InputStream is,
                                      final ValueDeserializer deserializer
                                     ) throws DeserializerException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsArray();
    }
    catch (IOException e){
      throw new DeserializerException(e);
    }
    finally
    {
      reader.reset();
    }
  }

}
