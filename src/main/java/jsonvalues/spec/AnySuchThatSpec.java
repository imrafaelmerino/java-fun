package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class AnySuchThatSpec implements JsValuePredicate
{

  @Override
  public JsSpec nullable()
  {
    return this;
  }

  @Override
  public JsSpec optional()
  {
    return new AnySuchThatSpec(false, predicate);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofValueSuchThat(predicate);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final boolean required;
  final Function<JsValue,Optional<Error>> predicate;

  AnySuchThatSpec(final boolean required,
                  final Function<JsValue, Optional<Error>> predicate
                 )
  {
    this.required = required;
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {

    if (value.isNothing() && required) return Optional.of(new Error(value,ERROR_CODE.REQUIRED));
    return predicate.apply(value);

  }
}
