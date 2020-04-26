package jsonvalues.spec;

import jsonvalues.Json;

import java.util.Set;
public interface Schema<T extends Json<T>> extends JsSpec
{
  Set<JsErrorPair> test(final T json);
}
