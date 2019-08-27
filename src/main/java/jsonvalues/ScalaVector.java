package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.Vector;
import scala.collection.mutable.Builder;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.END_ARRAY;

final class ScalaVector implements MyVector<ScalaVector>
{

    static final Vector<JsElem> EMPTY_VECTOR = new scala.collection.immutable.Vector<>(0,
                                                                                       0,
                                                                                       0
    );
    static final CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>> bf = new CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>>()
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

    static final ScalaVector EMPTY = new ScalaVector(EMPTY_VECTOR);
    private final Vector<JsElem> vector;


    ScalaVector(final Vector<JsElem> vector)
    {
        this.vector = vector;
    }

    @Override
    public ScalaVector add(final Collection<? extends JsElem> list)
    {
        Vector<JsElem> r = this.vector;
        for (final JsElem jsElem : list) r = r.appendBack(jsElem);
        return new ScalaVector(r);
    }


    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaVector add(final ScalaVector that)
    {
        return new ScalaVector(vector.$plus$plus(that.vector,
                                                 bf
                                                ));
    }

    @Override
    public ScalaVector appendBack(final JsElem elem)
    {
        return new ScalaVector(vector.appendBack(elem));
    }

    @Override
    public ScalaVector appendFront(final JsElem elem)
    {
        return new ScalaVector(vector.appendFront(elem));
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return vector.contains(e);
    }

    @Override
    public JsElem get(final int index)
    {
        return vector.apply(index);
    }

    @Override
    public int hashCode()
    {
        return vector.hashCode();
    }

    @Override
    public JsElem head()
    {
        if (this.isEmpty()) throw UserError.headOfEmptyArr();

        return vector.head();
    }

    @Override
    public ScalaVector init()
    {
        if (this.isEmpty()) throw UserError.initOfEmptyArr();

        return new ScalaVector(vector.init());
    }

    @Override
    public boolean isEmpty()
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
    public JsElem last()
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
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaVector remove(final int index)
    {
        if (index == 0) return new ScalaVector(vector.tail());
        if (index == vector.size() - 1) return new ScalaVector(vector.init());

        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaVector(tuple._1.$plus$plus(tuple._2.tail(),
                                                   bf
                                                  ));
    }

    @Override
    public ScalaVector add(final int index,
                           final JsElem ele
                          )
    {

        if (index == 0) return new ScalaVector(vector.appendFront(ele));
        if (index == -1 || index == vector.size()) return new ScalaVector(vector.appendBack(ele));
        if (index < -1 || index > vector.size()) throw UserError.indexOutOfBounds(vector.size(),
                                                                                  index,
                                                                                  "add"
                                                                                 );
        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaVector(tuple._1.appendBack(ele)
                                       .$plus$plus(tuple._2,
                                                     bf
                                                    ));
    }

    @Override
    public int size()
    {
        return vector.size();
    }

    @Override
    public Stream<JsElem> stream()
    {
        return JavaConverters.seqAsJavaList(vector)
                             .stream();
    }

    @Override
    public ScalaVector tail()
    {
        if (this.isEmpty()) throw UserError.tailOfEmptyArr();

        return new ScalaVector(vector.tail());
    }

    @Override
    public ScalaVector update(final int index,
                              final JsElem ele
                             )
    {
        return new ScalaVector(vector.updateAt(index,
                                               ele
                                              ));
    }


    @Override
    public boolean equals(final @Nullable Object that)
    {
        return this.eq(that);
    }

    ScalaVector parse(final JsParser parser) throws MalformedJson
    {
        JsParser.Event elem;
        ScalaVector newRoot = this;
        while ((elem = parser.next()) != END_ARRAY)
        {
            switch (elem)
            {
                case VALUE_STRING:
                    newRoot = newRoot.appendBack(parser.getJsString());
                    break;
                case VALUE_NUMBER:
                    newRoot = newRoot.appendBack(parser.getJsNumber());
                    break;
                case VALUE_FALSE:
                    newRoot = newRoot.appendBack(FALSE);
                    break;
                case VALUE_TRUE:
                    newRoot = newRoot.appendBack(TRUE);
                    break;
                case VALUE_NULL:
                    newRoot = newRoot.appendBack(NULL);
                    break;
                case START_OBJECT:
                    final ScalaMap newObj = ScalaMap.EMPTY.parse(parser);
                    newRoot = newRoot.appendBack(new ImmutableJsObj(newObj));
                    break;
                case START_ARRAY:
                    final ScalaVector newVector = EMPTY.parse(parser);
                    newRoot = newRoot.appendBack(new ImmutableJsArray(newVector));
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());
            }
        }
        return newRoot;

    }

    ScalaVector parse(final JsParser parser,
                      final ParseBuilder.Options options,
                      final JsPath path
                     ) throws MalformedJson
    {
        JsParser.Event elem;
        ScalaVector newRoot = this;
        JsPair pair;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            assert elem != null;
            final JsPath currentPath = path.inc();
            switch (elem)
            {
                case VALUE_STRING:

                    pair = JsPair.of(currentPath,
                                     parser.getJsString()
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case VALUE_NUMBER:
                    pair = JsPair.of(currentPath,
                                     parser.getJsNumber()
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;


                    break;

                case VALUE_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case VALUE_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case VALUE_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new ImmutableJsObj(ScalaMap.EMPTY.parse(parser,
                                                                                             options,
                                                                                             currentPath
                                                                                            )));
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new ImmutableJsArray(EMPTY.parse(parser,
                                                                                      options,
                                                                                      currentPath.index(-1)
                                                                                     )));
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());


            }
        }
        return newRoot;
    }

}
