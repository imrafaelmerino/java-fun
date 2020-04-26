package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.*;

class IsLongSuchThat extends AbstractPredicate implements JsLongPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsLongSuchThat(required,true,predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new IsLongSuchThat(false,nullable,predicate);
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
  final LongFunction<Optional<Error>> predicate;

   IsLongSuchThat(final boolean required,
                        final boolean nullable,
                        final LongFunction<Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isLong,
                                                                          LONG_EXPECTED,
                                                                          required,
                                                                          nullable
                                                                         )
                                                                .apply(value);

    if(error.isPresent()|| value.isNull())return error;
    return predicate.apply(value.toJsLong().value);
  }
}
