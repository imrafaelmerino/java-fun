package jsonvalues.spec;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

 class IsStrSuchThat extends AbstractPredicate implements JsStrPredicate
{

  final Function<String,Optional<Error>> predicate;

  public IsStrSuchThat(final boolean required,
                       final boolean nullable,
                       final Function<String,Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isStr,
                                                                          STRING_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent())return error;
    return predicate.apply(value.toJsStr().value);
  }
}
