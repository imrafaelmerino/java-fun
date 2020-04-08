package com.dslplatform.json.derializers.types;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import io.vavr.collection.HashMap;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public final class JsObjDeserializer extends AbstractJsObjDeserializer
{

  private final JsValueDeserializer valueDeserializer;

  public JsObjDeserializer(final JsValueDeserializer valueDeserializer)
  {
    this.valueDeserializer = valueDeserializer;
  }

  @Override
  public JsObj value(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      if (isEmptyObj(reader)) return EMPTY_OBJ;

      String key = reader.readKey();
      HashMap<String, JsValue> map = EMPTY_MAP.put(key,
                                                   valueDeserializer.value(reader
                                                                          )
                                                  );
      byte nextToken;
      while ((nextToken = reader.getNextToken()) == ',')
      {
        reader.getNextToken();
        key = reader.readKey();
        map = map.put(key,
                      valueDeserializer.value(reader
                                             )
                     );
      }
      if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
      return new JsObj(map);
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);

    }
  }

  public JsObj valueSuchThat(final JsonReader<?> reader,
                             final Function<JsObj, Optional<Error>> fn
                            ) throws DeserializerException
  {
    try
    {
      final JsObj value = value(reader);
      final Optional<Error> result = fn.apply(value);
      if (!result.isPresent()) return value;
      throw reader.newParseError(result.toString());
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }

  }

  public JsValue nullOrValueSuchThat(final JsonReader<?> reader,
                                     final Function<JsObj, Optional<Error>> fn
                                    ) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : valueSuchThat(reader,
                                                            fn
                                                           );
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }
  }
}
