package jsonvalues.myseqs.mutable;

import jsonvalues.JsElem;
import jsonvalues.MutableSeq;

import java.util.HashSet;
import java.util.Iterator;

public class JavaHashSet implements MutableSeq
{

    private final HashSet<JsElem> set;

    private JavaHashSet(final HashSet<JsElem> set)
    {
        this.set = set;
    }

    public JavaHashSet()
    {
        this.set = new HashSet<>();
    }

    @Override
    public MutableSeq copy()
    {
        return new JavaHashSet(new HashSet<>(set));
    }

    @Override
    public JsElem head()
    {
        throw new UnsupportedOperationException("head of hashset");
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return set.iterator();
    }

    @Override
    public MutableSeq tail()
    {
        throw new UnsupportedOperationException("tail of hashset");
    }

    @Override
    public MutableSeq init()
    {

        throw new UnsupportedOperationException("init of hashset");

    }

    @Override
    public JsElem last()
    {
        throw new UnsupportedOperationException("last of hashset");

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
    public void appendFront(final JsElem e)
    {
        set.add(e);
    }

    @Override
    public void appendBack(final JsElem e)
    {
        set.add(e);
    }

    @Override
    public void update(final int i,
                       final JsElem jsElem
                      )
    {
        throw new UnsupportedOperationException("update by index of hashset");

    }

    @Override
    public void add(final int i,
                    final JsElem jsElem
                   )
    {
        throw new UnsupportedOperationException("add by index of hashset");

    }

    @Override
    public void remove(final int i)
    {
        throw new UnsupportedOperationException("remove by index of hashset");

    }

}
