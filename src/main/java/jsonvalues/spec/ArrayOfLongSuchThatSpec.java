package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class ArrayOfLongSuchThatSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfLongSuchThatSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfLongSuchThatSpec(predicate, false, nullable);

  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofArrayOfLongSuchThat(predicate,
                                                               nullable
                                                              );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
  private ArrayOfLongSpec isArrayOfLong;
  private final Function<JsArray,Optional<Error>> predicate;


   ArrayOfLongSuchThatSpec(final Function<JsArray,Optional<Error>> predicate,
                           final boolean required,
                           final boolean nullable
                          )
  {
    super(required,
          nullable
         );
    this.isArrayOfLong = new ArrayOfLongSpec(required, nullable);
    this.predicate = predicate;
  }
  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfLong.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());
  }
}
