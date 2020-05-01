package jsonvalues.spec;
import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class NumberSuchThatSpec extends AbstractPredicateSpec implements JsNumberPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new NumberSuchThatSpec(required, true, predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new NumberSuchThatSpec(false, nullable, predicate);
  }

  @Override
  public SpecParser parser()
  {
    return   ParserFactory.INSTANCE.ofNumberSuchThat(predicate,
                                                     nullable
                                                    );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsNumber,Optional<Error>> predicate;

   NumberSuchThatSpec(final boolean required,
                      final boolean nullable,
                      final Function<JsNumber,Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isNumber,
                                                                          NUMBER_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent()|| value.isNull())return error;
    return predicate.apply(value.toJsNumber());
  }
}
