package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.OBJ_CONDITION;

class IsArrayOfTestedObj extends AbstractPredicate implements JsArrayPredicate
{
  private Predicate<JsObj> predicate;

  public IsArrayOfTestedObj(final Predicate<JsObj> predicate,
                            final boolean required,
                            final boolean nullable
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
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsObj()),
                                           OBJ_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
