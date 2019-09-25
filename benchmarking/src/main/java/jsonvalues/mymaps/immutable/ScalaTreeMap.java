package jsonvalues.mymaps.immutable;

import jsonvalues.ImmutableMap;
import jsonvalues.JsElem;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.immutable.TreeMap;
import scala.math.Ordering;
import scala.runtime.AbstractFunction1;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

public class ScalaTreeMap implements ImmutableMap
{

    private static AbstractFunction1<String, String> af1(UnaryOperator<String> uo)
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


    private final TreeMap<String, JsElem> map;

    private static final TreeMap<String, JsElem> EMPTY_HASH_MAP = new TreeMap<>(Ordering.String$.MODULE$);

    public ScalaTreeMap()
    {
        map = EMPTY_HASH_MAP;
    }

    private ScalaTreeMap(final TreeMap<String, JsElem> map)
    {
        this.map = map;
    }


    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaTreeMap remove(final String key)
    {
        return new ScalaTreeMap(((TreeMap<String, JsElem>) map).$minus(key));
    }

    public ScalaTreeMap update(final String key,
                               final JsElem e
                              )
    {
        return new ScalaTreeMap((TreeMap<String, JsElem>) (map).updated(key,
                                                                        e
                                                                       ));
    }

    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaTreeMap tail(String head)
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty");
        return new ScalaTreeMap(((TreeMap<String, JsElem>) this.map).$minus(head));
    }

    public boolean contains(final String key)
    {
        return map.contains(key);
    }


    public final Set<String> keys()
    {
        return JavaConverters.setAsJavaSet(map.keys()
                                              .toSet());
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
        return map.hashCode();
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

}
