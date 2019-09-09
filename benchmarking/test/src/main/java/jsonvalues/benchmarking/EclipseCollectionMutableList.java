package jsonvalues.benchmarking;

import jsonvalues.JsElem;
import jsonvalues.MutableMap;
import jsonvalues.MutableSeq;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

import java.util.Iterator;

public class EclipseCollectionMutableList implements MutableSeq
{

    MutableList<JsElem> list;

    public EclipseCollectionMutableList(final MutableList<JsElem> list)
    {
        this.list = list;
    }

    public EclipseCollectionMutableList()
    {
        this.list = Lists.mutable.empty();
    }


    @Override
    public MutableSeq copy()
    {
        return null;
    }

    @Override
    public JsElem head()
    {
        return list.get(0);
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return list.iterator();
    }

    @Override
    public MutableSeq tail()
    {
        return new EclipseCollectionMutableList(list.subList(1,list.size()-1));
    }

    @Override
    public MutableSeq init()
    {
        return new EclipseCollectionMutableList(list.subList(0,list.size()-1));
    }

    @Override
    public JsElem last()
    {
        return list.get(list.size()-1);
    }

    @Override
    public JsElem get(final int index)
    {
        return list.get(index);
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
    public MutableSeq empty()
    {
        return new EclipseCollectionMutableList();
    }

    @Override
    public MutableSeq appendFront(final JsElem elem)
    {
        list.add(0,elem);
        return new EclipseCollectionMutableList(list);
    }

    @Override
    public MutableSeq appendBack(final JsElem elem)
    {
        return null;
    }

    @Override
    public MutableSeq update(final int index,
                             final JsElem ele
                            )
    {
        list.set(index,ele);
        return new EclipseCollectionMutableList(list);
    }

    @Override
    public MutableSeq add(final int index,
                          final JsElem ele
                         )
    {
        list.add(index,ele);
        return new EclipseCollectionMutableList(list);
    }

    @Override
    public MutableSeq remove(final int index)
    {

        list.remove(index);
        return new EclipseCollectionMutableList(list);
    }

    @Override
    public MutableMap emptyObject()
    {
        return new EclipseCollectionMutableMap();
    }
}
