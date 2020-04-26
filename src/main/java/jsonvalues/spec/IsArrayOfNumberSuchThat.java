package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;

class IsArrayOfNumberSuchThat extends AbstractPredicate implements JsArrayPredicate
{
  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  @Override
  public JsSpec nullable()
  {
    return new IsArrayOfNumberSuchThat(predicate,required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArrayOfNumberSuchThat(predicate,false,nullable);

  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
  final Function<JsArray,Optional<Error>> predicate;
  private IsArrayOfNumber isArrayOfNumber;



   IsArrayOfNumberSuchThat(final Function<JsArray,Optional<Error>> predicate,
                                 final boolean required,
                                 final boolean nullable
                                )
  {
    super(required,
          nullable
         );
    this.isArrayOfNumber=new IsArrayOfNumber(required,nullable);
    this.predicate = predicate;
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    final Optional<Error> result = isArrayOfNumber.test(value);
    if(result.isPresent()|| value.isNull())return result;
    return predicate.apply(value.toJsArray());
  }
}
