package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsArrayOfIntegralSuchThat extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfIntegralSuchThat(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfIntegralSuchThat(predicate,false,nullable);

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
  private IsArrayOfIntegral isArrayOfIntegral;
  final Function<JsArray, Optional<Error>> predicate;




   IsArrayOfIntegralSuchThat(final Function<JsArray, Optional<Error>> predicate,
                                   final boolean required,
                                   final boolean nullable
                                  )
  {
    super(required,
          nullable
         );
    this.isArrayOfIntegral = new IsArrayOfIntegral(required,
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
