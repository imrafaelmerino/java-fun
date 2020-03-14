package jsonvalues.spec;

import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsDecimalSuchThat extends AbstractPredicate implements JsDecimalPredicate
{

  final Predicate<BigDecimal> predicate;

  public IsDecimalSuchThat(final boolean required,
                           final boolean nullable,
                           final Predicate<BigDecimal> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isDecimal,
                                                                          DECIMAL_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent())return error;
    return Functions.testElem(v -> predicate.test(v.toJsBigDec().value),
                              DECIMAL_CONDITION,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
