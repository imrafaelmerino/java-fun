package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.END_ARRAY;

final class JavaVector implements MyVector<JavaVector>
{
    private List<JsElem> elements;



    JavaVector(final List<JsElem> pvector)
    {
        elements = pvector;
    }

    JavaVector()
    {
        this.elements = new ArrayList<>();
    }


    @Override
    public JavaVector add(final Collection<? extends JsElem> list)
    {
        elements.addAll(list);
        return this;
    }

    @Override
    public JavaVector add(final JavaVector list)
    {
        elements.addAll(list.elements);
        return this;
    }

    @Override
    public JavaVector appendBack(final JsElem elem)
    {
        elements.add(elem);
        return this;
    }

    @Override
    public JavaVector appendFront(final JsElem elem)
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
    public JavaVector init()
    {
        if (isEmpty()) throw UserError.initOfEmptyArr();
        return new JavaVector(IntStream.range(0,
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
    public JavaVector remove(final int index)
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
    public int size()
    {
        return elements.size();
    }

    @Override
    public Stream<JsElem> stream()
    {
        return elements.stream();
    }

    @Override
    public JavaVector tail()
    {
        if (isEmpty()) throw UserError.tailOfEmptyArr();

        return new JavaVector(elements.stream()
                                      .skip(1)
                                      .collect(Collectors.toList()));

    }

    @Override
    public JavaVector update(final int index,
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
    public JavaVector add(final int index,
                          final JsElem ele
                         )
    {
        try
        {

            if (index == - 1) elements.add(ele);
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

    @Override
    public boolean equals(final @Nullable Object that)
    {
        return this.eq(that);
    }

    void parse(final JsParser parser) throws MalformedJson
    {
        JsParser.Event elem;
        while ((elem = parser.next()) != END_ARRAY)
        {
            assert elem != null;
            switch (elem)
            {
                case VALUE_STRING:
                    this.appendBack(parser.getJsString());
                    break;
                case VALUE_NUMBER:
                    this.appendBack(parser.getJsNumber());
                    break;
                case VALUE_FALSE:
                    this.appendBack(FALSE);
                    break;
                case VALUE_TRUE:
                    this.appendBack(TRUE);
                    break;
                case VALUE_NULL:
                    this.appendBack(NULL);
                    break;
                case START_OBJECT:
                    final JavaMap obj = new JavaMap();
                    obj.parse(parser);
                    this.appendBack(new MutableJsObj(obj));
                    break;
                case START_ARRAY:
                    final JavaVector arr = new JavaVector();
                    arr.parse(parser);
                    this.appendBack(new MutableJsArray(arr));
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }
        }
    }

    void parse(final JsParser parser,
               final ParseBuilder.Options options,
               final JsPath path
              ) throws MalformedJson
    {
        JsParser.Event elem;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            assert elem != null;
            final JsPath currentPath = path.inc();
            switch (elem)
            {
                case VALUE_STRING:
                    JsPair.of(currentPath,
                              parser.getJsString()
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case VALUE_NUMBER:
                    JsPair.of(currentPath,
                              parser.getJsNumber()
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case VALUE_FALSE:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case VALUE_TRUE:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case VALUE_NULL:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        final JavaMap obj = new JavaMap();
                        obj.parse(parser,
                                  options,
                                  currentPath
                                 );
                        this.appendBack(new MutableJsObj(obj));
                    }
                    break;

                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        final JavaVector arr = new JavaVector();
                        arr.parse(parser,
                                  options,
                                  currentPath.index(-1)
                                 );

                        this.appendBack(new MutableJsArray(arr));
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }
        }
    }
}
