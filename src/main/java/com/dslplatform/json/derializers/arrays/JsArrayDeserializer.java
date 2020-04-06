package com.dslplatform.json.derializers.arrays;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.derializers.types.JsTypeDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public abstract class JsArrayDeserializer
{

  final static JsArray EMPTY = JsArray.empty();
  private final JsTypeDeserializer deserializer;

  public JsArrayDeserializer(final JsTypeDeserializer deserializer)
  {
    this.deserializer = deserializer;
  }

  public JsValue nullOrArray(final JsonReader<?> reader) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : array(reader);
  }

  public JsArray arrayWithNull(final JsonReader<?> reader) throws IOException
  {
    if (ifIsEmptyArray(reader)) return EMPTY;

    JsArray buffer = appendNullOrValue(reader,
                                       EMPTY
                                      );
    while (reader.getNextToken() == ',')
    {
      reader.getNextToken();
      buffer = appendNullOrValue(reader,
                                 buffer
                                );
    }
    reader.checkArrayEnd();
    return buffer;
  }

  private JsArray appendNullOrValue(final JsonReader<?> reader,
                                    final JsArray buffer
                                   ) throws IOException
  {
    return reader.wasNull() ? buffer.append(JsNull.NULL) : buffer.append(deserializer.value(reader));

  }

  public JsValue nullOrArrayWithNull(final JsonReader<?> reader) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : arrayWithNull(reader);
  }

  public JsArray array(final JsonReader<?> reader) throws IOException
  {
    if (ifIsEmptyArray(reader)) return EMPTY;
    JsArray buffer = EMPTY.append(deserializer.value(reader));
    while (reader.getNextToken() == ',')
    {
      reader.getNextToken();
      buffer = buffer.append(deserializer.value(reader));
    }
    reader.checkArrayEnd();
    return buffer;
  }


  public JsValue nullOrArraySuchThat(final JsonReader<?> reader,
                                     final Function<JsArray, Optional<Error>> fn
                                    ) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : arraySuchThat(reader,
                                                          fn
                                                         );
  }

  public JsValue arrayWithNullSuchThat(final JsonReader<?> reader,
                                       final Function<JsArray, Optional<Error>> fn
                                      ) throws IOException
  {
    final JsArray array = arrayWithNull(reader);
    final Optional<Error> result = fn.apply(array);
    if (!result.isPresent()) return array;
    throw reader.newParseError(result.toString());

  }

  public JsValue nullOrArrayWithNullSuchThat(final JsonReader<?> reader,
                                             final Function<JsArray, Optional<Error>> fn
                                            ) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : arrayWithNullSuchThat(reader,
                                                                  fn
                                                                 );
  }

  public JsArray arraySuchThat(final JsonReader<?> reader,
                               final Function<JsArray, Optional<Error>> fn
                              ) throws IOException
  {
    final JsArray array = array(reader);
    final Optional<Error> result = fn.apply(array);
    if (!result.isPresent()) return array;
    throw reader.newParseError(result.toString());

  }

  boolean ifIsEmptyArray(final JsonReader<?> reader) throws IOException
  {
    if (reader.last() != '[') throw reader.newParseError("Expecting '[' for list start");
    reader.getNextToken();
    return reader.last() == ']';
  }

}
