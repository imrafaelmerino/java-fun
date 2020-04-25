package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsArrayOfTestedValue extends AbstractPredicate implements JsArrayPredicate
{
  final Function<JsValue, Optional<Error>> predicate;


  IsArrayOfTestedValue(final Function<JsValue, Optional<Error>> predicate,
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
    return Functions.testArrayOfTestedValue(predicate::apply,
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
