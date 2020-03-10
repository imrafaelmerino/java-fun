package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

@FunctionalInterface
public interface JsPredicate extends JsSpec
{
  Optional<Error> test(JsValue value);
}
