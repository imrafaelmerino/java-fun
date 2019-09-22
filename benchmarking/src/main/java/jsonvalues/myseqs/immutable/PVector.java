package jsonvalues.myseqs.immutable;

import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;
import org.pcollections.TreePVector;

import java.util.Iterator;

public class PVector implements ImmutableSeq
{
    private final org.pcollections.PVector<JsElem> vector;

    private PVector(final org.pcollections.PVector<JsElem> vector)
    {
        this.vector = vector;
    }

    public PVector()
    {
        vector = TreePVector.empty();
    }

    @Override
    public JsElem head()
    {
        return vector.get(0);
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return null;
    }

    @Override
    public ImmutableSeq tail()
    {
        return new PVector(vector.minus(0));
    }

    @Override
    public ImmutableSeq init()
    {
        return null;
    }

    @Override
    public JsElem last()
    {
        return vector.get(vector.size() - 1);
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
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new PVector(vector.plus(0,
                                       e
                                      ));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {
        return new PVector(vector.plus(e));
    }

    @Override
    public ImmutableSeq update(final int i,
                               final JsElem e
                              )
    {
        return new PVector(vector.with(i,
                                       e
                                      ));
    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem e
                           )
    {
        return new PVector(vector.plus(i,
                                       e));
    }

    @Override
    public ImmutableSeq remove(final int i)
    {
        return new PVector(vector.minus(i));
    }

}
