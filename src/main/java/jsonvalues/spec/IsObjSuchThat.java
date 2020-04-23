package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;
import static jsonvalues.spec.ERROR_CODE.*;

class IsObjSuchThat extends AbstractPredicate implements JsObjPredicate
{

  final Function<JsObj,Optional<Error>> predicate;

   IsObjSuchThat(final boolean required,
                       final boolean nullable,
                       final Function<JsObj,Optional<Error>> predicate
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

    if(error.isPresent()|| value.isNull())return error;
    return predicate.apply(value.toJsObj());
  }
}
