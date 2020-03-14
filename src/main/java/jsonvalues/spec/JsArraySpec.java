package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.*;

public class JsArraySpec implements  Schema<JsArray>
{
  private final List<JsSpec> specs = new ArrayList<>();

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

    JsPath currentPath = parent;
    final List<JsSpec> specs = parentSpec.specs;
    for (int i = 0; i < specs.size(); i++)
    {

      currentPath = currentPath.inc();
      final JsSpec spec = specs.get(i);
      Functions.addErrors(errors,
                          array.get(currentPath),
                          currentPath,
                          spec
                         );
    }

    return errors;
  }
}
