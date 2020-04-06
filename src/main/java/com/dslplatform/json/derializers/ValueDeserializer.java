package com.dslplatform.json.derializers;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsValue;

import java.io.IOException;

@FunctionalInterface
public interface ValueDeserializer
{

  JsValue read(JsonReader<?> reader)throws IOException;
}
