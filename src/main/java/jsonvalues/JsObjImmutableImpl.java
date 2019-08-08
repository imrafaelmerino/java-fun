package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.ifJsonElse;
import static jsonvalues.Functions.ifObjElse;
import static jsonvalues.Trampoline.more;

class JsObjImmutableImpl extends AbstractJsObj<MyScalaImpl.Map, JsArray>
{
    public static final long serialVersionUID = 1L;
    @SuppressWarnings("squid:S3008")//EMPTY should be a valid name
    static JsObjImmutableImpl EMPTY = new JsObjImmutableImpl(MyScalaImpl.Map.EMPTY);
    private static final JsPath EMPTY_PATH = JsPath.empty();
    private transient volatile int hascode;
    private transient volatile @Nullable String str;


    JsObjImmutableImpl(final MyScalaImpl.Map myMap)
    {
        super(myMap);
    }

    @Override
    JsArray emptyArray()
    {
        return JsArrayImmutableImpl.EMPTY;
    }

    @Override
    AbstractJsObj<MyScalaImpl.Map, JsArray> emptyObject()
    {
        return EMPTY;
    }

    //equals method is inherited, so it's implemented. The purpose of this method is to cache
    //the hashcode once calculated. the object is immutable and it won't change
    @SuppressWarnings("squid:S1206")
    @Override
    public final int hashCode()
    {
        if (hascode != 0) return hascode;
        hascode = super.hashCode();
        return hascode;

    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    AbstractJsObj<MyScalaImpl.Map, JsArray> of(final MyScalaImpl.Map map)
    {
        return new JsObjImmutableImpl(map);
    }

    @Override
    public JsObj toImmutable()
    {
        return this;
    }

    @Override
    public JsObj toMutable()
    {
        Map<String, JsElem> acc = new HashMap<>();
        @SuppressWarnings("squid:S1905")// in return checkerframework does its job!
        final Set<@KeyFor("map") String> keys = (Set<@KeyFor("map") String>) map.fields();
        keys.forEach(key -> accept(val -> acc.put(key,
                                                  val
                                                 ),
                                   obj -> acc.put(key,
                                                  obj.toMutable()
                                                 ),
                                   arr -> acc.put(key,
                                                  arr.toMutable()
                                                 )
                                  ).accept(map.get(key))
                    );
        return new JsObjMutableImpl(new MyJavaImpl.Map(acc));

    }


    @Override
    public final String toString()
    {
        if (str != null) return str;
        str = super.toString();
        return str;

    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {

        return mapElems(this,
                        requireNonNull(fn),
                        p -> true,
                        EMPTY_PATH
                       )
        .get();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return mapElems(this,
                        requireNonNull(fn),
                        predicate,
                        EMPTY_PATH
                       )
        .get();
    }

    private Trampoline<JsObj> mapElems(final JsObj obj,
                                       final Function<? super JsPair, ? extends JsElem> fn,
                                       final Predicate<? super JsPair> predicate,
                                       final JsPath path
                                      )
    {

        return obj.ifEmptyElse(Trampoline.done(obj),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.key(head.getKey());

                                   final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapElems(tail,
                                                                                                     fn,
                                                                                                     predicate,
                                                                                                     path
                                                                                                    ));
                                   return ifJsonElse(headJson -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                       headJson
                                                                                                                      )),
                                                     headElem ->
                                                     {
                                                         JsElem headMapped = JsPair.of(headPath,
                                                                                       headElem
                                                                                      )
                                                                                   .ifElse(predicate,
                                                                                           p -> fn.apply(p),
                                                                                           p -> headElem
                                                                                          );
                                                         return more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                      headMapped
                                                                                                                     ));
                                                     }

                                                    ).apply(head.getValue());
                               }
                              );


    }

    @Override
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return MapFunctions.mapElems_(requireNonNull(fn),
                                      p -> true,
                                      EMPTY_PATH
                                     )
                           .apply(this)
                           .get();
    }

    @Override
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return MapFunctions.mapElems_(requireNonNull(fn),
                                      requireNonNull(predicate),
                                      EMPTY_PATH
                                     )
                           .apply(this)
                           .get();
    }


    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn)
    {
        return MapFunctions.mapKeys(requireNonNull(fn),
                                    p -> true,
                                    EMPTY_PATH
                                   )
                           .apply(this)
                           .get();
    }

    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return MapFunctions.mapKeys(requireNonNull(fn),
                                    requireNonNull(predicate),
                                    EMPTY_PATH
                                   )
                           .apply(this)
                           .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn)
    {
        return MapFunctions.mapKeys_(requireNonNull(fn),
                                     p -> true,
                                     EMPTY_PATH
                                    )
                           .apply(this)
                           .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") // xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return MapFunctions.mapKeys_(requireNonNull(fn),
                                     requireNonNull(predicate),
                                     EMPTY_PATH
                                    )
                           .apply(this)
                           .get();
    }


    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate
                              )
    {

        return MapFunctions.mapJsObj(requireNonNull(fn),
                                     requireNonNull(predicate),
                                     EMPTY_PATH
                                    )
                           .apply(this)
                           .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return MapFunctions.mapJsObj(requireNonNull(fn),
                                     (path, obj) -> true,
                                     EMPTY_PATH
                                    )
                           .apply(this)
                           .get();
    }


    @Override
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
    {
        return MapFunctions.mapJsObj_(fn,
                                      predicate,
                                      EMPTY_PATH
                                     )
                           .apply(this)
                           .get();

    }

    @Override
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return MapFunctions.mapJsObj_(fn,
                                      (path, obj) -> true,
                                      EMPTY_PATH
                                     )
                           .apply(this)
                           .get();

    }


    @Override
    public final JsObj filterElems(final Predicate<? super JsPair> filter)
    {
        return filterValues(this,
                            requireNonNull(filter),
                            EMPTY_PATH
                           )
        .get();
    }

    private Trampoline<JsObj> filterValues(final JsObj obj,
                                           final Predicate<? super JsPair> predicate,
                                           final JsPath path
                                          )
    {
        return obj.ifEmptyElse(Trampoline.done(obj),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.key(head.getKey());

                                   final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterValues(tail,
                                                                                                         predicate,
                                                                                                         path
                                                                                                        ));
                                   return ifJsonElse(headElem -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                       headElem
                                                                                                                      )),
                                                     headElem -> JsPair.of(headPath,
                                                                           headElem
                                                                          )
                                                                       .ifElse(predicate,
                                                                               p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                           headElem
                                                                                                                                          )),
                                                                               p -> tailCall
                                                                              )

                                                    )
                                   .apply(head.getValue());

                               }
                              );

    }


    @Override
    public final JsObj filterElems_(final Predicate<? super JsPair> filter)
    {
        return FilterFunctions.filterObjElems_(requireNonNull(filter),
                                               JsPath.empty().index(-1)
                                              )
                              .apply(this)
                              .get();

    }

    @Override
    public final JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return filterJsObjs(this,
                            requireNonNull(filter),
                            EMPTY_PATH
                           )
        .get();
    }

    private Trampoline<JsObj> filterJsObjs(final JsObj obj,
                                           final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                           final JsPath path
                                          )
    {
        return obj.ifEmptyElse(Trampoline.done(obj),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.key(head.getKey());

                                   final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterJsObjs(tail,
                                                                                                         predicate,
                                                                                                         path
                                                                                                        ));
                                   return ifObjElse(json -> JsPair.of(headPath,
                                                                      json
                                                                     )
                                                                  .ifElse(p -> predicate.test(p.path,
                                                                                              json
                                                                                             ),
                                                                          p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                     json
                                                                                                                                    )),
                                                                          p -> tailCall
                                                                         ),
                                                    value -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                   value
                                                                                                                  ))
                                                   )
                                   .apply(head.getValue());
                               }

                              );
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return FilterFunctions.filterObjObjs_(requireNonNull(filter),
                                              EMPTY_PATH
                                             )
                              .apply(this)
                              .get();

    }

    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> filter)
    {
        return filterKeys(this,
                          requireNonNull(filter),
                          EMPTY_PATH
                         )
        .get();
    }

    private Trampoline<JsObj> filterKeys(final JsObj obj,
                                         final Predicate<? super JsPair> predicate,
                                         final JsPath path
                                        )
    {
        return obj.ifEmptyElse(Trampoline.done(obj),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.key(head.getKey());

                                   final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterKeys(tail,
                                                                                                       predicate,
                                                                                                       path
                                                                                                      ));
                                   return JsPair.of(headPath,
                                                    head.getValue()
                                                   )
                                                .ifElse(predicate,
                                                        p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                    head.getValue()
                                                                                                                   )),


                                                        p -> tailCall
                                                       );
                               }
                              );


    }


    @Override
    public final JsObj filterKeys_(final Predicate<? super JsPair> filter)
    {
        return FilterFunctions.filterObjKeys_(requireNonNull(filter),
                                              EMPTY_PATH
                                             )
                              .apply(this)
                              .get();

    }


    /**
     * Serialize this {@code ScalaJsObj} instance.
     *
     * @serialData The {@code String}) representation of this json object.
     */
    private void writeObject(ObjectOutputStream s) throws IOException
    {
        s.defaultWriteObject();
        s.writeObject(toString());

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
        final String json = (String) s.readObject();
        try
        {
            map = ((JsObjImmutableImpl) JsObj.parse(json)
                                             .orElseThrow()).map;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsObj.class.getName(),
                                                             malformedJson.getMessage()
                                                            ));
        }

    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public boolean isImmutable()
    {
        return true;
    }

}
