package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.HashMap;
import scala.collection.mutable.Builder;
import scala.runtime.AbstractFunction1;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static jsonvalues.Constants.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.END_ARRAY;
import static jsonvalues.JsParser.Event.END_OBJECT;

class MyScalaImpl
{
    private MyScalaImpl()
    {
    }

    static final class Map implements MyMap<Map>
    {
        static final HashMap<String, JsElem> EMPTY_HASH_MAP = new HashMap<>();
        static final Map EMPTY = new Map();
        private final scala.collection.immutable.Map<String, JsElem> persistentMap;

        Map()
        {
            this.persistentMap = EMPTY_HASH_MAP;
        }

        Map(final scala.collection.immutable.Map<String, JsElem> map)
        {
            this.persistentMap = map;
        }

        static AbstractFunction1<String, String> af1(UnaryOperator<String> uo)
        {
            return new AbstractFunction1<String, String>()
            {
                @Override
                public String apply(final String str)
                {
                    return uo.apply(str);
                }
            };
        }

        @Override
        public java.util.Iterator<java.util.Map.Entry<String, JsElem>> iterator()
        {
            final Iterator<Tuple2<String, JsElem>> iterator = persistentMap.iterator();
            return JavaConverters.asJavaIterator(iterator.map(it -> new AbstractMap.SimpleEntry<>(it._1,
                                                                                                  it._2
                                                              )
                                                             ));
        }

        @Override
        public boolean contains(final String key)
        {
            return persistentMap.contains(key);
        }


        @Override
        public final Set<String> fields()
        {
            return JavaConverters.setAsJavaSet(persistentMap.keys()
                                                            .toSet());

        }

        @Override
        public JsElem get(final String key)
        {

            return persistentMap.apply(key);
        }

        @Override
        public Optional<JsElem> getOptional(final String key)
        {
            return persistentMap.contains(key) ? Optional.of(persistentMap.get(key)
                                                                          .get()) : Optional.empty();
        }

        @Override
        public int hashCode()
        {
            return ((HashMap<String, JsElem>) persistentMap).hashCode();
        }

        @Override
        public java.util.Map.Entry<String, JsElem> head()
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("head of empty map");

            final Tuple2<String, JsElem> head = persistentMap.head();

            return new AbstractMap.SimpleEntry<>(head._1,
                                                 head._2
            );
        }

        @Override
        public boolean isEmpty()
        {
            return persistentMap.isEmpty();
        }

        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Map remove(final String key)
        {
            return new Map(((HashMap<String, JsElem>) persistentMap).$minus(key));
        }

