package jsonvalues.myseqs.immutable;

import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;
import jsonvalues.Trampoline;

import java.util.Collections;
import java.util.Iterator;
import static jsonvalues.ClojureFns.*;

public class ClojureVector implements ImmutableSeq
{

    private final IPersistentVector vector;

    private ClojureVector(final IPersistentVector vector)
    {
        this.vector = vector;
    }

    public ClojureVector()
    {
        this.vector = PersistentVector.EMPTY;
    }

    @Override
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new ClojureVector((IPersistentVector) vec.invoke(cons.invoke(e,
                                                                            vector
                                                                           )));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {
        return new ClojureVector((IPersistentVector) conj.invoke(vector,
                                                                 e
                                                                ));
    }

    @Override
    public JsElem head()
    {
        return (JsElem) vector.seq()
                              .first();
    }


    @Override
    public ImmutableSeq init()
    {
        return new ClojureVector((IPersistentVector) butlast.invoke(vector));
    }

    @Override
    public JsElem last()
    {
        return (JsElem) last.invoke(vector);
    }

    @Override
    public JsElem get(final int i)
    {
        return (JsElem) nth.invoke(vector,
                                   i
                                  );
    }

    @Override
    public int size()
    {
        return vector.seq()
                     .count();
    }

    @Override
    public boolean isEmpty()
    {
        return (boolean) isEmpty.invoke(vector);

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
        return vector.toString();
    }


    @Override
    public Iterator<JsElem> iterator()
    {
        if (isEmpty()) return Collections.emptyIterator();
        @SuppressWarnings("unchecked")// list is a PersistentVector
        final Iterator<JsElem> iterator = ((PersistentVector) vector).iterator();
        return iterator;
    }

    @Override
    public ImmutableSeq tail()
    {
        return new ClojureVector((PersistentVector) vec.invoke(rest.invoke(vector)));
    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem e
                           )
    {
        if (i == 0) return new ClojureVector((IPersistentVector) vec.invoke(cons.invoke(e,
                                                                                        vector
                                                                                       )));

        if (i == vector.count()) return new ClojureVector((IPersistentVector) (conj.invoke(vector,
                                                                                           e
                                                                                          )));

        final IPersistentVector xs = (IPersistentVector) subvec.invoke(vector,
                                                                       0,
                                                                       i
                                                                      );

        final IPersistentVector ys = (IPersistentVector) subvec.invoke(vector,
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

        final IPersistentVector xs = (IPersistentVector) subvec.invoke(vector,
                                                                       0,
                                                                       i
                                                                      );
        final IPersistentVector ys = (IPersistentVector) subvec.invoke(vector,
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
        return new ClojureVector((IPersistentVector) assoc.invoke(vector,
                                                                  i,
                                                                  e
                                                                 ));
    }

}
