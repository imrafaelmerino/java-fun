package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsInt extends AbstractPredicate implements JsIntPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsInt(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsInt(false,nullable);
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

   IsInt(final boolean required,
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
