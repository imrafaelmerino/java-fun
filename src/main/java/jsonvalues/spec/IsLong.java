package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

 class IsLong extends AbstractPredicate implements JsLongPredicate
{

  public IsLong(final boolean required,
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
    return Functions.testElem(JsValue::isLong,
                              LONG_EXPECTED,
                              required,
                              nullable).apply(value);
  }
}
