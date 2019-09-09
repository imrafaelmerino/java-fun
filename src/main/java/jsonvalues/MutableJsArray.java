package jsonvalues;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

final class MutableJsArray extends AbstractJsArray<MutableSeq, MutableMap>
{

    MutableJsArray(final MutableSeq array)
    {
        super(array);
    }

    @Override
    public JsArray add(final int index,
                       final JsElem elem
                      )
    {
        seq.add(index,
                elem
               );
        return of(seq);
    }


    @Override
    JsArray of(final MutableSeq vector)
    {
        return new MutableJsArray(vector);
    }

    @Override
    JsObj of(final MutableMap map)
    {
        return new MutableJsObj(map);
    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapMutableArrElems(this).map(requireNonNull(fn),
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
        return new OpMapMutableArrElems(this).map(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                             .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapMutableArrElems(this).map_(requireNonNull(fn),
                                                   p -> true,
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                             .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return new OpMapMutableArrElems(this).map_(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                             .get();
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
        return new OpMapMutableArrKeys(this).map_(requireNonNull(fn),
                                                  it -> true,
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                            .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return new OpMapMutableArrKeys(this).map_(requireNonNull(fn),
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

        return new OpMapMutableArrObjs(this).map(requireNonNull(fn),
                                                 requireNonNull(predicate),
                                                 JsPath.empty()
                                                       .index(-1)
                                                )
                                            .get();

    }

    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableArrObjs(this).map(requireNonNull(fn),
                                                 (p, o) -> true,
                                                 JsPath.empty()
                                                       .index(-1)
                                                )
                                            .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {
        return new OpMapMutableArrObjs(this).map_(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                            .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableArrObjs(this).map_(requireNonNull(fn),
                                                  (p, o) -> true,
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                            .get();

    }


    @Override
    public JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableArrElems(this).filter(JsPath.empty()
                                                              .index(-1),
                                                        filter
                                                       )
                                                .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableArrElems(this).filter_(JsPath.empty()
                                                               .index(-1),
                                                         filter
                                                        )
                                                .get();
    }


    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableArrObjs(this).filter(JsPath.empty()
                                                             .index(-1),
                                                       filter
                                                      )
                                               .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableArrObjs(this).filter_(JsPath.empty()
                                                              .index(-1),
                                                        filter
                                                       )
                                               .get();
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
        return new OpFilterMutableArrKeys(this).filter_(JsPath.empty()
                                                              .index(-1),
                                                        requireNonNull(filter)
                                                       )
                                               .get();

    }


    public MutableJsArray copy()
    {
        return new MutableJsArray(seq.copy());
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
            JsArray copy = this.copy();
            TryPatch<JsArray> accPatch = head.apply(copy);
            for (OpPatch<JsArray> op : tail) accPatch = accPatch.flatMap(op::apply);
            return accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
    }


}
