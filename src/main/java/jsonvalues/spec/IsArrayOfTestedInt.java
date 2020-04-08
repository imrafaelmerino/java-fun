package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

import static jsonvalues.spec.ERROR_CODE.INT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class IsArrayOfTestedInt extends AbstractPredicate implements JsArrayPredicate
{
  final IntFunction<Optional<Error>> predicate;
  final boolean elemNullable;

   IsArrayOfTestedInt(final IntFunction<Optional<Error>> predicate,
                            final boolean required,
                            final boolean nullable
                           )
  {
    this(predicate,
         required,
         nullable,
         false
        );
  }

   IsArrayOfTestedInt(final IntFunction<Optional<Error>> predicate,
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
                                             if (v.isInt()) return predicate.apply(v.toJsInt().value);
                                             else return Optional.of(new Error(v,
                                                                               INT_EXPECTED)
                                                                    );
                                           },
                                           required,
                                           nullable
                                          )
                    .apply(value);
  }
}
