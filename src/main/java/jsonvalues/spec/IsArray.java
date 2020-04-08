package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;


class IsArray extends AbstractPredicate implements JsArrayPredicate
{
  final boolean elemNullable;
  private IsArray(final boolean required,
                  final boolean nullable,
                  final boolean elemNullable
                 )
  {
    super(required,
          nullable
         );
    this.elemNullable = elemNullable;
  }

  IsArray(final boolean required,
          final boolean nullable
         )
  {
    this(required,
          nullable,
         false
         );
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArray(required,
                               nullable)
                    .apply(value);
  }
}
