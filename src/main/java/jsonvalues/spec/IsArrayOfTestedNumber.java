package jsonvalues.spec;

import jsonvalues.JsNumber;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfTestedNumber extends AbstractPredicate implements JsArrayPredicate
{
  final Function<JsNumber, Optional<Error>> predicate;
  final boolean elemNullable;

  public IsArrayOfTestedNumber(final Function<JsNumber,Optional<Error>> predicate,
                               final boolean required,
                               final boolean nullable
                              )
  {
    this(predicate,required,
          nullable,false
         );
  }

  public IsArrayOfTestedNumber(final Function<JsNumber,Optional<Error>> predicate,
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
                                             if (v.isNumber()) return predicate.apply(v.toJsNumber());
                                             else return Optional.of(new Error(v,
                                                                               NUMBER_EXPECTED)
                                                                    );
                                           },
                                           required,
                                           nullable
                                          )
                    .apply(value);
  }
}
