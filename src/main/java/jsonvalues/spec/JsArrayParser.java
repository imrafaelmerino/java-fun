package jsonvalues.spec;

import com.dslplatform.json.derializers.ValueDeserializer;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;

import static jsonvalues.spec.JsParser.getDeserializer;

public class JsArrayParser
{

  private final ValueDeserializer deserializer;

  /**

   @param spec the Json spec what defines the schema that every element in the array has to conform
   */
  public JsArrayParser(final JsArraySpec spec
                      )
  {

    final Vector<ValueDeserializer> deserializers = JsArrayParser.createDeserializers(spec.specs,
                                                                                      Vector.empty()
                                                                                     );
    deserializer = DeserializersFactory.ofArraySpec(deserializers,
                                                    false
                                                   );

  }



  static Vector<ValueDeserializer> createDeserializers(final Vector<JsSpec> spec,
                                                       final Vector<ValueDeserializer> result
                                                      )
  {
    if (spec.isEmpty()) return result;

    final JsSpec head = spec.head();

    if (head instanceof JsObjSpec)
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

