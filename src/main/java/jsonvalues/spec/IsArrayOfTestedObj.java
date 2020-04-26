package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfTestedObj extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfTestedObj(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfTestedObj(predicate,false,nullable);
  }
  @Override
  public boolean isNullable()
  {
    return nullable;
  }
  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsObj,Optional<Error>> predicate;



   IsArrayOfTestedObj(final Function<JsObj,Optional<Error>> predicate,
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
