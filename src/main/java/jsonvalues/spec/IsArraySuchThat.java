package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;


class IsArraySuchThat extends AbstractPredicate implements JsArrayPredicate
{
  final Function<JsArray, Optional<Error>> predicate;
  private IsArray isArray;

  IsArraySuchThat(final Function<JsArray, Optional<Error>> predicate,
                       final boolean required,
                       final boolean nullable
                      )
  {
    super(required,
          nullable
         );
    this.isArray = new IsArray(required,
                               nullable);
    this.predicate = predicate;
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArray.test(value);
    if (result.isPresent()) return result;
    return predicate.apply(value.toJsArray());
  }
}
