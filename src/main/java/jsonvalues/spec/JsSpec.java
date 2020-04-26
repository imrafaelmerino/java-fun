package jsonvalues.spec;

public interface JsSpec {
  /**
   When this spec is associated to a key in a JsObjSpec, the required flag indicates whether or
   not the key is optional.
   */
  boolean isRequired();

  /**
   A spec describes a value associated to a key in a JsObj or a value in a JsArray. The flag nullable
   indicated whether or not it can be null
   */
  boolean isNullable();


  JsSpec nullable();
  JsSpec optional();


}


