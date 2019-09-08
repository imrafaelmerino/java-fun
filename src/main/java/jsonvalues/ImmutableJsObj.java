package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

final class ImmutableJsObj extends AbstractJsObj<ImmutableMap, ImmutableSeq>
{

    @SuppressWarnings("squid:S3008")//EMPTY should be a valid name
    private static final JsPath EMPTY_PATH = JsPath.empty();
    private volatile int hascode;
    //squid:S3077: doesn't make any sese, volatile is perfectly valid here an as a matter of fact
    //is a recomendation from Efective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")
    @Nullable
    private volatile String str;


    ImmutableJsObj(final ImmutableMap myMap)
    {
        super(myMap);
    }


    /**
     equals method is inherited, so it's implemented. The purpose of this method is to cache
     the hashcode once calculated. the object is immutable and it won't change
     Single-check idiom  Item 83 from Effective Java
     */
    @SuppressWarnings("squid:S1206")
    @Override
    public final int hashCode()
    {
        int result = hascode;
        if (result == 0)
            hascode = result = super.hashCode();
        return result;
    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    JsObj of(final ImmutableMap map)
    {
        return new ImmutableJsObj(map);
    }

    @Override
    JsArray of(final ImmutableSeq vector)
    {
        return new ImmutableJsArray(vector);
    }


    /**
     // Single-check idiom  Item 83 from effective java
     */
    @Override
    public final String toString()
    {
        String result = str;
        if (result == null)
            str = result = super.toString();
        return result;
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
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
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

        return new OpMapImmutableObjObjs(this).map(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   JsPath.empty()
                                                  )
                                              .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableObjObjs(this).map(requireNonNull(fn),
                                                   (p, o) -> true,
                                                   JsPath.empty()
                                                  )
                                              .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
    {
        return new OpMapImmutableObjObjs(this).map_(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                   )
                                              .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableObjObjs(this).map_(requireNonNull(fn),
                                                    (p, o) -> true,
                                                    JsPath.empty()
                                                   )
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

    @Override
    public TryPatch<JsObj> patch(final JsArray arrayOps)
    {
        try
        {
            final List<OpPatch<JsObj>> ops = new Patch<JsObj>(arrayOps).ops;
            if (ops.isEmpty()) return new TryPatch<>(this);
            OpPatch<JsObj> head = ops.get(0);
            List<OpPatch<JsObj>> tail = ops.subList(1,
                                                    ops.size()
                                                   );
            TryPatch<JsObj> accPatch = head.apply(this);
            for (OpPatch<JsObj> op : tail) accPatch = accPatch.flatMap(op::apply);
            return accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
    }

}
