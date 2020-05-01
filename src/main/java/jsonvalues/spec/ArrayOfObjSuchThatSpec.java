package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class ArrayOfObjSuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfObjSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfObjSuchThatSpec(predicate, false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfObjSuchThat(predicate,
                                                        nullable
                                                       );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  private final Function<JsArray, Optional<Error>> predicate;
  private ArrayOfObjSpec isArrayOfObj;



  ArrayOfObjSuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                         final boolean required,
                         final boolean nullable
                        )
  {
    super(required,
          nullable
         );
    this.isArrayOfObj = new ArrayOfObjSpec(required,
                                           nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfObj.test(value);
    if (result.isPresent()|| value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
