package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Function;
import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class ArrayOfTestedStrSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfTestedStrSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfTestedStrSpec(predicate, false, nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfStrEachSuchThat(predicate,
                                                                   nullable
                                                                  );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<String, Optional<Error>> predicate;

  ArrayOfTestedStrSpec(final Function<String, Optional<Error>> predicate,
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
                                             if (v.isStr()) return predicate.apply(v.toJsStr().value);
                                             else return Optional.of(new Error(v,
                                                                               STRING_EXPECTED
                                                                     )
                                                                    );
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
