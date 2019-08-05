package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.*;
import static jsonvalues.Trampoline.more;

class JsArrayImmutableImpl extends AbstractJsArray<MyScalaImpl.Vector, JsObj>
{
    public static final long serialVersionUID = 1L;

    @SuppressWarnings("squid:S3008")//EMPTY should be a valid name
    static JsArrayImmutableImpl EMPTY = new JsArrayImmutableImpl(MyScalaImpl.Vector.EMPTY);
    private volatile int hascode;
    private volatile @Nullable String str;

    JsArrayImmutableImpl(final MyScalaImpl.Vector array)
    {
        super(array);
    }

    @Override
    AbstractJsArray<MyScalaImpl.Vector, JsObj> emptyArray()
    {
        return EMPTY;
    }

    @Override
    JsObj emptyObject()
    {
        return JsObjImmutableImpl.EMPTY;
    }

    //equals method is inherited, so it's implemented. The purpose of this method is to cache
    //the hashcode once calculated. the object is immutable and it won't change
    @SuppressWarnings("squid:S1206")
    @Override
    public final int hashCode()
    {
        if (hascode != 0) return hascode;
        hascode = super.hashCode();
        return hascode;

    }

    @Override
    JsArray of(final MyScalaImpl.Vector vector)
    {
        return new JsArrayImmutableImpl(vector);
    }

    @Override
    public JsArray toImmutable()
    {
        return this;
    }

