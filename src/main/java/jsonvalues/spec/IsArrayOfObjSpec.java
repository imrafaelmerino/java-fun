package jsonvalues.spec;

import jsonvalues.*;
import java.util.HashSet;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.NULL;
import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

public class IsArrayOfObjSpec implements Schema<JsArray>
{

  final boolean nullable;
  final boolean required;
  final JsObjSpec spec;

  IsArrayOfObjSpec(final boolean nullable,
                   final boolean required,
                   final JsObjSpec jsObjSpec
                  )
  {
    this.nullable = nullable;
    this.required = required;
    this.spec = jsObjSpec;

  }

  @Override
  public Set<JsErrorPair> test(final JsArray array)
  {
    if (array == null)
    {
      if (nullable) return new HashSet<>();
      else
      {
        final HashSet<JsErrorPair> empty = new HashSet<>();
        empty.add(JsErrorPair.of(JsPath.empty(),
                                 new Error(JsNull.NULL,
                                           NULL
                                 )
                                ));
        return empty;
      }
    }
    return apply(JsPath.fromIndex(-1),
                 array
                );
  }

  public Set<JsErrorPair> apply(final JsPath path,
                                final JsArray array
                               )
  {
    Set<JsErrorPair> result = new HashSet<>();
    if (array.isEmpty()) return result;
    final JsPath currentPath = path.inc();
    for (JsValue value : array)
    {
     if (!value.isObj())
      {
        result.add(JsErrorPair.of(currentPath,
                                  new Error(value,
                                            OBJ_EXPECTED
                                  )
                                 ));
      } else
      {
        result.addAll(spec.test(value.toJsObj()));
      }

    }
    return result;
  }

}


