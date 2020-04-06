package jsonvalues.spec;

import jsonvalues.JsValue;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_CONDITION;
import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class IsArrayOfTestedDecimal extends AbstractPredicate implements JsArrayPredicate
{
  final Function<BigDecimal,Optional<Error>> predicate;
  final boolean elemNullable;

  public IsArrayOfTestedDecimal(final Function<BigDecimal,Optional<Error>> predicate,
                                final boolean required,
                                final boolean nullable
                               )
  {
   this(predicate,required,nullable,false);
  }

  public IsArrayOfTestedDecimal(final Function<BigDecimal,Optional<Error>> predicate,
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
                                             if (v.isDouble() || v.isBigDec()) return predicate.apply(v.toJsBigDec().value);
                                             else return Optional.of(new Error(v,
                                                                               DECIMAL_EXPECTED)
                                                                    );
                                           },
                                           required,
                                           nullable
                                          )
                    .apply(value);
  }
}
