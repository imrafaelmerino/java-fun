package com.dslplatform.json.derializers.types;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.derializers.types.arrays.JsArrayOfValueDeserializer;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.spec.Error;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public final class JsValueDeserializer extends AbstractDeserializer
{
  private JsObjDeserializer objDeserializer;

  private JsArrayOfValueDeserializer arrayDeserializer;

  public void setNumberDeserializer(final JsNumberDeserializer numberDeserializer)
  {
    this.numberDeserializer = numberDeserializer;
  }

  private JsNumberDeserializer numberDeserializer;

  public void setObjDeserializer(final JsObjDeserializer objDeserializer)
  {
    this.objDeserializer = objDeserializer;
  }

  public void setArrayDeserializer(final JsArrayOfValueDeserializer arrayDeserializer)
  {
    this.arrayDeserializer = arrayDeserializer;
  }

  @Override
  public JsValue value(final JsonReader<?> reader) throws DeserializerException
  {

    try
    {
      switch (reader.last())
      {
        case 't':
          if (!reader.wasTrue())
          {
            throw new DeserializerException(reader.newParseErrorAt("Expecting 'true' for true constant",
                                                                   0
                                                                  ));
          }
          return JsBool.TRUE;
        case 'f':
          if (!reader.wasFalse())
          {
            throw new DeserializerException(reader.newParseErrorAt("Expecting 'false' for false constant",
                                                                         0
                                                                        ));
          }
          return JsBool.FALSE;
        case '"':
          return JsStr.of(reader.readString());
        case '{':
          return objDeserializer.value(reader);
        case '[':
          return arrayDeserializer.array(reader);
        default:
          return numberDeserializer.value(reader);
      }
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsValue valueSuchThat(final JsonReader<?> reader,
                               final Function<JsValue, Optional<Error>> fn

                              ) throws DeserializerException
  {
    try
    {
      final JsValue value = value(reader);
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
                                     final Function<JsValue, Optional<Error>> fn
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
