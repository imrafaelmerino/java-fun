package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class IsArrayOfIntegralSuchThat extends AbstractPredicate implements JsArrayPredicate
{

  private IsArrayOfIntegral isArrayOfIntegral;
  final Function<JsArray, Optional<Error>> predicate;
  final boolean elemNullable;


  public IsArrayOfIntegralSuchThat(final Function<JsArray, Optional<Error>> predicate,
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

  public IsArrayOfIntegralSuchThat(final Function<JsArray, Optional<Error>> predicate,
                                   final boolean required,
                                   final boolean nullable,
                                   final boolean elemNullable
                                  )
  {
    super(required,
          nullable
         );
    this.isArrayOfIntegral = new IsArrayOfIntegral(required,
                                                   nullable);
    this.predicate = predicate;
    this.elemNullable = elemNullable;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfIntegral.test(value);
    if (result.isPresent()) return result;
    return predicate.apply(value.toJsArray());
  }
}
