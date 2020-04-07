package com.dslplatform.json;


public class DeserializerException extends RuntimeException {
  public DeserializerException(final String message)
  {
    super(message);
  }

  public DeserializerException(final Throwable cause)
  {
    super(cause);
  }
}
