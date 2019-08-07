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
import static jsonvalues.Functions.ifJsonElse;
import static jsonvalues.Functions.ifObjElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class JsObjMutableImpl extends AbstractJsObj<MyJavaImpl.Map, JsArrayMutableImpl>
{
    public static final long serialVersionUID = 1L;

    JsObjMutableImpl(final MyJavaImpl.Map map)
    {
        super(map);
    }

    JsObjMutableImpl()
    {
        super(new MyJavaImpl.Map());
    }

    @Override
    JsArrayMutableImpl emptyArray()
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector());
    }

    @Override
    JsObj emptyObject()
    {
        return new JsObjMutableImpl(new MyJavaImpl.Map());
    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    JsObj of(final MyJavaImpl.Map map)
    {
        return new JsObjMutableImpl(map);
    }

    private static Trampoline<JsObj> removeOldKeyAndPutNew(final String oldKey,
                                                           final String newKey,
                                                           final JsElem elem,
                                                           final Trampoline<Trampoline<JsObj>> tail

                                                          )
    {
        return more(tail).map(it ->
                              {
                                  it.remove(oldKey);
                                  return it.put(newKey,
                                                elem
                                               );
                              });
    }

    @Override
    public JsObj toImmutable()
    {
        Map<String, JsElem> acc = new HashMap<>();
        @SuppressWarnings("squid:S1905")// in return checkerframework does its job!
        final Set<@KeyFor("map") String> keys = (Set<@KeyFor("map") String>) map.fields();
        keys.forEach(key -> accept(val -> acc.put(key,
                                                  val
                                                 ),
                                   obj -> acc.put(key,
                                                  obj.toImmutable()
                                                 ),
                                   arr -> acc.put(key,
                                                  arr.toImmutable()
                                                 )
                                  ).accept(map.get(key))
                    );
        return new JsObjImmutableImpl(MyScalaImpl.Map.EMPTY.updateAll(acc));

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
        return MapFunctions._mapElems__(requireNonNull(fn),
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
        return MapFunctions._mapElems__(requireNonNull(fn),
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
        return MapFunctions._mapKeys__(requireNonNull(fn),
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
        return MapFunctions._mapKeys__(requireNonNull(fn),
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
        return MapFunctions._mapJsObj__(requireNonNull(fn),
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
        return MapFunctions._mapJsObj__(requireNonNull(fn),
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
                JsArrayMutableImpl.filterValues_(pair.elem.asJsArray(),
                                                 predicate,
                                                 pair.path.index(-1)
                                                );
        }

        return obj;

    }

    @Override
    public JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> predicate)
    {
        JsPath path = JsPath.empty();
        final Iterator<Map.Entry<String, JsElem>> iterator = this.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsElem value = entry.getValue();
            if (value.isObj() && predicate.negate()
                                          .test(path.key(entry.getKey()),
                                                value.asJsObj()
                                               )
            ) iterator.remove();
        }
        return this;

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return FilterFunctions._filterObjObjs__(requireNonNull(filter),
                                                JsPath.empty()
                                               )
                              .apply(this);
    }


    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> predicate)
    {
        JsPath path = JsPath.empty();
        final Iterator<Map.Entry<String, JsElem>> iterator = this.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );
            if (predicate.negate()
                         .test(pair))
                iterator.remove();
        }
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj filterKeys_(final Predicate<? super JsPair> filter)
    {
        return FilterFunctions._filterObjKeys__(requireNonNull(filter),
                                                JsPath.empty()
                                               )
                              .apply(this);

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
            map = ((JsObjMutableImpl) JsObj._parse_(json)
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
