package jsonvalues.spec;

import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import java.util.HashSet;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;


public class JsArraySpec implements  Schema<JsArray>

{

  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public JsArraySpec optional(){
    return new JsArraySpec(specs,false,nullable);
  }
  @Override
  public JsArraySpec nullable(){
   return new JsArraySpec(specs,required,true);
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

  JsArraySpec(final Vector<JsSpec> specs,boolean required,boolean nullable) {
    this.specs = specs;
    this.required = required;
    this.nullable = nullable;
  }

  JsArraySpec(final Vector<JsSpec> specs) {
    this(specs,true,false);
  }


  public static JsArraySpec tuple(JsSpec spec, JsSpec... others){
    Vector<JsSpec> specs = Vector.empty();
    specs = specs.append(spec);
    for(JsSpec s:others) specs=specs.append(s);
    return new JsArraySpec(specs);
  }

  @Override
  public Set<JsErrorPair> test(final JsArray json)
  {
    return test(JsPath.empty().append(JsPath.fromIndex(-1)), this, new HashSet<>(), json);
  }



  static Set<JsErrorPair> test(final JsPath parent,
                               final JsArraySpec parentSpec,
                               final Set<JsErrorPair> errors,
                               final JsArray array
                              )
  {
    final Vector<JsSpec> specs = parentSpec.specs;
    final int specsSize = parentSpec.specs.size();
    if(specsSize > 0 && array.size() > specsSize && parentSpec.strict){
      errors.add(JsErrorPair.of(parent.tail().index(specsSize),
                                new Error(array.get(specsSize),
                                          SPEC_MISSING)
                               )
                );
      return errors;
    }
    JsPath currentPath = parent;

    for (int i = 0; i < specsSize; i++)
    {

      currentPath = currentPath.inc();
      final JsSpec spec = specs.get(i);

      Functions.addErrors(errors,
                          array.get(i),
                          currentPath,
                          spec
                         );
    }
    return errors;
  }
}
