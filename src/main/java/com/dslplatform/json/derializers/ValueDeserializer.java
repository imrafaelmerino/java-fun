package com.dslplatform.json.derializers;

import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.JsonReader;
import jsonvalues.JsValue;


@FunctionalInterface
public interface ValueDeserializer
{

  JsValue read(JsonReader<?> reader)throws DeserializerException;
}
