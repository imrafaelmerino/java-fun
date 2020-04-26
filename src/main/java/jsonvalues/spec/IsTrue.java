package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsTrue extends AbstractPredicate implements JsBoolPredicate
{
  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public JsSpec nullable()
  {
    return new IsTrue(required,
                      true
    );
  }

  @Override
  public JsSpec optional()
  {
    return new IsTrue(false,
                      nullable
    );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }

  IsTrue(final boolean required,
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
    return Functions.testElem(JsValue::isTrue,
                              TRUE_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
