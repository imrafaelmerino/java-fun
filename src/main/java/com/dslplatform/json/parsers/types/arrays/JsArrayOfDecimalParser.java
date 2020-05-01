package com.dslplatform.json.parsers.types.arrays;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.parsers.types.JsDecimalParser;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.io.IOException;
import java.math.BigDecimal;

import jsonvalues.spec.Error;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class JsArrayOfDecimalParser extends JsArrayParser
{

  private JsDecimalParser parser;

  public JsArrayOfDecimalParser(final JsDecimalParser parser)
  {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  public JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                         final Function<BigDecimal, Optional<Error>> fn
                                        ) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : arrayEachSuchThat(reader,
                                                                fn
                                                               );
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);
    }
  }

  private JsArray appendNullOrValue(final JsonReader<?> reader,
                                    final Function<BigDecimal, Optional<Error>> fn,
                                    JsArray buffer
                                   ) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? buffer.append(JsNull.NULL) : buffer.append(parser.valueSuchThat(reader,
                                                                                                fn
                                                                                               ));
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);

    }

  }




  public JsArray arrayEachSuchThat(final JsonReader<?> reader,
                                   final Function<BigDecimal, Optional<Error>> fn
                                  ) throws JsParserException
  {
    try
    {
      if (ifIsEmptyArray(reader)) return EMPTY;

      JsArray buffer = EMPTY.append(parser.valueSuchThat(reader,
                                                         fn
                                                        ));
      while (reader.getNextToken() == ',')
      {
        reader.getNextToken();
        buffer = buffer.append(parser.valueSuchThat(reader,
                                                    fn
                                                   ));
      }
      reader.checkArrayEnd();
      return buffer;
    }
    catch (IOException e)
    {
      throw new JsParserException(e);

    }
  }
}
