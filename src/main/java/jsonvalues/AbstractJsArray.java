package jsonvalues;


import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;
import static jsonvalues.AbstractJsObj.streamOfObj;
import static jsonvalues.Functions.ifJsonElse;
import static jsonvalues.Functions.isSameType;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;


abstract class AbstractJsArray<T extends MyVector<T>, O extends JsObj> extends AbstractJson implements JsArray

{
    public static final long serialVersionUID = 1L;

    @SuppressWarnings("squid:S00116") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    private final transient JsPath MINUS_ONE_PATH = JsPath.empty()
                                                          .index(-1);

    protected transient T array;

    AbstractJsArray(T array)
    {
        this.array = array;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public JsArray appendAll(final JsPath path,
                             final JsArray elems

                            )
    {

        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.appendAll(elems)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().appendAll(elems)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().appendAll(tail,
                                                                                                                                                              elems
                                                                                                                                                             ),
                                                                                                                                 a -> emptyArray().appendAll(tail,
                                                                                                                                                             elems
                                                                                                                                                            )
                                                                                                                                )
                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
                                                                                                                            .asJson()
                                                                                                                            .appendAll(tail,
                                                                                                                                       elems
                                                                                                                                      )
                                                                                                                      ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    @Override
    public JsArray append(final JsPath path,
                          final JsElem elem
                         )
    {

        Objects.requireNonNull(elem);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.append(elem)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().append(elem)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().append(tail,
                                                                                                                                                           elem
                                                                                                                                                          ),
                                                                                                                                 a -> emptyArray().append(tail,
                                                                                                                                                          elem
                                                                                                                                                         )
                                                                                                                                )
                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
                                                                                                                            .asJson()
                                                                                                                            .append(tail,
                                                                                                                                    elem
                                                                                                                                   )
                                                                                                                      ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    @Override
    public JsArray appendAll(final JsArray array
                            )
    {
        return appendAllBackTrampoline(this,
                                       requireNonNull(array)
                                      ).get();

    }

    static Trampoline<JsArray> appendFront(final JsElem head,
                                           final Trampoline<Trampoline<JsArray>> tail
                                          )
    {
        return more(tail).map(it -> it.prepend(head));
    }

    @Override
    public JsArray prependAll(final JsArray array
                             )
    {
        return appendAllFrontTrampoline(this,
                                        requireNonNull(array)
                                       ).get();

    }

    @SuppressWarnings("Duplicates")
    @Override
    public JsArray prependAll(final JsPath path,
                              final JsArray elems
                             )
    {
        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.prependAll(elems)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().prependAll(elems)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().prependAll(tail,
                                                                                                                                                               elems
                                                                                                                                                              ),
                                                                                                                                 a -> emptyArray().prependAll(tail,
                                                                                                                                                              elems
                                                                                                                                                             )
                                                                                                                                )

                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
                                                                                                                            .asJson()
                                                                                                                            .prependAll(tail,
                                                                                                                                        elems
                                                                                                                                       )
                                                                                                                      ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    @SuppressWarnings("Duplicates")
    @Override
    public JsArray prepend(final JsPath path,
                           final JsElem elem
                          )
    {
        Objects.requireNonNull(elem);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.prepend(elem)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().prepend(elem)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().prepend(tail,
                                                                                                                                                            elem
                                                                                                                                                           ),
                                                                                                                                 a -> emptyArray().prepend(tail,
                                                                                                                                                           elem
                                                                                                                                                          )
                                                                                                                                )

                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
                                                                                                                            .asJson()
                                                                                                                            .prepend(tail,
                                                                                                                                     elem
                                                                                                                                    )
                                                                                                                      ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    @Override
    public JsArray append(final JsElem e,
                          final JsElem... others
                         )
    {
        T vector = array.appendBack(requireNonNull(e));
        for (JsElem other : requireNonNull(others)) vector = vector.appendBack(requireNonNull(other));
        return of(vector);
    }

    @Override
    public JsArray prepend(final JsElem e,
                           final JsElem... others
                          )
    {
        T vector = array;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++)
        {
            final JsElem other = others[othersLength - 1 - i];
            vector = vector.appendFront(requireNonNull(other));
        }
        return of(vector.appendFront(requireNonNull(e)));
    }

