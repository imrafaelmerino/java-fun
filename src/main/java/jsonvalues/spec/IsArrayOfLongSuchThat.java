package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class IsArrayOfLongSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private IsArrayOfLong isArrayOfLong;
  final Function<JsArray,Optional<Error>> predicate;


   IsArrayOfLongSuchThat(final Function<JsArray,Optional<Error>> predicate,
                               final boolean required,
                               final boolean nullable
                              )
  {
    super(required,
          nullable
         );
    this.isArrayOfLong = new IsArrayOfLong(required,nullable);
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
