package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.immutable.HashMap;
import scala.runtime.AbstractFunction1;

import java.util.AbstractMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static jsonvalues.MyConstants.COMMA;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MyJsParser.Event.END_OBJECT;

final class MyScalaMap implements MyMap<MyScalaMap>
{
    static final HashMap<String, JsElem> EMPTY_HASH_MAP = new HashMap<>();
    static final MyScalaMap EMPTY = new MyScalaMap();
    private final scala.collection.immutable.Map<String, JsElem> persistentMap;

    MyScalaMap()
    {
        this.persistentMap = EMPTY_HASH_MAP;
    }

    MyScalaMap(final scala.collection.immutable.Map<String, JsElem> map)
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
    public MyScalaMap remove(final String key)
    {
        return new MyScalaMap(((HashMap<String, JsElem>) persistentMap).$minus(key));
    }

    @Override
    public int size()
    {
        return persistentMap.size();
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public MyScalaMap tail(String head)
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty map");
        return new MyScalaMap(((HashMap<String, JsElem>) persistentMap).$minus(head));
    }

    @Override
    public String toString()
    {
        if (persistentMap.isEmpty()) return MyConstants.EMPTY_OBJ_AS_STR;


        return persistentMap.keysIterator()
                            .map(af1(key -> String.format("\"%s\":%s",
                                                          key,
                                                          persistentMap.apply(key)
                                                         )))
                            .mkString(MyConstants.OPEN_CURLY,
                                      COMMA,
                                      MyConstants.CLOSE_CURLY
                                     );
    }

    @Override
    public MyScalaMap update(final String key,
                             final JsElem je
                            )
    {
        return new MyScalaMap(persistentMap.updated(key,
                                                    je
                                                   ));
    }


    @Override
    public MyScalaMap updateAll(final java.util.Map<String, JsElem> map)
    {
        scala.collection.immutable.Map<String, JsElem> newMap = this.persistentMap;
        for (java.util.Map.Entry<String, JsElem> entry : map.entrySet())
            newMap = newMap.updated(entry.getKey(),
                                    entry.getValue()
                                   );
        return new MyScalaMap(newMap);
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        return this.eq(obj);
    }

    MyScalaMap parse(final MyJsParser parser) throws MalformedJson
    {
        MyScalaMap newRoot = this;
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            MyJsParser.Event elem = parser.next();
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
                    final MyScalaMap newObj = EMPTY.parse(parser);
                    newRoot = newRoot.update(key,
                                             new MyImmutableJsObj(newObj)
                                            );
                    break;
                case START_ARRAY:
                    final MyScalaVector newArr = MyScalaVector.EMPTY.parse(parser);
                    newRoot = newRoot.update(key,
                                             new MyImmutableJsArray(newArr)
                                            );
                    break;

            }
        }
        return newRoot;


    }

    MyScalaMap parse(final MyJsParser parser,
                     final ParseOptions.Options options,
                     final JsPath path
                    ) throws MalformedJson
    {

        MyScalaMap newRoot = this;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            MyJsParser.Event elem = parser.next();
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
                                                 new MyImmutableJsObj(EMPTY.parse(parser,
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
                                                 new MyImmutableJsArray(MyScalaVector.EMPTY.parse(parser,
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
