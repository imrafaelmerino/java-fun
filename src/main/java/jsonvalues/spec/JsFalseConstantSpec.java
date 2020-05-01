package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class JsFalseConstantSpec extends AbstractPredicateSpec implements JsValuePredicate
{
  @Override
  public JsSpec nullable()
  {
    return new JsFalseConstantSpec(required,
                                   false);
  }

  @Override
  public JsSpec optional()
  {
    return new JsFalseConstantSpec(false,
                                   nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofFalse(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  JsFalseConstantSpec(final boolean required,
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
    return Functions.testElem(JsValue::isFalse,
                              FALSE_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
