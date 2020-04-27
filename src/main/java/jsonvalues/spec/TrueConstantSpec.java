package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class TrueConstantSpec extends AbstractPredicateSpec implements JsBoolPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new TrueConstantSpec(required,
                                true
    );
  }

  @Override
  public JsSpec optional()
  {
    return new TrueConstantSpec(false,
                                nullable
    );
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofTrue(nullable);
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }

  TrueConstantSpec(final boolean required,
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
    return Functions.testElem(JsValue::isTrue,
                              TRUE_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
