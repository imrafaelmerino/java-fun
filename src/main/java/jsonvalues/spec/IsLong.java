package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

 class IsLong extends AbstractPredicate implements JsLongPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsLong(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsLong(false,nullable);
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
   IsLong(final boolean required,
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
