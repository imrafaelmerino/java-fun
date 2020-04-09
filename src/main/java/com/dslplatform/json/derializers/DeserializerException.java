package com.dslplatform.json.derializers;


public class DeserializerException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DeserializerException(final String message)
  {
    super(message);
  }

  public DeserializerException(final Throwable cause)
  {
    super(cause);
  }
}
