package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsArrayOfStrSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  final Function<JsArray, Optional<Error>> predicate;
  private IsArrayOfStr isArrayOfString;
  final boolean elemNullable;

  public IsArrayOfStrSuchThat(final Function<JsArray, Optional<Error>> predicate,
                              final boolean required,
                              final boolean nullable,
                              final boolean elemNullable
                             )
  {
    super(required,
          nullable
         );
    this.isArrayOfString = new IsArrayOfStr(required,
                                            nullable);
    this.predicate = predicate;
    this.elemNullable = elemNullable;
  }

  public IsArrayOfStrSuchThat(final Function<JsArray, Optional<Error>> predicate,
                              final boolean required,
                              final boolean nullable
                             )
  {
    this(predicate,
         required,
         nullable,
         false
        );
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfString.test(value);
    if (result.isPresent()) return result;
    return predicate.apply(value.toJsArray());
  }
}
