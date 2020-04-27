package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.*;

class IntSuchThatSpec extends AbstractPredicateSpec implements JsIntPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IntSuchThatSpec(required, true, predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new IntSuchThatSpec(false, nullable, predicate);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofIntSuchThat(predicate,
                                                nullable
                                               );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final IntFunction<Optional<Error>> predicate;

   IntSuchThatSpec(final boolean required,
                   final boolean nullable,
                   final IntFunction<Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isInt,
                                                                     INT_EXPECTED,
                                                                     required,
                                                                     nullable
                                                                    )
                                                           .apply(value);

    if (error.isPresent() || value.isNull()) return error;
    return predicate.apply(value.toJsInt().value);
  }
}
