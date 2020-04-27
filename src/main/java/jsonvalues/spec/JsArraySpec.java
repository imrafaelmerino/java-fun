package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.*;


public class JsArraySpec implements JsSpec

{

  @Override
  public JsArraySpec optional()
  {
    return new JsArraySpec(specs,
                           false,
                           nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {

    Vector<SpecDeserializer> deserializers = Vector.empty();
    for (final JsSpec spec : specs)
    {
      deserializers = deserializers.append(spec.deserializer());
    }
    return DeserializersFactory.INSTANCE.ofArraySpec(deserializers,
                                                     nullable
                                                    );
  }



  @Override
  public JsArraySpec nullable()
  {
    return new JsArraySpec(specs,
                           required,
                           true);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  Vector<JsSpec> specs;
  private boolean strict = true;
  final boolean required;
  final boolean nullable;

  JsArraySpec(final Vector<JsSpec> specs,
              boolean required,
              boolean nullable
             )
  {
    this.specs = specs;
    this.required = required;
    this.nullable = nullable;
  }

  JsArraySpec(final Vector<JsSpec> specs)
  {
    this(specs,
         true,
         false);
  }


  public static JsArraySpec tuple(JsSpec spec,
                                  JsSpec... others
                                 )
  {
    Vector<JsSpec> specs = Vector.empty();
    specs = specs.append(spec);
    for (JsSpec s : others) specs = specs.append(s);
    return new JsArraySpec(specs);
  }


  public Set<JsErrorPair> test(final JsArray array){
    return test(JsPath.empty(),array);
  }

  @Override
  public Set<JsErrorPair> test(final JsPath parentPath,
                               final JsValue value
                              )
  {
    return test(JsPath.empty()
                      .append(JsPath.fromIndex(-1)),
                this,
                new HashSet<>(),
                value
               );
  }
  private Set<JsErrorPair> test(final JsPath parent,
                               final JsArraySpec parentSpec,
                               final Set<JsErrorPair> errors,
                               final JsValue parentValue
                              )
  {
    if(parentValue.isNull() && nullable) return errors;

    if(!parentValue.isArray()) {
      errors.add(JsErrorPair.of(parent,new Error(parentValue,ARRAY_EXPECTED)));
      return errors;
    }
    JsArray array = parentValue.toJsArray();
    final Vector<JsSpec> specs = parentSpec.specs;
    final int specsSize = parentSpec.specs.size();
    if (specsSize > 0 && array.size() > specsSize && parentSpec.strict)
    {
      errors.add(JsErrorPair.of(parent.tail()
                                      .index(specsSize),
                                new Error(array.get(specsSize),
                                          SPEC_MISSING
                                )
                               )
                );
      return errors;
    }
    JsPath currentPath = parent;

    for (int i = 0; i < specsSize; i++)
    {
      currentPath = currentPath.inc();
      final JsSpec spec = specs.get(i);
      errors.addAll(spec.test(currentPath,array.get(i)));
    }
    return errors;
  }


}
