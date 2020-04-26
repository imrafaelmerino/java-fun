package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class IsBoolean extends AbstractPredicate implements JsBoolPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsBoolean(required,
                         true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsBoolean(false,
                         nullable);
  }

  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

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
                              nullable
                             )
                    .apply(value);

  }
}
