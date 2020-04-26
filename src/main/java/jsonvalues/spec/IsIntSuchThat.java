package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

import static jsonvalues.spec.ERROR_CODE.*;

class IsIntSuchThat extends AbstractPredicate implements JsIntPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new IsIntSuchThat(required,true,predicate);
  }

  @Override
  public JsSpec optional()
  {
    return new IsIntSuchThat(false,nullable,predicate);
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
  final IntFunction<Optional<Error>> predicate;

   IsIntSuchThat(final boolean required,
                       final boolean nullable,
                       final IntFunction<Optional<Error>> predicate
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
    final Optional<Error> error = jsonvalues.spec.Functions.testElem(JsValue::isInt,
                                                                     INT_EXPECTED,
                                                                     required,
                                                                     nullable
                                                                    )
                                                           .apply(value);

    if (error.isPresent() || value.isNull()) return error;
    return predicate.apply(value.toJsInt().value);
  }
}
