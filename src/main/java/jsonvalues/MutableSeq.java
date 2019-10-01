package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.function.Predicate;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

/**
 Represents a mutable data structure where elements of a JsArray are stored. Each mutable Json factory {@link MutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.MutableJsons#withSeq(Class)}.
 The default mutable implementation that {@link Jsons#mutable} uses is the Java {@link java.util.ArrayList}

 */
public interface MutableSeq extends MySeq<MutableSeq>
{


    /**
     creates and returns a copy of this sequence
     @return a new instance
     */
    MutableSeq copy();

    /**
     appends the element to the front of the seq
     @param elem the given element
     */
    void appendFront(final JsElem elem);

    /**
     appends the element to the back of the seq
     @param elem the given element
     */
    void appendBack(final JsElem elem);

    /**
     updates the element located at the index with a new element. It will be called by the library only
     if the index exists
     @param index the given index
     @param ele the given element
     */
    void update(final int index,
                final JsElem ele
               );

    /**
     adds an element at the index, shifting elements at greater or equal indexes one position to the right.
     It's called by the library only if the index exists
     @param index the given index
     @param ele the given element
     */
    void add(final int index,
             final JsElem ele
            );

    /**
     removes the element located at the index. It will be called by the library only
     if the index exists
     @param index the given index
     */
    void remove(final int index);


    default MutableSeq parse(final MutableJsons fac,
                             final JsonParser parser
                            ) throws IOException
    {
        MutableSeq root = this;
        JsonToken elem;
        while ((elem = parser.nextToken()) != JsonToken.END_ARRAY)
        {
            assert elem != null;
            switch (elem.id())
            {
                case 6:
                    root.appendBack(JsStr.of(parser.getValueAsString()));
                    break;
                case 7:
                    root.appendBack(JsNumber.of(parser));
                    break;
                case 8:
                    root.appendBack(JsBigDec.of(parser.getDecimalValue()));
                    break;
                case 10:
                    root.appendBack(FALSE);
                    break;
                case 9:
                    root.appendBack(TRUE);
                    break;
                case 11:
                    root.appendBack(NULL);
                    break;
                case 1:
                    root.appendBack(new MutableJsObj(fac.object.emptyMap()
                                                               .parse(fac,
                                                                      parser
                                                                     ),
                                                     fac
                    ));
                    break;
                case 3:
                    root.appendBack(new MutableJsArray(fac.array.emptySeq()
                                                                .parse(fac,
                                                                       parser
                                                                      ),
                                                       fac
                    ));
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }
        }

        return root;
    }

    default MutableSeq parse(MutableJsons fac,
                             final JsonParser parser,
                             final ParseBuilder.Options options,
                             final JsPath path
                            ) throws IOException
    {
        JsonToken elem;
        MutableSeq root = this;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.nextToken()) != JsonToken.END_ARRAY)
        {
            assert elem != null;
            final JsPath currentPath = path.inc();
            switch (elem.id())
            {
                case 6:
                    JsPair.of(currentPath,
                              JsStr.of(parser.getValueAsString())
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case 7:
                    JsPair.of(currentPath,
                              JsNumber.of(parser)
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case 8:
                    JsPair.of(currentPath,
                              JsBigDec.of(parser.getDecimalValue())
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case 10:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case 9:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case 11:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case 1:
                    if (options.keyFilter.test(currentPath))
                    {
                        root.appendBack(new MutableJsObj(fac.object.emptyMap()
                                                                   .parse(fac,
                                                                          parser,
                                                                          options,
                                                                          currentPath
                                                                         ),
                                                         fac
                        ));
                    }
                    break;

                case 3:
                    if (options.keyFilter.test(currentPath))
                    {
                        root.appendBack(new MutableJsArray(fac.array.emptySeq()
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

        return root;
    }


}
