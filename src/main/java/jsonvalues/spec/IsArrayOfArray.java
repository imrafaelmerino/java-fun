package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

class IsArrayOfArray extends AbstractPredicate implements JsArrayPredicate
{
  public IsArrayOfArray(final boolean required,
                        final boolean nullable
                       )
  {
    super(required, nullable);
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArrayOfTestedElem(JsValue::isArray, ARRAY_EXPECTED, required, nullable).apply(value);
  }
}
