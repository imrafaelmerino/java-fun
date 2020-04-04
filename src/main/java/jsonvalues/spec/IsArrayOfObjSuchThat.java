package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Predicate;

class IsArrayOfObjSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private Predicate<JsArray> predicate;
  private IsArrayOfObj isArrayOfObj;

  public IsArrayOfObjSuchThat(Predicate<JsArray> predicate,
                              final boolean required,
                              final boolean nullable
                             )
  {
    super(required,
          nullable
         );
    this.isArrayOfObj = new IsArrayOfObj(required,nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfObj.test(value);
    if(result.isPresent())return result;
    return Functions.testArraySuchThat(predicate).apply(value);
  }
}
