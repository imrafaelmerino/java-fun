package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfStr extends AbstractPredicate implements JsArrayPredicate
{
  final boolean elemNullable;
   IsArrayOfStr(final boolean required,
                      final boolean nullable
                     )
  {
    this(required,
          nullable,
         false
         );
  }

   IsArrayOfStr(final boolean required,
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
      if(v.isStr())return Optional.empty();
      else return Optional.of(new Error(v,STRING_EXPECTED));
    }, required, nullable).apply(value);
  }
}
