package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfStr extends AbstractPredicate implements JsArrayPredicate
{
  public IsArrayOfStr(final boolean required,
                      final boolean nullable
                     )
  {
    super(required,
          nullable
         );
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArrayOfTestedElem(JsValue::isStr, STRING_EXPECTED, required, nullable).apply(value);

  }
}
