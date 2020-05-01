package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class JsArrayOfBoolSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public boolean isRequired()
  {
    return required;
  }


  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfBoolSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfBoolSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfBool(nullable);
  }

  JsArrayOfBoolSpec(final boolean required,
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
      if(v.isBool())return Optional.empty();
      else return Optional.of(new Error(v,BOOLEAN_EXPECTED));
    }, required, nullable).apply(value);
  }
}
