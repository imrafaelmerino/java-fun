package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class IsStr extends AbstractPredicate implements JsStrPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsStr(required,
                     true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsStr(false,
                     nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofStr(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  IsStr(final boolean required,
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
    return Functions.testElem(JsValue::isStr,
                              STRING_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
