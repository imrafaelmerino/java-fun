package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;


final class ImmutableJsArray extends AbstractJsArray<ImmutableSeq, ImmutableMap>
{

    private volatile int hascode;
    //squid:S3077: doesn't make any sense, volatile is perfectly valid here an as a matter of fact
    //is a recommendation from Effective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")
    @Nullable
    private volatile String str;

    ImmutableJsArray(final ImmutableSeq array)
    {
        super(array);
    }


    @Override
    public JsArray add(final int index,
                       final JsElem elem
                      )
    {
        return new ImmutableJsArray(seq.add(index,
                                            elem
                                           ));
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
    JsArray of(final ImmutableSeq vector)
    {
        return new ImmutableJsArray(vector);
    }

    @Override
    JsObj of(final ImmutableMap map)
    {
        return new ImmutableJsObj(map);
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
    public TryPatch<JsArray> patch(final JsArray arrayOps)
    {
        try
        {
            final List<OpPatch<JsArray>> ops = new Patch<JsArray>(arrayOps).ops;
            if (ops.isEmpty()) return new TryPatch<>(this);
            OpPatch<JsArray> head = ops.get(0);
            List<OpPatch<JsArray>> tail = ops.subList(1,
                                                      ops.size()
                                                     );
            TryPatch<JsArray> accPatch = head.apply(this);
            for (OpPatch<JsArray> op : tail) accPatch = accPatch.flatMap(op::apply);
            return accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
    }


    @Override
    /**
     Single-check idiom  Item 83 from Effective Java
     */
    public final String toString()
    {
        String result = str;
        if (result == null)
            str = result = super.toString();
        return result;

    }

    @Override
    public final JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapImmutableArrElems(this).map(requireNonNull(fn),
                                                    p -> true,
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                               .get();

    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                            final Predicate<? super JsPair> predicate
                           )
    {
        return new OpMapImmutableArrElems(this).map(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                               .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapImmutableArrElems(this).map_(requireNonNull(fn),
                                                     p -> true,
                                                     JsPath.empty()
                                                           .index(-1)
                                                    )
                                               .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return new OpMapImmutableArrElems(this).map_(requireNonNull(fn),
                                                     requireNonNull(predicate),
                                                     JsPath.empty()
                                                           .index(-1)
                                                    )
                                               .get();
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
        return new OpMapImmutableArrKeys(this).map_(requireNonNull(fn),
                                                    it -> true,
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return new OpMapImmutableArrKeys(this).map_(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate
                                )
    {

        return new OpMapImmutableArrObjs(this).map(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                              .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableArrObjs(this).map(requireNonNull(fn),
                                                   (p, o) -> true,
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                              .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {
        return new OpMapImmutableArrObjs(this).map_(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableArrObjs(this).map_(requireNonNull(fn),
                                                    (p, o) -> true,
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }


    @Override
    public final JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrElems(this).filter(JsPath.empty()
                                                                .index(-1),
                                                          requireNonNull(filter)
                                                         )

                                                  .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrElems(this).filter_(JsPath.empty()
                                                                 .index(-1),
                                                           requireNonNull(filter)
                                                          )

                                                  .get();
    }

    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableArrObjs(this).filter(JsPath.empty()
                                                               .index(-1),
                                                         requireNonNull(filter)
                                                        )

                                                 .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableArrObjs(this).filter_(JsPath.empty()
                                                                .index(-1),
                                                          requireNonNull(filter)
                                                         )
                                                 .get();
    }


    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrKeys(this).filter_(JsPath.empty()
                                                                .index(-1),
                                                          filter
                                                         )
                                                 .get();
    }


}
