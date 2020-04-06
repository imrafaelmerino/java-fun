package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.*;

class IsLongSuchThat extends AbstractPredicate implements JsLongPredicate
{

  final LongFunction<Optional<Error>> predicate;

  public IsLongSuchThat(final boolean required,
                        final boolean nullable,
                        final LongFunction<Optional<Error>> predicate
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
    return predicate.apply(value.toJsLong().value);
  }
}
