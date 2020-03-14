package jsonvalues.spec;

import jsonvalues.JsValue;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsIntegralSuchThat extends AbstractPredicate implements JsIntegralPredicate
{

  final Predicate<BigInteger> predicate;

  public IsIntegralSuchThat(final boolean required,
                            final boolean nullable,
                            final Predicate<BigInteger> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isIntegral,
                                                                          INTEGRAL_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent())return error;
    return Functions.testElem(v -> predicate.test(v.toJsBigInt().value),
                              INTEGRAL_CONDITION,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
