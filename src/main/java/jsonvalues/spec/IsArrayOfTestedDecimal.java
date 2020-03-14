package jsonvalues.spec;

import jsonvalues.JsValue;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_CONDITION;

class IsArrayOfTestedDecimal extends AbstractPredicate implements JsArrayPredicate
{
  private Predicate<BigDecimal> predicate;

  public IsArrayOfTestedDecimal(final Predicate<BigDecimal> predicate,
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
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsBigDec().value),
                                           DECIMAL_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
