package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsNull extends AbstractPredicate implements JsPrimitivePredicate
{

  public IsNull(final boolean required,
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
    return Functions.testElem(JsValue::isNull,
                              NULL_EXPECTED,
                              required,
                              nullable).apply(value);
  }
}
