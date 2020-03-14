package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.STRING_CONDITION;

class IsArrayOfTestedStr extends AbstractPredicate implements JsArrayPredicate
{
  private Predicate<String> predicate;

  public IsArrayOfTestedStr(final Predicate<String> predicate,
                            final boolean required,
                            final boolean nullable
                           )
  {
    super(required, nullable );
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArrayOfTestedElem(v -> predicate.test(v.toJsStr().value),
                                           STRING_CONDITION,
                                           required,
                                           nullable
                                          ).apply(value);
  }
}
