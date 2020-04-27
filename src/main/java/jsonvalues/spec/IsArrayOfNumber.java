package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfNumber extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfNumber(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfNumber(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfNumber(nullable);
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }

   IsArrayOfNumber(final boolean required,
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
      if(v.isNumber())return Optional.empty();
      else return Optional.of(new Error(v,NUMBER_EXPECTED));
    }, required, nullable).apply(value);
  }
}