    public JsArray toMutable()
    {
        List<JsElem> acc = new ArrayList<>();
        array.forEach(accept(acc::add,
                             obj -> acc.add(obj.toMutable()),
                             arr -> acc.add(arr.toMutable())
                            ));
        return new JsArrayMutableImpl(new MyJavaImpl.Vector(acc));

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
    public final String toString()
    {
        if (str != null) return str;
        str = super.toString();
        return str;

    }

    @Override
    public final JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return mapValues(this,
                         requireNonNull(fn),
                         it -> true,
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
        return mapValues(this,
                         requireNonNull(fn),
                         predicate,
                         Functions.MINUS_ONE_INDEX
                        )
        .get();
    }

    private Trampoline<JsArray> mapValues(final JsArray array,
                                          final Function<? super JsPair, ? extends JsElem> fn,
                                          final Predicate<? super JsPair> predicate,
                                          final JsPath path
                                         )
    {


        return array.ifEmptyElse(Trampoline.done(array),
                                 (head, tail) ->
                                 {
                                     final JsPath headPath = path.inc();

                                     final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapValues(tail,
                                                                                                          fn,
                                                                                                          predicate,
                                                                                                          headPath
                                                                                                         ));
                                     return ifJsonElse(elem -> appendFront(elem,
                                                                           () -> tailCall
                                                                          ),
                                                       elem -> JsPair.of(headPath,
                                                                         elem
                                                                        )
                                                                     .ifElse(predicate,
                                                                             p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(p))),
                                                                             p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(p.elem))

                                                                            )
                                                      ).apply(head);
                                 }
                                );
    }


    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return MapFunctions.mapArrElems_(requireNonNull(fn),
                                         it -> true,
                                         MINUS_ONE_INDEX
                                        )
                           .apply(this)
                           .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return MapFunctions.mapArrElems_(requireNonNull(fn),
                                         requireNonNull(predicate),
                                         MINUS_ONE_INDEX
                                        )
                           .apply(this)
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
        return MapFunctions.mapArrKeys_(requireNonNull(fn),
                                        it -> true,
                                        MINUS_ONE_INDEX
                                       )
                           .apply(this)
                           .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return MapFunctions.mapArrKeys_(requireNonNull(fn),
                                        requireNonNull(predicate),
                                        MINUS_ONE_INDEX
                                       )
                           .apply(this)
                           .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate
                                )
    {

        return MapFunctions.mapArrJsObj(requireNonNull(fn),
                                        requireNonNull(predicate),
                                        MINUS_ONE_INDEX
                                       )
                           .apply(this)
                           .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return MapFunctions.mapArrJsObj(requireNonNull(fn),
                                        (p, o) -> true,
                                        MINUS_ONE_INDEX
                                       )
                           .apply(this)
                           .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {
        return MapFunctions.mapArrJsObj_(requireNonNull(fn),
                                         requireNonNull(predicate),
                                         MINUS_ONE_INDEX
                                        )
                           .apply(this)
                           .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return MapFunctions.mapArrJsObj_(requireNonNull(fn),
                                         (p, o) -> true,
                                         MINUS_ONE_INDEX
                                        )
                           .apply(this)
                           .get();
    }


    @Override
    public final JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return filterValues(this,
                            requireNonNull(filter),
                            JsPath.empty()
                                  .index(-1)
                           )
        .get();
    }

    private Trampoline<JsArray> filterValues(final JsArray arr,
                                             final Predicate<? super JsPair> predicate,
                                             final JsPath path
                                            )
    {


        return arr.ifEmptyElse(Trampoline.done(arr),
                               (head, tail) ->
                               {

                                   final JsPath headPath = path.inc();

                                   final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterValues(tail,
                                                                                                           predicate,
                                                                                                           headPath
                                                                                                          ));
                                   return ifJsonElse(elem -> appendFront(elem,
                                                                         () -> tailCall
                                                                        ),
                                                     elem -> JsPair.of(headPath,
                                                                       elem
                                                                      )
                                                                   .ifElse(predicate,
                                                                           () -> appendFront(elem,
                                                                                             () -> tailCall
                                                                                            ),
                                                                           () -> tailCall
                                                                          )
                                                    )
                                   .apply(head);
                               }
                              );


    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {


        return filterValues_(this,
                             requireNonNull(filter),
                             JsPath.empty()
                                   .index(-1)
                            ).get();

    }

    //squid:S00100_ naming convention: xx_ traverses the whole json
    @SuppressWarnings("squid:S00100")
    static Trampoline<JsArray> filterValues_(final JsArray arr,
                                             final Predicate<? super JsPair> predicate,
                                             final JsPath path
                                            )
    {


        return arr.ifEmptyElse(Trampoline.done(arr),
                               (head, tail) ->
                               {

                                   final JsPath headPath = path.inc();

                                   final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterValues_(tail,
                                                                                                            predicate,
                                                                                                            headPath
                                                                                                           ));
                                   return ifJsonElse(elem -> appendFront_(() -> elem.isObj() ?
                                                                          JsObjImmutableImpl.filterValues_(elem.asJsObj(),
                                                                                                           predicate,
                                                                                                           headPath
                                                                                                          ) :
                                                                          filterValues_(elem.asJsArray(),
                                                                                        predicate,
                                                                                        headPath.index(-1)
                                                                                       )
                                   ,
                                                                          () -> tailCall
                                                                         ),
                                                     elem -> JsPair.of(headPath,
                                                                       elem
                                                                      )
                                                                   .ifElse(predicate,
                                                                           () -> appendFront(elem,
                                                                                             () -> tailCall
                                                                                            ),
                                                                           () -> tailCall
                                                                          )
                                                    )
                                   .apply(head);
                               }
                              );


    }

    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return filterJsObjs(this,
                            requireNonNull(filter),
                            JsPath.empty()
                                  .index(-1)
                           )
        .get();
    }

    private Trampoline<JsArray> filterJsObjs(final JsArray arr,
                                             final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                             final JsPath path
                                            )
    {


        return arr.ifEmptyElse(Trampoline.done(arr),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.inc();

                                   final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterJsObjs(tail,
                                                                                                           predicate,
                                                                                                           headPath
                                                                                                          ));
                                   return ifObjElse(json -> JsPair.of(headPath,
                                                                      json
                                                                     )
                                                                  .ifElse(p -> predicate.test(p.path,
                                                                                              json
                                                                                             ),
                                                                          p -> appendFront(json,
                                                                                           () -> tailCall
                                                                                          ),
                                                                          p -> tailCall
                                                                         )
                                   ,
                                                    value -> appendFront(value,
                                                                         () -> tailCall
                                                                        )
                                                   )
                                   .apply(head);
                               }

                              );


    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return filterJsObjs_(this,
                             requireNonNull(filter),
                             JsPath.empty()
                                   .index(-1)
                            ).get();

    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Trampoline<JsArray> filterJsObjs_(final JsArray arr,
                                             final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                             final JsPath path
                                            )
    {


        return arr.ifEmptyElse(Trampoline.done(arr),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.inc();

                                   final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterJsObjs_(tail,
                                                                                                            predicate,
                                                                                                            headPath
                                                                                                           ));
                                   return ifJsonElse(json -> JsPair.of(headPath,
                                                                       json
                                                                      )
                                                                   .ifElse(p -> predicate.test(p.path,
                                                                                               json
                                                                                              ),
                                                                           p -> appendFront_(() -> JsObjImmutableImpl.filterJsObjs_(json,
                                                                                                                                    predicate,
                                                                                                                                    headPath
                                                                                                                                   ),
                                                                                             () -> tailCall

                                                                                            ),
                                                                           p -> tailCall

                                                                          ),
                                                     array -> appendFront_(() -> filterJsObjs_(array,
                                                                                               predicate,
                                                                                               headPath.index(-1)
                                                                                              ),
                                                                           () -> tailCall
                                                                          ),
                                                     value -> appendFront(value,
                                                                          () -> tailCall
                                                                         )
                                                    )
                                   .apply(head);
                               }

                              );


    }

    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return filterKeys_(this,
                           requireNonNull(filter),
                           JsPath.empty()
                                 .index(-1)
                          ).get();

    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Trampoline<JsArray> filterKeys_(final JsArray arr,
                                           final Predicate<? super JsPair> predicate,
                                           final JsPath path
                                          )
    {


        return arr.ifEmptyElse(Trampoline.done(arr),
                               (head, tail) ->
                               {

                                   final JsPath headPath = path.inc();

                                   final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterKeys_(tail,
                                                                                                          predicate,
                                                                                                          headPath
                                                                                                         ));


                                   return ifJsonElse(elem -> appendFront_(() -> elem.isArray() ? filterKeys_(elem.asJsArray(),
                                                                                                             predicate,
                                                                                                             headPath.index(-1)
                                                                                                            ) :
                                                                          JsObjImmutableImpl.filterKeys_(elem.asJsObj(),
                                                                                                         predicate,
                                                                                                         headPath
                                                                                                        )
                                   ,
                                                                          () -> tailCall
                                                                         ),
                                                     elem -> appendFront(elem,
                                                                         () -> tailCall

                                                                        )
                                                    )
                                   .apply(head);
                               }
                              );


    }

    /**
     * Serialize this {@code ScalaJsObj} instance.
     *
     * @serialData The {@code String}) representation of this json object.
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
            array = ((JsArrayImmutableImpl) JsArray.parse(json)
                                                   .orElseThrow()).array;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsArray.class.getName(),
                                                             malformedJson.getMessage()
                                                            ));
        }

    }


}
