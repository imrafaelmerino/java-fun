package jsonvalues;

import com.fasterxml.jackson.core.JsonFactory;

class JacksonFactory
{

  private JacksonFactory()
  {
  }

  static final JsonFactory INSTANCE = new JsonFactory();
}
