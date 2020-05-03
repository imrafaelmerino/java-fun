package com.dslplatform.json.parsers;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final  class JsArrayOfIntegralParser extends JsArrayParser
{

    private JsIntegralParser parser;

    JsArrayOfIntegralParser(final JsIntegralParser parser)
    {
        super(Objects.requireNonNull(parser));
        this.parser = parser;
    }

    JsValue nullOrArrayEachSuchThat(final JsonReader<?> reader,
                                    final Function<BigInteger, Optional<Error>> fn
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


    JsArray arrayEachSuchThat(final JsonReader<?> reader,
                              final Function<BigInteger, Optional<Error>> fn
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