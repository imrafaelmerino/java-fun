package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfIntegralSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfIntegralSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfIntegralSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofArrayOfIntegralSuchThat(predicate,
                                                            nullable
                                                           );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
  private JsArrayOfIntegralSpec isArrayOfIntegral;
  private final Function<JsArray, Optional<Error>> predicate;




   JsArrayOfIntegralSuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                                 final boolean required,
                                 final boolean nullable
                                )
  {
    super(required,
          nullable
         );
    this.isArrayOfIntegral = new JsArrayOfIntegralSpec(required,
                                                       nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfIntegral.test(value);
    if (result.isPresent()|| value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
