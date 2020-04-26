package jsonvalues.spec;

import jsonvalues.JsValue;


import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;
import static jsonvalues.spec.ERROR_CODE.*;
class IsArrayOfTestedIntegral extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfTestedIntegral(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfTestedIntegral(predicate,false,nullable);
  }
  @Override
  public boolean isNullable()
  {
    return nullable;
  }
  @Override
  public boolean isRequired()
  {
    return required;
  }
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
    return Functions.testArrayOfTestedValue(v ->
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
