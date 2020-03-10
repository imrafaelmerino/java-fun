package jsonvalues.spec;

import jsonvalues.JsArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JsArraySpec implements  Schema<JsArray>
{
  private final List<JsSpec> specs = new ArrayList<>();

  public static JsArraySpec of(JsSpec spec, JsSpec... others){
    final JsArraySpec arraySpec = new JsArraySpec();
    arraySpec.specs.add(spec);
    for(JsSpec x:others) arraySpec.specs.add(x);
    return arraySpec;
  }

  @Override
  public Set<JsErrorPair> validate(final JsArray json)
  {

    return null;
  }

  static Set<JsErrorPair> validate(Set<JsErrorPair> errors,final JsArray json)
  {
    return null;
  }
}
