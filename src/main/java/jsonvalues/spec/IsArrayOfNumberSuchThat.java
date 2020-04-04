package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Predicate;

class IsArrayOfNumberSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private Predicate<JsArray> predicate;
  private IsArrayOfNumber isArrayOfNumber;

  public IsArrayOfNumberSuchThat(Predicate<JsArray> predicate,
                                 final boolean required,
                                 final boolean nullable
                                )
  {
    super(required,
          nullable
         );
    this.isArrayOfNumber=new IsArrayOfNumber(required,nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfNumber.test(value);
    if(result.isPresent())return result;
    return Functions.testArraySuchThat(predicate).apply(value);
  }
}
