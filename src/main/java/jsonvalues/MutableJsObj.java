package jsonvalues;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

final class MutableJsObj extends AbstractJsObj<MutableMap, MutableSeq>
{
    public static final long serialVersionUID = 1L;

    MutableJsObj(final MutableMap map)
    {
        super(map);
    }


    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    JsObj of(final MutableMap map)
    {
        return new MutableJsObj(map);
    }

    @Override
    JsArray of(final MutableSeq vector)
    {
        return new MutableJsArray(vector);
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


    public JsObj copy()
    {
        return new MutableJsObj(map.copy());
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
            JsObj copy = this.copy();
            TryPatch<JsObj> accPatch = head.apply(copy);
            for (OpPatch<JsObj> op : tail) accPatch = accPatch.flatMap(op::apply);
            return accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
    }

}
