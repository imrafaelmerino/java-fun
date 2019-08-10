package jsonvalues;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

class JsArrayMutable extends AbstractJsArray<MyJavaImpl.Vector, JsObj>
{
    public static final long serialVersionUID = 1L;

    JsArrayMutable()
    {
        super(new MyJavaImpl.Vector());
    }


    JsArrayMutable(final MyJavaImpl.Vector array)
    {
        super(array);
    }

    @Override
    JsArray emptyArray()
    {
        return new JsArrayMutable(new MyJavaImpl.Vector());
    }

    @Override
    JsObj emptyObject()
    {
        return new JsObjMutable(new MyJavaImpl.Map());
    }

    @Override
    JsArray of(final MyJavaImpl.Vector vector)
    {
        return new JsArrayMutable(vector);
    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return MapFns._mapElems_(requireNonNull(fn),
                                 p -> true,
                                 JsPath.empty()
                                       .index(-1)
                                )
                     .apply(this,
                            this
                           )
                     .get();
    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                            final Predicate<? super JsPair> predicate
                           )
    {
        return MapFns._mapElems_(requireNonNull(fn),
                                 requireNonNull(predicate),
                                 JsPath.empty()
                                       .index(-1)
                                )
                     .apply(this,
                            this
                           )
                     .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return MapFns._mapArrElems__(requireNonNull(fn),
                                     it -> true,
                                     JsPath.empty()
                                           .index(-1)
                                    )
                     .apply(this,
                            this
                           )
                     .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return MapFns._mapArrElems__(requireNonNull(fn),
                                     predicate,
                                     JsPath.empty()
                                           .index(-1)
                                    )
                     .apply(this,
                            this
                           )
                     .get();
    }


    @Override
    public JsArray toImmutable()
    {
        List<JsElem> acc = new ArrayList<>();
        array.forEach(MatchFns.accept(acc::add,
                                      obj -> acc.add(obj.toImmutable()),
                                      arr -> acc.add(arr.toImmutable())
                                     ));
        return new JsArrayImmutable(MyScalaImpl.Vector.EMPTY.add(acc));

    }

    public JsArray toMutable()
    {
        return this;
    }


    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn)
    {
        return this;
    }

    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn)
    {
        return MapFns._mapArrKeys__(requireNonNull(fn),
                                    it -> true,
                                    JsPath.empty()
                                          .index(-1)
                                   )
                     .apply(this,
                            this
                           )
                     .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return MapFns._mapArrKeys__(requireNonNull(fn),
                                    requireNonNull(predicate),
                                    JsPath.empty()
                                          .index(-1)
                                   )
                     .apply(this,
                            this
                           )
                     .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate
                                )
    {

        return MapFns._mapJsObj_(requireNonNull(fn),
                                 requireNonNull(predicate),
                                 JsPath.empty()
                                       .index(-1)
                                )
                     .apply(this,
                            this
                           )
                     .get();

    }

    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return MapFns._mapJsObj_(requireNonNull(fn),
                                 (a, r) -> true,
                                 JsPath.empty()
                                       .index(-1)
                                )
                     .apply(this,
                            this
                           )
                     .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {

        return MapFns._mapArrJsObj__(requireNonNull(fn),
                                     requireNonNull(predicate),
                                     JsPath.empty()
                                           .index(-1)
                                    )
                     .apply(this,
                            this
                           )
                     .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return MapFns._mapArrJsObj__(requireNonNull(fn),
                                     (p, o) -> true,
                                     JsPath.empty()
                                           .index(-1)
                                    )
                     .apply(this,
                            this
                           )
                     .get();

    }


    @Override
    public JsArray filterElems(final Predicate<? super JsPair> predicate)
    {
        JsPath currentPath = JsPath.fromIndex(-1);
        final Iterator<JsElem> iterator = this.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();

        }
        return this;

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return filterValues_(this,
                             requireNonNull(filter),
                             JsPath.empty()
                                   .index(-1)
                            );
    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsArray filterValues_(final JsArray arr,
                                 final Predicate<? super JsPair> predicate,
                                 final JsPath path
                                )
    {
        JsPath currentPath = path;
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();
            else if (pair.elem.isObj())
                JsObjMutable.filterValues_(pair.elem.asJsObj(),
                                           predicate,
                                           currentPath
                                          );
            else if (pair.elem.isArray())
                filterValues_(pair.elem.asJsArray(),
                              predicate,
                              currentPath.index(-1)
                             );
        }
        return arr;
    }


    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> predicate)
    {
        JsPath currentPath = JsPath.fromIndex(-1);
        final Iterator<JsElem> iterator = this.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsElem next = iterator.next();
            if (next.isObj() && predicate.negate()
                                         .test(currentPath,
                                               next.asJsObj()
                                              )
            ) iterator.remove();

        }

        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return FilterFns._filterArrObjs__(requireNonNull(filter),
                                          JsPath.empty()
                                                .index(-1)
                                         )
                        .apply(this);
    }

    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new ArrFilterKey(this)._filter__(JsPath.empty()
                                                      .index(-1),
                                                requireNonNull(filter)
                                               );

    }


    /**
     * Serialize this {@code JsArray} instance.
     *
     * @serialData The {@code String}) representation of this json array.
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
            array = ((JsArrayMutable) JsArray._parse_(json)
                                             .orElseThrow()).array;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsArrayMutable.class.getName(),
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
