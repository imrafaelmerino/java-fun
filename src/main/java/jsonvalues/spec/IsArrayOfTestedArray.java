package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.ARRAY_CONDITION;

class IsArrayOfTestedArray extends AbstractPredicate implements JsArrayPredicate
{
  private Predicate<JsArray> predicate;

  public IsArrayOfTestedArray(final Predicate<JsArray> predicate,
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
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsArray()),
                                           ARRAY_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
