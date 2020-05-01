package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsArrayOfTestedIntSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfTestedIntSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfTestedIntSpec(predicate, false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfIntEachSuchThat(predicate,
                                                            nullable
                                                           );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final IntFunction<Optional<Error>> predicate;



  JsArrayOfTestedIntSpec(final IntFunction<Optional<Error>> predicate,
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
