package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

class Functions
{
  static Function<JsValue, Optional<Error>> testArray(boolean required,
                                                      boolean nullable
                                                     )
  {
    return value ->
    {
      final Optional<Error> error = testFlags(required,
                                              nullable
                                             ).apply(value);
      if (error.isPresent()) return error;
      return value.isNull() || value.isArray() ? Optional.empty() : Optional.of(new Error(value,
                                                                        ARRAY_EXPECTED
      ));
    };

  }

  static Function<JsValue, Optional<Error>> testElem(final Predicate<JsValue> elemCondition,
                                                     final ERROR_CODE errorCode,
                                                     final boolean required,
                                                     final boolean nullable
                                                    )
  {

    return value ->
    {
      final Optional<Error> error = testFlags(required,
                                              nullable
                                             ).apply(value);
      if (error.isPresent() || value.isNull()) return error;
      if (!elemCondition.test(value)) return Optional.of(new Error(value,
                                                                   errorCode)
                                                        );
      return Optional.empty();
    };
  }

  static Function<JsValue, Optional<Error>> testArrayOfTestedValue(final Function<JsValue, Optional<Error>> elemCondition,
                                                                   final boolean required,
                                                                   final boolean nullable
                                                                  )
  {

    return testArrayPredicate(required,
                              nullable,
                              array ->
                              {
                                for (final JsValue next : array)
                                {
                                  final Optional<Error> result = elemCondition.apply(next);
                                  if (result.isPresent()) return result;
                                }
                                return Optional.empty();
                              }
                             );
  }

  private static Function<JsValue, Optional<Error>> testArrayPredicate(final boolean required,
                                                                       final boolean nullable,
                                                                       final Function<JsArray, Optional<Error>> validation
                                                                      )
  {
    return value ->
    {
      final Optional<Error> errors = testArray(required,
                                               nullable
                                              ).apply(value);
      if (errors.isPresent() || value.isNull()) return errors;
      return validation.apply(value.toJsArray());
    };
  }

  private static Function<JsValue, Optional<Error>> testFlags(boolean required,
                                                              boolean nullable
                                                             )
  {
    return value ->
    {
      if (value.isNothing() && required) return Optional.of(new Error(value,
                                                                      REQUIRED
      ));
      if (value.isNull() && !nullable) return Optional.of(new Error(value,
                                                                    NULL
      ));
      return Optional.empty();
    };
  }

  static void addErrors(final Set<JsErrorPair> errors,
                        final JsValue value,
                        final JsPath currentPath,
                        final JsSpec spec
                       )
  {
    if (spec instanceof JsObjSpec)
    {
      final JsObjSpec objSpec = (JsObjSpec) spec;
      if(objSpec.nullable && value.isNull()) return;
      if (!value.isObj())
      {
        errors.add(JsErrorPair.of(currentPath,
                                  new Error(value,
                                            OBJ_EXPECTED
                                  )
                                 )
                  );
      }
      else
      {

        final JsObj obj = value.toJsObj();
        errors.addAll(JsObjSpec.test(currentPath,
                                     objSpec,
                                     errors,
                                     obj
                                    ));
      }

    }

    else if (spec instanceof JsArraySpec)
    {

      if (!value.isArray()) {errors.add(JsErrorPair.of(currentPath,
                                                      new Error(value,
                                                                ARRAY_EXPECTED
                                                      )
                                                     ));}
      else
      {
        final JsArraySpec arraySpec = (JsArraySpec) spec;
        final JsArray array = value.toJsArray();
        errors.addAll(JsArraySpec.test(currentPath.append(JsPath.fromIndex(-1)),
                                       arraySpec,
                                       errors,
                                       array
                                      )
                     );
      }

    }

    else if(spec instanceof IsArrayOfObjSpec){
      final IsArrayOfObjSpec isArraySpec = (IsArrayOfObjSpec) spec;
      final Optional<Error> error = testFlags(isArraySpec.required,
                                              isArraySpec.nullable).apply(value);
      if(error.isPresent()) errors.add(JsErrorPair.of(currentPath,error.get()));
      else if(value.isNull())return;
      else if (!value.isArray())
      {
        errors.add(JsErrorPair.of(currentPath,
                                  new Error(value,
                                            ARRAY_EXPECTED
                                  )
                                 ));
      }
      else
      {
        final JsArray array = value.toJsArray();
        for (int i = 0; i < array.size(); i++)
        {
          Functions.addErrors(errors,
                              array.get(i),
                              currentPath.index(i),
                              isArraySpec.spec
                             );
        }
      }


    }
    else
    {
      final JsValuePredicate predicate = (JsValuePredicate) spec;
      final Optional<Error> error = predicate.test(value);
      error.ifPresent(e -> errors.add(JsErrorPair.of(currentPath,
                                                     e
                                                    )
                                     )
                     );
    }
  }
}
