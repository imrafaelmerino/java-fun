package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.generic.CanBuildFrom;
import scala.collection.mutable.Builder;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MyConstants.*;
import static jsonvalues.MyJsParser.Event.END_ARRAY;

final class MyScalaVector implements MyVector<MyScalaVector>
{

    static final scala.collection.immutable.Vector<JsElem> EMPTY_VECTOR = new scala.collection.immutable.Vector<>(0,
                                                                                                                  0,
                                                                                                                  0
    );
    static final CanBuildFrom<scala.collection.immutable.Vector<JsElem>, JsElem, scala.collection.immutable.Vector<JsElem>> bf = new CanBuildFrom<scala.collection.immutable.Vector<JsElem>, JsElem, scala.collection.immutable.Vector<JsElem>>()
    {
        @Override
        public Builder<JsElem, scala.collection.immutable.Vector<JsElem>> apply()
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }

        @Override
        public Builder<JsElem, scala.collection.immutable.Vector<JsElem>> apply(final scala.collection.immutable.Vector<JsElem> v)
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }
    };

    static final MyScalaVector EMPTY = new MyScalaVector(EMPTY_VECTOR);
    private final scala.collection.immutable.Vector<JsElem> vector;


    MyScalaVector(final scala.collection.immutable.Vector<JsElem> vector)
    {
        this.vector = vector;
    }

    @Override
    public MyScalaVector add(final Collection<? extends JsElem> list)
    {
        scala.collection.immutable.Vector<JsElem> r = this.vector;
        for (final JsElem jsElem : list) r = r.appendBack(jsElem);
        return new MyScalaVector(r);
    }


    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public MyScalaVector add(final MyScalaVector that)
    {
        return new MyScalaVector(vector.$plus$plus(that.vector,
                                                   bf
                                                  ));
    }

    @Override
    public MyScalaVector appendBack(final JsElem elem)
    {
        return new MyScalaVector(vector.appendBack(elem));
    }

    @Override
    public MyScalaVector appendFront(final JsElem elem)
    {
        return new MyScalaVector(vector.appendFront(elem));
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
    public MyScalaVector init()
    {
        if (this.isEmpty()) throw UserError.initOfEmptyArr();

        return new MyScalaVector(vector.init());
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
        if (vector.isEmpty()) return MyConstants.EMPTY_ARR_AS_STR;

        return vector.mkString(OPEN_BRACKET,
                               COMMA,
                               CLOSE_BRACKET
                              );
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public MyScalaVector remove(final int index)
    {


        if (index == 0) return new MyScalaVector(vector.tail());
        if (index == vector.size() - 1) return new MyScalaVector(vector.init());

        Tuple2<scala.collection.immutable.Vector<JsElem>, scala.collection.immutable.Vector<JsElem>> tuple = vector.splitAt(index);


        return new MyScalaVector(tuple._1.init()
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
    public MyScalaVector tail()
    {
        if (this.isEmpty()) throw UserError.tailOfEmptyArr();

        return new MyScalaVector(vector.tail());
    }

    @Override
    public MyScalaVector update(final int index,
                                final JsElem ele
                               )
    {
        return new MyScalaVector(vector.updateAt(index,
                                                 ele
                                                ));
    }

    @Override
    public boolean equals(final @Nullable Object that)
    {
        return this.eq(that);
    }

    MyScalaVector parse(final MyJsParser parser) throws MalformedJson
    {

        MyJsParser.Event elem;
        MyScalaVector newRoot = this;
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
                    final MyScalaMap newObj = MyScalaMap.EMPTY.parse(parser);
                    newRoot = newRoot.appendBack(new MyImmutableJsObj(newObj));
                    break;

                case START_ARRAY:
                    final MyScalaVector newVector = EMPTY.parse(parser);

                    newRoot = newRoot.appendBack(new MyImmutableJsArray(newVector));
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());
            }
        }

        return newRoot;


    }

    MyScalaVector parse(final MyJsParser parser,
                        final ParseOptions.Options options,
                        final JsPath path
                       ) throws MalformedJson
    {
        MyJsParser.Event elem;
        MyScalaVector newRoot = this;
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
                        newRoot = newRoot.appendBack(new MyImmutableJsObj(MyScalaMap.EMPTY.parse(parser,
                                                                                                 options,
                                                                                                 currentPath
                                                                                                )));
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new MyImmutableJsArray(EMPTY.parse(parser,
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