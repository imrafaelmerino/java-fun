package jsonvalues;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class JavaList implements MutableSeq
{
    private List<JsElem> elements;


    JavaList(final List<JsElem> pvector)
    {
        elements = pvector;
    }

    JavaList()
    {
        this.elements = new ArrayList<>();
    }

    @Override
    public JavaList appendBack(final JsElem elem)
    {
        elements.add(elem);
        return this;
    }

    @Override
    public JavaList appendFront(final JsElem elem)
    {
        elements.add(0,
                     elem
                    );
        return this;
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return elements.contains(e);
    }

    @Override
    public JsElem get(final int index)
    {
        return elements.get(index);
    }

    @Override
    public int hashCode()
    {
        return elements.hashCode();

    }

    @Override
    public JsElem head()
    {
        if (isEmpty()) throw UserError.headOfEmptyArr();
        return elements.get(0);
    }

    @Override
    public JavaList init()
    {
        if (isEmpty()) throw UserError.initOfEmptyArr();
        return new JavaList(IntStream.range(0,
                                            elements.size() - 1
                                           )
                                     .mapToObj(i -> elements.get(i))
                                     .collect(Collectors.toList()));

    }

    @Override
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return elements.iterator();
    }

    @Override
    public JsElem last()
    {
        if (isEmpty()) throw UserError.lastOfEmptyArr();
        return elements.get(size() - 1);
    }

    @Override
    public String toString()
    {
        if (elements.isEmpty()) return "[]";

        return elements.stream()
                       .map(JsElem::toString)
                       .collect(Collectors.joining(",",
                                                   "[",
                                                   "]"
                                                  ));
    }

    @Override
    public JavaList remove(final int index)
    {
        try
        {
            elements.remove(index);
            return this;
        }
        catch (UnsupportedOperationException e)
        {
            throw UserError.unsupportedOperationOnList(elements.getClass(),
                                                       "remove"
                                                      );
        }

    }

    @Override
    public JavaMap emptyObject()
    {
        return new JavaMap();
    }

    @Override
    public JavaList copy()
    {
        return new JavaList(new ArrayList<>(elements));
    }

    @Override
    public int size()
    {
        return elements.size();
    }

    @Override
    public JavaList empty()
    {
        return new JavaList();
    }

    @Override
    public JavaList tail()
    {
        if (isEmpty()) throw UserError.tailOfEmptyArr();

        return new JavaList(elements.stream()
                                    .skip(1)
                                    .collect(Collectors.toList()));

    }

    @Override
    public JavaList update(final int index,
                           final JsElem ele
                          )
    {
        try
        {
            elements.set(index,
                         ele
                        );
            return this;
        }
        catch (UnsupportedOperationException e)
        {
            throw UserError.unsupportedOperationOnList(elements.getClass(),
                                                       "set"
                                                      );
        }
        catch (IndexOutOfBoundsException e)
        {

            throw UserError.indexOutOfBounds(this.size(),
                                             index,
                                             "set"
                                            );
        }

    }

    @Override
    public JavaList add(final int index,
                        final JsElem ele
                       )
    {
        try
        {

            if (index == -1) elements.add(ele);
            else elements.add(index,
                              ele
                             );
            return this;
        }
        catch (UnsupportedOperationException e)
        {
            throw UserError.unsupportedOperationOnList(elements.getClass(),
                                                       "add"
                                                      );
        }
        catch (IndexOutOfBoundsException e)
        {

            throw UserError.indexOutOfBounds(this.size(),
                                             index,
                                             "add"
                                            );
        }

    }

}
