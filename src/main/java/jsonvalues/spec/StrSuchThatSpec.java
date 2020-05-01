package jsonvalues.spec;
import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

 class StrSuchThatSpec extends AbstractPredicateSpec implements JsStrPredicate
{


  @Override
  public JsSpec nullable()
  {
    return new StrSuchThatSpec(required, true, predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new StrSuchThatSpec(false, nullable, predicate);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofStrSuchThat(predicate,
                                                nullable
                                               );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<String,Optional<Error>> predicate;

   StrSuchThatSpec(final boolean required,
                   final boolean nullable,
                   final Function<String,Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isStr,
                                                                          STRING_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent()|| value.isNull())return error;
    return predicate.apply(value.toJsStr().value);
  }
}
