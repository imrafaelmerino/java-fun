package jsonvalues.spec;

import jsonvalues.JsNothing;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsPath;

import java.util.HashSet;
import java.util.Set;

public class IsObjSpec implements Schema<JsObj>
{

  final boolean nullable;
  final boolean required;
  final JsObjSpec spec;

  public IsObjSpec(final boolean nullable,
                   final boolean required,
                   final JsObjSpec spec
                  )
  {
    this.nullable = nullable;
    this.required = required;
    this.spec = spec;
  }

  @Override
  public Set<JsErrorPair> test(final JsObj obj)
  {
    Set<JsErrorPair> errors = new HashSet<>();
    if (obj == null){
      if (nullable) return errors;
      else
      {
        errors.add(JsErrorPair.of(JsPath.empty(),
                                  new Error(JsNull.NULL,
                                            ERROR_CODE.NULL
                                  )
                                 )
                  );

        return errors;
      }
    }
    else if (obj.isNothing())
    {
      if (!required) return errors;
      else
      {
        errors.add(JsErrorPair.of(JsPath.empty(),
                                  new Error(JsNothing.NOTHING,
                                            ERROR_CODE.REQUIRED
                                  )
                                 ));

        return errors;
      }
    }
    return spec.test(obj);
  }


}
