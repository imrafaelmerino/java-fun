package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsIntegral extends AbstractPredicate implements JsIntegralPredicate
{

  public IsIntegral(final boolean required,
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
    return Functions.testElem(JsValue::isIntegral,
                              INTEGRAL_EXPECTED,
                              required,
                              nullable).apply(value);

  }
}
