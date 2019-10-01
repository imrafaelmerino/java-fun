package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.function.Predicate;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

/**
 Represents an immutable data structure where elements of a JsArray are stored. Each immutable Json factory {@link ImmutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.ImmutableJsons#withSeq(Class)}.
 The default immutable implementation that {@link Jsons#immutable} uses is the <a href="https://www.scala-lang.org/api/2.12.0/scala/collection/immutable/Vector.html">immutable Scala Vector</a>.
 */
public interface ImmutableSeq extends MySeq<ImmutableSeq>
{


    /**
     appends the element to the front of the seq
     @param elem the given element
     @return a seq
     */
    ImmutableSeq appendFront(final JsElem elem);

    /**
     appends the element to the back of the seq
     @param elem the given element
     @return a seq
     */
    ImmutableSeq appendBack(final JsElem elem);

    /**
     updates the element located at the index with a new element. It will be called by the library only
     if the index exists
     @param index the given index
     @param ele the given element
     @return an updated seq
     */
    ImmutableSeq update(final int index,
                        final JsElem ele
                       );

    /**
     adds an element at the index, shifting elements at greater or equal indexes one position to the right.
     It's called by the library only if the index exists
     @param index the given index
     @param ele the given element
     @return a seq
     */
    ImmutableSeq add(final int index,
                     final JsElem ele
                    );

    /**
     removes the element located at the index. It will be called by the library only
     if the index exists
     @param index the given index
     @return a seq with the element located at the index removed
     */
    ImmutableSeq remove(final int index);


    default ImmutableSeq parse(final ImmutableJsons fac,
                               final JsonParser parser
                              ) throws IOException
    {
        JsonToken elem;
        ImmutableSeq newRoot = this;
        while ((elem = parser.nextToken()) != JsonToken.END_ARRAY)
        {
            switch (elem.id())
            {
                case 1:
                    final ImmutableMap newObj = fac.object.emptyMap.parse(fac,
                                                                          parser
                                                                         );
                    newRoot = newRoot.appendBack(new ImmutableJsObj(newObj,
                                                                    fac
                    ));
                    break;
                case 3:
                    final ImmutableSeq newSeq = fac.array.emptySeq.parse(fac,
                                                                         parser
                                                                        );
                    newRoot = newRoot.appendBack(new ImmutableJsArray(newSeq,
                                                                      fac
                    ));
                    break;
                case 6:
                    newRoot = newRoot.appendBack(JsStr.of(parser.getValueAsString()));
                    break;
                case 7:
                    newRoot = newRoot.appendBack(JsNumber.of(parser));
                    break;
                case 8:
                    newRoot = newRoot.appendBack(JsBigDec.of(parser.getDecimalValue()));
                    break;
                case 9:
                    newRoot = newRoot.appendBack(TRUE);
                    break;
                case 10:
                    newRoot = newRoot.appendBack(FALSE);
                    break;
                case 11:
                    newRoot = newRoot.appendBack(NULL);
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());
            }
        }
        return newRoot;
    }

    default ImmutableSeq parse(final ImmutableJsons fac,
                               final JsonParser parser,
                               final ParseBuilder.Options options,
                               final JsPath path
                              ) throws IOException
    {
        JsonToken elem;
        ImmutableSeq newRoot = this;
        JsPair pair;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.nextToken()) != JsonToken.END_ARRAY)
        {
            assert elem != null;
            final JsPath currentPath = path.inc();
            switch (elem.id())
            {
                case 6:

                    pair = JsPair.of(currentPath,
                                     JsStr.of(parser.getValueAsString())
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case 7:

                    pair = JsPair.of(currentPath,
                                     JsNumber.of(parser)
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case 8:

                    pair = JsPair.of(currentPath,
                                     JsBigDec.of(parser.getDecimalValue())
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case 9:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case 10:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case 11:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case 1:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new ImmutableJsObj(fac.object.emptyMap.parse(fac,
                                                                                                  parser,
                                                                                                  options,
                                                                                                  currentPath
                                                                                                 ),
                                                                        fac
                                                     )
                                                    );
                    }
                    break;
                case 3:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new ImmutableJsArray(fac.array.emptySeq.parse(fac,
                                                                                                   parser,
                                                                                                   options,
                                                                                                   currentPath.index(-1)
                                                                                                  ),
                                                                          fac
                                                     )
                                                    );
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());


            }
        }
        return newRoot;
    }



}
