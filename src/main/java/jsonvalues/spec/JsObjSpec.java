package jsonvalues.spec;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;


public class JsObjSpec implements Schema<JsObj>
{

  final boolean strict;
  Map<String, JsSpec> bindings = HashMap.empty();

  static Set<JsErrorPair> test(final JsPath parent,
                               final JsObjSpec parentObjSpec,
                               final Set<JsErrorPair> errors,
                               final JsObj json
                              )
  {

    for (final Tuple2<String, JsValue> next : json)
    {
      final String key = next._1;
      final JsValue value = next._2;
      final JsPath keyPath = JsPath.fromKey(key);
      final JsPath currentPath = parent.append(keyPath);
      final JsSpec spec = parentObjSpec.bindings.getOrElse(key,
                                                           null
                                                          );
      if (spec == null) {
        if(parentObjSpec.strict)
        {
          errors.add(JsErrorPair.of(currentPath,
                                    new Error(value,
                                              SPEC_MISSING
                                    )
                                   ));
        }
      } else Functions.addErrors(errors,
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
    return test(JsPath.empty(),
                this,
                new HashSet<>(),
                json
               );
  }

  public final static class Pair
  {
    public final String key;
    public final JsSpec spec;

    public static Pair pair(final String key,
                            final JsSpec spec
                           )
    {
      return new Pair(requireNonNull(key),
                      requireNonNull(spec)
      );
    }

    private Pair(final String key,
                 final JsSpec spec
                )
    {
      this.key = key;
      this.spec = spec;
    }
  }

  public JsObjSpec strict(Pair pair,
                          Pair... others
                         )
  {
    return new JsObjSpec(true,
                         pair,
                         others
    );
  }

  public JsObjSpec lenient(Pair pair,
                           Pair... others
                          )
  {
    return new JsObjSpec(false,
                         pair,
                         others
    );
  }

  JsObjSpec(boolean strict,
            Pair pair,
            Pair... others
           )
  {
    bindings = bindings.put(pair.key,
                            pair.spec
                           );
    for (Pair p : others)
      bindings = bindings.put(p.key,
                              p.spec
                             );
    this.strict = strict;
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            boolean strict
           )
  {
    bindings = bindings.put(key,
                            spec
                           );
    this.strict = strict;
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            boolean strict
           )
  {
    this(key,
         spec,
         strict
        );
    bindings = bindings.put(key1,
                            spec1
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         strict
        );
    bindings = bindings.put(key2,
                            spec2
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         strict
        );
    bindings = bindings.put(key3,
                            spec3
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         true
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         strict
        );
    bindings = bindings.put(key4,
                            spec4
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4,
                                 String key5,
                                 JsSpec spec5
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4,
                                  String key5,
                                  JsSpec spec5
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         key4,
         spec4,
         strict
        );
    bindings = bindings.put(key5,
                            spec5
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4,
                                 String key5,
                                 JsSpec spec5,
                                 String key6,
                                 JsSpec spec6
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4,
                                  String key5,
                                  JsSpec spec5,
                                  String key6,
                                  JsSpec spec6
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         key4,
         spec4,
         key5,
         spec5,
         strict
        );
    bindings = bindings.put(key6,
                            spec6
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4,
                                 String key5,
                                 JsSpec spec5,
                                 String key6,
                                 JsSpec spec6,
                                 String key7,
                                 JsSpec spec7
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4,
                                  String key5,
                                  JsSpec spec5,
                                  String key6,
                                  JsSpec spec6,
                                  String key7,
                                  JsSpec spec7
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         key4,
         spec4,
         key5,
         spec5,
         key6,
         spec6,
         strict
        );
    bindings = bindings.put(key7,
                            spec7
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4,
                                 String key5,
                                 JsSpec spec5,
                                 String key6,
                                 JsSpec spec6,
                                 String key7,
                                 JsSpec spec7,
                                 String key8,
                                 JsSpec spec8
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4,
                                  String key5,
                                  JsSpec spec5,
                                  String key6,
                                  JsSpec spec6,
                                  String key7,
                                  JsSpec spec7,
                                  String key8,
                                  JsSpec spec8
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         key4,
         spec4,
         key5,
         spec5,
         key6,
         spec6,
         key7,
         spec7,
         strict
        );
    bindings = bindings.put(key8,
                            spec8
                           );
  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4,
                                 String key5,
                                 JsSpec spec5,
                                 String key6,
                                 JsSpec spec6,
                                 String key7,
                                 JsSpec spec7,
                                 String key8,
                                 JsSpec spec8,
                                 String key9,
                                 JsSpec spec9
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         key9,
                         spec9,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4,
                                  String key5,
                                  JsSpec spec5,
                                  String key6,
                                  JsSpec spec6,
                                  String key7,
                                  JsSpec spec7,
                                  String key8,
                                  JsSpec spec8,
                                  String key9,
                                  JsSpec spec9
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         key9,
                         spec9,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         key4,
         spec4,
         key5,
         spec5,
         key6,
         spec6,
         key7,
         spec7,
         key8,
         spec8,
         strict
        );
    bindings = bindings.put(key9,
                            spec9
                           );

  }

  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4,
                                 String key5,
                                 JsSpec spec5,
                                 String key6,
                                 JsSpec spec6,
                                 String key7,
                                 JsSpec spec7,
                                 String key8,
                                 JsSpec spec8,
                                 String key9,
                                 JsSpec spec9,
                                 String key10,
                                 JsSpec spec10
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         key9,
                         spec9,
                         key10,
                         spec10,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4,
                                  String key5,
                                  JsSpec spec5,
                                  String key6,
                                  JsSpec spec6,
                                  String key7,
                                  JsSpec spec7,
                                  String key8,
                                  JsSpec spec8,
                                  String key9,
                                  JsSpec spec9,
                                  String key10,
                                  JsSpec spec10
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         key9,
                         spec9,
                         key10,
                         spec10,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9,
            String key10,
            JsSpec spec10,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         key4,
         spec4,
         key5,
         spec5,
         key6,
         spec6,
         key7,
         spec7,
         key8,
         spec8,
         key9,
         spec9,
         strict
        );
    bindings = bindings.put(key10,
                            spec10
                           );
  }


  public static JsObjSpec strict(String key,
                                 JsSpec spec,
                                 String key1,
                                 JsSpec spec1,
                                 String key2,
                                 JsSpec spec2,
                                 String key3,
                                 JsSpec spec3,
                                 String key4,
                                 JsSpec spec4,
                                 String key5,
                                 JsSpec spec5,
                                 String key6,
                                 JsSpec spec6,
                                 String key7,
                                 JsSpec spec7,
                                 String key8,
                                 JsSpec spec8,
                                 String key9,
                                 JsSpec spec9,
                                 String key10,
                                 JsSpec spec10,
                                 String key11,
                                 JsSpec spec11
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         key9,
                         spec9,
                         key10,
                         spec10,
                         key11,
                         spec11,
                         true
    );
  }


  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4,
                                  String key5,
                                  JsSpec spec5,
                                  String key6,
                                  JsSpec spec6,
                                  String key7,
                                  JsSpec spec7,
                                  String key8,
                                  JsSpec spec8,
                                  String key9,
                                  JsSpec spec9,
                                  String key10,
                                  JsSpec spec10,
                                  String key11,
                                  JsSpec spec11
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         key4,
                         spec4,
                         key5,
                         spec5,
                         key6,
                         spec6,
                         key7,
                         spec7,
                         key8,
                         spec8,
                         key9,
                         spec9,
                         key10,
                         spec10,
                         key11,
                         spec11,
                         false
    );
  }

  JsObjSpec(String key,
            JsSpec spec,
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9,
            String key10,
            JsSpec spec10,
            String key11,
            JsSpec spec11,
            boolean strict
           )
  {
    this(key,
         spec,
         key1,
         spec1,
         key2,
         spec2,
         key3,
         spec3,
         key4,
         spec4,
         key5,
         spec5,
         key6,
         spec6,
         key7,
         spec7,
         key8,
         spec8,
         key9,
         spec9,
         key10,
         spec10,
         strict
        );
    bindings = bindings.put(key11,
                            spec11
                           );
  }
}
