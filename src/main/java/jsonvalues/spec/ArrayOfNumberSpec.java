package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class ArrayOfNumberSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfNumberSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfNumberSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfNumber(nullable);
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }

   ArrayOfNumberSpec(final boolean required,
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
      if(v.isNumber())return Optional.empty();
      else return Optional.of(new Error(v,NUMBER_EXPECTED));
    }, required, nullable).apply(value);
  }
}
