package jsonvalues.myseqs.mutable;

import jsonvalues.JsElem;
import jsonvalues.MutableSeq;

import java.util.HashSet;
import java.util.Iterator;

public class JavaHashSet implements MutableSeq
{

    private final HashSet<JsElem> set;

    public JavaHashSet()
    {
        this.set = new HashSet<>();
    }

    @Override
    public MutableSeq copy()
    {
        return null;
    }

    @Override
    public JsElem head()
    {
        return null;
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return null;
    }

    @Override
    public MutableSeq tail()
    {
        return null;
    }

    @Override
    public MutableSeq init()
    {
        return null;
    }

    @Override
    public JsElem last()
    {
        return null;
    }

    @Override
    public JsElem get(final int i)
    {
        return null;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean contains(final JsElem jsElem)
    {
        return false;
    }

    @Override
    public void appendFront(final JsElem jsElem)
    {

    }

    @Override
    public void appendBack(final JsElem jsElem)
    {
    }

    @Override
    public void update(final int i,
                       final JsElem jsElem
                      )
    {
    }

    @Override
    public void add(final int i,
                    final JsElem jsElem
                   )
    {
    }

    @Override
    public void remove(final int i)
    {

    }

}
