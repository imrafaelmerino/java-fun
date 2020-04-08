package com.dslplatform.json.derializers.types.arrays;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.derializers.types.JsStrDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class JsArrayOfStringDeserializer extends JsArrayDeserializer
{
  private JsStrDeserializer deserializer;

  public JsArrayOfStringDeserializer(final JsStrDeserializer deserializer)
  {
    super(Objects.requireNonNull(deserializer));
    this.deserializer = deserializer;
  }

  public JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                         final Function<String, Optional<Error>> fn
                                        ) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : arrayEachSuchThat(reader,
                                                                fn
                                                               );
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }
  }

  public JsValue arrayWithNullEachSuchThat(final JsonReader<?> reader,
                                           final Function<String, Optional<Error>> fn
                                          ) throws DeserializerException
  {
    try
    {
      if (ifIsEmptyArray(reader)) return EMPTY;

      JsArray buffer = appendNullOrValue(reader,
                                         fn,
                                         EMPTY
                                        );

      while (reader.getNextToken() == ',')
      {
        reader.getNextToken();
        buffer = appendNullOrValue(reader,
                                   fn,
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

  public JsValue nullOrArrayWithNullEachSuchThat(final JsonReader<?> reader,
                                                 final Function<String, Optional<Error>> fn
                                                ) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : arrayWithNullEachSuchThat(reader,
                                                                        fn
                                                                       );
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }
  }

  public JsArray arrayEachSuchThat(final JsonReader<?> reader,
                                   final Function<String, Optional<Error>> fn
                                  ) throws DeserializerException
  {
    try
    {
      if (ifIsEmptyArray(reader)) return EMPTY;

      JsArray buffer = EMPTY.append(deserializer.valueSuchThat(reader,
                                                               fn
                                                              ));
      while (reader.getNextToken() == ',')
      {
        reader.getNextToken();
        buffer = buffer.append(deserializer.valueSuchThat(reader,
                                                          fn
                                                         ));
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
                                    final Function<String, Optional<Error>> fn,
                                    JsArray buffer
                                   ) throws DeserializerException
  {
    try
    {
      return reader.wasNull() ? buffer.append(JsNull.NULL) : buffer.append(deserializer.valueSuchThat(reader,
                                                                                                      fn
                                                                                                     ));
    }
    catch (ParsingException e)
    {
      throw new DeserializerException(e);

    }

  }


}
