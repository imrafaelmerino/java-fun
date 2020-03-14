package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsObjSuchThat extends AbstractPredicate implements JsObjPredicate
{

  final Predicate<JsObj> predicate;

  public IsObjSuchThat(final boolean required,
                       final boolean nullable,
                       final Predicate<JsObj> predicate
                      )
  {
    super(required,
          nullable
         );
    this.predicate = predicate;
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isObj,
                                                                          OBJ_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent())return error;
    return Functions.testElem(v -> predicate.test(v.toJsObj()),
                              OBJ_CONDITION,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
