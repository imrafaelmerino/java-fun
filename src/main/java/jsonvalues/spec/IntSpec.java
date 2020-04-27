package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IntSpec extends AbstractPredicateSpec implements JsIntPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IntSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new IntSpec(false, nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofInt(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

   IntSpec(final boolean required,
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
    return Functions.testElem(JsValue::isInt,
                              INT_EXPECTED,
                              required,
                              nullable).apply(value);

  }
}
