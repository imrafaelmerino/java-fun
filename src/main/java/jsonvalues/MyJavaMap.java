package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static jsonvalues.MyConstants.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MyJsParser.Event.END_OBJECT;

class MyJavaMap implements MyMap<MyJavaMap>
{

    private java.util.Map<String, JsElem> elements;

    MyJavaMap(final java.util.Map<String, JsElem> elements)
    {
        this.elements = elements;
    }

    MyJavaMap()
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
    public MyJavaMap remove(final String key)
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
        if (elements.isEmpty()) return MyConstants.EMPTY_OBJ_AS_STR;

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
    public MyJavaMap tail(final String head)
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty map");
        final java.util.Map<String, JsElem> tail = elements.keySet()
                                                           .stream()
                                                           .filter(key -> !key.equals(head))
                                                           .collect(Collectors.toMap(Function.identity(),
                                                                                     (@KeyFor("elements") String key) -> elements.get(key)
                                                                                    )
                                                                   );
        return new MyJavaMap(tail);

    }

    @Override
    public MyJavaMap update(final String key,
                            final JsElem je
                           )
    {
        elements.put(key,
                     je
                    );
        return this;
    }

    @Override
    public MyJavaMap updateAll(final java.util.Map<String, JsElem> map)
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

    void parse(final MyJsParser parser) throws MalformedJson
    {
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            MyJsParser.Event elem = parser.next();
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
                    final MyJavaMap obj = new MyJavaMap();
                    obj.parse(parser);
                    this.update(key,
                                new MyMutableJsObj(obj)
                               );
                    break;
                case START_ARRAY:
                    final MyJavaVector arr = new MyJavaVector();
                    arr.parse(parser);
                    this.update(key,
                                new MyMutableJsArray(arr)
                               );
                    break;

            }


        }
    }

    void parse(final MyJsParser parser,
               final ParseOptions.Options options,
               final JsPath path
              ) throws MalformedJson
    {
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            MyJsParser.Event elem = parser.next();
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
                        final MyJavaMap obj = new MyJavaMap();
                        obj.parse(parser,
                                  options,
                                  currentPath
                                 );
                        this.update(key,
                                    new MyMutableJsObj(obj)
                                   );
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaVector arr = new MyJavaVector();
                        arr.parse(parser,
                                  options,
                                  currentPath.index(-1)
                                 );
                        this.update(key,
                                    new MyMutableJsArray(arr)
                                   );
                    }
                    break;
            }


        }
    }
}
