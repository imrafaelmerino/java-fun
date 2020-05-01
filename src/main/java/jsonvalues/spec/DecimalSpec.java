package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class DecimalSpec extends AbstractPredicateSpec implements JsDecimalPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new DecimalSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new DecimalSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofDecimal(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
   DecimalSpec(final boolean required,
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
    return Functions.testElem(JsValue::isDecimal,
                              DECIMAL_EXPECTED,
                              required,
                              nullable).apply(value);

  }
}
