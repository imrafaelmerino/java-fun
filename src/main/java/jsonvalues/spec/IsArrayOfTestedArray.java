package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;
import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

class IsArrayOfTestedArray extends AbstractPredicate implements JsArrayPredicate
{
  final Function<JsArray,Optional<Error>> predicate;



   IsArrayOfTestedArray(final Function<JsArray,Optional<Error>> predicate,
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
                                             if (v.isArray()) return predicate.apply(v.toJsArray());
                                             else return Optional.of(new Error(v,
                                                                               ARRAY_EXPECTED)
                                                                    );
                                           },
                                           required,
                                           nullable
                                          )
                    .apply(value);
  }
}
