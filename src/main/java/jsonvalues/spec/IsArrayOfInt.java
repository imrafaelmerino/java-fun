package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfInt extends AbstractPredicate implements JsArrayPredicate
{


  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfInt(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfInt(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofArrayOfInt(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

   IsArrayOfInt(final boolean required,
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
      if(v.isInt())return Optional.empty();
      else return Optional.of(new Error(v,INT_EXPECTED));
    }, required, nullable).apply(value);

  }
}
