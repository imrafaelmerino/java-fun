package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Predicate;


class IsArrayOfBoolSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private Predicate<JsArray> predicate;
  private IsArrayOfBool isArrayOfBool;

  public IsArrayOfBoolSuchThat(Predicate<JsArray> predicate,
                               final boolean required,
                               final boolean nullable
                              )
  {
    super(required,
          nullable
         );
    this.isArrayOfBool = new IsArrayOfBool(required,nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfBool.test(value);
    if(result.isPresent())return result;
    return Functions.testArraySuchThat(predicate).apply(value);

  }
}
