package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Predicate;

class IsArrayOfDecimalSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private Predicate<JsArray> predicate;
  private IsArrayOfDecimal isArrayOfDecimal;

  public IsArrayOfDecimalSuchThat(Predicate<JsArray> predicate,
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
    return Functions.testArraySuchThat(predicate).apply(value);
  }
}
