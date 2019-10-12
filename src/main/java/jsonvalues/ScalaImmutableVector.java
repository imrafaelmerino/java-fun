package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.Vector;
import scala.collection.mutable.Builder;

import java.io.IOException;
import java.util.function.Predicate;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

final class ScalaImmutableVector extends ImmutableSeq
{

    private static final Vector<JsElem> EMPTY_VECTOR = new scala.collection.immutable.Vector<>(0,
                                                                                               0,
                                                                                               0
    );
    private static final CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>> bf = new CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>>()
    {
        @Override
        public Builder<JsElem, Vector<JsElem>> apply()
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }

        @Override
        public Builder<JsElem, Vector<JsElem>> apply(final Vector<JsElem> v)
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }
    };

    private final Vector<JsElem> vector;

    ScalaImmutableVector()
    {
        this.vector = EMPTY_VECTOR;
    }

    ScalaImmutableVector(final Vector<JsElem> vector)
    {
        this.vector = vector;
    }

    @Override
    ScalaImmutableVector appendBack(final JsElem elem)
    {
        return new ScalaImmutableVector(vector.appendBack(elem));
    }

    @Override
    ScalaImmutableVector appendFront(final JsElem elem)
    {
        return new ScalaImmutableVector(vector.appendFront(elem));
    }

    @Override
    boolean contains(final JsElem e)
    {
        return vector.contains(e);
    }

    @Override
    JsElem get(final int index)
    {
        return vector.apply(index);
    }

    public int getHashCode()
    {
        return vector.hashCode();
    }

    @Override
    JsElem head()
    {
        if (this.isEmpty()) throw UserError.headOfEmptyArr();

        return vector.head();
    }

    @Override
    ScalaImmutableVector init()
    {
        if (this.isEmpty()) throw UserError.initOfEmptyArr();

        return new ScalaImmutableVector(vector.init());
    }

    @Override
    boolean isEmpty()
    {
        return vector.isEmpty();
    }

    @Override
    public java.util.Iterator<JsElem> iterator()
    {
        return JavaConverters.asJavaIterator(vector.iterator()
                                                   .toIterator());
    }

    @Override
    JsElem last()
    {
        if (this.isEmpty()) throw UserError.lastOfEmptyArr();

        return vector.last();
    }

    @Override
    public String toString()
    {
        if (vector.isEmpty()) return "[]";

        return vector.mkString("[",
                               ",",
                               "]"
                              );
    }

    @Override
    @SuppressWarnings("squid:S00117")
        // api de scala uses $ to name methods
    ScalaImmutableVector remove(final int index)
    {
        if (index == 0) return new ScalaImmutableVector(vector.tail());
        if (index == vector.size() - 1) return new ScalaImmutableVector(vector.init());

        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaImmutableVector(tuple._1.$plus$plus(tuple._2.tail(),
                                                            bf
                                                           ));
    }

    @Override
    ScalaImmutableVector add(final int index,
                             final JsElem ele
                            )
    {

        if (index == 0) return new ScalaImmutableVector(vector.appendFront(ele));
        if (index == -1 || index == vector.size()) return new ScalaImmutableVector(vector.appendBack(ele));
        if (index < -1 || index > vector.size()) throw UserError.indexOutOfBounds(vector.size(),
                                                                                  index,
                                                                                  "add"
                                                                                 );
        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaImmutableVector(tuple._1.appendBack(ele)
                                                .$plus$plus(tuple._2,
                                                            bf
                                                           ));
    }

    @Override
    int size()
    {
        return vector.size();
    }


    @Override
    ScalaImmutableVector tail()
    {
        if (this.isEmpty()) throw UserError.tailOfEmptyArr();

        return new ScalaImmutableVector(vector.tail());
    }

    @Override
    ScalaImmutableVector update(final int index,
                                final JsElem ele
                               )
    {
        return new ScalaImmutableVector(vector.updateAt(index,
                                                        ele
                                                       ));
    }

    ImmutableSeq parse(final ImmutableJsons fac,
                       final JsonParser parser
                      ) throws IOException
    {
        Vector<JsElem> root = EMPTY_VECTOR;
        while (true)
        {
            JsonToken token = parser.nextToken();
            JsElem elem;
            switch (token.id())
            {
                case JsonTokenId.ID_END_ARRAY:
                    return new ScalaImmutableVector(root);
                case JsonTokenId.ID_START_OBJECT:
                    elem = new ImmutableJsObj(fac.object.emptyMap.parse(fac,
                                                                        parser
                                                                       ),
                                              fac
                    );
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    elem = new ImmutableJsArray(fac.array.emptySeq.parse(fac,
                                                                         parser
                                                                        ),
                                                fac
                    );
                    break;
                case JsonTokenId.ID_STRING:
                    elem = JsStr.of(parser.getValueAsString());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    elem = JsNumber.of(parser);
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    elem = JsBigDec.of(parser.getDecimalValue());
                    break;
                case JsonTokenId.ID_TRUE:
                    elem = TRUE;
                    break;
                case JsonTokenId.ID_FALSE:
                    elem = FALSE;
                    break;
                case JsonTokenId.ID_NULL:
                    elem = NULL;
                    break;
                default:
                    throw InternalError.tokenNotExpected(token.name());
            }
            root = root.appendBack(elem);
        }
    }

    ImmutableSeq parse(final ImmutableJsons fac,
                       final JsonParser parser,
                       final ParseBuilder.Options options,
                       final JsPath path
                      ) throws IOException
    {
        JsonToken elem;
        JsPair pair;
        Vector<JsElem> root = EMPTY_VECTOR;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.nextToken()) != JsonToken.END_ARRAY)
        {
            final JsPath currentPath = path.inc();
            switch (elem.id())
            {
                case JsonTokenId.ID_STRING:

                    pair = JsPair.of(currentPath,
                                     JsStr.of(parser.getValueAsString())
                                    );
                    root = condition.test(pair) ? root.appendBack(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_NUMBER_INT:

                    pair = JsPair.of(currentPath,
                                     JsNumber.of(parser)
                                    );
                    root = condition.test(pair) ? root.appendBack(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:

                    pair = JsPair.of(currentPath,
                                     JsBigDec.of(parser.getDecimalValue())
                                    );
                    root = condition.test(pair) ? root.appendBack(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    root = condition.test(pair) ? root.appendBack(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    root = condition.test(pair) ? root.appendBack(options.elemMap.apply(pair)) : root;
                    break;
                case JsonTokenId.ID_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    root = condition.test(pair) ? root.appendBack(options.elemMap.apply(pair)) : root;
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        root = root.appendBack(new ImmutableJsObj(fac.object.emptyMap.parse(fac,
                                                                                            parser,
                                                                                            options,
                                                                                            currentPath
                                                                                           ),
                                                                  fac
                                               )
                                              );
                    }
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        root = root.appendBack(new ImmutableJsArray(fac.array.emptySeq.parse(fac,
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
        return new ScalaImmutableVector(root);
    }


}
