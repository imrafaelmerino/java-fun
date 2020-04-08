package com.dslplatform.json.derializers.specs;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import jsonvalues.JsValue;


@FunctionalInterface
public interface SpecDeserializer
{

  JsValue read(JsonReader<?> reader)throws DeserializerException;
}
