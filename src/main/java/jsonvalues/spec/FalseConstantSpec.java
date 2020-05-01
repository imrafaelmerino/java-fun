package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class FalseConstantSpec extends AbstractPredicateSpec implements JsBoolPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new FalseConstantSpec(required,
                                 false);
  }

  @Override
  public JsSpec optional()
  {
    return new FalseConstantSpec(false,
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

  FalseConstantSpec(final boolean required,
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
