package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class ArrayOfStrSuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfStrSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfStrSuchThatSpec(predicate, false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofArrayOfStrSuchThat(predicate,
                                                       nullable
                                                      );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
  private final Function<JsArray, Optional<Error>> predicate;
  private ArrayOfStrSpec isArrayOfString;

   ArrayOfStrSuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                          final boolean required,
                          final boolean nullable
                         )
  {
    super(required,
          nullable
         );
    this.isArrayOfString = new ArrayOfStrSpec(required,
                                              nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfString.test(value);
    if (result.isPresent() || value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
