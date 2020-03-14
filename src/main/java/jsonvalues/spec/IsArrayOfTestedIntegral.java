package jsonvalues.spec;

import jsonvalues.JsValue;


import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_CONDITION;

class IsArrayOfTestedIntegral extends AbstractPredicate implements JsArrayPredicate
{
  private Predicate<BigInteger> predicate;

  public IsArrayOfTestedIntegral(final Predicate<BigInteger> predicate,
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
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsBigInt().value),
                                           INTEGRAL_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
