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

class MyImmutableJsObj extends MyAbstractJsObj<MyScalaMap, JsArray>
{
    public static final long serialVersionUID = 1L;
    @SuppressWarnings("squid:S3008")//EMPTY should be a valid name
    static MyImmutableJsObj EMPTY = new MyImmutableJsObj(MyScalaMap.EMPTY);
    private static final JsPath EMPTY_PATH = JsPath.empty();
    private transient volatile int hascode;
    private transient volatile @Nullable String str;


    MyImmutableJsObj(final MyScalaMap myMap)
    {
        super(myMap);
    }

    @Override
    JsArray emptyArray()
    {
        return MyImmutableJsArray.EMPTY;
    }

    @Override
    MyAbstractJsObj<MyScalaMap, JsArray> emptyObject()
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
    MyAbstractJsObj<MyScalaMap, JsArray> of(final MyScalaMap map)
    {
        return new MyImmutableJsObj(map);
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
        keys.forEach(key -> MatchExp.accept(val -> acc.put(key,
                                                           val
                                                          ),
                                            obj -> acc.put(key,
                                                           obj.toMutable()
                                                          ),
                                            arr -> acc.put(key,
                                                           arr.toMutable()
                                                          )
                                           )
                                    .accept(map.get(key))
                    );
        return new MyMutableJsObj(new MyJavaMap(acc));

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

        return new OpMapImmutableObjElems(this).map(requireNonNull(fn),
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
        return new OpMapImmutableObjElems(this).map(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    EMPTY_PATH
                                                   )
                                               .get();
    }


    @Override
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapImmutableObjElems(this).map_(requireNonNull(fn),
                                                     p -> true,
                                                     EMPTY_PATH
                                                    )
                                               .get();
    }

    @Override
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return new OpMapImmutableObjElems(this).map_(requireNonNull(fn),
                                                     requireNonNull(predicate),
                                                     EMPTY_PATH
                                                    )
                                               .get();
    }


    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn)
    {
        return new OpMapImmutableObjKeys(this).map(requireNonNull(fn),
                                                   it -> true,
                                                   EMPTY_PATH
                                                  )
                                              .get();
    }

    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return new OpMapImmutableObjKeys(this).map(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   EMPTY_PATH
                                                  )
                                              .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn)
    {
        return new OpMapImmutableObjKeys(this).map_(requireNonNull(fn),
                                                    it -> true,
                                                    EMPTY_PATH
                                                   )
                                              .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") // xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapImmutableObjKeys(this).map_(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    EMPTY_PATH
                                                   )
                                              .get();
    }


    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate
                              )
    {

        return OpMap.mapJsObj(requireNonNull(fn),
                              requireNonNull(predicate),
                              EMPTY_PATH
                             )
                    .apply(this)
                    .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return OpMap.mapJsObj(requireNonNull(fn),
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
        return OpMap.mapJsObj_(fn,
                               predicate,
                               EMPTY_PATH
                              )
                    .apply(this)
                    .get();

    }

    @Override
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return OpMap.mapJsObj_(fn,
                               (path, obj) -> true,
                               EMPTY_PATH
                              )
                    .apply(this)
                    .get();

    }


    @Override
    public final JsObj filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableObjElems(this).filter(JsPath.empty(),
                                                          requireNonNull(filter)
                                                         )

                                                  .get();
    }


    @Override
    public final JsObj filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableObjElems(this).filter_(JsPath.empty(),
                                                           requireNonNull(filter)
                                                          )

                                                  .get();

    }

    @Override
    public final JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableObjObjs(this).filter(JsPath.empty(),
                                                         requireNonNull(filter)
                                                        )

                                                 .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableObjObjs(this).filter_(JsPath.empty(),
                                                          requireNonNull(filter)
                                                         )

                                                 .get();

    }

    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableObjKeys(this).filter(filter)
                                                 .get();

    }

    @Override
    public JsObj filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableObjKeys(this).filter_(JsPath.empty(),
                                                          filter
                                                         )
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
            map = ((MyImmutableJsObj) JsObj.parse(json)
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
