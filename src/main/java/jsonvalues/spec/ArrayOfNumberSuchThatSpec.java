package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class ArrayOfNumberSuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new ArrayOfNumberSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfNumberSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfNumberSuchThat(predicate,
                                                           nullable
                                                          );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  private final Function<JsArray,Optional<Error>> predicate;
  private ArrayOfNumberSpec isArrayOfNumber;



   ArrayOfNumberSuchThatSpec(final Function<JsArray,Optional<Error>> predicate,
                             final boolean required,
                             final boolean nullable
                            )
  {
    super(required,
          nullable
         );
    this.isArrayOfNumber=new ArrayOfNumberSpec(required, nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfNumber.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());
  }
}
