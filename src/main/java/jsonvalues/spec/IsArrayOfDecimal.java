package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;
import java.util.Optional;
import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfDecimal extends AbstractPredicate implements JsArrayPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfDecimal(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfDecimal(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return   DeserializersFactory.INSTANCE.ofArrayOfDecimal(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

   IsArrayOfDecimal(final boolean required,
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
      if(v.isDecimal())return Optional.empty();
      else return Optional.of(new Error(v,DECIMAL_EXPECTED));
    }, required, nullable).apply(value);
  }
}