    @Override
    public boolean containsElem(final JsElem el)
    {
        return array.contains(requireNonNull(el));
    }

    abstract JsArray emptyArray();

    abstract O emptyObject();

    @Override
    public final boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (!(that instanceof AbstractJsArray)) return false;
        final AbstractJsArray<?, ?> thatArray = (AbstractJsArray) that;
        return this.array.equals(thatArray.array);

    }


    @Override
    public final JsElem get(final Position pos)
    {


        return requireNonNull(pos).match(key -> JsNothing.NOTHING,
                                         index ->
                                         {
                                             if (index == -1 && !array.isEmpty()) return array.last();
                                             return (array.isEmpty() || index < 0 || index > array.size() - 1) ?
                                             JsNothing.NOTHING : array.get(index);
                                         }
                                        );


    }

    @Override
    public int hashCode()
    {
        return array.hashCode();
    }

    @Override
    public final JsElem head()
    {

        return array.head();

    }

    @Override
    public final JsArray init()
    {
        return of(array.init());

    }

    @Override
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    public final JsArray intersection(final JsArray that,
                                      final TYPE ARRAY_AS
                                     )
    {
        return intersection(this,
                            requireNonNull(that),
                            requireNonNull(ARRAY_AS)
                           ).get();


    }

    @SuppressWarnings("squid:S00117") // ARRAY_AS should be a valid name for an enum constant
    static Trampoline<JsArray> intersection(JsArray a,
                                            JsArray b,
                                            JsArray.TYPE ARRAY_AS
                                           )
    {


        switch (ARRAY_AS)
        {
            case SET:
                return intersectionAsSet(a,
                                         b
                                        );
            case LIST:
                return intersectionAsList(a,
                                          b
                                         );
            case MULTISET:
                return intersectionAsMultiSet(a,
                                              b
                                             );
        }

        throw new IllegalArgumentException(ARRAY_AS.name() + " option not supported");
    }

    static Trampoline<JsArray> put(final JsPath path,
                                   final JsElem head,
                                   final Trampoline<Trampoline<JsArray>> tail
                                  )
    {
        return more(tail).map(it -> it.put(path,
                                           head
                                          ));
    }

    private static Trampoline<JsArray> intersectionAsList(JsArray a,
                                                          JsArray b
                                                         )
    {

        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsArray tail = a.tail();

        final JsElem otherHead = b.head();
        final JsArray otherTail = b.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsList(tail,
                                                                                  otherTail
                                                                                 );

        if (head.equals(otherHead)) return appendFront(head,
                                                       tailCall
                                                      );
        return more(tailCall);


    }


    private static Trampoline<JsArray> intersectionAsMultiSet(JsArray a,
                                                              JsArray b
                                                             )
    {

        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsArray tail = a.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsMultiSet(tail,
                                                                                      b
                                                                                     );

        if (b.containsElem(head)) return appendFront(head,
                                                     tailCall
                                                    );
        return more(tailCall);
    }

    private static Trampoline<JsArray> intersectionAsSet(JsArray a,
                                                         JsArray b
                                                        )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsArray tail = a.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsSet(tail,
                                                                                 b
                                                                                );

        if (b.containsElem(head) && !tail.containsElem(head)) return appendFront(head,
                                                                                 tailCall
                                                                                );
        return more(tailCall);


    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public JsArray intersection_(final JsArray that)
    {
        return intersection_(this,
                             requireNonNull(that)
                            ).get();
    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsArray> intersection_(final JsArray a,
                                             final JsArray b
                                            )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = intersectionAsList(a.tail(),
                                                                b.tail()
                                                               );

        if (head.isJson() && isSameType(otherHead).test(head))
        {
            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> intersection_(obj,
                                                                              obj1,
                                                                              JsArray.TYPE.LIST
                                                                             ));
            return appendFront_(() -> headCall,
                                () -> tailCall
                               );
        } else if (head.equals(otherHead)) return appendFront(head,
                                                              () -> tailCall
                                                             );
        else return more(() -> tailCall);


    }


    @Override
    public final boolean isEmpty()
    {
        return array.isEmpty();
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return array.iterator();
    }

    @Override
    public final JsElem last()
    {

        return array.last();
    }


    abstract JsArray of(T vector);

    @Override
    public final JsArray put(final JsPath path,
                             final Function<? super JsElem, ? extends JsElem> fn
                            )
    {

        requireNonNull(fn);

        return requireNonNull(path).head()
                                   .match(head -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();

                                              return tail.ifEmptyElse(() -> Functions.ifNothingElse(() -> this,
                                                                                                    elem -> of(nullPadding(index,
                                                                                                                           array,
                                                                                                                           elem
                                                                                                                          ))
                                                                                                   )
                                                                                     .apply(fn.apply(get(path))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () ->
                                                                                                 {
                                                                                                     final JsElem newElem = tail.head()
                                                                                                                                .match(key -> emptyObject().put(tail,
                                                                                                                                                                fn
                                                                                                                                                               ),
                                                                                                                                       i -> emptyArray().put(tail,
                                                                                                                                                             fn
                                                                                                                                                            )
                                                                                                                                      );
                                                                                                     return of(nullPadding(index,
                                                                                                                           array,
                                                                                                                           newElem
                                                                                                                          ));
                                                                                                 },
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
                                                                                                                            .asJson()
                                                                                                                            .put(tail,
                                                                                                                                 fn
                                                                                                                                )
                                                                                                                      ))

                                                                                                )
                                                                     );

                                          }

                                         );

    }

    @Override
    public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                        final Function<? super JsPair, R> map,
                                        final Predicate<? super JsPair> predicate
                                       )
    {
        return reduce(this,
                      requireNonNull(op),
                      requireNonNull(map),
                      requireNonNull(predicate),
                      JsPath.empty()
                            .index(-1),
                      Optional.empty()
                     )
        .get();

    }

    private <T> Trampoline<Optional<T>> reduce(final JsArray arr,
                                               final BinaryOperator<T> op,
                                               final Function<? super JsPair, T> fn,
                                               final Predicate<? super JsPair> predicate,
                                               final JsPath path,
                                               final Optional<T> result
                                              )
    {

        return arr.ifEmptyElse(done(result),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.inc();
                                   return ifJsonElse(json -> more(() -> reduce(tail,
                                                                               op,
                                                                               fn,
                                                                               predicate,
                                                                               headPath,
                                                                               result
                                                                              )),
                                                     elem -> JsPair.of(headPath,
                                                                       elem
                                                                      )
                                                                   .ifElse(predicate,
                                                                           p -> more(() -> reduce(tail,
                                                                                                  op,
                                                                                                  fn,
                                                                                                  predicate,
                                                                                                  headPath,
                                                                                                  reduceElem(p,
                                                                                                             op,
                                                                                                             fn,
                                                                                                             result
                                                                                                            )
                                                                                                 )),
                                                                           p -> more(() -> reduce(tail,
                                                                                                  op,
                                                                                                  fn,
                                                                                                  predicate,
                                                                                                  headPath,
                                                                                                  result
                                                                                                 ))
                                                                          )


                                                    )
                                   .apply(head);
                               }
                              );

    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final <R> Optional<R> reduce_(final BinaryOperator<R> op,
                                         final Function<? super JsPair, R> map,
                                         final Predicate<? super JsPair> predicate
                                        )
    {
        return reduce_(this,
                       requireNonNull(op),
                       requireNonNull(map),
                       requireNonNull(predicate),
                       MINUS_ONE_PATH,
                       Optional.empty()
                      ).get();
    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static <T> Trampoline<Optional<T>> reduce_(final JsArray arr,
                                               final BinaryOperator<T> op,
                                               final Function<? super JsPair, T> fn,
                                               final Predicate<? super JsPair> predicate,
                                               final JsPath path,
                                               final Optional<T> result
                                              )
    {


        return arr.ifEmptyElse(done(result),
                               (head, tail) ->
                               {
                                   final JsPath headPath = path.inc();

                                   return ifJsonElse(json -> more(() -> reduce_(json,
                                                                                op,
                                                                                fn,
                                                                                predicate,
                                                                                headPath,
                                                                                result
                                                                               )
                                                                 ).flatMap(r -> reduce_(tail,
                                                                                        op,
                                                                                        fn,
                                                                                        predicate,
                                                                                        path,
                                                                                        r
                                                                                       )),
                                                     elem -> JsPair.of(headPath,
                                                                       elem
                                                                      )
                                                                   .ifElse(predicate,
                                                                           p -> more(() -> reduce_(tail,
                                                                                                   op,
                                                                                                   fn,
                                                                                                   predicate,
                                                                                                   headPath,
                                                                                                   reduceElem(p,
                                                                                                              op,
                                                                                                              fn,
                                                                                                              result
                                                                                                             )
                                                                                                  )),
                                                                           p -> more(() -> reduce_(tail,
                                                                                                   op,
                                                                                                   fn,
                                                                                                   predicate,
                                                                                                   headPath,
                                                                                                   result
                                                                                                  ))
                                                                          )

                                                    )
                                   .apply(head);
                               }
                              );


    }

    @Override
    public final JsArray remove(final JsPath path)
    {


        return requireNonNull(path).head()
                                   .match(head -> this,
                                          index ->
                                          {
                                              final int maxIndex = array.size() - 1;
                                              if (index < -1 || index > maxIndex) return this;
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> of(index == -1 ? array.remove(maxIndex) : array.remove(index)),
                                                                      () -> ifJsonElse(json -> of(array.update(index,
                                                                                                               json.remove(tail)
                                                                                                              )),
                                                                                       e -> this
                                                                                      )
                                                                      .apply(array.get(index))
                                                                     );
                                          }

                                         );


    }

    @Override
    public final int size()
    {
        return array.size();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public Stream<JsPair> stream_()
    {
        return streamOfArr(this,
                           JsPath.empty()
                          );
    }

    @Override
    public Stream<JsPair> stream()
    {
        return IntStream.range(0,
                               size()
                              )
                        .mapToObj(Integer::toString)
                        .flatMap(i -> Stream.of(JsPair.of(JsPath.of(i),
                                                          get(i)
                                                         )));

    }

    static Stream<JsPair> streamOfArr(final JsArray array,
                                      final JsPath path
                                     )
    {


        requireNonNull(path);
        return requireNonNull(array).ifEmptyElse(() -> Stream.of(JsPair.of(path,
                                                                           array
                                                                          )),
                                                 () -> range(0,
                                                             array.size()
                                                            ).mapToObj(pair -> JsPair.of(path.index(pair),
                                                                                         array.get(Index.of(pair))

                                                                                        ))
                                                             .flatMap(pair -> Functions.ifValueElse(e -> Stream.of(pair),
                                                                                                    o -> streamOfObj(o,
                                                                                                                     pair.path
                                                                                                                    ),
                                                                                                    a -> streamOfArr(a,
                                                                                                                     pair.path
                                                                                                                    )
                                                                                                   )
                                                                                       .apply(pair.elem)
                                                                     )
                                                );


    }

    @Override
    public final JsArray tail()
    {

        return of(array.tail());

    }

    @Override
    public String toString()
    {

        return array.toString();


    }

    @Override
    @SuppressWarnings("squid:S00117") //  ARRAY_AS  should be a valid name
    public final JsArray union(final JsArray that,
                               final TYPE ARRAY_AS
                              )
    {

        return union(this,
                     requireNonNull(that),
                     requireNonNull(ARRAY_AS)
                    ).get();
    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS  should be a valid name
    static Trampoline<JsArray> union(JsArray a,
                                     JsArray b,
                                     JsArray.TYPE ARRAY_AS
                                    )
    {


        switch (ARRAY_AS)
        {
            case SET:
                return unionAsSet(a,
                                  b
                                 );
            case LIST:
                return unionAsList(a,
                                   b
                                  );
            case MULTISET:
                return unionAsMultiSet(a,
                                       b
                                      );
        }

        throw new IllegalArgumentException(ARRAY_AS.name() + " option not supported");

    }

    private static Trampoline<JsArray> unionAsList(final JsArray a,
                                                   final JsArray b
                                                  )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        final Trampoline<JsArray> tailCall = unionAsList(a.tail(),
                                                         b.tail()
                                                        );
        return appendFront(a.head(),
                           () -> tailCall
                          );


    }

    private static Trampoline<JsArray> unionAsMultiSet(final JsArray a,
                                                       final JsArray b
                                                      )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        return more(() -> () -> a.appendAll(b));

    }

    private static Trampoline<JsArray> unionAsSet(final JsArray a,
                                                  final JsArray b
                                                 )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        JsElem last = b.last();

        final Trampoline<JsArray> initCall = unionAsSet(a,
                                                        b.init()
                                                       );

        if (!a.containsElem(last)) return appendBack(last,
                                                     () -> initCall
                                                    );


        return more(() -> initCall);
    }

    private static Trampoline<JsArray> appendBack(final JsElem head,
                                                  final Trampoline<Trampoline<JsArray>> tail
                                                 )
    {
        return more(tail).map(it -> it.append(head));
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray union_(final JsArray that
                               )
    {

        return union_(this,
                      requireNonNull(that)
                     )
        .get();
    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsArray> union_(final JsArray a,
                                      final JsArray b
                                     )
    {

        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);


        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = union_(a.tail(),
                                                    b.tail()
                                                   );


        if (head.isJson() && isSameType(otherHead).test(head))
        {


            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> union_(obj,
                                                                       obj1,
                                                                       JsArray.TYPE.LIST
                                                                      ));


            return appendFront_(() -> headCall,
                                () -> tailCall
                               );
        }

        return appendFront(head,
                           () -> tailCall
                          );


    }

    private Trampoline<JsArray> appendAllBackTrampoline(final JsArray arr1,
                                                        final JsArray arr2
                                                       )
    {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return Trampoline.done(arr1);
        if (arr1.isEmpty()) return Trampoline.done(arr2);
        return Trampoline.more(() -> appendAllBackTrampoline(arr1.append(arr2.head()),
                                                             arr2.tail()
                                                            ));
    }

    private Trampoline<JsArray> appendAllFrontTrampoline(final JsArray arr1,
                                                         final JsArray arr2
                                                        )
    {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return Trampoline.done(arr1);
        if (arr1.isEmpty()) return Trampoline.done(arr2);
        return Trampoline.more(() -> appendAllFrontTrampoline(arr1.prepend(arr2.last()),
                                                              arr2.init()
                                                             ));
    }

    private T nullPadding(final int index,
                          final T arr,
                          final JsElem e
                         )
    {
        assert arr != null;
        assert e != null;

        return nullPaddingTrampoline(index,
                                     arr,
                                     e
                                    ).get();
    }

    private Trampoline<T> nullPaddingTrampoline(final int i,
                                                final T arr,
                                                final JsElem e
                                               )
    {

        if (i == arr.size()) return Trampoline.done(arr.appendBack(e));

        if (i == -1) return Trampoline.done(arr.update(array.size() - 1,
                                                       e
                                                      ));

        if (i < arr.size()) return Trampoline.done(arr.update(i,
                                                              e
                                                             ));


        return Trampoline.more(() -> nullPaddingTrampoline(i,
                                                           arr.appendBack(JsNull.NULL),
                                                           e
                                                          ));
    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsArray> combiner_(final JsArray a,
                                         final JsArray b
                                        )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);


        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = combiner_(a.tail(),
                                                       b.tail()
                                                      );


        if (head.isJson() && isSameType(otherHead).test(head))
        {


            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> combiner_(obj,
                                                                          obj1
                                                                         ));


            return appendFront_(() -> headCall,
                                () -> tailCall
                               );
        }

        return appendFront(head.isNull() ? otherHead : head,
                           () -> tailCall
                          );


    }


    @SuppressWarnings("squid:S1602") // curly braces makes IntelliJ to format the code in a more legible way
    private BiPredicate<Integer, JsPath> putEmptyJson(final MyVector<?> parray)
    {

        return (index, tail) ->
        {
            return index > parray.size() - 1 || parray.isEmpty() || parray.get(index)
                                                                          .isNotJson()
            ||
            (tail.head()
                 .isKey() && parray.get(index)
                                   .isArray()
            )
            ||
            (tail.head()
                 .isIndex() && parray.get(index)
                                     .isObj()
            );


        };


    }
}