package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArraySuchThat extends AbstractPredicate implements JsArrayPredicate
{

  final Function<JsArray,Optional<Error>> predicate;

   IsArraySuchThat(final boolean required,
                         final boolean nullable,
                         final Function<JsArray,Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isArray,
                                                                          ARRAY_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent())return error;
    return predicate.apply(value.toJsArray());
  }
}
