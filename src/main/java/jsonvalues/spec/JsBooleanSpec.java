package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class JsBooleanSpec extends AbstractPredicateSpec implements JsValuePredicate
{
  @Override
  public JsSpec nullable()
  {
    return new JsBooleanSpec(required,
                             true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsBooleanSpec(false,
                             nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofBool(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  JsBooleanSpec(final boolean required,
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

    return Functions.testElem(JsValue::isBool,
                              BOOLEAN_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);

  }
}
