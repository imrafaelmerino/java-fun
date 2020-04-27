package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.*;

class LongSuchThatSpec extends AbstractPredicateSpec implements JsLongPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new LongSuchThatSpec(required, true, predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new LongSuchThatSpec(false, nullable, predicate);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofLongSuchThat(predicate,
                                                        nullable
                                                       );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final LongFunction<Optional<Error>> predicate;

   LongSuchThatSpec(final boolean required,
                    final boolean nullable,
                    final LongFunction<Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isLong,
                                                                          LONG_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent()|| value.isNull())return error;
    return predicate.apply(value.toJsLong().value);
  }
}
