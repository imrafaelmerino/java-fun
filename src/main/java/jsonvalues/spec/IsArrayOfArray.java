package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

class IsArrayOfArray extends AbstractPredicate implements JsArrayPredicate

{
  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfArray(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfArray(false,nullable);
  }

  IsArrayOfArray(final boolean required,
                 final boolean nullable
                )
  {
    super(required, nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArrayOfTestedValue(v-> {
      if(v.isArray())return Optional.empty();
      else return Optional.of(new Error(v,ARRAY_EXPECTED));
    }, required, nullable).apply(value);
  }
}
