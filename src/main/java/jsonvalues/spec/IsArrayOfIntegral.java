package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfIntegral extends AbstractPredicate implements JsArrayPredicate
{
  public IsArrayOfIntegral(final boolean required,
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
    return Functions.testArrayOfTestedElem(JsValue::isIntegral, INTEGRAL_EXPECTED, required, nullable).apply(value);

  }
}
