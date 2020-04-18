package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class IsArrayOfTestedInt extends AbstractPredicate implements JsArrayPredicate
{
  final IntFunction<Optional<Error>> predicate;



  IsArrayOfTestedInt(final IntFunction<Optional<Error>> predicate,
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

    return Functions.testArrayOfTestedElem(v ->
                                           {
                                             if (v.isInt()) return predicate.apply(v.toJsInt().value);
                                             else return Optional.of(new Error(v,
                                                                               INT_EXPECTED
                                                                     )
                                                                    );
                                           },
                                           required,
                                           nullable
                                          )
                    .apply(value);
  }
}
