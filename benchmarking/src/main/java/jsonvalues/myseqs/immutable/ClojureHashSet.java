package jsonvalues.myseqs.immutable;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.IPersistentSet;
import clojure.lang.PersistentHashSet;
import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;

import java.util.Iterator;
import java.util.function.Function;

public class ClojureHashSet implements ImmutableSeq
{

    //todo esta repetido , cambiar paqueteria
    private static final Function<String, IFn> cljFn = fn -> Clojure.var("clojure.core",
                                                                         fn
                                                                        );

    private static final IFn toSet = cljFn.apply("set");
    private static final IFn isEmpty = cljFn.apply("empty?");
    private static final IFn butlast = cljFn.apply("butlast");
    private static final IFn first = cljFn.apply("first");
    private static final IFn rest = cljFn.apply("rest");
    private static final IFn conj = cljFn.apply("conj");
    private static final IFn last = cljFn.apply("last");
    private final IPersistentSet set;

    private ClojureHashSet(final IPersistentSet set)
    {
        this.set = set;
    }

    public ClojureHashSet()
    {
        set = PersistentHashSet.EMPTY;
    }


    @Override
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new ClojureHashSet((IPersistentSet) set.cons(e));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {
        return new ClojureHashSet((IPersistentSet) conj.invoke(set,
                                                               e
                                                              ));
    }

    @Override
    public JsElem head()
    {
        return (JsElem) first.invoke(set);
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return null;
    }

    @Override
    public ImmutableSeq tail()
    {
        return new ClojureHashSet((IPersistentSet) toSet.invoke(rest.invoke(set)));
    }

    @Override
    public ImmutableSeq init()
    {
        return new ClojureHashSet((IPersistentSet) toSet.invoke(butlast.invoke(set)));
    }

    @Override
    public JsElem last()
    {
        return (JsElem) last.invoke(set);
    }

    @Override
    public JsElem get(final int i)
    {
        throw new UnsupportedOperationException("get by index of hashset");

    }

    @Override
    public int size()
    {
        return set.count();
    }

    @Override
    public boolean isEmpty()
    {
        return (boolean) isEmpty.invoke(set);
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return set.contains(e);
    }

    @Override
    public ImmutableSeq update(final int i,
                               final JsElem e
                              )
    {
        throw new UnsupportedOperationException("update by index of hashset");

    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem e
                           )
    {
        throw new UnsupportedOperationException("add by index of hashset");

    }

    @Override
    public ImmutableSeq remove(final int i)
    {
        throw new UnsupportedOperationException("remove by index of hashset");

    }

    @Override
    public String toString()
    {
        return set.toString();
    }

}
