package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.util.Optional;

class AnySpec implements JsValuePredicate
{

  @Override
  public JsSpec nullable()
  {
    return this;
  }

  @Override
  public JsSpec optional()
  {
    return new AnySpec(false);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofValue();
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final boolean required;

  AnySpec(final boolean required)
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
