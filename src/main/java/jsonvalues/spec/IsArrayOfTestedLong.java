package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfTestedLong extends AbstractPredicate implements JsArrayPredicate
{
  final LongFunction<Optional<Error>> predicate;



   IsArrayOfTestedLong(final LongFunction<Optional<Error>> predicate,
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
    return Functions.testArrayOfTestedValue(v ->
                                           {
                                             if (v.isLong() || v.isInt()) return predicate.apply(v.toJsLong().value);
                                             else return Optional.of(new Error(v,
                                                                               LONG_EXPECTED)
                                                                    );
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
