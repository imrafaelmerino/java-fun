package jsonvalues.spec;



/**
 * represents an internal error of the library. If this exception is thrown, it means there is an error
 * in the source code of the library and something need to be changed.
 *
 */
class InternalError  extends UnsupportedOperationException
{

  InternalError(final String message
                       )
  {
    super(message);
  }

  static InternalError longWasExpected(String message)
  {
    return new InternalError(message);
  }

  static InternalError decimalWasExpected(String message)
  {
    return new InternalError(message);
  }

  static InternalError integerWasExpected(String message)
  {
    return new InternalError(message);
  }

  static InternalError integralWasExpected(String message)
  {
    return new InternalError(message);
  }

  static InternalError numberWasExpected(String message)
  {
    return new InternalError(message);
  }

  static InternalError stringWasExpected(String message)
  {
    return new InternalError(message);
  }

  static InternalError objWasExpected(String message)
  {
    return new InternalError(message);
  }


}
