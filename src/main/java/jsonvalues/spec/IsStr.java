package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class IsStr extends AbstractPredicate implements JsStrPredicate
{

  public IsStr(final boolean required,
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
    return Functions.testElem(JsValue::isStr,
                              STRING_EXPECTED,
                              required,
                              nullable).apply(value);
  }
}
