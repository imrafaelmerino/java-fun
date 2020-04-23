package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsArrayOfObjSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  final Function<JsArray, Optional<Error>> predicate;
  private IsArrayOfObj isArrayOfObj;



  IsArrayOfObjSuchThat(final Function<JsArray, Optional<Error>> predicate,
                       final boolean required,
                       final boolean nullable
                      )
  {
    super(required,
          nullable
         );
    this.isArrayOfObj = new IsArrayOfObj(required,
                                         nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfObj.test(value);
    if (result.isPresent()|| value.isNull()) return result;
    return predicate.apply(value.toJsArray());
  }
}
