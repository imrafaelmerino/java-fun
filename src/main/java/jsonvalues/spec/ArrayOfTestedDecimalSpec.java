package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class ArrayOfTestedDecimalSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfTestedDecimalSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfTestedDecimalSpec(predicate, false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofArrayOfDecimalEachSuchThat(predicate,
                                                               nullable
                                                              );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<BigDecimal, Optional<Error>> predicate;


  ArrayOfTestedDecimalSpec(final Function<BigDecimal, Optional<Error>> predicate,
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
