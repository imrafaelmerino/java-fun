package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class IsArrayOfIntSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  final Function<JsArray,Optional<Error>> predicate;
  private IsArrayOfInt isArrayOfInt;

   IsArrayOfIntSuchThat(final Function<JsArray,Optional<Error>> predicate,
                              final boolean required,
                              final boolean nullable
                             )
  {
    super(required,
          nullable
         );
    this.isArrayOfInt = new IsArrayOfInt(required,nullable);
    this.predicate = predicate;

  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfInt.test(value);
    if(result.isPresent())return result;
    return predicate.apply(value.toJsArray());
  }
}
