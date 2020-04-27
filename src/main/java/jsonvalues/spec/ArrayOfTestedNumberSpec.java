package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class ArrayOfTestedNumberSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfTestedNumberSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfTestedNumberSpec(predicate, false, nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfNumberEachSuchThat(predicate,
                                                                      nullable
                                                                     );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  private final Function<JsNumber, Optional<Error>> predicate;

   ArrayOfTestedNumberSpec(final Function<JsNumber,Optional<Error>> predicate,
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
    return Functions.testArrayOfTestedValue(v ->
                                           {
                                             if (v.isNumber()) return predicate.apply(v.toJsNumber());
                                             else return Optional.of(new Error(v,
                                                                               NUMBER_EXPECTED)
                                                                    );
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
