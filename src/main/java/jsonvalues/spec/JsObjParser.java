package jsonvalues.spec;

import com.dslplatform.json.derializers.ValueDeserializer;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;

import static jsonvalues.spec.JsParser.getDeserializer;

public class JsObjParser
{

  /**
   @param spec the Json spec what defines the schema the json has to conform
   @param strict if true, no more keys different than the specified by the spec are allowed
   */
  public JsObjParser(final JsObjSpec spec,
                     final boolean strict
                    )
  {}

  static Tuple2<Vector<String>, Map<String, ValueDeserializer>> createDeserializers(final Map<String, JsSpec> specs,
                                                                                    final Map<String, ValueDeserializer> result,
                                                                                    final Vector<String> requiredKeys
                                                                                   )
  {

    if (specs.isEmpty()) return new Tuple2<>(requiredKeys,
                                             result);
    else
    {
      final Tuple2<String, JsSpec> head = specs.head();
      final JsSpec spec = head._2;
      if (spec instanceof Schema<?>)
      {
        if (spec instanceof JsObjSpec)
        {
          final JsObjSpec jsObjSpec = (JsObjSpec) spec;
          final Tuple2<Vector<String>, Map<String, ValueDeserializer>> pair = createDeserializers(jsObjSpec.bindings,
                                                                                                  HashMap.empty(),
                                                                                                  Vector.empty()
                                                                                                 );
          return createDeserializers(specs.tail(),
                              result.put(head._1,
                                         DeserializersFactory.ofObjSpec(
                                           pair._1,
                                           pair._2,
                                           false
                                                                       )
                                        ),
                              requiredKeys.append(head._1)
                             );
        }
        else if (spec instanceof JsArraySpec)
        {
          final JsArraySpec jsArraySpec = (JsArraySpec) spec;
          return createDeserializers(specs.tail(),
                              result.put(head._1,
                                         DeserializersFactory.ofArraySpec(JsArrayParser.createDeserializers(jsArraySpec.specs,
                                                                                                            Vector.empty()
                                                                                                           ),
                                                                          false
                                                                         )
                                        ),
                              requiredKeys
                             );
        }
      }
      else if (spec instanceof JsPredicate)
      {
        final JsPredicate jsPredicate = (JsPredicate) spec;
        final Tuple2<Boolean, ValueDeserializer> pair = getDeserializer(jsPredicate);
        if (pair._1) requiredKeys.append(head._1);
        return createDeserializers(specs.tail(),
                            result.put(head._1,
                                       pair._2
                                      ),
                            requiredKeys
                           );
      }
      throw new RuntimeException("Spec without deserializers "+ spec.getClass().getName());
    }
  }
}


