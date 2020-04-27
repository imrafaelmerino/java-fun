package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class NumberSpec extends AbstractPredicateSpec implements JsNumberPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new NumberSpec(required,
                          true);
  }

  @Override
  public JsSpec optional()
  {
    return new NumberSpec(false,
                          nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofNumber(nullable);
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }

  NumberSpec(final boolean required,
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
    return Functions.testElem(JsValue::isNumber,
                              NUMBER_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);

  }
}
