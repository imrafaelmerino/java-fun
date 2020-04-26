package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;


class IsArray extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public JsSpec nullable()
  {
    return new IsArray(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArray(false,nullable);
  }

  IsArray(final boolean required,
          final boolean nullable
         )
  {
    super(required,
          nullable
         );

  }

  @Override
  public boolean isRequired()
  {
    return required;
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArray(required,
                               nullable)
                    .apply(value);
  }
}
