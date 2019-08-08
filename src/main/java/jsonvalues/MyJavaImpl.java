package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static jsonvalues.Constants.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.END_ARRAY;
import static jsonvalues.JsParser.Event.END_OBJECT;

class MyJavaImpl
{
    private MyJavaImpl()
    {
    }

    static class Map implements MyMap<Map>
    {

        private java.util.Map<String, JsElem> elements;

        Map(final java.util.Map<String, JsElem> elements)
        {
            this.elements = elements;
        }

        Map()
        {
            this.elements = new HashMap<>();
        }

        @Override
        public Iterator<java.util.Map.Entry<String, JsElem>> iterator()
        {
            return elements.entrySet()
                           .iterator();
        }

        @Override
        public boolean contains(final String key)
        {
            return elements.containsKey(key);
        }

        @Override
        @SuppressWarnings({"cast", "squid:S1905"}) //// in return checkerframework doesn't complain and does its job!
        public Set<String> fields()
        {
            return (Set<String>) elements.keySet();
        }

        @Override
        public JsElem get(final String key)
        {
            if (!elements.containsKey(key)) throw new NoSuchElementException("key " + key + " not found");
            return elements.get(key);
        }

        @Override
        public Optional<JsElem> getOptional(final String key)
        {
            return Optional.ofNullable(elements.get(key));
        }

        @Override
        public java.util.Map.Entry<String, JsElem> head()
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("head of empty map");
            return elements.keySet()
                           .stream()
                           .findFirst()
                           .map(key -> new AbstractMap.SimpleEntry<>(key,
                                                                     elements.get(key)
                           ))
                           .orElseThrow(() -> new UnsupportedOperationException("Map not empty without a key!"));
        }

        @Override
        public boolean isEmpty()
        {
            return elements.isEmpty();
        }


        @Override
        public Map remove(final String key)
        {
            elements.remove(key);
            return this;
        }

        @Override
        public int size()
        {
            return elements.size();
        }

        @Override
        public String toString()
        {
            if (elements.isEmpty()) return Constants.EMPTY_OBJ_AS_STR;

            return elements.keySet()
                           .stream()
                           .map(key -> String.format("\"%s\":%s",
                                                     key,
                                                     elements.get(key)
                                                    )
                               )
                           .collect(Collectors.joining(COMMA,
                                                       OPEN_CURLY,
                                                       CLOSE_CURLY
                                                      )
                                   );

        }

