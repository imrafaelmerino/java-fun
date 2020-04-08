package com.dslplatform.json.derializers.types.arrays;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.derializers.types.AbstractDeserializer;
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
  private final AbstractDeserializer deserializer;

  public JsArrayDeserializer(final AbstractDeserializer deserializer)
  {
    this.deserializer = deserializer;
  }

  public JsValue nullOrArray(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : array(reader);
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsArray arrayWithNull(final JsonReader<?> reader) throws DeserializerException
  {
    try
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
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
  }

  private JsArray appendNullOrValue(final JsonReader<?> reader,
                                    final JsArray buffer
                                   )throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? buffer.append(JsNull.NULL) : buffer.append(deserializer.value(reader));
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }

  }

  public JsValue nullOrArrayWithNull(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : arrayWithNull(reader);
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsArray array(final JsonReader<?> reader) throws DeserializerException
  {
    try
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
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
  }


  public JsValue nullOrArraySuchThat(final JsonReader<?> reader,
                                     final Function<JsArray, Optional<Error>> fn
                                    ) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : arraySuchThat(reader,
                                                            fn
                                                           );
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsValue arrayWithNullSuchThat(final JsonReader<?> reader,
                                       final Function<JsArray, Optional<Error>> fn
                                      ) throws DeserializerException
  {
    try
    {
      final JsArray array = arrayWithNull(reader);
      final Optional<Error> result = fn.apply(array);
      if (!result.isPresent()) return array;
      throw reader.newParseError(result.toString());
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsValue nullOrArrayWithNullSuchThat(final JsonReader<?> reader,
                                             final Function<JsArray, Optional<Error>> fn
                                            ) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : arrayWithNullSuchThat(reader,
                                                                    fn
                                                                   );
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);
    }
  }

  public JsArray arraySuchThat(final JsonReader<?> reader,
                               final Function<JsArray, Optional<Error>> fn
                              ) throws DeserializerException
  {
    try
    {
      final JsArray array = array(reader);
      final Optional<Error> result = fn.apply(array);
      if (!result.isPresent()) return array;
      throw reader.newParseError(result.toString());
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }

  }

  boolean ifIsEmptyArray(final JsonReader<?> reader) throws DeserializerException
  {
    try
    {
      if (reader.last() != '[') throw reader.newParseError("Expecting '[' for list start");
      reader.getNextToken();
      return reader.last() == ']';
    }
    catch (IOException e)
    {
      throw new DeserializerException(e);
    }
  }

}
