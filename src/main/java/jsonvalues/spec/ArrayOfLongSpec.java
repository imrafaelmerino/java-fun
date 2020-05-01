package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;
import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class ArrayOfLongSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfLongSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfLongSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofArrayOfLong(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

   ArrayOfLongSpec(final boolean required,
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
      if(v.isInt() || v.isLong())return Optional.empty();
      else return Optional.of(new Error(v,LONG_EXPECTED));
    }, required, nullable).apply(value);
  }
}
