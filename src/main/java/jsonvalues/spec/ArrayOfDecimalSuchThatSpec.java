package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class ArrayOfDecimalSuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new ArrayOfDecimalSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfDecimalSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecDeserializer deserializer()
  {
    return   DeserializersFactory.INSTANCE.ofArrayOfDecimalSuchThat(predicate,
                                                                    nullable
                                                                   );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  private ArrayOfDecimalSpec isArrayOfDecimal;
  private final Function<JsArray,Optional<Error>> predicate;



   ArrayOfDecimalSuchThatSpec(final Function<JsArray,Optional<Error>> predicate,
                              final boolean required,
                              final boolean nullable
                             )
  {
    super(required,
          nullable
         );
    this.isArrayOfDecimal = new ArrayOfDecimalSpec(required, nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfDecimal.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());
  }
}
