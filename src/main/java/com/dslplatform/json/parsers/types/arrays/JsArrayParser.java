package com.dslplatform.json.parsers.types.arrays;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.parsers.types.AbstractParser;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;
import jsonvalues.spec.Error;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public abstract class JsArrayParser
{

  final static JsArray EMPTY = JsArray.empty();
  private final AbstractParser parser;

  public JsArrayParser(final AbstractParser parser)
  {
    this.parser = parser;
  }

  public JsValue nullOrArray(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : array(reader);
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);
    }
  }


  private JsArray appendNullOrValue(final JsonReader<?> reader,
                                    final JsArray buffer
                                   )throws JsParserException
  {
    try
    {
      return reader.wasNull() ? buffer.append(JsNull.NULL) : buffer.append(parser.value(reader));
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);
    }

  }


  public JsArray array(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      if (ifIsEmptyArray(reader)) return EMPTY;
      JsArray buffer = EMPTY.append(parser.value(reader));
      while (reader.getNextToken() == ',')
      {
        reader.getNextToken();
        buffer = buffer.append(parser.value(reader));
      }
      reader.checkArrayEnd();
      return buffer;
    }
    catch (IOException e)
    {
      throw new JsParserException(e);
    }
  }


  public JsValue nullOrArraySuchThat(final JsonReader<?> reader,
                                     final Function<JsArray, Optional<Error>> fn
                                    ) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : arraySuchThat(reader,
                                                            fn
                                                           );
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e);
    }
  }




  public JsArray arraySuchThat(final JsonReader<?> reader,
                               final Function<JsArray, Optional<Error>> fn
                              ) throws JsParserException
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
      throw new JsParserException(e);

    }

  }

  boolean ifIsEmptyArray(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      if (reader.last() != '[')
        throw reader.newParseError("Expecting '[' for list start");
      reader.getNextToken();
      return reader.last() == ']';
    }
    catch (IOException e)
    {
      throw new JsParserException(e);
    }
  }

}
