package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import org.checkerframework.checker.nullness.qual.KeyFor;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

final class JavaMap extends MutableMap
{

    private java.util.Map<String, JsElem> elements;

    JavaMap(final java.util.Map<String, JsElem> elements)
    {
        this.elements = elements;
    }

    JavaMap()
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
    public Set<String> keys()
    {
        return (Set<String>) elements.keySet();
    }

    @Override
    public JsElem get(final String key)
    {
        if (!elements.containsKey(key)) throw InternalError.keyNotFound(key);
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
        if (this.isEmpty()) throw UserError.headOfEmptyObj();
        return elements.keySet()
                       .stream()
                       .findFirst()
                       .map(key -> new AbstractMap.SimpleEntry<>(key,
                                                                 elements.get(key)
                       ))
                       .orElseThrow(InternalError::notEmptyMapWithoutAKey);
    }

    @Override
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }


    @Override
    public void remove(final String key)
    {
        elements.remove(key);
    }

    @Override
    public int size()
    {
        return elements.size();
    }

    @Override
    public String toString()
    {
        if (elements.isEmpty()) return "{}";

        return elements.keySet()
                       .stream()
                       .map(key -> String.format("\"%s\":%s",
                                                 key,
                                                 elements.get(key)
                                                )
                           )
                       .collect(Collectors.joining(",",
                                                   "{",
                                                   "}"
                                                  )
                               );

    }

    @Override
    public JavaMap tail(final String head)
    {
        if (this.isEmpty()) throw UserError.tailOfEmptyObj();
        final java.util.Map<String, JsElem> tail = elements.keySet()
                                                           .stream()
                                                           .filter(key -> !key.equals(head))
                                                           .collect(Collectors.toMap(Function.identity(),
                                                                                     (@KeyFor("elements") String key) -> elements.get(key)
                                                                                    )
                                                                   );
        return new JavaMap(tail);

    }


    @Override
    public void update(final String key,
                       final JsElem je
                      )
    {
        elements.put(key,
                     je
                    );
    }

    @Override
    public JavaMap copy()
    {
        return new JavaMap(new HashMap<>(elements));
    }

    @Override
    public int hashCode()
    {
        return elements.hashCode();

    }

    public MutableMap parse(final MutableJsons fac,
                            final JsonParser parser
                           ) throws IOException
    {
        String key = parser.nextFieldName();
        for (; key != null; key = parser.nextFieldName())
        {
            JsElem value;
            JsonToken token = parser.nextToken();
            switch (token.id())
            {
                case JsonTokenId.ID_STRING:
                    value = JsStr.of(parser.getValueAsString());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    value = JsNumber.of(parser);
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    value = JsBigDec.of(parser.getDecimalValue());
                    break;
                case JsonTokenId.ID_FALSE:
                    value = FALSE;
                    break;
                case JsonTokenId.ID_TRUE:
                    value = TRUE;
                    break;
                case JsonTokenId.ID_NULL:
                    value = NULL;
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    value = new MutableJsObj(new JavaMap().parse(fac,
                                                                 parser
                                                                ),
                                             fac
                    );

                    break;
                case JsonTokenId.ID_START_ARRAY:
                    value = new MutableJsArray(new JavaList().parse(fac,
                                                                    parser
                                                                   ),
                                               fac

                    );
                    break;
                default:
                    throw InternalError.tokenNotExpected(token.name());

            }
            this.elements.put(key,
                              value
                             );
        }
        return this;
    }

    public MutableMap parse(final MutableJsons fac,
                            final JsonParser parser,
                            final ParseBuilder.Options options,
                            final JsPath path
                           ) throws IOException
    {
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        Map<String, JsElem> root = new HashMap<>();
        while (parser.nextToken() != JsonToken.END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.currentName());
            final JsPath currentPath = path.key(key);
            JsonToken elem = parser.nextToken();
            switch (elem.id())
            {
                case JsonTokenId.ID_STRING:
                    JsPair.of(currentPath,
                              JsStr.of(parser.getValueAsString())
                             )
                          .consumeIf(condition,
                                     p -> root.put(key,
                                                   options.elemMap.apply(p)
                                                  )
                                    );

                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    JsPair.of(currentPath,
                              JsNumber.of(parser)
                             )
                          .consumeIf(condition,
                                     p -> root.put(key,
                                                   options.elemMap.apply(p)
                                                  )
                                    );

                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    JsPair.of(currentPath,
                              JsBigDec.of(parser.getDecimalValue())
                             )
                          .consumeIf(condition,
                                     p -> root.put(key,
                                                   options.elemMap.apply(p)
                                                  )
                                    );

                    break;
                case JsonTokenId.ID_FALSE:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> root.put(key,
                                                   options.elemMap
                                                   .apply(p)
                                                  )
                                    );

                    break;
                case JsonTokenId.ID_TRUE:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> root.put(key,
                                                   options.elemMap
                                                   .apply(p)
                                                  )
                                    );

                    break;
                case JsonTokenId.ID_NULL:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> root.put(key,
                                                   options.elemMap
                                                   .apply(p)
                                                  )
                                    );

                    break;
                case JsonTokenId.ID_START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        root.put(key,
                                 new MutableJsObj(new JavaMap()
                                                  .parse(fac,
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
                        root.put(key,
                                 new MutableJsArray(new JavaList()
                                                    .parse(fac,
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
        return new JavaMap(root);

    }

}
