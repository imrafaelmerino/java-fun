package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;
import static jsonvalues.spec.ERROR_CODE.DECIMAL_CONDITION;

class IsArraySuchThat extends AbstractPredicate implements JsArrayPredicate
{

  final Predicate<JsArray> predicate;

  public IsArraySuchThat(final boolean required,
                         final boolean nullable,
                         final Predicate<JsArray> predicate
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
    return Functions.testElem(v -> predicate.test(v.toJsArray()),
                              DECIMAL_CONDITION,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
