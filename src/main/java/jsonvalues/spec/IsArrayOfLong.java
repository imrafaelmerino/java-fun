package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfLong extends AbstractPredicate implements JsArrayPredicate
{


   IsArrayOfLong(final boolean required,
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
    return Functions.testArrayOfTestedElem(v-> {
      if(v.isInt() || v.isLong())return Optional.empty();
      else return Optional.of(new Error(v,LONG_EXPECTED));
    }, required, nullable).apply(value);
  }
}
