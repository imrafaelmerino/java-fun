package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.LongPredicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsLongSuchThat extends AbstractPredicate implements JsLongPredicate
{

  final LongPredicate predicate;

  public IsLongSuchThat(final boolean required,
                        final boolean nullable,
                        final LongPredicate predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isLong,
                                                                          LONG_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent())return error;
    return Functions.testElem(v -> predicate.test(v.toJsLong().value),
                              LONG_CONDITION,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
