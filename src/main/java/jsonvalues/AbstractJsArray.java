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
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

/**
 Explicit instantiation of JsArray interface to reduce class file size in subclasses.

 @param <V> type of the seq implementation to hold json arrays
 @param <M> type of the map implementation to hold json objects
 */
abstract class AbstractJsArray<V extends MySeq<V, M>, M extends MyMap<M, V>> implements JsArray

{

    protected  V seq;

    AbstractJsArray(V seq)
    {
        this.seq = seq;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public final JsArray appendAll(final JsPath path,
                                   final JsArray elems

                                  )
    {

        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                                    arr.appendAll(elems)
                                                                                                                   )),
                                                                                               e -> of(nullPadding(index,
                                                                                                                   seq,
                                                                                                                   of(seq.empty()).appendAll(elems)
                                                                                                                  ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      seq,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> of(seq.emptyObject()).appendAll(tail,
                                                                                                                                                                      elems
                                                                                                                                                                     ),
                                                                                                                                 a -> of(seq.empty()).appendAll(tail,
                                                                                                                                                                elems
                                                                                                                                                               )
                                                                                                                                )
                                                                                                                     )),
                                                                                                 () -> of(seq.update(index,
                                                                                                                     seq.get(index)
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
    public final JsArray append(final JsPath path,
                                final JsElem elem
                               )
    {
        if (requireNonNull(path).isEmpty()) return this;
        Objects.requireNonNull(elem);
        return path.head()
                   .match(key -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                    arr.append(elem)
                                                                                                   )),
                                                                               e -> of(nullPadding(index,
                                                                                                   seq,
                                                                                                   of(seq.empty()).append(elem)
                                                                                                  ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> of(nullPadding(index,
                                                                                                      seq,
                                                                                                      tail.head()
                                                                                                          .match(o -> of(seq.emptyObject()).append(tail,
                                                                                                                                                   elem
                                                                                                                                                  ),
                                                                                                                 a -> of(seq.empty()).append(tail,
                                                                                                                                             elem
                                                                                                                                            )
                                                                                                                )
                                                                                                     )),
                                                                                 () -> of(seq.update(index,
                                                                                                     seq.get(index)
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
    public final JsArray appendAll(final JsArray array
                                  )
    {
        return appendAllBackTrampoline(this,
                                       requireNonNull(array)
                                      ).get();

    }

    @Override
    public final JsArray prependAll(final JsArray array
                                   )
    {
        return appendAllFrontTrampoline(this,
                                        requireNonNull(array)
                                       ).get();

    }

    @SuppressWarnings("Duplicates")
    @Override
    public final JsArray prependAll(final JsPath path,
                                    final JsArray elems
                                   )
    {
        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                                    arr.prependAll(elems)
                                                                                                                   )),
                                                                                               e -> of(nullPadding(index,
                                                                                                                   seq,
                                                                                                                   of(seq.empty()).prependAll(elems)
                                                                                                                  ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      seq,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> of(seq.emptyObject()).prependAll(tail,
                                                                                                                                                                       elems
                                                                                                                                                                      ),
                                                                                                                                 a -> of(seq.empty()).prependAll(tail,
                                                                                                                                                                 elems
                                                                                                                                                                )
                                                                                                                                )

                                                                                                                     )),
                                                                                                 () -> of(seq.update(index,
                                                                                                                     seq.get(index)
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
    public final JsArray prepend(final JsPath path,
                                 final JsElem elem
                                )
    {
        Objects.requireNonNull(elem);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                    arr.prepend(elem)
                                                                                                   )),
                                                                               e -> of(nullPadding(index,
                                                                                                   seq,
                                                                                                   of(seq.empty()).prepend(elem)
                                                                                                  ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> of(nullPadding(index,
                                                                                                      seq,
                                                                                                      tail.head()
                                                                                                          .match(o -> of(seq.emptyObject()).prepend(tail,
                                                                                                                                                    elem
                                                                                                                                                   ),
                                                                                                                 a -> of(seq.empty()).prepend(tail,
                                                                                                                                              elem
                                                                                                                                             )
                                                                                                                )

                                                                                                     )),
                                                                                 () -> of(seq.update(index,
                                                                                                     seq.get(index)
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
    public final JsArray append(final JsElem e,
                                final JsElem... others
                               )
    {
        V acc = this.seq.appendBack(requireNonNull(e));
        for (JsElem other : requireNonNull(others)) acc = acc.appendBack(requireNonNull(other));
        return of(acc);
    }

    @Override
    public final JsArray prepend(final JsElem e,
                                 final JsElem... others
                                )
    {
        V acc = seq;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++)
        {
            final JsElem other = others[othersLength - 1 - i];
            acc = acc.appendFront(requireNonNull(other));
        }
        return of(acc.appendFront(requireNonNull(e)));
    }

    @Override
    public final boolean containsElem(final JsElem el)
    {
        return seq.contains(requireNonNull(el));
    }

    @Override
    public final boolean equals(final @Nullable Object that)
    {
        if (!(that instanceof AbstractJsArray<?, ?>)) return false;
        if (this == that) return true;
        final MySeq<?, ?> thatArray = ((AbstractJsArray<?, ?>) that).seq;
        final boolean thatEmpty = thatArray.isEmpty();
        final boolean thisEmpty = isEmpty();
        if (thatEmpty && thisEmpty) return true;
        if (this.size() != thatArray.size()) return false;
        return yContainsX(seq,
                          thatArray
                         ) && yContainsX(thatArray,
                                         seq
                                        );

    }


    private boolean yContainsX(final MySeq<?, ?> x,
                               final MySeq<?, ?> y
                              )
    {
        for (int i = 0; i < x.size(); i++)
        {
            if (!Objects.equals(x.get(i),
                                y.get(i)
                               ))
                return false;

        }
        return true;

    }

    private boolean yContainsSameX(MySeq<?, ?> x,
                                   MySeq<?, ?> y
                                  )
    {
        for (int i = 0; i < x.size(); i++)
        {

            final JsElem a = x.get(i);
            final JsElem b = y.get(i);
            if (a.isObj() && b.isObj())
            {
                if (!a.asJsObj()
                      .same(b.asJsObj())) return false;
            } else if (a.isArray() && b.isArray())
            {
                if (!a.asJsArray()
                      .same(b.asJsArray())) return false;
            } else if (!Objects.equals(a,
                                       b
                                      ))
                return false;

        }
        return true;

    }


    @Override
    public final boolean same(final JsArray that)
    {
        if (this == that) return true;
        final MySeq<?,?> other = ((AbstractJsArray<?, ?>) that).seq;
        final boolean thatEmpty = that.isEmpty();
        final boolean thisEmpty = isEmpty();
        if (thatEmpty && thisEmpty) return true;
        if (seq.size() != other.size()) return false;
        return yContainsSameX(seq,
                              other
                             ) && yContainsSameX(other,
                                                 seq
                                                );
    }

    @Override
    public final JsElem get(final Position pos)
    {


        return requireNonNull(pos).match(key -> JsNothing.NOTHING,
                                         index ->
                                         {
                                             if (index == -1 && !seq.isEmpty()) return seq.last();
                                             return (seq.isEmpty() || index < 0 || index > seq.size() - 1) ?
                                             JsNothing.NOTHING : seq.get(index);
                                         }
                                        );


    }

    @Override
    public int hashCode()
    {
        return seq.hashCode();
    }

    @Override
    public final JsElem head()
    {
        return seq.head();
    }

    @Override
    public final JsArray init()
    {
        return of(seq.init());
    }

    @Override
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public final JsArray intersection(final JsArray that,
                                      final TYPE ARRAY_AS
                                     )
    {
        return intersection(this,
                            requireNonNull(that),
                            requireNonNull(ARRAY_AS)
                           ).get();
    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private static Trampoline<JsArray> intersection(JsArray a,
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
            default:
                throw InternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }

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

        if (head.equals(otherHead)) return more(tailCall).map(it -> it.prepend(head));

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

        if (b.containsElem(head)) return more(tailCall).map(it -> it.prepend(head));

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

        if (b.containsElem(head) && !tail.containsElem(head))
            return more(tailCall).map(it -> it.prepend(head));

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
    private Trampoline<JsArray> intersection_(final JsArray a,
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

        if (head.isJson() && head.isSameType(otherHead))
        {
            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpIntersectionJsons().intersection_(obj,
                                                                                                              obj1,
                                                                                                              JsArray.TYPE.LIST
                                                                                                             ));

            return more(() -> tailCall).flatMap(tailResult -> headCall.map(tailResult::prepend));

        } else if (head.equals(otherHead))
            return more(() -> tailCall).map(it -> it.prepend(head));
        else return more(() -> tailCall);
    }


    @Override
    public final boolean isEmpty()
    {
        return seq.isEmpty();
    }

    @Override
    public final Iterator<JsElem> iterator()
    {
        return seq.iterator();
    }

    @Override
    public final JsElem last()
    {

        return seq.last();
    }


    abstract JsArray of(V vector);

    abstract JsObj of(M map);

    @Override
    public final JsArray add(JsPath path,
                             final Function<? super JsElem, ? extends JsElem> fn
                            )
    {
        if (requireNonNull(path).isEmpty()) throw UserError.pathEmpty("add");
        final JsPath tail = path.tail();
        final Position head = path.head();
        return head.match(key ->
                          {
                              throw UserError.addingKeyIntoArray(key,
                                                                 this,
                                                                 path,
                                                                 "add"
                                                                );
                          },
                          index -> tail.ifEmptyElse(() -> of(seq.add(index,
                                                                     fn.apply(get(head))
                                                                    )),
                                                    () ->
                                                    {
                                                        final JsElem headElem = get(head);

                                                        if (headElem.isNothing())
                                                            throw UserError.parentNotFound(JsPath.fromIndex(index),
                                                                                           this,
                                                                                           "add"
                                                                                          );
                                                        if (!headElem.isJson())
                                                            throw UserError.parentIsNotAJson(JsPath.fromIndex(index),
                                                                                             this,
                                                                                             path,
                                                                                             "add"
                                                                                            );
                                                        if (headElem.isObj() && tail.head()
                                                                                    .isIndex())
                                                            throw UserError.addingIndexIntoObject(tail.head()
                                                                                                      .asIndex().n,
                                                                                                  this,
                                                                                                  path,
                                                                                                  "add"
                                                                                                 );
                                                        if (headElem.isArray() && tail.head()
                                                                                      .isKey())
                                                            throw UserError.addingKeyIntoArray(tail.head()
                                                                                                   .asKey().name,
                                                                                               this,
                                                                                               path,
                                                                                               "add"
                                                                                              );


                                                        return of(seq.update(index,
                                                                             headElem.asJson()
                                                                                     .add(tail,
                                                                                          fn
                                                                                         )
                                                                            ));
                                                    }
                                                   )


                         );
    }

    @Override
    public final JsArray put(final JsPath path,
                             final Function<? super JsElem, ? extends JsElem> fn
                            )
    {

        requireNonNull(fn);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();

                              return tail.ifEmptyElse(() -> ifNothingElse(() -> this,
                                                                          elem -> of(nullPadding(index,
                                                                                                 seq,
                                                                                                 elem
                                                                                                ))
                                                                         )
                                                      .apply(fn.apply(get(path))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () ->
                                                                                 {
                                                                                     final JsElem newElem = tail.head()
                                                                                                                .match(key -> of(seq.emptyObject()).put(tail,
                                                                                                                                                        fn
                                                                                                                                                       ),
                                                                                                                       i -> of(seq.empty()).put(tail,
                                                                                                                                                fn
                                                                                                                                               )
                                                                                                                      );
                                                                                     return of(nullPadding(index,
                                                                                                           seq,
                                                                                                           newElem
                                                                                                          ));
                                                                                 },
                                                                                 () -> of(seq.update(index,
                                                                                                     seq.get(index)
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
        return new OpMapReduce<>(predicate,
                                 map,
                                 op
        ).reduce(this);


    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final <R> Optional<R> reduce_(final BinaryOperator<R> op,
                                         final Function<? super JsPair, R> map,
                                         final Predicate<? super JsPair> predicate
                                        )
    {
        return new OpMapReduce<>(predicate,
                                 map,
                                 op
        ).reduce_(this);

    }

    @Override
    public final JsArray remove(final JsPath path)
    {

        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              final int maxIndex = seq.size() - 1;
                              if (index < -1 || index > maxIndex) return this;
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> of(index == -1 ? seq.remove(maxIndex) : seq.remove(index)),
                                                      () -> ifJsonElse(json -> of(seq.update(index,
                                                                                             json.remove(tail)
                                                                                            )),
                                                                       e -> this
                                                                      )
                                                      .apply(seq.get(index))
                                                     );
                          }

                         );


    }

    @Override
    public final int size()
    {
        return seq.size();
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
                        .mapToObj(i ->
                                  {
                                      final JsPath path = JsPath.fromIndex(i);
                                      return JsPair.of(path,
                                                       get(path)
                                                      );
                                  });

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
                                                             .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                                   pair.path
                                                                                                                  ),
                                                                                                  a -> streamOfArr(a,
                                                                                                                   pair.path
                                                                                                                  ),
                                                                                                  e -> Stream.of(pair)
                                                                                                 )
                                                                                      .apply(pair.elem)
                                                                     )
                                                );


    }

    @Override
    public final JsArray tail()
    {
        return of(seq.tail());
    }

    @Override
    public String toString()
    {
        return seq.toString();
    }

    @Override
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public final JsArray union(final JsArray that,
                               final JsArray.TYPE ARRAY_AS
                              )
    {
        return union(this,
                     requireNonNull(that),
                     requireNonNull(ARRAY_AS)
                    ).get();
    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private Trampoline<JsArray> union(JsArray a,
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
            default:
                throw InternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }
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
        return more(() -> tailCall).map(it -> it.prepend(a.head()));
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
        if (!a.containsElem(last)) return more(() -> initCall).map(it -> it.append(last));
        return more(() -> initCall);
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
    private Trampoline<JsArray> union_(final JsArray a,
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
        if (head.isJson() && head.isSameType(otherHead))
        {
            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();
            Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpUnionJsons().union_(obj,
                                                                                                obj1,
                                                                                                JsArray.TYPE.LIST
                                                                                               ));
            return more(() -> tailCall).flatMap(tailResult -> headCall.map(tailResult::prepend));

        }
        return more(() -> tailCall).map(it -> it.prepend(head));
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

    private V nullPadding(final int index,
                          final V arr,
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

    private Trampoline<V> nullPaddingTrampoline(final int i,
                                                final V arr,
                                                final JsElem e
                                               )
    {

        if (i == arr.size()) return Trampoline.done(arr.appendBack(e));

        if (i == -1) return Trampoline.done(arr.update(seq.size() - 1,
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


    @SuppressWarnings("squid:S1602") // curly braces makes IntelliJ to format the code in a more legible way
    private BiPredicate<Integer, JsPath> putEmptyJson(final V parray)
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