package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;


class IsArraySuchThat extends AbstractPredicate implements JsArrayPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new IsArraySuchThat(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArraySuchThat(predicate,false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfValueSuchThat(predicate,
                                                                 nullable
                                                                );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsArray, Optional<Error>> predicate;
  private IsArray isArray;

  IsArraySuchThat(final Function<JsArray, Optional<Error>> predicate,
                       final boolean required,
                       final boolean nullable
                      )
  {
    super(required,
          nullable
         );
    this.isArray = new IsArray(required,
                               nullable);
    this.predicate = predicate;
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArray.test(value);
    if (result.isPresent()|| value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
