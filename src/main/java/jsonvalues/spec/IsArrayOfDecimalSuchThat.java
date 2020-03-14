package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Predicate;

class IsArrayOfDecimalSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private Predicate<JsArray> predicate;

  public IsArrayOfDecimalSuchThat(Predicate<JsArray> predicate,
                                  final boolean required,
                                  final boolean nullable
                                 )
  {
    super(required,
          nullable
         );
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArraySuchThat(predicate,
                                       required,
                                       nullable).apply(value);
  }
}
