package jsonvalues.myseqs.mutable;

import jsonvalues.JsElem;
import jsonvalues.MutableSeq;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

import java.util.Iterator;

public class EclipseCollectionList implements MutableSeq
{

    private final MutableList<JsElem> list;

    private EclipseCollectionList(final MutableList<JsElem> list)
    {
        this.list = list;
    }

    public EclipseCollectionList()
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
        return new EclipseCollectionList(list.subList(1,
                                                      list.size()
                                                     ));
    }

    @Override
    public MutableSeq init()
    {
        return new EclipseCollectionList(list.subList(0,
                                                      list.size() - 1
                                                     ));
    }

    @Override
    public JsElem last()
    {
        return list.get(list.size() - 1);
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
    public void appendFront(final JsElem elem)
    {
        list.add(0,
                 elem
                );
    }

    @Override
    public void appendBack(final JsElem elem)
    {
        list.add(list.size(),elem);
    }

    @Override
    public void update(final int index,
                       final JsElem ele
                      )
    {
        list.set(index,
                 ele
                );
    }

    @Override
    public void add(final int index,
                    final JsElem ele
                   )
    {
        list.add(index,
                 ele
                );
    }

    @Override
    public void remove(final int index)
    {

        list.remove(index);
    }


}
