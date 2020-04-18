package com.dslplatform.json.derializers.types.arrays;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.derializers.types.JsLongDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.LongFunction;

public final  class JsArrayOfLongDeserializer extends JsArrayDeserializer
{
    private JsLongDeserializer deserializer;

    public JsArrayOfLongDeserializer(final JsLongDeserializer deserializer)
    {
        super(Objects.requireNonNull(deserializer));
        this.deserializer = deserializer;
    }

    public JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                           final LongFunction<Optional<Error>> fn
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




    public JsArray arrayEachSuchThat(final JsonReader<?>reader,
                                     final LongFunction<Optional<Error>> fn
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
                                      final LongFunction<Optional<Error>> fn,
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
