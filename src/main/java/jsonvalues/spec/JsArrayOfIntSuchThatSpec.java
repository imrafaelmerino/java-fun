package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class JsArrayOfIntSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec

{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfIntSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfIntSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofArrayOfIntSuchThat(predicate,
                                                       nullable
                                                      );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  private final Function<JsArray,Optional<Error>> predicate;
  private JsArrayOfIntSpec isArrayOfInt;

   JsArrayOfIntSuchThatSpec(final Function<JsArray,Optional<Error>> predicate,
                            final boolean required,
                            final boolean nullable
                           )
  {
    super(required,
          nullable
         );
    this.isArrayOfInt = new JsArrayOfIntSpec(required, nullable);
    this.predicate = predicate;

  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfInt.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());
  }
}
