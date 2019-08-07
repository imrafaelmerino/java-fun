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
import static jsonvalues.Functions.*;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class JsArrayMutableImpl extends AbstractJsArray<MyJavaImpl.Vector, JsObj>
{
    public static final long serialVersionUID = 1L;

    JsArrayMutableImpl()
    {
        super(new MyJavaImpl.Vector());
    }


    JsArrayMutableImpl(final MyJavaImpl.Vector array)
    {
        super(array);
    }

    @Override
    JsArray emptyArray()
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector());
    }

    @Override
    JsObj emptyObject()
    {
        return new JsObjMutableImpl(new MyJavaImpl.Map());
    }

    @Override
    JsArray of(final MyJavaImpl.Vector vector)
    {
        return new JsArrayMutableImpl(vector);
    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return mapElems(this,
                        this,
                        fn,
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
        return mapElems(this,
                        this,
                        requireNonNull(fn),
                        predicate,
                        JsPath.empty()
                              .index(-1)
                       )
        .get();
    }

    private Trampoline<JsArray> mapElems(final JsArray acc,
                                         final JsArray remaining,
                                         final Function<? super JsPair, ? extends JsElem> fn,
                                         final Predicate<? super JsPair> predicate,
                                         final JsPath path
                                        )
    {


        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = path.inc();

                                         final Trampoline<JsArray> tailCall = more(() -> mapElems(acc,
                                                                                                  tail,
                                                                                                  fn,
                                                                                                  predicate,
                                                                                                  headPath
                                                                                                 ));

                                         return ifJsonElse(elem -> AbstractJsArray.put(new JsPath(headPath.last()),
                                                                                       elem,
                                                                                       () -> tailCall
                                                                                      ),
                                                           elem -> JsPair.of(headPath,
                                                                             elem
                                                                            )
                                                                         .ifElse(predicate,
                                                                                 p -> AbstractJsArray.put(new JsPath(headPath.last()),
                                                                                                          fn.apply(p),
                                                                                                          () -> tailCall
                                                                                                         ),
                                                                                 p -> AbstractJsArray.put(new JsPath(headPath.last()),
                                                                                                          elem,
                                                                                                          () -> tailCall
                                                                                                         )
                                                                                )
                                                          ).apply(head);


                                     }
                                    );
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return MapFunctions._mapArrElems__(requireNonNull(fn),
                                           it -> true,
                                           MINUS_ONE_INDEX
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
        return MapFunctions._mapArrElems__(requireNonNull(fn),
                                           predicate,
                                           MINUS_ONE_INDEX
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
        array.forEach(accept(acc::add,
                             obj -> acc.add(obj.toImmutable()),
                             arr -> acc.add(arr.toImmutable())
                            ));
        return new JsArrayImmutableImpl(MyScalaImpl.Vector.EMPTY.add(acc));

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
        return MapFunctions._mapArrKeys__(requireNonNull(fn),
                                          it -> true,
                                          MINUS_ONE_INDEX
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
        return MapFunctions._mapArrKeys__(requireNonNull(fn),
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

        return mapJsObj(this,
                        this,
                        requireNonNull(fn),
                        requireNonNull(predicate),
                        MINUS_ONE_INDEX
                       )
        .get();

    }

    @SuppressWarnings("squid:S00100")
    //  naming convention: _xx_ returns immutable object
    private Trampoline<JsArray> mapJsObj(final JsArray acc,
                                         final JsArray remaining,
                                         final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                         final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                         final JsPath path
                                        )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = path.inc();

                                         final Trampoline<JsArray> tailCall = more(() -> mapJsObj(acc,
                                                                                                  tail,
                                                                                                  fn,
                                                                                                  predicate,
                                                                                                  headPath
                                                                                                 ));
                                         return ifObjElse(obj -> JsPair.of(headPath,
                                                                           obj
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   obj
                                                                                                  ),
                                                                               p -> AbstractJsArray.put(new JsPath(headPath.last()),
                                                                                                        fn.apply(p.path,
                                                                                                                 obj
                                                                                                                ),
                                                                                                        () -> tailCall
                                                                                                       ),
                                                                               p -> AbstractJsArray.put(new JsPath(headPath.last()),
                                                                                                        p.elem,
                                                                                                        () -> tailCall
                                                                                                       )
                                                                              ),
                                                          value -> AbstractJsArray.put(new JsPath(headPath.last()),
                                                                                       value,
                                                                                       () -> tailCall
                                                                                      )
                                                         ).apply(head);
                                     }

                                    );
    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return mapJsObj(this,
                        this,
                        requireNonNull(fn),
                        (p, o) -> true,
                        MINUS_ONE_INDEX
                       )
        .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {

        return MapFunctions._mapArrJsObj__(requireNonNull(fn),
                                           requireNonNull(predicate),
                                           MINUS_ONE_INDEX
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
        return MapFunctions._mapArrJsObj__(requireNonNull(fn),
                                           (p, o) -> true,
                                           MINUS_ONE_INDEX
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
                             MINUS_ONE_INDEX
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
                JsObjMutableImpl.filterValues_(pair.elem.asJsObj(),
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
        return filterObjs_(this,
                           requireNonNull(filter),
                           MINUS_ONE_INDEX
                          );


    }

    static JsArray filterObjs_(final JsArray arr,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate,
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
            if (pair.elem.isJson())
            {
                if (pair.elem.isObj() && predicate.negate()
                                                  .test(pair.path,
                                                        pair.elem.asJsObj()
                                                       )
                ) iterator.remove();
                else if (pair.elem.isObj()) JsObjMutableImpl.filterObjs_(pair.elem.asJsObj(),
                                                                         predicate,
                                                                         pair.path
                                                                        );
                else if (pair.elem.isArray()) filterObjs_(pair.elem.asJsArray(),
                                                          predicate,
                                                          pair.path.index(-1)
                                                         );

            }

        }
        return arr;
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
        return filterKeys_(this,
                           requireNonNull(filter),
                           MINUS_ONE_INDEX
                          );

    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsArray filterKeys_(final JsArray arr,
                               final Predicate<? super JsPair> predicate,
                               final JsPath path
                              )
    {
        JsPath currentPath = path;
        for (final JsElem elem : arr)
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          elem
                                         );
            if (pair.elem.isArray())
                filterKeys_(pair.elem.asJsArray(),
                            predicate,
                            currentPath.index(-1)
                           );
            else if (pair.elem.isObj())
                JsObjMutableImpl.filterKeys_(pair.elem.asJsObj(),
                                             predicate,
                                             currentPath
                                            );

        }
        return arr;

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
            array = ((JsArrayMutableImpl) JsArray._parse_(json)
                                                 .orElseThrow()).array;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsArrayMutableImpl.class.getName(),
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
