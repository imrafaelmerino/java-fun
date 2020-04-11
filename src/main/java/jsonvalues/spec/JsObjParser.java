package jsonvalues.spec;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.derializers.specs.SpecDeserializer;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import jsonvalues.JsObj;

import java.io.InputStream;

import static com.dslplatform.json.MyDslJson.INSTANCE;
import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.JsParser.getDeserializer;

public class JsObjParser
{

  /**
   @param spec the Json spec what defines the schema the json has to conform
   @param strict if true, no more keys different than the specified by the spec are allowed
   */
  private final SpecDeserializer deserializer;

  public JsObjParser(final JsObjSpec spec)
  {


    final Tuple2<Vector<String>, Map<String, SpecDeserializer>> pair = createDeserializers(spec.bindings,
                                                                                           HashMap.empty(),
                                                                                           Vector.empty()
                                                                                          );


    deserializer =  DeserializersFactory.INSTANCE.ofObjSpec(pair._1,
                                                  pair._2,
                                                  false
                                                 );

  }


  /**
   * parses an array of bytes into a Json object that must conform the spec of the parser. If the
   * array of bytes doesn't represent a well-formed Json or is a well-formed Json that doesn't
   * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
   * returned.
   *
   * @param bytes a Json object serialized in an array of bytes
   * @return a try computation with the result
   */
  public JsObj parse(byte[] bytes) throws DeserializerException
  {
    return INSTANCE.deserializeToJsObj(requireNonNull(bytes),
                                       deserializer
                                      );

  }

  /**
   * parses a string into a Json object that must conform the spec of the parser. If the
   * string doesn't represent a well-formed Json or is a well-formed Json that doesn't
   * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
   * returned.
   *
   * @param str a Json object serialized in a string
   * @return a try computation with the result
   */
  public JsObj parse(String str) throws DeserializerException
  {

    return INSTANCE.deserializeToJsObj(requireNonNull(str).getBytes(),
                                       deserializer
                                      );
  }

  /**
   * parses an input stream of bytes into a Json object that must conform the spec of the parser. If the
   * the input stream of bytes doesn't represent a well-formed Json object or is a well-formed Json that doesn't
   * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
   * returned. Any I/O exception processing the input stream is wrapped in a Try computation as well
   *
   * @param inputstream the input stream of bytes
   * @return a try computation with the result
   */
  public JsObj parse(InputStream inputstream) throws DeserializerException
  {
    return INSTANCE.deserializeToJsObj(requireNonNull(inputstream),
                                       deserializer
                                      );
  }


  static Tuple2<Vector<String>, Map<String, SpecDeserializer>> createDeserializers(final Map<String, JsSpec> specs,
                                                                                   final Map<String, SpecDeserializer> result,
                                                                                   final Vector<String> requiredKeys
                                                                                  )
  {

    if (specs.isEmpty()) return new Tuple2<>(requiredKeys,
                                             result
    );
    else
    {
      final Tuple2<String, JsSpec> head = specs.head();
      final JsSpec spec = head._2;
      if (spec instanceof Schema<?>)
      {
        if (spec instanceof IsObjSpec)
        {
          final IsObjSpec isObjSpec = (IsObjSpec) spec;
          final Tuple2<Vector<String>, Map<String, SpecDeserializer>> pair = createDeserializers(isObjSpec.spec.bindings,
                                                                                                 HashMap.empty(),
                                                                                                 Vector.empty()
                                                                                                );
          Vector<String> updatedRequiredKeys = requiredKeys;
          if (isObjSpec.required) updatedRequiredKeys = updatedRequiredKeys.append(head._1);
          return createDeserializers(specs.tail(),
                                     result.put(head._1,
                                                 DeserializersFactory.INSTANCE.ofObjSpec(pair._1,
                                                                               pair._2,
                                                                               isObjSpec.nullable
                                                                              )
                                               ),
                                     updatedRequiredKeys
                                    );
        } else if (spec instanceof IsArraySpec)
        {
          final IsArraySpec isArraySpec = (IsArraySpec) spec;
          final Vector<SpecDeserializer> deserializers = JsArrayParser.createDeserializers(isArraySpec.arraySpec.specs,
                                                                                           Vector.empty()
                                                                                          );
          Vector<String> requiredKeysUpdated = requiredKeys;
          if (isArraySpec.required) requiredKeysUpdated=requiredKeysUpdated.append(head._1);
          return createDeserializers(specs.tail(),
                                     result.put(head._1,
                                                 DeserializersFactory.INSTANCE.ofArraySpec(deserializers,
                                                                                 isArraySpec.nullable
                                                                                )
                                               ),
                                     requiredKeysUpdated
                                    );
        } else if (spec instanceof IsArrayOfObjSpec)
        {
          final IsArrayOfObjSpec arrayOfObjSpec = (IsArrayOfObjSpec) spec;
          final Tuple2<Vector<String>, Map<String, SpecDeserializer>> pair = createDeserializers(arrayOfObjSpec.spec.bindings,
                                                                                                 HashMap.empty(),
                                                                                                 Vector.empty()
                                                                                                );
          Vector<String> requiredKeysUpdated = requiredKeys;
          if (arrayOfObjSpec.required) requiredKeysUpdated = requiredKeys.append(head._1);
          return createDeserializers(specs.tail(),
                                     result.put(head._1,
                                                 DeserializersFactory.INSTANCE.ofArrayOfObjSpec(pair._1,
                                                                                      pair._2,
                                                                                      arrayOfObjSpec.nullable,
                                                                                      arrayOfObjSpec.elemNullable
                                                                                     )
                                               ),
                                     requiredKeysUpdated
                                    );
        } else if (spec instanceof JsObjSpec)
        {
          final JsObjSpec jsObjSpec = (JsObjSpec) spec;
          final Tuple2<Vector<String>, Map<String, SpecDeserializer>> pair = createDeserializers(jsObjSpec.bindings,
                                                                                                 HashMap.empty(),
                                                                                                 Vector.empty()
                                                                                                );

          return createDeserializers(specs.tail(),
                                     result.put(head._1,
                                                 DeserializersFactory.INSTANCE.ofObjSpec(pair._1,
                                                                               pair._2,
                                                                               false
                                                                              )
                                               ),
                                     requiredKeys.append(head._1)
                                    );
        } else if (spec instanceof JsArraySpec)
        {
          final JsArraySpec jsArraySpec = (JsArraySpec) spec;
          return createDeserializers(specs.tail(),
                                     result.put(head._1,
                                                 DeserializersFactory.INSTANCE.ofArraySpec(JsArrayParser.createDeserializers(jsArraySpec.specs,
                                                                                                                   Vector.empty()
                                                                                                                  ),
                                                                                 false
                                                                                )
                                               ),
                                     requiredKeys
                                    );
        }
      } else if (spec instanceof JsPredicate)
      {
        final JsPredicate jsPredicate = (JsPredicate) spec;
        final Tuple2<Boolean, SpecDeserializer> pair = getDeserializer(jsPredicate);
        Vector<String> updateReqKeys=requiredKeys;
        if (pair._1) updateReqKeys = updateReqKeys.append(head._1);
        return createDeserializers(specs.tail(),
                                   result.put(head._1,
                                              pair._2
                                             ),
                                  updateReqKeys
                                  );
      }
      throw new RuntimeException("Spec without deserializers " + spec.getClass()
                                                                     .getName());
    }
  }



}


