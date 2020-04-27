package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsDecimal extends AbstractPredicate implements JsDecimalPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new IsDecimal(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsDecimal(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofDecimal(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
   IsDecimal(final boolean required,
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
    return Functions.testElem(JsValue::isDecimal,
                              DECIMAL_EXPECTED,
                              required,
                              nullable).apply(value);

  }
}
