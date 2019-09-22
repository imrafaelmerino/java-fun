package jsonvalues.myseqs.immutable;

import io.vavr.collection.HashSet;
import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;

import java.util.Iterator;

public class VavrHashSet implements ImmutableSeq
{
    private final HashSet<JsElem> set;

    private VavrHashSet(final HashSet<JsElem> set)
    {
        this.set = set;
    }

    public VavrHashSet()
    {
        set = HashSet.empty();
    }

    @Override
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new VavrHashSet(set.add(e));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {
        return new VavrHashSet(set.add(e));
    }

    @Override
    public JsElem head()
    {
        return set.head();
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return set.iterator();
    }

    @Override
    public ImmutableSeq tail()
    {
        return new VavrHashSet(set.tail());
    }

    @Override
    public ImmutableSeq init()
    {
        return new VavrHashSet(set.init());
    }

    @Override
    public JsElem last()
    {
        return set.last();
    }

    @Override
    public JsElem get(final int i)
    {
        throw new UnsupportedOperationException("get by index of hashset");
    }

    @Override
    public int size()
    {
        return set.size();
    }

    @Override
    public boolean isEmpty()
    {
        return set.isEmpty();
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return set.contains(e);
    }

    @Override
    public ImmutableSeq update(final int i,
                               final JsElem jsElem
                              )
    {
        throw new UnsupportedOperationException("update by index of hashset");

    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem jsElem
                           )
    {
        throw new UnsupportedOperationException("add by index of hashset");
    }

    @Override
    public ImmutableSeq remove(final int i)
    {
        throw new UnsupportedOperationException("remove by index of hashset");
    }
}
