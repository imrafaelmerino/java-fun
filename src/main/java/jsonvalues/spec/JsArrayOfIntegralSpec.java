package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;
import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class JsArrayOfIntegralSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfIntegralSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfIntegralSpec(false, nullable);

  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfIntegral(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  JsArrayOfIntegralSpec(final boolean required,
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
    return Functions.testArrayOfTestedValue(v ->
                                           {
                                             if (v.isIntegral()) return Optional.empty();
                                             else return Optional.of(new Error(v,
                                                                               INTEGRAL_EXPECTED));
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
