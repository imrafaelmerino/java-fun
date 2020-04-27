package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class IsIntegralSuchThat extends AbstractPredicate implements JsIntegralPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new IsIntegralSuchThat(required,true,predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new IsIntegralSuchThat(false,nullable,predicate);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofIntegralSuchThat(predicate,
                                                             nullable
                                                            );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<BigInteger, Optional<Error>> predicate;

  IsIntegralSuchThat(final boolean required,
                     final boolean nullable,
                     final Function<BigInteger, Optional<Error>> predicate
                    )
  {
    super(required,
          nullable
         );
    this.predicate = predicate;
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isIntegral,
                                                                     INTEGRAL_EXPECTED,
                                                                     required,
                                                                     nullable
                                                                    )
                                                           .apply(value);

    if (error.isPresent()|| value.isNull()) return error;
    return predicate.apply(value.toJsBigInt().value);
  }
}
