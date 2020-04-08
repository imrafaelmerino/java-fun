package jsonvalues.spec;

import jsonvalues.Json;

import java.util.Set;
interface Schema<T extends Json<T>> extends JsSpec
{
  Set<JsErrorPair> test(T json);
}
