package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfInt extends AbstractPredicate implements JsArrayPredicate
{

   IsArrayOfInt(final boolean required,
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
    return Functions.testArrayOfTestedValue(v-> {
      if(v.isInt())return Optional.empty();
      else return Optional.of(new Error(v,INT_EXPECTED));
    }, required, nullable).apply(value);

  }
}
