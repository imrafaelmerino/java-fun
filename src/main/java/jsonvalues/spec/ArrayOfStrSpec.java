package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class ArrayOfStrSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfStrSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfStrSpec(false, nullable);
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

   ArrayOfStrSpec(final boolean required,
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
