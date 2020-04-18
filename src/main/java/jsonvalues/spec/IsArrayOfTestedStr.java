package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Function;
import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class IsArrayOfTestedStr extends AbstractPredicate implements JsArrayPredicate
{
  final Function<String, Optional<Error>> predicate;

  IsArrayOfTestedStr(final Function<String, Optional<Error>> predicate,
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
                                             if (v.isStr()) return predicate.apply(v.toJsStr().value);
                                             else return Optional.of(new Error(v,
                                                                               STRING_EXPECTED
                                                                     )
                                                                    );
                                           },
                                           required,
                                           nullable
                                          )
                    .apply(value);
  }
}
