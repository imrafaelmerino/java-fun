package jsonvalues.spec;

import jsonvalues.JsValue;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class IsArrayOfTestedDecimal extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfTestedDecimal(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfTestedDecimal(predicate,false,nullable);
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
  final Function<BigDecimal, Optional<Error>> predicate;


  IsArrayOfTestedDecimal(final Function<BigDecimal, Optional<Error>> predicate,
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
                                             if (v.isDouble() || v.isBigDec()) return predicate.apply(v.toJsBigDec().value);
                                             else return Optional.of(new Error(v,
                                                                               DECIMAL_EXPECTED
                                                                     )
                                                                    );
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
