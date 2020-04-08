package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

//todo elemNullable
class IsArrayOfInt extends AbstractPredicate implements JsArrayPredicate
{

  final boolean elemNullable;
   IsArrayOfInt(final boolean required,
                      final boolean nullable
                     )
  {
    this(required,
          nullable,
         false
         );
  }

   IsArrayOfInt(final boolean required,
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
      if(v.isInt())return Optional.empty();
      else return Optional.of(new Error(v,INT_EXPECTED));
    }, required, nullable).apply(value);

  }
}
