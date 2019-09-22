package jsonvalues.myseqs.immutable;

import io.vavr.collection.List;
import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;

import java.util.Iterator;

public class VavrList implements ImmutableSeq
{

    private final List<JsElem> list;

    private VavrList(final List<JsElem> list)
    {
        this.list = list;
    }

    public VavrList()
    {
        list = List.empty();
    }

    @Override
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new VavrList(list.prepend(e));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {
        return new VavrList(list.append(e));
    }

    @Override
    public JsElem head()
    {
        return list.head();
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return list.iterator();
    }

    @Override
    public ImmutableSeq tail()
    {
        return new VavrList(list.tail());
    }

    @Override
    public ImmutableSeq init()
    {
        return new VavrList(list.init());
    }

    @Override
    public JsElem last()
    {
        return list.last();
    }

    @Override
    public JsElem get(final int i)
    {
        return list.get(i);
    }

    @Override
    public int size()
    {
        return list.size();
    }

    @Override
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return list.contains(e);
    }

    @Override
    public ImmutableSeq update(final int i,
                               final JsElem e
                              )
    {
        return new VavrList(list.update(i,
                                        e
                                       ));
    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem e
                           )
    {
        return new VavrList(list.insert(i,
                                        e
                                       ));

    }

    @Override
    public ImmutableSeq remove(final int i)
    {
        return new VavrList(list.removeAt(i));
    }
}
