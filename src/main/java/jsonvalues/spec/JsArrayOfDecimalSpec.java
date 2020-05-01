package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;
import java.util.Optional;
import static jsonvalues.spec.ERROR_CODE.*;

class JsArrayOfDecimalSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{

  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfDecimalSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfDecimalSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return   ParserFactory.INSTANCE.ofArrayOfDecimal(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

   JsArrayOfDecimalSpec(final boolean required,
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
    return Functions.testArrayOfTestedValue(v-> {
      if(v.isDecimal())return Optional.empty();
      else return Optional.of(new Error(v,DECIMAL_EXPECTED));
    }, required, nullable).apply(value);
  }
}
