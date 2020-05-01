package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfTestedValueSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfTestedValueSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfTestedValueSpec(predicate, false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofArrayOfValueEachSuchThat(predicate,
                                                             nullable
                                                            );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  private final Function<JsValue, Optional<Error>> predicate;


  JsArrayOfTestedValueSpec(final Function<JsValue, Optional<Error>> predicate,
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
    return Functions.testArrayOfTestedValue(predicate,
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
