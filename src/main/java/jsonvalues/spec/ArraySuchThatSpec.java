package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;


class ArraySuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new ArraySuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArraySuchThatSpec(predicate, false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfValueSuchThat(predicate,
                                                          nullable
                                                         );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsArray, Optional<Error>> predicate;
  private ArraySpec isArray;

  ArraySuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                    final boolean required,
                    final boolean nullable
                   )
  {
    super(required,
          nullable
         );
    this.isArray = new ArraySpec(required,
                                 nullable);
    this.predicate = predicate;
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArray.test(value);
    if (result.isPresent()|| value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
