package jsonvalues.spec;

import jsonvalues.JsValue;
import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfLong extends AbstractPredicate implements JsArrayPredicate
{
  final boolean elemNullable;
   IsArrayOfLong(final boolean required,
                       final boolean nullable
                      )
  {
    this(required,
          nullable,
          false
         );
  }

   IsArrayOfLong(final boolean required,
                       final boolean nullable,
                       final boolean elemNullable
                      )
  {
    super(required,
          nullable
         );
    this.elemNullable = elemNullable;
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
