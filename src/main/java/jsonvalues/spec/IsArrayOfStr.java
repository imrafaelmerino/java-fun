package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfStr extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfStr(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfStr(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfStr(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

   IsArrayOfStr(final boolean required,
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
      if(v.isStr())return Optional.empty();
      else return Optional.of(new Error(v,STRING_EXPECTED));
    }, required, nullable).apply(value);
  }
}
