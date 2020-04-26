package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsObj extends AbstractPredicate implements JsObjPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsObj(required,
                     true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsObj(false,
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

  IsObj(final boolean required,
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
    return Functions.testElem(JsValue::isObj,
                              OBJ_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);

  }
}
