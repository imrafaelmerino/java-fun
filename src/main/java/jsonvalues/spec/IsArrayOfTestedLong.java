package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfTestedLong extends AbstractPredicate implements JsArrayPredicate
{
  final LongFunction<Optional<Error>> predicate;
  final boolean elemNullable;

  public IsArrayOfTestedLong(final LongFunction<Optional<Error>> predicate,
                             final boolean required,
                             final boolean nullable
                            )
  {
    this(predicate,required,
          nullable,false
         );
  }

  public IsArrayOfTestedLong(final LongFunction<Optional<Error>> predicate,
                             final boolean required,
                             final boolean nullable,
                             final boolean elemNullable
                            )
  {
    super(required,
          nullable
         );
    this.predicate = predicate;
    this.elemNullable = elemNullable;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArrayOfTestedElem(v ->
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
