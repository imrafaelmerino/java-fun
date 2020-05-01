package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsObj;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class JsArrayOfTestedObjSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfTestedObjSpec(predicate, required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfTestedObjSpec(predicate, false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfObjEachSuchThat(predicate,
                                                            nullable
                                                           );
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsObj,Optional<Error>> predicate;



   JsArrayOfTestedObjSpec(final Function<JsObj,Optional<Error>> predicate,
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
                                             if (v.isObj()) return predicate.apply(v.toJsObj());
                                             else return Optional.of(new Error(v,
                                                                               OBJ_EXPECTED)
                                                                    );
                                           },
                                            required,
                                            nullable
                                           )
                    .apply(value);
  }
}
