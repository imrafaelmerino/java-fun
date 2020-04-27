package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;


class IsArrayOfBoolSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  @Override
  public boolean isRequired()
  {
    return required;
  }


  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfBoolSuchThat(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfBoolSuchThat(predicate,false,nullable);

  }

  @Override
  public SpecDeserializer deserializer()
  {
    return   DeserializersFactory.INSTANCE.ofArrayOfBoolSuchThat(predicate,
                                                                 nullable
                                                                );
  }

  private IsArrayOfBool isArrayOfBool;
  private final Function<JsArray,Optional<Error>> predicate;


   IsArrayOfBoolSuchThat(final Function<JsArray,Optional<Error>>  predicate,
                               final boolean required,
                               final boolean nullable
                              )
  {
    super(required,
          nullable
         );
    this.isArrayOfBool = new IsArrayOfBool(required,nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfBool.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());

  }
}
