package jsonvalues.spec;

import io.vavr.Tuple2;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

public class JsObjSpec implements Schema<JsObj>
{
  private final Map<String,JsSpec> bindings = new HashMap<>();

  static Set<JsErrorPair> test(final JsPath parent,
                               final JsObjSpec parentObjSpec,
                               final Set<JsErrorPair> errors,
                               final JsObj json)
  {
    for (final Tuple2<String, JsValue> next : json)
    {
      final String key = next._1;
      final JsValue value = next._2;
      final JsPath keyPath = JsPath.fromKey(key);
      final JsPath currentPath = parent.append(keyPath);
      final JsSpec spec = parentObjSpec.bindings.get(key);
      Functions.addErrors(errors,
                          value,
                          currentPath,
                          spec
                         );

    }
    return errors;
  }



  @Override
  public Set<JsErrorPair> test(final JsObj json)
  {
    return test(JsPath.empty(),this,new HashSet<>(),json);
  }

  public final static class Pair {
    public final String key;
    public final JsSpec spec;

    public static Pair pair(final String key,
                            final JsSpec spec){
      return new Pair(requireNonNull(key),
                      requireNonNull(spec));
    }

    private Pair(final String key,
                 final JsSpec spec
               )
    {
      this.key = key;
      this.spec = spec;
    }
  }

  public JsObjSpec(Pair pair, Pair... others){
     bindings.put(pair.key,pair.spec);
     for(Pair p:others) bindings.put(p.key,p.spec);
  }

  public JsObjSpec(String key, JsSpec spec){
    bindings.put(key,spec);
  }

  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1){
    this(key,spec);
    bindings.put(key1,spec1);
  }

  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2
                  ){
    this(key,spec,key1,spec1);
    bindings.put(key2,spec2);
  }
  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2);
    bindings.put(key3,spec3);
  }

  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3,
                   String key4, JsSpec spec4
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2,
         key3,spec3);
    bindings.put(key4,spec4);
  }

  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3,
                   String key4, JsSpec spec4,
                   String key5, JsSpec spec5
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2,
         key3,spec3,
         key4,spec4);
    bindings.put(key5,spec5);
  }

  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3,
                   String key4, JsSpec spec4,
                   String key5, JsSpec spec5,
                   String key6, JsSpec spec6
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2,
         key3,spec3,
         key4,spec4,
         key5,spec5);
    bindings.put(key6,spec6);
  }

  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3,
                   String key4, JsSpec spec4,
                   String key5, JsSpec spec5,
                   String key6, JsSpec spec6,
                   String key7, JsSpec spec7
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2,
         key3,spec3,
         key4,spec4,
         key5,spec5,
         key6,spec6);
    bindings.put(key7,spec7);
  }

  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3,
                   String key4, JsSpec spec4,
                   String key5, JsSpec spec5,
                   String key6, JsSpec spec6,
                   String key7, JsSpec spec7,
                   String key8, JsSpec spec8
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2,
         key3,spec3,
         key4,spec4,
         key5,spec5,
         key6,spec6,
         key7,spec7);
    bindings.put(key8,spec8);
  }
  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3,
                   String key4, JsSpec spec4,
                   String key5, JsSpec spec5,
                   String key6, JsSpec spec6,
                   String key7, JsSpec spec7,
                   String key8, JsSpec spec8,
                   String key9, JsSpec spec9
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2,
         key3,spec3,
         key4,spec4,
         key5,spec5,
         key6,spec6,
         key7,spec7,
         key8,spec8);
    bindings.put(key9,spec9);
  }
  public JsObjSpec(String key, JsSpec spec,
                   String key1, JsSpec spec1,
                   String key2, JsSpec spec2,
                   String key3, JsSpec spec3,
                   String key4, JsSpec spec4,
                   String key5, JsSpec spec5,
                   String key6, JsSpec spec6,
                   String key7, JsSpec spec7,
                   String key8, JsSpec spec8,
                   String key9, JsSpec spec9,
                   String key10, JsSpec spec10
                  ){
    this(key,spec,
         key1,spec1,
         key2,spec2,
         key3,spec3,
         key4,spec4,
         key5,spec5,
         key6,spec6,
         key7,spec7,
         key8,spec8,
         key9,spec9);
    bindings.put(key10,spec10);
  }


}
