package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;
import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfLong extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfLong(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfLong(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofArrayOfLong(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

   IsArrayOfLong(final boolean required,
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
      if(v.isInt() || v.isLong())return Optional.empty();
      else return Optional.of(new Error(v,LONG_EXPECTED));
    }, required, nullable).apply(value);
  }
}
