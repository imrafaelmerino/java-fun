package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsIntegral extends AbstractPredicate implements JsIntegralPredicate

{

  @Override
  public JsSpec nullable()
  {
    return new IsIntegral(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsIntegral(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofIntegral(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
   IsIntegral(final boolean required,
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
    return Functions.testElem(JsValue::isIntegral,
                              INTEGRAL_EXPECTED,
                              required,
                              nullable).apply(value);

  }
}
