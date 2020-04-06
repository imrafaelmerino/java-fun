package com.dslplatform.json;

import com.dslplatform.json.derializers.ValueDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

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

  private JsonReader<?> getReader(final InputStream is) throws IOException
  {
    return localReader.get()
                      .process(is);
  }

  public JsObj deserializeToJsObj(final byte[] bytes,
                                  final ValueDeserializer deserializer
                                 ) throws IOException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return deserializer.read(reader)
                         .toJsObj();
    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final byte[] bytes,
                                      final Function<JsonReader<?>, JsValue> deserializer
                                     ) throws IOException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return deserializer.apply(reader)
                         .toJsArray();
    }
    finally
    {
      reader.reset();
    }
  }

  public JsObj deserializeToJsObj(final InputStream is,
                                  final Function<JsonReader<?>, JsValue> deserializer

                                 ) throws IOException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return deserializer.apply(reader)
                         .toJsObj();
    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final InputStream is,
                                      final Function<JsonReader<?>, JsValue> deserializer
                                     ) throws IOException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return deserializer.apply(reader)
                         .toJsArray();
    }
    finally
    {
      reader.reset();
    }
  }

}
