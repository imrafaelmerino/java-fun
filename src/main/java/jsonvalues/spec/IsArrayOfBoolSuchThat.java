package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;


class IsArrayOfBoolSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private IsArrayOfBool isArrayOfBool;
  final Function<JsArray,Optional<Error>> predicate;
  final boolean elemNullable;

  public IsArrayOfBoolSuchThat(final Function<JsArray,Optional<Error>>  predicate,
                               final boolean required,
                               final boolean nullable
                              )
  {
   this(predicate,required,nullable,false);
  }
  public IsArrayOfBoolSuchThat(final Function<JsArray,Optional<Error>>  predicate,
                               final boolean required,
                               final boolean nullable,
                               final boolean elemNullable
                              )
  {
    super(required,
          nullable
         );
    this.isArrayOfBool = new IsArrayOfBool(required,nullable);
    this.predicate = predicate;
    this.elemNullable = elemNullable;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfBool.test(value);
    if(result.isPresent())return result;
    return predicate.apply(value.toJsArray());

  }
}
