package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class JsArrayOfLongSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfLongSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfLongSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public JsSpecParser parser()
  {
    return JsSpecParsers.INSTANCE.ofArrayOfLongSuchThat(predicate,
                                                        nullable
                                                       );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
  private JsArrayOfLongSpec isArrayOfLong;
  private final Function<JsArray,Optional<Error>> predicate;


   JsArrayOfLongSuchThatSpec(final Function<JsArray,Optional<Error>> predicate,
                             final boolean required,
                             final boolean nullable
                            )
  {
    super(required,
          nullable
         );
    this.isArrayOfLong = new JsArrayOfLongSpec(required, nullable);
    this.predicate = predicate;
  }
  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfLong.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());
  }
}
