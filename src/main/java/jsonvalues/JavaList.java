package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

final class JavaList extends MutableSeq
{
    private List<JsElem> elements;


    private JavaList(final List<JsElem> seq)
    {
        elements = seq;
    }

    JavaList()
    {
        this.elements = new ArrayList<>();
    }

    @Override
    public void appendBack(final JsElem elem)
    {
        elements.add(elem);
    }

    @Override
    public void appendFront(final JsElem elem)
    {
        elements.add(0,
                     elem
                    );
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
    @SuppressWarnings("squid:S1206") //equals method never used. Implemented in AbstractJsArray
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
    public void remove(final int index)
    {
        try
        {
            elements.remove(index);
        }
        catch (UnsupportedOperationException e)
        {
            throw UserError.unsupportedOperationOnList(elements.getClass(),
                                                       "remove"
                                                      );
        }

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
    public JavaList tail()
    {
        if (isEmpty()) throw UserError.tailOfEmptyArr();

        return new JavaList(elements.stream()
                                    .skip(1)
                                    .collect(Collectors.toList()));

    }

    @Override
    public void update(final int index,
                       final JsElem ele
                      )
    {
        try
        {
            elements.set(index,
                         ele
                        );
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
    public void add(final int index,
                    final JsElem ele
                   )
    {
        try
        {

            if (index == -1) elements.add(ele);
            else elements.add(index,
                              ele
                             );
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

    public MutableSeq parse(final MutableJsons fac,
                            final JsonParser parser
                           ) throws IOException
    {
        while (true)
        {
            JsonToken elem = parser.nextToken();
            switch (elem.id())
            {
                case JsonTokenId.ID_END_ARRAY:
                    return this;
                case JsonTokenId.ID_STRING:
                    this.elements.add(JsStr.of(parser.getValueAsString()));
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    this.elements.add(JsNumber.of(parser));
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    this.elements.add(JsBigDec.of(parser.getDecimalValue()));
                    break;
                case JsonTokenId.ID_FALSE:
                    this.elements.add(FALSE);
                    break;
                case JsonTokenId.ID_TRUE:
                    this.elements.add(TRUE);
                    break;
                case JsonTokenId.ID_NULL:
                    this.elements.add(NULL);
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    this.elements.add(new MutableJsObj(new JavaMap().parse(fac,
                                                                           parser
                                                                          ),
                                                       fac
                    ));
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    this.elements.add(new MutableJsArray(new JavaList().parse(fac,
                                                                              parser
                                                                             ),
                                                         fac
                    ));
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }
        }

    }

    public JavaList parse(MutableJsons fac,
                          final JsonParser parser,
                          final ParseBuilder.Options options,
                          final JsPath path
                         ) throws IOException
    {
        JsonToken elem;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.nextToken()) != JsonToken.END_ARRAY)
        {
            final JsPath currentPath = path.inc();
            switch (elem.id())
            {
                case JsonTokenId.ID_STRING:
                    JsPair.of(currentPath,
                              JsStr.of(parser.getValueAsString())
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    JsPair.of(currentPath,
                              JsNumber.of(parser)
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    JsPair.of(currentPath,
                              JsBigDec.of(parser.getDecimalValue())
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case JsonTokenId.ID_FALSE:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case JsonTokenId.ID_TRUE:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case JsonTokenId.ID_NULL:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> this.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        this.appendBack(new MutableJsObj(new JavaMap()
                                                         .parse(fac,
                                                                parser,
                                                                options,
                                                                currentPath
                                                               ),
                                                         fac
                        ));
                    }
                    break;

                case JsonTokenId.ID_START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        this.appendBack(new MutableJsArray(new JavaList()
                                                           .parse(fac,
                                                                  parser,
                                                                  options,
                                                                  currentPath.index(-1)
                                                                 ),
                                                           fac
                        ));
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }
        }

        return this;
    }


}
