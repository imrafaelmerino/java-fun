package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;
import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfDecimal extends AbstractPredicate implements JsArrayPredicate
{
  public IsArrayOfDecimal(final boolean required,
                          final boolean nullable
                         )
  {
    super(required,
          nullable
         );
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArrayOfTestedElem(JsValue::isDecimal, DECIMAL_EXPECTED, required, nullable).apply(value);

  }
}
