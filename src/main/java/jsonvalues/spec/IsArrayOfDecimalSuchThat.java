package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class IsArrayOfDecimalSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private IsArrayOfDecimal isArrayOfDecimal;
  final Function<JsArray,Optional<Error>> predicate;



   IsArrayOfDecimalSuchThat(final Function<JsArray,Optional<Error>> predicate,
                                  final boolean required,
                                  final boolean nullable
                                 )
  {
    super(required,
          nullable
         );
    this.isArrayOfDecimal = new IsArrayOfDecimal(required,nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfDecimal.test(value);
    if(result.isPresent())return result;
    return predicate.apply(value.toJsArray());
  }
}
