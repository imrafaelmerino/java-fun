package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;
import java.util.Optional;
import static jsonvalues.spec.ERROR_CODE.*;

class ArrayOfDecimalSpec extends AbstractPredicateSpec implements JsArrayPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new ArrayOfDecimalSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfDecimalSpec(false, nullable);
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

   ArrayOfDecimalSpec(final boolean required,
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
