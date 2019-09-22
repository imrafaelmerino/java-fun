package jsonvalues.mymaps.immutable;

import jsonvalues.ImmutableMap;
import jsonvalues.JsElem;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.Map;
import scala.collection.immutable.HashMap;
import scala.jdk.CollectionConverters;
import scala.runtime.AbstractFunction1;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

abstract class ScalaMap implements ImmutableMap
{

    Map<String, JsElem> map;

    ScalaMap(final Map<String, JsElem> map)
    {
        this.map = map;
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


    public boolean contains(final String key)
    {
        return map.contains(key);
    }


    public final Set<String> keys()
    {
        return JavaConverters.setAsJavaSet(map.keys()
                                              .toSet());
//        return CollectionConverters.<String>SetHasAsJava(map.keys()
//                                                            .toSet()).asJava();
    }


    public JsElem get(final String key)
    {
        try
        {
            return map.apply(key);
        }
        catch (NoSuchElementException e)
        {
            throw new UnsupportedOperationException("key not found");
        }
    }


    public Optional<JsElem> getOptional(final String key)
    {
        return map.contains(key) ? Optional.of(map.get(key)
                                                  .get()) : Optional.empty();
    }


    public int hashCode()
    {
        return ((HashMap<String, JsElem>) map).hashCode();
    }


    public java.util.Map.Entry<String, JsElem> head()
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("head of empty");

        final Tuple2<String, JsElem> head = map.head();

        return new java.util.AbstractMap.SimpleEntry<>(head._1,
                                                       head._2
        );
    }


    public boolean isEmpty()
    {
        return map.isEmpty();
    }


    public java.util.Iterator<java.util.Map.Entry<String, JsElem>> iterator()
    {
        final Iterator<Tuple2<String, JsElem>> iterator = map.iterator();
        return JavaConverters.asJavaIterator(iterator.map(it -> new java.util.AbstractMap.SimpleEntry<>(it._1,
                                                                                                        it._2
                                                          )
                                                         ));
    }


    public int size()
    {
        return map.size();
    }


    public String toString()
    {
        if (map.isEmpty()) return "{}";

        return map.keysIterator()
                  .map(af1(key -> String.format("\"%s\":%s",
                                                key,
                                                map.apply(key)
                                               )))
                  .mkString("{",
                            ",",
                            "}"
                           );
    }


    protected abstract ScalaMap of(Map<String, JsElem> map);
}
