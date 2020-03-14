package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.LongPredicate;

import static jsonvalues.spec.ERROR_CODE.LONG_CONDITION;

class IsArrayOfTestedLong extends AbstractPredicate implements JsArrayPredicate
{
  private LongPredicate predicate;

  public IsArrayOfTestedLong(final LongPredicate predicate,
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
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsLong().value),
                                           LONG_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
