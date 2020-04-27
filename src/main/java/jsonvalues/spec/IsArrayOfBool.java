package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class IsArrayOfBool extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public boolean isRequired()
  {
    return required;
  }


  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfBool(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfBool(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfBool(nullable);
  }

  IsArrayOfBool(final boolean required,
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
      if(v.isBool())return Optional.empty();
      else return Optional.of(new Error(v,BOOLEAN_EXPECTED));
    }, required, nullable).apply(value);
  }
}
