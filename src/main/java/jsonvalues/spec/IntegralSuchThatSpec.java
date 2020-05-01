package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class IntegralSuchThatSpec extends AbstractPredicateSpec implements JsIntegralPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new IntegralSuchThatSpec(required, true, predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new IntegralSuchThatSpec(false, nullable, predicate);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofIntegralSuchThat(predicate,
                                                      nullable
                                                     );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<BigInteger, Optional<Error>> predicate;

  IntegralSuchThatSpec(final boolean required,
                       final boolean nullable,
                       final Function<BigInteger, Optional<Error>> predicate
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

    if (error.isPresent()|| value.isNull()) return error;
    return predicate.apply(value.toJsBigInt().value);
  }
}
