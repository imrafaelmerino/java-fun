package com.dslplatform.json.parsers.types.arrays;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.parsers.types.JsValueParser;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import jsonvalues.spec.Error;

import java.util.function.Function;

public final class JsArrayOfValueParser extends JsArrayParser
{
  private JsValueParser parser;

  public JsArrayOfValueParser(final JsValueParser parser)
  {
    super(Objects.requireNonNull(parser));
    this.parser = parser;
  }

  public JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                         final Function<JsValue, Optional<Error>> fn
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



  public JsArray arrayEachSuchThat(final JsonReader<?> reader,
                                   final Function<JsValue, Optional<Error>> fn
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
