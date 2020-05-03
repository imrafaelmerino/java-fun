package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;
import static jsonvalues.spec.ERROR_CODE.*;

class JsObjSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate
{

  @Override
  public JsSpec nullable()
  {
    return new JsObjSuchThatSpec(required, true, predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new JsObjSuchThatSpec(false, nullable, predicate);
  }

  @Override
  public JsSpecParser parser()
  {
    return  JsSpecParsers.INSTANCE.ofObjSuchThat(predicate,
                                                 nullable
                                                );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsObj,Optional<Error>> predicate;

   JsObjSuchThatSpec(final boolean required,
                     final boolean nullable,
                     final Function<JsObj,Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isObj,
                                                                          OBJ_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent()|| value.isNull())return error;
    return predicate.apply(value.toJsObj());
  }
}
