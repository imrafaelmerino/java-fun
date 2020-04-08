package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class IsBoolean extends AbstractPredicate implements JsBoolPredicate
{

   IsBoolean(final boolean required,
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

    return Functions.testElem(JsValue::isBool,
                              BOOLEAN_EXPECTED,
                              required,
                              nullable).apply(value);

  }
}
