package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class JsNumberSpec extends AbstractPredicateSpec implements JsValuePredicate
{
  @Override
  public JsSpec nullable()
  {
    return new JsNumberSpec(required,
                            true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsNumberSpec(false,
                            nullable);
  }

  @Override
  public JsSpecParser parser()
  {
    return  JsSpecParsers.INSTANCE.ofNumber(nullable);
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }

  JsNumberSpec(final boolean required,
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
    return Functions.testElem(JsValue::isNumber,
                              NUMBER_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);

  }
}
