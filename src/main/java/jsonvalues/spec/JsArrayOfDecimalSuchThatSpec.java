package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class JsArrayOfDecimalSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{

  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfDecimalSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfDecimalSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecParser parser()
  {
    return   ParserFactory.INSTANCE.ofArrayOfDecimalSuchThat(predicate,
                                                             nullable
                                                            );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  private JsArrayOfDecimalSpec isArrayOfDecimal;
  private final Function<JsArray,Optional<Error>> predicate;



   JsArrayOfDecimalSuchThatSpec(final Function<JsArray,Optional<Error>> predicate,
                                final boolean required,
                                final boolean nullable
                               )
  {
    super(required,
          nullable
         );
    this.isArrayOfDecimal = new JsArrayOfDecimalSpec(required, nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfDecimal.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());
  }
}
