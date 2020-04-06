package jsonvalues.spec;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class IsNumberSuchThat extends AbstractPredicate implements JsNumberPredicate
{

  final Function<JsNumber,Optional<Error>> predicate;

  public IsNumberSuchThat(final boolean required,
                          final boolean nullable,
                          final Function<JsNumber,Optional<Error>> predicate
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
    return predicate.apply(value.toJsNumber());
  }
}
