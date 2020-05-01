package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;

public interface JsSpec
{
  /**
   When this spec is associated to a key in a JsObjSpec, the required flag indicates whether or
   not the key is optional.
   */
  boolean isRequired();

  /**
   @return the same spec with the nullable flag enabled
   */
  JsSpec nullable();


  /**
   @return the same spec with the optional flag enabled
   */
  JsSpec optional();

  /**
   @return the deserializer used during the parsing process to parse an array of bytes or string
   into a json value
   */
  SpecParser parser();


  Set<JsErrorPair> test(final JsPath parentPath,
                        final JsValue value
                       );



}


