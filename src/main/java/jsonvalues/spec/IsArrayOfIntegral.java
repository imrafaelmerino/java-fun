package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfIntegral extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfIntegral(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfIntegral(false,nullable);

  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfIntegral(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  IsArrayOfIntegral(final boolean required,
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
    return Functions.testArrayOfTestedValue(v ->
                                           {
                                             if (v.isIntegral()) return Optional.empty();
                                             else return Optional.of(new Error(v,
                                                                               INTEGRAL_EXPECTED));
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
