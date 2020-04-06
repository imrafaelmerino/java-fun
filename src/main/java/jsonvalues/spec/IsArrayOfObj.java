package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class IsArrayOfObj extends AbstractPredicate implements JsArrayPredicate
{
  final boolean elemNullable;
  public IsArrayOfObj(final boolean required,
                      final boolean nullable
                     )
  {
    this(required,
          nullable,
         false
         );
  }

  public IsArrayOfObj(final boolean required,
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
      if(v.isObj())return Optional.empty();
      else return Optional.of(new Error(v,OBJ_EXPECTED));
    }, required, nullable).apply(value);
  }
}
