package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsValueSuchThat implements JsPredicate
{
  final boolean required;
  final Function<JsValue,Optional<Error>> predicate;

  IsValueSuchThat(final boolean required,
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
