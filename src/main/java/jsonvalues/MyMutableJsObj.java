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
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class MyMutableJsObj extends MyAbstractJsObj<MyJavaMap, MyMutableJsArray>
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
        return mapKeys(this,
                       this,
                       requireNonNull(fn),
                       p -> true,
                       JsPath.empty()
                      )
        .get();
    }

    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return mapKeys(this,
                       this,
                       requireNonNull(fn),
                       requireNonNull(predicate),
                       JsPath.empty()
                      )
        .get();
    }

    private Trampoline<JsObj> mapKeys(final JsObj acc,
                                      final JsObj remaining,
                                      final Function<? super JsPair, String> fn,
                                      final Predicate<? super JsPair> predicate,
                                      final JsPath path
                                     )
    {

        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = path.key(head.getKey());
                                         final Trampoline<JsObj> tailCall = more(() -> mapKeys(acc,
                                                                                               remaining.tail(head.getKey()),
                                                                                               fn,
                                                                                               predicate,
                                                                                               path
                                                                                              ));
                                         return JsPair.of(headPath,
                                                          head.getValue()
                                                         )
                                                      .ifElse(predicate,
                                                              p -> more(() -> tailCall).map(it ->
                                                                                            {
                                                                                                it.remove(head.getKey());
                                                                                                return it.put(fn.apply(p),
                                                                                                              p.elem
                                                                                                             );
                                                                                            }),
                                                              p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                         p.elem
                                                                                                                        ))
                                                             );
                                     }
                                    );


    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn)
    {
        return OpMap._mapKeys__(requireNonNull(fn),
                                p -> true,
                                JsPath.empty()
                               )
                    .apply(this,
                           this
                          )
                    .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return OpMap._mapKeys__(requireNonNull(fn),
                                requireNonNull(predicate),
                                JsPath.empty()
                               )
                    .apply(this,
                           this
                          )
                    .get();
    }


    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate
                              )
    {

        return mapJsObj(this,
                        this,
                        requireNonNull(fn),
                        requireNonNull(predicate),
                        JsPath.empty()
                       )
        .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return mapJsObj(this,
                        this,
                        requireNonNull(fn),
                        (p, o) -> true,
                        JsPath.empty()
                       )
        .get();
    }

    private Trampoline<JsObj> mapJsObj(final JsObj acc,
                                       final JsObj remaining,
                                       final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                       final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                       final JsPath path
                                      )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = path.key(head.getKey());

                                         final Trampoline<JsObj> tailCall = more(() -> mapJsObj(acc,
                                                                                                tail,
                                                                                                fn,
                                                                                                predicate,
                                                                                                path
                                                                                               ));
                                         return ifObjElse(obj -> JsPair.of(headPath,
                                                                           obj
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   obj
                                                                                                  ),
                                                                               p ->
                                                                               {
                                                                                   obj.remove(head.getKey());
                                                                                   return more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                                fn.apply(p.path,
                                                                                                                                                         obj
                                                                                                                                                        )
                                                                                                                                               ));
                                                                               },
                                                                               p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                          p.elem
                                                                                                                                         ))
                                                                              ),
                                                          value -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                         value
                                                                                                        ))
                                                         )
                                         .apply(head.getValue());
                                     }

                                    );
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
    {
        return OpMap._mapJsObj__(requireNonNull(fn),
                                 requireNonNull(predicate),
                                 JsPath.empty()
                                )
                    .apply(this,
                           this
                          )
                    .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return OpMap._mapJsObj__(requireNonNull(fn),
                                 (p, o) -> true,
                                 JsPath.empty()
                                )
                    .apply(this,
                           this
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
