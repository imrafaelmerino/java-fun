package jsonvalues.spec;

import jsonvalues.JsNumber;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsNumberSuchThat extends AbstractPredicate implements JsNumberPredicate
{

  final Predicate<JsNumber> predicate;

  public IsNumberSuchThat(final boolean required,
                          final boolean nullable,
                          final Predicate<JsNumber> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isNumber,
                                                                          NUMBER_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent())return error;
    return Functions.testElem(v -> predicate.test(v.toJsNumber()),
                              NUMBER_CONDITION,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
