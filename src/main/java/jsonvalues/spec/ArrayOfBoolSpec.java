package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class ArrayOfBoolSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public boolean isRequired()
  {
    return required;
  }


  @Override
  public JsSpec nullable()
  {
    return new ArrayOfBoolSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfBoolSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfBool(nullable);
  }

  ArrayOfBoolSpec(final boolean required,
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
