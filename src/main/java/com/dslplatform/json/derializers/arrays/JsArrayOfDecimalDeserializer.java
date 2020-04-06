package com.dslplatform.json.derializers.arrays;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.derializers.types.JsDecimalDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.io.IOException;
import java.math.BigDecimal;

import jsonvalues.spec.Error;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class JsArrayOfDecimalDeserializer extends JsArrayDeserializer
{

  private JsDecimalDeserializer deserializer;

  public JsArrayOfDecimalDeserializer(final JsDecimalDeserializer deserializer)
  {
    super(Objects.requireNonNull(deserializer));
    this.deserializer = deserializer;
  }

  public JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                         final Function<BigDecimal, Optional<Error>> fn
                                        ) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : arrayEachSuchThat(reader,
                                                              fn
                                                             );
  }

  private JsArray appendNullOrValue(final JsonReader<?> reader,
                                    final Function<BigDecimal, Optional<Error>> fn,
                                    JsArray buffer
                                   ) throws IOException
  {
    return reader.wasNull() ? buffer.append(JsNull.NULL) : buffer.append(deserializer.valueSuchThat(reader,
                                                                                                    fn
                                                                                                   ));

  }

  public JsValue arrayWithNullEachSuchThat(final JsonReader<?> reader,
                                           final Function<BigDecimal, Optional<Error>> fn
                                          ) throws IOException
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

  public JsValue nullOrArrayWithNullEachSuchThat(final JsonReader<?> reader,
                                                 final Function<BigDecimal, Optional<Error>> fn
                                                ) throws IOException
  {
    return reader.wasNull() ? JsNull.NULL : arrayWithNullEachSuchThat(reader,
                                                                      fn
                                                                     );
  }

  public JsArray arrayEachSuchThat(final JsonReader<?> reader,
                                   final Function<BigDecimal, Optional<Error>> fn
                                  ) throws IOException
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
}
