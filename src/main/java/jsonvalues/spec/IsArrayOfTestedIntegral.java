package jsonvalues.spec;

import jsonvalues.JsValue;


import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfTestedIntegral extends AbstractPredicate implements JsArrayPredicate
{
  final Function<BigInteger,Optional<Error>> predicate;
  final boolean elemNullable;

  public IsArrayOfTestedIntegral(final Function<BigInteger,Optional<Error>> predicate,
                                 final boolean required,
                                 final boolean nullable
                                )
  {
    this(predicate,required,
          nullable,false
         );
  }


  public IsArrayOfTestedIntegral(final Function<BigInteger,Optional<Error>> predicate,
                                 final boolean required,
                                 final boolean nullable,
                                 final boolean elemNullable
                                )
  {
    super(required,
          nullable
         );
    this.predicate = predicate;
    this.elemNullable = elemNullable;
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
