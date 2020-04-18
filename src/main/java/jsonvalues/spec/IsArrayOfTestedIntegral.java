package jsonvalues.spec;

import jsonvalues.JsValue;


import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;
import static jsonvalues.spec.ERROR_CODE.*;
class IsArrayOfTestedIntegral extends AbstractPredicate implements JsArrayPredicate
{
  final Function<BigInteger,Optional<Error>> predicate;

   IsArrayOfTestedIntegral(final Function<BigInteger,Optional<Error>> predicate,
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
    return Functions.testArrayOfTestedElem(v ->
                                           {
                                             if (v.isIntegral()) return predicate.apply(v.toJsBigInt().value);
                                             else return Optional.of(new Error(v,
                                                                               INTEGRAL_EXPECTED)
                                                                    );
                                           },
                                           required,
                                           nullable
                                          )
                    .apply(value);
  }
}
