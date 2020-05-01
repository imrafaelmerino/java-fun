package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;


class ArrayOfBoolSuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{

  @Override
  public boolean isRequired()
  {
    return required;
  }


  @Override
  public JsSpec nullable()
  {
    return new ArrayOfBoolSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfBoolSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecParser parser()
  {
    return   ParserFactory.INSTANCE.ofArrayOfBoolSuchThat(predicate,
                                                          nullable
                                                         );
  }

  private ArrayOfBoolSpec isArrayOfBool;
  private final Function<JsArray,Optional<Error>> predicate;


   ArrayOfBoolSuchThatSpec(final Function<JsArray,Optional<Error>>  predicate,
                           final boolean required,
                           final boolean nullable
                          )
  {
    super(required,
          nullable
         );
    this.isArrayOfBool = new ArrayOfBoolSpec(required, nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfBool.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());

  }
}
