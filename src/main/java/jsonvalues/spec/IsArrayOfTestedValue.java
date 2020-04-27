package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsArrayOfTestedValue extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfTestedValue(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfTestedValue(predicate,false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofArrayOfValueEachSuchThat(predicate,
                                                                    nullable
                                                                   );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsValue, Optional<Error>> predicate;


  IsArrayOfTestedValue(final Function<JsValue, Optional<Error>> predicate,
                       final boolean required,
                       final boolean nullable
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
    return Functions.testArrayOfTestedValue(predicate::apply,
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
