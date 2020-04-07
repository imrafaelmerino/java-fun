package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsNothing;
import jsonvalues.JsNull;
import jsonvalues.JsPath;

import java.util.HashSet;
import java.util.Set;

public class IsArraySpec implements Schema<JsArray>
{
  final JsArraySpec arraySpec;
  final boolean nullable;
  final boolean required;


  public IsArraySpec(final JsArraySpec arraySpec,
                     final boolean nullable,
                     final boolean required
                    )
  {
    this.arraySpec = arraySpec;
    this.nullable = nullable;
    this.required = required;
  }

  @Override
  public Set<JsErrorPair> test(final JsArray array)
  {
    if(array == null){
      Set<JsErrorPair> errors = new HashSet<>();
      if(nullable) return errors;
      else {
        errors.add(JsErrorPair.of(JsPath.empty(),new Error(JsNull.NULL,ERROR_CODE.NULL)));
        return  errors;
      }
    }
    else if(array.isNothing()){
      Set<JsErrorPair> errors = new HashSet<>();
      if(!required) return errors;
      else{
        errors.add(JsErrorPair.of(JsPath.empty(),new Error(JsNothing.NOTHING,ERROR_CODE.REQUIRED)));
        return errors;
      }
    }

    return arraySpec.test(array);
  }
}


