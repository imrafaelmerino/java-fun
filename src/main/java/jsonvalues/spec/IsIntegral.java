package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsIntegral extends AbstractPredicate implements JsIntegralPredicate

{
  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public JsSpec nullable()
  {
    return new IsIntegral(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsIntegral(false,nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
   IsIntegral(final boolean required,
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
