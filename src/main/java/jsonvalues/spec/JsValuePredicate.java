package jsonvalues.spec;


import jsonvalues.JsValue;

import java.util.Optional;

 interface JsValuePredicate extends JsSpec
{

  Optional<Error> test(final JsValue value);

}
