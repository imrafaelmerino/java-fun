package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class ArrayOfTestedIntSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfTestedIntSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfTestedIntSpec(predicate, false, nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return  DeserializersFactory.INSTANCE.ofArrayOfIntEachSuchThat(predicate,
                                                                   nullable
                                                                  );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final IntFunction<Optional<Error>> predicate;



  ArrayOfTestedIntSpec(final IntFunction<Optional<Error>> predicate,
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
                                             if (v.isInt()) return predicate.apply(v.toJsInt().value);
                                             else return Optional.of(new Error(v,
                                                                               INT_EXPECTED
                                                                     )
                                                                    );
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
