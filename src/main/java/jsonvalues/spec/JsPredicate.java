package jsonvalues.spec;


import jsonvalues.JsValue;

import java.util.Optional;

 interface JsPredicate extends JsSpec
{

  Optional<Error> test(JsValue value);

}
