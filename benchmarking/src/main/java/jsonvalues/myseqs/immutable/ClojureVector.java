package jsonvalues.myseqs.immutable;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;
import jsonvalues.Trampoline;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

public class ClojureVector implements ImmutableSeq
{

    //todo esta repetido , cambiar paqueteria
    private static final Function<String, IFn> cljFn = fn -> Clojure.var("clojure.core",
                                                                         fn
                                                                        );

    private static final IFn rest = cljFn.apply("rest");
    private static final IFn vec = cljFn.apply("vec");
    private static final IFn isEmpty = cljFn.apply("empty?");
    private static final IFn butlast = cljFn.apply("butlast");
    private static final IFn assoc = cljFn.apply("assoc");
    private static final IFn concat = cljFn.apply("concat");
    private static final IFn subvec = cljFn.apply("subvec");
    private static final IFn conj = cljFn.apply("conj");
    private static final IFn cons = cljFn.apply("cons");
    private static final IFn last = cljFn.apply("last");
    private static final IFn nth = cljFn.apply("nth");
    private final IPersistentVector list;

    private ClojureVector(final IPersistentVector list)
    {
        this.list = list;
    }

    public ClojureVector()
    {
        this.list = PersistentVector.EMPTY;
    }

    @Override
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new ClojureVector((IPersistentVector) vec.invoke(cons.invoke(e,
                                                                            list
                                                                           )));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {
        return new ClojureVector((IPersistentVector) conj.invoke(list,
                                                                 e
                                                                ));
    }

    @Override
    public JsElem head()
    {
        return (JsElem) list.seq()
                            .first();
    }


    @Override
    public ImmutableSeq init()
    {
        return new ClojureVector((IPersistentVector) butlast.invoke(list));
    }

    @Override
    public JsElem last()
    {
        return (JsElem) last.invoke(list);
    }

    @Override
    public JsElem get(final int i)
    {
        return (JsElem) nth.invoke(list,
                                   i
                                  );
    }

    @Override
    public int size()
    {
        return list.seq()
                   .count();
    }

    @Override
    public boolean isEmpty()
    {
        return (boolean) isEmpty.invoke(list);

    }

    @Override
    public boolean contains(final JsElem e)
    {
        return contains(this,
                        e
                       ).get();
    }

    /**
     Clojure doesnt have a contains element function, it has the some function, but it's easier to
     implement the function by myself
     @param list the list
     @param e the element
     @return true if the list contains the element
     */
    private static Trampoline<Boolean> contains(final ImmutableSeq list,
                                                final JsElem e
                                               )
    {
        if (list.isEmpty()) return Trampoline.done(false);
        if (list.head()
                .equals(e)) return Trampoline.done(true);
        return Trampoline.more(() -> contains(list.tail(),
                                              e
                                             ));
    }


    @Override
    public String toString()
    {
        return list.toString();
    }


    @Override
    public Iterator<JsElem> iterator()
    {
        if (isEmpty()) return Collections.emptyIterator();
        return ((PersistentVector) list).iterator();
    }

    @Override
    public ImmutableSeq tail()
    {
        return new ClojureVector((PersistentVector) vec.invoke(rest.invoke(list)));
    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem e
                           )
    {
        if (i == 0) return new ClojureVector((IPersistentVector) vec.invoke(cons.invoke(e,
                                                                                        list
                                                                                       )));

        if (i == list.count()) return new ClojureVector((IPersistentVector) (conj.invoke(list,
                                                                                         e
                                                                                        )));

        final IPersistentVector xs = (IPersistentVector) subvec.invoke(list,
                                                                       0,
                                                                       i
                                                                      );

        final IPersistentVector ys = (IPersistentVector) subvec.invoke(list,
                                                                       i
                                                                      );

        return new ClojureVector((IPersistentVector) vec.invoke(concat.invoke(xs.cons(e),
                                                                              ys
                                                                             )));
    }

    @Override
    public ImmutableSeq remove(final int i)
    {
        if (i == 0) return tail();

        final IPersistentVector xs = (IPersistentVector) subvec.invoke(list,
                                                                       0,
                                                                       i
                                                                      );
        final IPersistentVector ys = (IPersistentVector) subvec.invoke(list,
                                                                       i + 1
                                                                      );
        return new ClojureVector((IPersistentVector) vec.invoke(concat.invoke(xs,
                                                                              ys
                                                                             )));
    }

    @Override
    public ImmutableSeq update(final int i,
                               final JsElem e
                              )
    {
        return new ClojureVector((IPersistentVector) assoc.invoke(list,
                                                                  i,
                                                                  e
                                                                 ));
    }

}
