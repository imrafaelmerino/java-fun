package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class ArrayOfIntegralSuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfIntegralSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfIntegralSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofArrayOfIntegralSuchThat(predicate,
                                                                   nullable
                                                                  );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
  private ArrayOfIntegralSpec isArrayOfIntegral;
  private final Function<JsArray, Optional<Error>> predicate;




   ArrayOfIntegralSuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                               final boolean required,
                               final boolean nullable
                              )
  {
    super(required,
          nullable
         );
    this.isArrayOfIntegral = new ArrayOfIntegralSpec(required,
                                                     nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfIntegral.test(value);
    if (result.isPresent()|| value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
