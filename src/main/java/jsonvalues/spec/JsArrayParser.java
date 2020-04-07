package jsonvalues.spec;

import com.dslplatform.json.DeserializerException;
import com.dslplatform.json.derializers.ValueDeserializer;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import java.io.IOException;
import java.io.InputStream;
import static java.util.Objects.requireNonNull;
import static jsonvalues.JsonLibsFactory.dslJson;
import static jsonvalues.spec.JsParser.getDeserializer;

public class JsArrayParser
{

  private final ValueDeserializer deserializer;

  /**

   @param spec the Json spec what defines the schema that every element in the array has to conform
   */
  public JsArrayParser(final JsArraySpec spec)
  {

    final Vector<ValueDeserializer> deserializers = JsArrayParser.createDeserializers(spec.specs,
                                                                                      Vector.empty()
                                                                                     );
    deserializer = DeserializersFactory.ofArraySpec(deserializers,
                                                    false
                                                   );

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
  public JsArray parse(final byte[] bytes) throws IOException
  {

  return dslJson.deserializeToJsArray(requireNonNull(bytes),
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
    return dslJson.deserializeToJsArray(requireNonNull(str).getBytes(),
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
    return dslJson.deserializeToJsArray(requireNonNull(inputstream),
                                        this.deserializer
                                       );

  }

  static Vector<ValueDeserializer> createDeserializers(final Vector<JsSpec> spec,
                                                       final Vector<ValueDeserializer> result
                                                      )
  {
    if (spec.isEmpty()) return result;

    final JsSpec head = spec.head();
    if (head instanceof IsObjSpec)
    {
      final IsObjSpec isObjSpec = (IsObjSpec) head;
      final Tuple2<Vector<String>, Map<String, ValueDeserializer>> pair = JsObjParser.createDeserializers(isObjSpec.spec.bindings,
                                                                                                          HashMap.empty(),
                                                                                                          Vector.empty()
                                                                                                         );
      return createDeserializers(spec.tail(),
                                 result.append(DeserializersFactory.ofObjSpec(pair._1,
                                                                              pair._2,
                                                                              isObjSpec.nullable
                                                                             )
                                              )
                                );
    } else if (head instanceof IsArraySpec)
    {
      final IsArraySpec arraySpec = (IsArraySpec) head;
      final Vector<ValueDeserializer> deserializers = createDeserializers(arraySpec.arraySpec.specs,
                                                                          Vector.empty()
                                                                         );
      return createDeserializers(spec.tail(),
                                 result.append(
                                   DeserializersFactory.ofArraySpec(deserializers,
                                                                    arraySpec.nullable
                                                                   )
                                              )
                                );
    } else if (head instanceof JsObjSpec)
    {
      final JsObjSpec jsObjSpec = (JsObjSpec) head;
      final Tuple2<Vector<String>, Map<String, ValueDeserializer>> pair = JsObjParser.createDeserializers(jsObjSpec.bindings,
                                                                                                          HashMap.empty(),
                                                                                                          Vector.empty()
                                                                                                         );
      return createDeserializers(spec.tail(),
                                 result.append(
                                   DeserializersFactory.ofObjSpec(pair._1,
                                                                  pair._2,
                                                                  false
                                                                 )
                                              )
                                );
    } else if (head instanceof IsArrayOfObjSpec)
    {
      final IsArrayOfObjSpec arrayOfObjSpec = (IsArrayOfObjSpec) head;
      final Tuple2<Vector<String>, Map<String, ValueDeserializer>> pair =
        JsObjParser.createDeserializers(arrayOfObjSpec.spec.bindings,
                                        HashMap.empty(),
                                        Vector.empty()
                                       );
      return createDeserializers(spec.tail(),
                                 result.append(
                                   DeserializersFactory.ofArrayOfObjSpec(pair._1,
                                                                         pair._2,
                                                                         arrayOfObjSpec.nullable,
                                                                         arrayOfObjSpec.elemNullable
                                                                        )
                                              )
                                );
    } else if (head instanceof JsArraySpec)
    {
      final JsArraySpec jsArraySpec = (JsArraySpec) head;
      return createDeserializers(spec.tail(),
                                 result.append(
                                   DeserializersFactory.ofArraySpec(createDeserializers(jsArraySpec.specs,
                                                                                        Vector.empty()
                                                                                       ),
                                                                    false
                                                                   )
                                              )
                                );
    } else if (head instanceof JsPredicate)
    {
      final JsPredicate jsPredicate = (JsPredicate) head;
      return createDeserializers(spec.tail(),
                                 result.append(getDeserializer(jsPredicate)._2
                                              )
                                );
    }
    throw new RuntimeException("Spec without deserializers " + spec.getClass()
                                                                   .getName());

  }
}

