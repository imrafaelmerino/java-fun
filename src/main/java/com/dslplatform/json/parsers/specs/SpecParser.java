package com.dslplatform.json.parsers.specs;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import jsonvalues.JsValue;


@FunctionalInterface
public interface SpecParser
{

  JsValue parse(JsonReader<?> reader)throws JsParserException;
}
