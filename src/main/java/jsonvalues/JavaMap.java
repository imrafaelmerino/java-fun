package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

final class JavaMap implements MutableMap
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
    public JavaMap remove(final String key)
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
    public JavaMap empty()
    {
        return new JavaMap();
    }

    @Override
    public JavaMap update(final String key,
                          final JsElem je
                         )
    {
        elements.put(key,
                     je
                    );
        return this;
    }

    @Override
    public JavaList emptyArray()
    {
        return new JavaList();
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

}
