package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;

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

final class MyMutableJsObj extends MyAbstractJsObj<MyJavaMap, MyMutableJsArray>
{
    public static final long serialVersionUID = 1L;

    MyMutableJsObj(final MyJavaMap map)
    {
        super(map);
    }

    MyMutableJsObj()
    {
        super(new MyJavaMap());
    }

    @Override
    MyMutableJsArray emptyArray()
    {
        return new MyMutableJsArray(new MyJavaVector());
    }

    @Override
    JsObj emptyObject()
    {
        return new MyMutableJsObj(new MyJavaMap());
    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    JsObj of(final MyJavaMap map)
    {
        return new MyMutableJsObj(map);
    }

    @Override
    public JsObj toImmutable()
    {
        Map<String, JsElem> acc = new HashMap<>();
        @SuppressWarnings("squid:S1905")// in return checkerframework does its job!
        final Set<@KeyFor("map") String> keys = (Set<@KeyFor("map") String>) map.fields();
        keys.forEach(key -> MatchExp.accept(val -> acc.put(key,
                                                           val
                                                          ),
                                            obj -> acc.put(key,
                                                           obj.toImmutable()
                                                          ),
                                            arr -> acc.put(key,
                                                           arr.toImmutable()
                                                          )
                                           )
                                    .accept(map.get(key))
                    );
        return new MyImmutableJsObj(MyScalaMap.EMPTY.updateAll(acc));

    }

    @Override
    public JsObj toMutable()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapMutableObjElems(this).map(requireNonNull(fn),
                                                  p -> true,
                                                  JsPath.empty()
                                                 )
                                             .get();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapMutableObjElems(this).map(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                 )
                                             .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapMutableObjElems(this).map_(requireNonNull(fn),
                                                   p -> true,
                                                   JsPath.empty()
                                                  )
                                             .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return new OpMapMutableObjElems(this).map_(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   JsPath.empty()
                                                  )
                                             .get();
    }


    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn)
    {
        return new OpMapMutableObjKeys(this).map(requireNonNull(fn),
                                                 it -> true,
                                                 JsPath.empty()
                                                )
                                            .get();
    }

    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return new OpMapMutableObjKeys(this).map(requireNonNull(fn),
                                                 requireNonNull(predicate),
                                                 JsPath.empty()
                                                )
                                            .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn)
    {
        return new OpMapMutableObjKeys(this).map_(requireNonNull(fn),
                                                  it -> true,
                                                  JsPath.empty()
                                                 )
                                            .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapMutableObjKeys(this).map_(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                 )
                                            .get();
    }


    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate
                              )
    {

        return new OpMapMutableObjObjs(this).map(requireNonNull(fn),
                                                 requireNonNull(predicate),
                                                 JsPath.empty()
                                                )
                                            .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableObjObjs(this).map(requireNonNull(fn),
                                                 (p, o) -> true,
                                                 JsPath.empty()
                                                )
                                            .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
    {
        return new OpMapMutableObjObjs(this).map_(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                 )
                                            .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableObjObjs(this).map_(requireNonNull(fn),
                                                  (p, o) -> true,
                                                  JsPath.empty()
                                                 )
                                            .get();
    }


    @Override
    public JsObj filterElems(final Predicate<? super JsPair> predicate)
    {
        return new OpFilterMutableObjElems(this).filter(JsPath.empty(),
                                                        predicate
                                                       )
                                                .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsObj filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableObjElems(this).filter_(JsPath.empty(),
                                                         filter
                                                        )
                                                .get();
    }


    @Override
    public JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableObjObjs(this).filter(JsPath.empty(),
                                                       filter
                                                      )
                                               .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableObjObjs(this).filter_(JsPath.empty(),
                                                        filter
                                                       )
                                               .get();
    }

    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableObjKeys(this).filter(requireNonNull(filter))
                                               .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableObjKeys(this).filter_(JsPath.empty(),
                                                        requireNonNull(filter)
                                                       )
                                               .get();

    }


    private void writeObject(ObjectOutputStream s) throws IOException
    {
        s.defaultWriteObject();
        s.writeObject(toString());

    }
    //squid:S4508: implemented after reviewing chapter 12 from Effectiva Java!
    @SuppressWarnings("squid:S4508")
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
        final String json = (String) s.readObject();
        try
        {
            map = ((MyMutableJsObj) JsObj._parse_(json)
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
        return true;
    }

    @Override
    public boolean isImmutable()
    {
        return false;
    }

}