        @Override
        public Map tail(final String head)
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty map");
            final java.util.Map<String, JsElem> tail = elements.keySet()
                                                               .stream()
                                                               .filter(key -> !key.equals(head))
                                                               .collect(Collectors.toMap(Function.identity(),
                                                                                         (@KeyFor("elements") String key) -> elements.get(key)
                                                                                        )
                                                                       );
            return new Map(tail);

        }

        @Override
        public Map update(final String key,
                          final JsElem je
                         )
        {
            elements.put(key,
                         je
                        );
            return this;
        }

        @Override
        public Map updateAll(final java.util.Map<String, JsElem> map)
        {
            elements.putAll(map);
            return this;
        }

        @Override
        public int hashCode()
        {
            return elements.hashCode();

        }

        @Override
        public boolean equals(final @Nullable Object obj)
        {
            return this.eq(obj);
        }

        void parse(final JsParser parser) throws MalformedJson
        {
            while (parser.next() != END_OBJECT)
            {
                final String key = parser.getString();
                JsParser.Event elem = parser.next();
                assert elem != null;
                switch (elem)
                {
                    case VALUE_STRING:
                        this.update(key,
                                    parser.getJsString()
                                   );
                        break;
                    case VALUE_NUMBER:
                        this.update(key,
                                    parser.getJsNumber()
                                   );
                        break;
                    case VALUE_FALSE:
                        this.update(key,
                                    FALSE
                                   );
                        break;
                    case VALUE_TRUE:
                        this.update(key,
                                    TRUE
                                   );
                        break;
                    case VALUE_NULL:
                        this.update(key,
                                    NULL
                                   );
                        break;
                    case START_OBJECT:
                        final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                        obj.parse(parser);
                        this.update(key,
                                    new JsObjMutable(obj)
                                   );
                        break;
                    case START_ARRAY:
                        final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                        arr.parse(parser);
                        this.update(key,
                                    new JsArrayMutable(arr)
                                   );
                        break;

                }


            }
        }

        void parse(final JsParser parser,
                   final ParseOptions.Options options,
                   final JsPath path
                  ) throws MalformedJson
        {
            final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
            while (parser.next() != END_OBJECT)
            {
                final String key = options.keyMap.apply(parser.getString());
                final JsPath currentPath = path.key(key);
                JsParser.Event elem = parser.next();
                assert elem != null;
                switch (elem)
                {
                    case VALUE_STRING:
                        JsPair.of(currentPath,
                                  parser.getJsString()
                                 )
                              .consumeIf(condition,
                                         p -> this.update(key,
                                                          options.elemMap.apply(p)
                                                         )
                                        );

                        break;
                    case VALUE_NUMBER:
                        JsPair.of(currentPath,
                                  parser.getJsNumber()
                                 )
                              .consumeIf(condition,
                                         p -> this.update(key,
                                                          options.elemMap.apply(p)
                                                         )
                                        );

                        break;
                    case VALUE_FALSE:
                        JsPair.of(currentPath,
                                  FALSE
                                 )
                              .consumeIf(condition,
                                         p -> this.update(key,
                                                          options.elemMap
                                                          .apply(p)
                                                         )
                                        );

                        break;
                    case VALUE_TRUE:
                        JsPair.of(currentPath,
                                  TRUE
                                 )
                              .consumeIf(condition,
                                         p -> this.update(key,
                                                          options.elemMap
                                                          .apply(p)
                                                         )
                                        );

                        break;
                    case VALUE_NULL:
                        JsPair.of(currentPath,
                                  NULL
                                 )
                              .consumeIf(condition,
                                         p -> this.update(key,
                                                          options.elemMap
                                                          .apply(p)
                                                         )
                                        );

                        break;
                    case START_OBJECT:
                        if (options.keyFilter.test(currentPath))
                        {
                            final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                            obj.parse(parser,
                                      options,
                                      currentPath
                                     );
                            this.update(key,
                                        new JsObjMutable(obj)
                                       );
                        }
                        break;
                    case START_ARRAY:
                        if (options.keyFilter.test(currentPath))
                        {
                            final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                            arr.parse(parser,
                                      options,
                                      currentPath.index(-1)
                                     );
                            this.update(key,
                                        new JsArrayMutable(arr)
                                       );
                        }
                        break;
                }


            }
        }
    }

    static class Vector implements MyVector<Vector>
    {

        private List<JsElem> elements;


        Vector(final List<JsElem> pvector)
        {
            elements = pvector;

        }

        Vector()
        {
            this.elements = new ArrayList<>();
        }


        @Override
        public Vector add(final Collection<? extends JsElem> list)
        {
            elements.addAll(list);
            return this;
        }

        @Override
        public Vector add(final Vector list)
        {
            elements.addAll(list.elements);
            return this;
        }

        @Override
        public Vector appendBack(final JsElem elem)
        {
            elements.add(elem);
            return this;
        }

        @Override
        public Vector appendFront(final JsElem elem)
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
            if (isEmpty()) throw new UnsupportedOperationException("head of empty vector");
            return elements.get(0);
        }

        @Override
        public Vector init()
        {
            if (isEmpty()) throw new UnsupportedOperationException("init of empty vector");
            return new Vector(IntStream.range(0,
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
            if (isEmpty()) throw new UnsupportedOperationException("last of empty vector");
            return elements.get(size() - 1);
        }

        @Override
        public String toString()
        {
            if (elements.isEmpty()) return Constants.EMPTY_ARR_AS_STR;

            return elements.stream()
                           .map(JsElem::toString)
                           .collect(Collectors.joining(COMMA,
                                                       Constants.OPEN_BRACKET,
                                                       Constants.CLOSE_BRACKET
                                                      ));
        }

        @Override
        public Vector remove(final int index)
        {
            elements.remove(index);
            return this;
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
        public Vector tail()
        {
            if (isEmpty()) throw new UnsupportedOperationException("tail of empty vector");

            return new Vector(elements.stream()
                                      .skip(1)
                                      .collect(Collectors.toList()));

        }

        @Override
        public Vector update(final int index,
                             final JsElem ele
                            )
        {
            elements.set(index,
                         ele
                        );
            return this;
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
                        final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                        obj.parse(parser);
                        this.appendBack(new JsObjMutable(obj));
                        break;
                    case START_ARRAY:
                        final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                        arr.parse(parser);
                        this.appendBack(new JsArrayMutable(arr));
                        break;
                }
            }
        }

        void parse(final JsParser parser,
                   final ParseOptions.Options options,
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
                            final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                            obj.parse(parser,
                                      options,
                                      currentPath
                                     );
                            this.appendBack(new JsObjMutable(obj));
                        }
                        break;

                    case START_ARRAY:
                        if (options.keyFilter.test(currentPath))
                        {
                            final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                            arr.parse(parser,
                                      options,
                                      currentPath.index(-1)
                                     );

                            this.appendBack(new JsArrayMutable(arr));
                        }
                        break;
                }
            }
        }
    }
}
