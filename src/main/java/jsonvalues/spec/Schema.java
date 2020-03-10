package jsonvalues.spec;

import jsonvalues.Json;

import java.util.Set;

@FunctionalInterface
public interface Schema<T extends Json<T>> extends JsSpec
{
  Set<JsErrorPair> validate(T json);
}
