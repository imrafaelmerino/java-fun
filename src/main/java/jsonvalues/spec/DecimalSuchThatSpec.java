package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class DecimalSuchThatSpec extends AbstractPredicateSpec implements JsDecimalPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new DecimalSuchThatSpec(required, true, predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new DecimalSuchThatSpec(false, nullable, predicate);
  }

  @Override
  public SpecParser parser()
  {
    return   ParserFactory.INSTANCE.ofDecimalSuchThat(predicate,
                                                      nullable
                                                     );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  final Function<BigDecimal,Optional<Error>> predicate;

   DecimalSuchThatSpec(final boolean required,
                       final boolean nullable,
                       final Function<BigDecimal,Optional<Error>> predicate
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

    if(error.isPresent() || value.isNull())return error;
    return predicate.apply(value.toJsBigDec().value);
  }
}
