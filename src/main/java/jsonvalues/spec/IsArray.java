package jsonvalues.spec;

import jsonvalues.JsValue;


import java.util.Optional;


class IsArray extends AbstractPredicate implements JsArrayPredicate
{

  public IsArray(final boolean required,
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
    return Functions.testArray(required,
                               nullable)
                    .apply(value);
  }
}
