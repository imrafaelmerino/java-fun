package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfObj extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfObj(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfObj(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfObj(nullable
                                                      );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
   IsArrayOfObj(final boolean required,
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
      if(v.isObj())return Optional.empty();
      else return Optional.of(new Error(v,OBJ_EXPECTED));
    }, required, nullable).apply(value);
  }
}
