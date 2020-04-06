package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class IsArrayOfBool extends AbstractPredicate implements JsArrayPredicate
{
  final boolean elemNullable;
  public IsArrayOfBool(final boolean required,
                       final boolean nullable
                      )
  {
    this(required,
          nullable,
         false
         );
  }
  public IsArrayOfBool(final boolean required,
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
      if(v.isBool())return Optional.empty();
      else return Optional.of(new Error(v,BOOLEAN_EXPECTED));
    }, required, nullable).apply(value);
  }
}
