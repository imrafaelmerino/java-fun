package jsonvalues.spec;

import jsonvalues.JsNumber;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_CONDITION;
import static jsonvalues.spec.ERROR_CODE.NUMBER_CONDITION;

class IsArrayOfTestedNumber extends AbstractPredicate implements JsArrayPredicate
{
  private Predicate<JsNumber> predicate;

  public IsArrayOfTestedNumber(final Predicate<JsNumber> predicate,
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
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsNumber()),
                                           NUMBER_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
