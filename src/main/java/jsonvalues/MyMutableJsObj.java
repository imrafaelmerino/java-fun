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
import static jsonvalues.MatchExp.ifJsonElse;
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
        return mapElems(this,
                        this,
                        requireNonNull(fn),
                        p -> true,
                        JsPath.empty()
                       ).get();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return mapElems(this,
                        this,
                        requireNonNull(fn),
                        predicate,
                        JsPath.empty()
                       ).get();
    }

    private Trampoline<JsObj> mapElems(final JsObj acc,
                                       final JsObj remaining,
                                       final Function<? super JsPair, ? extends JsElem> fn,
                                       final Predicate<? super JsPair> predicate,
                                       final JsPath path
                                      )
    {

        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = path.key(head.getKey());

                                         final Trampoline<JsObj> tailCall = more(() -> mapElems(acc,
                                                                                                tail,
                                                                                                fn,
                                                                                                predicate,
                                                                                                path
                                                                                               ));

                                         return ifJsonElse(elem -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                         elem
                                                                                                        )),
                                                           elem -> JsPair.of(headPath,
                                                                             elem
                                                                            )
                                                                         .ifElse(predicate,
                                                                                 p -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                                            fn.apply(p)
                                                                                                                           )),
                                                                                 p -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                                            elem
                                                                                                                           ))
                                                                                )

                                                          ).apply(head.getValue());

                                     }
                                    );


    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return OpMap._mapElems__(requireNonNull(fn),
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
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return OpMap._mapElems__(requireNonNull(fn),
                                 requireNonNull(predicate),
                                 JsPath.empty()
                                )
                    .apply(this,
                           this
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
        JsPath path = JsPath.empty();
        final Iterator<Map.Entry<String, JsElem>> iterator = this.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );

            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();
        }
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsObj filterElems_(final Predicate<? super JsPair> filter)
    {
        return filterValues_(this,
                             requireNonNull(filter),
                             JsPath.empty()
                            );
    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsObj filterValues_(final JsObj obj,
                               final Predicate<? super JsPair> predicate,
                               final JsPath path
                              )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );

            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();
            else if (pair.elem.isObj())
                filterValues_(pair.elem.asJsObj(),
                              predicate,
                              pair.path
                             );
            else if (pair.elem.isArray())
                MyMutableJsArray.filterValues_(pair.elem.asJsArray(),
                                               predicate,
                                               pair.path.index(-1)
                                              );
        }

        return obj;

    }

    @Override
    public JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> predicate)
    {
        return new OpObjFilterObjs(this)._filter_(JsPath.empty(),
                                                  predicate
                                                 );

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpObjFilterObjs(this)._filter__(JsPath.empty(),
                                                   filter
                                                  );
    }


    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> predicate)
    {
        return new OpFilterMutableObjKeys(this).filter(requireNonNull(predicate))
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
