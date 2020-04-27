package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsArrayOfStrSuchThat extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfStrSuchThat(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfStrSuchThat(predicate,false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofArrayOfStrSuchThat(predicate,
                                                              nullable
                                                             );
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsArray, Optional<Error>> predicate;
  private IsArrayOfStr isArrayOfString;

   IsArrayOfStrSuchThat(final Function<JsArray, Optional<Error>> predicate,
                              final boolean required,
                              final boolean nullable
                             )
  {
    super(required,
          nullable
         );
    this.isArrayOfString = new IsArrayOfStr(required,
                                            nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfString.test(value);
    if (result.isPresent() || value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
