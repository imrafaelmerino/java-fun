package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsFalse extends AbstractPredicate implements JsBoolPredicate
{
  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public JsSpec nullable()
  {
    return new IsFalse(required,
                       false);
  }

  @Override
  public JsSpec optional()
  {
    return new IsFalse(false,
                       nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  IsFalse(final boolean required,
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
                              nullable
                             )
                    .apply(value);
  }
}
