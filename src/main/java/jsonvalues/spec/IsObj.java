package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsObj extends AbstractPredicate implements JsObjPredicate
{

   IsObj(final boolean required,
               final boolean nullable
              )
  {
    super(required,
          nullable
         );
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testElem(JsValue::isObj,
                              OBJ_EXPECTED,
                              required,
                              nullable).apply(value);

  }
}
