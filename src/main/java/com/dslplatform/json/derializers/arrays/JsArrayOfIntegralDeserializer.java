package com.dslplatform.json.derializers.arrays;
import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.derializers.types.JsIntegralDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final  class JsArrayOfIntegralDeserializer extends JsArrayDeserializer
{

    private JsIntegralDeserializer deserializer;

    public JsArrayOfIntegralDeserializer(final JsIntegralDeserializer deserializer)
    {
        super(Objects.requireNonNull(deserializer));
        this.deserializer = deserializer;
    }

    public JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                           final Function<BigInteger, Optional<Error>> fn
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
                                             final Function<BigInteger, Optional<Error>> fn
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
                                                   final Function<BigInteger, Optional<Error>> fn
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

    public JsArray arrayEachSuchThat(final JsonReader<?>reader,
                                     final Function<BigInteger, Optional<Error>> fn
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
                                      final Function<BigInteger, Optional<Error>> fn,
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
