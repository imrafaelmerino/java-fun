package com.dslplatform.json.serializers;


public class SerializerException extends RuntimeException {
  public SerializerException(final String message)
  {
    super(message);
  }

  public SerializerException(final Throwable cause)
  {
    super(cause);
  }
}
