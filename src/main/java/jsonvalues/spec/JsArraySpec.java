package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.*;

public class JsArraySpec implements  Schema<JsArray>
{
  private final List<JsSpec> specs = new ArrayList<>();
  private boolean strict = true;

  public static JsArraySpec of(JsSpec spec, JsSpec... others){
    final JsArraySpec arraySpec = new JsArraySpec();
    arraySpec.specs.add(spec);
    arraySpec.specs.addAll(Arrays.asList(others));
    return arraySpec;
  }

  @Override
  public Set<JsErrorPair> test(final JsArray json)
  {

    return test(JsPath.empty().append(JsPath.fromIndex(-1)),this,new HashSet<>(),json);
  }



  static Set<JsErrorPair> test(final JsPath parent,
                               final JsArraySpec parentSpec,
                               final Set<JsErrorPair> errors,
                               final JsArray array
                              )
  {
    final List<JsSpec> specs = parentSpec.specs;
    final int specsSize = parentSpec.specs.size();
    if(specsSize > 0 && array.size() > specsSize && parentSpec.strict){
      errors.add(JsErrorPair.of(parent.tail().index(specsSize),
                                new Error(array.get(specsSize),
                                          ERROR_CODE.SPEC_MISSING)
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
