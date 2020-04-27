package jsonvalues.spec;

import com.dslplatform.json.MyDslJson;
import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;

import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class JsArrayParser
{

  private final SpecDeserializer deserializer;

  /**

   @param spec the Json spec what defines the schema that every element in the array has to conform
   */
  public JsArrayParser(final JsArraySpec spec)
  {

    deserializer = spec.deserializer();

  }


  /**
   * parses an array of bytes into a Json array that must conform the spec of the parser. If the
   * array of bytes doesn't represent a well-formed Json  or is a well-formed Json that doesn't
   * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
   * returned.
   *
   * @param bytes a Json array serialized in an array of bytes
   * @return a try computation with the result
   */
  public JsArray parse(final byte[] bytes)
  {

    return MyDslJson.INSTANCE.deserializeToJsArray(requireNonNull(bytes),
                                                   this.deserializer
                                                  );
  }


  /**
   * parses a string into a Json array that must conform the spec of the parser. If the
   * string doesn't represent a well-formed Json array or is a well-formed Json that doesn't
   * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
   * returned.
   *
   * @param str a Json array serialized in a string
   * @return a try computation with the result
   */
  public JsArray parse(String str) throws DeserializerException
  {
    return MyDslJson.INSTANCE.deserializeToJsArray(requireNonNull(str).getBytes(),
                                                   this.deserializer
                                                  );
  }

  /**
   * parses an input stream of bytes into a Json array that must conform the spec of the parser. If the
   * the input stream of bytes doesn't represent a well-formed Json array or is a well-formed Json that doesn't
   * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
   * returned. Any I/O exception processing the input stream is wrapped in a Try computation as well
   *
   * @param inputstream the input stream of bytes
   * @return a try computation with the result
   */
  public JsArray parse(InputStream inputstream) throws DeserializerException
  {
    return MyDslJson.INSTANCE.deserializeToJsArray(requireNonNull(inputstream),
                                                   this.deserializer
                                                  );

  }


}

