package jsonvalues.spec;

abstract class AbstractPredicate
{

  final boolean required;
  final boolean nullable;

  public AbstractPredicate(final boolean required,
                           final boolean nullable
                          )
  {
    this.required = required;
    this.nullable = nullable;
  }


}
