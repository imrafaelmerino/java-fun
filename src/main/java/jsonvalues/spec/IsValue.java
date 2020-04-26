package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

class IsValue implements JsValuePredicate
{

  @Override
  public JsSpec nullable()
  {
    return this;
  }

  @Override
  public JsSpec optional()
  {
    return new IsValue(false);
  }
  @Override
  public boolean isNullable()
  {
    return true;
  }
  @Override
  public boolean isRequired()
  {
    return required;
  }
  final boolean required;

  IsValue(final boolean required)
  {
    this.required = required;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {

    if (value.isNothing() && required) return Optional.of(new Error(value,ERROR_CODE.REQUIRED));
    return Optional.empty();

  }
}
