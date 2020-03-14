package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.IntPredicate;
import static jsonvalues.spec.ERROR_CODE.INT_CONDITION;

class IsArrayOfTestedInt extends AbstractPredicate implements JsArrayPredicate
{
  private IntPredicate predicate;

  public IsArrayOfTestedInt(final IntPredicate predicate,
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
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsInt().value),
                                           INT_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
