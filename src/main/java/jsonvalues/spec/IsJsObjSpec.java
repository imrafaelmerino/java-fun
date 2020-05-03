package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsJsObjSpec extends AbstractPredicateSpec implements JsValuePredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsJsObjSpec(required,
                           true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsJsObjSpec(false,
                           nullable);
  }

  @Override
  public JsSpecParser parser()
  {
    return  JsSpecParsers.INSTANCE.ofObj(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  IsJsObjSpec(final boolean required,
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
                              nullable
                             )
                    .apply(value);

  }
}