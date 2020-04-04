package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Predicate;

class IsArrayOfIntSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private Predicate<JsArray> predicate;
  private IsArrayOfInt isArrayOfInt;

  public IsArrayOfIntSuchThat(Predicate<JsArray> predicate,
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
    return Functions.testArraySuchThat(predicate).apply(value);
  }
}
