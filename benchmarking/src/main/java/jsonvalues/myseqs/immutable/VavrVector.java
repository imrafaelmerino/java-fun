package jsonvalues.myseqs.immutable;

import io.vavr.collection.Vector;
import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;

import java.util.Iterator;

public class VavrVector implements ImmutableSeq
{

    private final Vector<JsElem> vector;

    private VavrVector(final Vector<JsElem> vector)
    {
        this.vector = vector;
    }

    public VavrVector()
    {
        vector = Vector.empty();
    }

    @Override
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new VavrVector(vector.prepend(e));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {
        return new VavrVector(vector.append(e));
    }

    @Override
    public JsElem head()
    {
        return vector.head();
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return vector.iterator();
    }

    @Override
    public ImmutableSeq tail()
    {
        return new VavrVector(vector.tail());
    }

    @Override
    public ImmutableSeq init()
    {
        return new VavrVector(vector.init());
    }

    @Override
    public JsElem last()
    {
        return vector.last();
    }

    @Override
    public JsElem get(final int i)
    {
        return vector.get(i);
    }

    @Override
    public int size()
    {
        return vector.size();
    }

    @Override
    public boolean isEmpty()
    {
        return vector.isEmpty();
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return vector.contains(e);
    }

    @Override
    public ImmutableSeq update(final int i,
                               final JsElem e
                              )
    {
        return new VavrVector(vector.update(i,
                                            e
                                           ));
    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem e
                           )
    {
        return new VavrVector(vector.insert(i,
                                            e
                                           ));
    }

    @Override
    public ImmutableSeq remove(final int i)
    {
        return new VavrVector(vector.removeAt(i));
    }
}
