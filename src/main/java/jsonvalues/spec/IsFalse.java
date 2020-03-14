package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsFalse extends AbstractPredicate implements JsBoolPredicate
{

  public IsFalse(final boolean required,
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
    return Functions.testElem(JsValue::isFalse,
                              FALSE_EXPECTED,
                              required,
                              nullable).apply(value);
  }
}