        @Override
        public int size()
        {
            return persistentMap.size();
        }

        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Map tail(String head)
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty map");
            return new Map(((HashMap<String, JsElem>) persistentMap).$minus(head));
        }

        @Override
        public String toString()
        {
            if (persistentMap.isEmpty()) return Constants.EMPTY_OBJ_AS_STR;


            return persistentMap.keysIterator()
                                .map(af1(key -> String.format("\"%s\":%s",
                                                              key,
                                                              persistentMap.apply(key)
                                                             )))
                                .mkString(Constants.OPEN_CURLY,
                                          COMMA,
                                          Constants.CLOSE_CURLY
                                         );
        }

        @Override
        public Map update(final String key,
                          final JsElem je
                         )
        {
            return new Map(persistentMap.updated(key,
                                                 je
                                                ));
        }


        @Override
        public Map updateAll(final java.util.Map<String, JsElem> map)
        {
            scala.collection.immutable.Map<String, JsElem> newMap = this.persistentMap;
            for (java.util.Map.Entry<String, JsElem> entry : map.entrySet())
                newMap = newMap.updated(entry.getKey(),
                                        entry.getValue()
                                       );
            return new Map(newMap);
        }

        @Override
        public boolean equals(@Nullable Object obj)
        {
            return this.eq(obj);
        }

        MyScalaImpl.Map parse(final JsParser parser) throws MalformedJson
        {
            MyScalaImpl.Map newRoot = this;
            while (parser.next() != END_OBJECT)
            {
                final String key = parser.getString();
                JsParser.Event elem = parser.next();
                assert elem != null;
                switch (elem)
                {
                    case VALUE_STRING:
                        newRoot = newRoot.update(key,
                                                 parser.getJsString()
                                                );
                        break;
                    case VALUE_NUMBER:
                        newRoot = newRoot.update(key,
                                                 parser.getJsNumber()
                                                );
                        break;
                    case VALUE_FALSE:
                        newRoot = newRoot.update(key,
                                                 FALSE
                                                );
                        break;
                    case VALUE_TRUE:
                        newRoot = newRoot.update(key,
                                                 TRUE
                                                );
                        break;
                    case VALUE_NULL:
                        newRoot = newRoot.update(key,
                                                 NULL
                                                );
                        break;
                    case START_OBJECT:
                        final MyScalaImpl.Map newObj = EMPTY.parse(parser);
                        newRoot = newRoot.update(key,
                                                 new JsObjImmutable(newObj)
                                                );
                        break;
                    case START_ARRAY:
                        final MyScalaImpl.Vector newArr = MyScalaImpl.Vector.EMPTY.parse(parser);
                        newRoot = newRoot.update(key,
                                                 new JsArrayImmutable(newArr)
                                                );
                        break;

                }
            }
            return newRoot;


        }

        MyScalaImpl.Map parse(final JsParser parser,
                              final ParseOptions.Options options,
                              final JsPath path
                             ) throws MalformedJson
        {

            MyScalaImpl.Map newRoot = this;
            final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
            while (parser.next() != END_OBJECT)
            {
                final String key = options.keyMap.apply(parser.getString());
                final JsPath currentPath = path.key(key);
                JsParser.Event elem = parser.next();
                final JsPair pair;
                assert elem != null;
                switch (elem)
                {
                    case VALUE_STRING:
                        pair = JsPair.of(currentPath,
                                         parser.getJsString()
                                        );
                        newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                          options.elemMap.apply(pair)
                                                                         ) : newRoot;
                        break;
                    case VALUE_NUMBER:
                        pair = JsPair.of(currentPath,
                                         parser.getJsNumber()
                                        );
                        newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                          options.elemMap.apply(pair)
                                                                         ) : newRoot;
                        break;
                    case VALUE_TRUE:
                        pair = JsPair.of(currentPath,
                                         TRUE
                                        );
                        newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                          options.elemMap.apply(pair)
                                                                         ) : newRoot;
                        break;
                    case VALUE_FALSE:
                        pair = JsPair.of(currentPath,
                                         FALSE
                                        );
                        newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                          options.elemMap.apply(pair)
                                                                         ) : newRoot;
                        break;
                    case VALUE_NULL:
                        pair = JsPair.of(currentPath,
                                         NULL
                                        );
                        newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                          options.elemMap.apply(pair)
                                                                         ) : newRoot;
                        break;

                    case START_OBJECT:
                        if (options.keyFilter.test(currentPath))
                        {
                            newRoot = newRoot.update(key,
                                                     new JsObjImmutable(EMPTY.parse(parser,
                                                                                    options,
                                                                                    currentPath
                                                                                   ))
                                                    );
                        }
                        break;
                    case START_ARRAY:
                        if (options.keyFilter.test(currentPath))
                        {
                            newRoot = newRoot.update(key,
                                                     new JsArrayImmutable(MyScalaImpl.Vector.EMPTY.parse(parser,
                                                                                                         options,
                                                                                                         currentPath.index(-1)
                                                                                                        ))
                                                    );
                        }
                        break;
                }
            }
            return newRoot;
        }
    }

    static final class Vector implements MyVector<Vector>
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

        static final Vector EMPTY = new Vector(EMPTY_VECTOR);
        private final scala.collection.immutable.Vector<JsElem> vector;


        Vector(final scala.collection.immutable.Vector<JsElem> vector)
        {
            this.vector = vector;
        }

        @Override
        public Vector add(final Collection<? extends JsElem> list)
        {
            scala.collection.immutable.Vector<JsElem> r = this.vector;
            for (final JsElem jsElem : list) r = r.appendBack(jsElem);
            return new Vector(r);
        }


        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Vector add(final Vector that)
        {
            return new Vector(vector.$plus$plus(that.vector,
                                                bf
                                               ));
        }

        @Override
        public Vector appendBack(final JsElem elem)
        {
            return new Vector(vector.appendBack(elem));
        }

        @Override
        public Vector appendFront(final JsElem elem)
        {
            return new Vector(vector.appendFront(elem));
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
            return vector.head();
        }

        @Override
        public Vector init()
        {
            return new Vector(vector.init());
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
            return vector.last();
        }

        @Override
        public String toString()
        {
            if (vector.isEmpty()) return Constants.EMPTY_ARR_AS_STR;

            return vector.mkString(OPEN_BRACKET,
                                   COMMA,
                                   CLOSE_BRACKET
                                  );
        }

        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Vector remove(final int index)
        {


            if (index == 0) return new Vector(vector.tail());
            if (index == vector.size() - 1) return new Vector(vector.init());

            Tuple2<scala.collection.immutable.Vector<JsElem>, scala.collection.immutable.Vector<JsElem>> tuple = vector.splitAt(index);


            return new Vector(tuple._1.init()
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
        public Vector tail()
        {
            return new Vector(vector.tail());
        }

        @Override
        public Vector update(final int index,
                             final JsElem ele
                            )
        {
            return new Vector(vector.updateAt(index,
                                              ele
                                             ));
        }

        @Override
        public boolean equals(final @Nullable Object that)
        {
            return this.eq(that);
        }

        MyScalaImpl.Vector parse(final JsParser parser) throws MalformedJson
        {

            JsParser.Event elem;
            MyScalaImpl.Vector newRoot = this;
            while ((elem = parser.next()) != END_ARRAY)
            {
                assert elem != null;
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
                        final MyScalaImpl.Map newObj = MyScalaImpl.Map.EMPTY.parse(parser);
                        newRoot = newRoot.appendBack(new JsObjImmutable(newObj));
                        break;

                    case START_ARRAY:
                        final MyScalaImpl.Vector newVector = EMPTY.parse(parser);

                        newRoot = newRoot.appendBack(new JsArrayImmutable(newVector));
                        break;
                }
            }

            return newRoot;


        }

        MyScalaImpl.Vector parse(final JsParser parser,
                                 final ParseOptions.Options options,
                                 final JsPath path
                                ) throws MalformedJson
        {
            JsParser.Event elem;
            MyScalaImpl.Vector newRoot = this;
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
                            newRoot = newRoot.appendBack(new JsObjImmutable(MyScalaImpl.Map.EMPTY.parse(parser,
                                                                                                        options,
                                                                                                        currentPath
                                                                                                       )));
                        }
                        break;
                    case START_ARRAY:
                        if (options.keyFilter.test(currentPath))
                        {
                            newRoot = newRoot.appendBack(new JsArrayImmutable(EMPTY.parse(parser,
                                                                                          options,
                                                                                          currentPath.index(-1)
                                                                                         )));
                        }

                        break;
                }
            }
            return newRoot;
        }

    }
}
