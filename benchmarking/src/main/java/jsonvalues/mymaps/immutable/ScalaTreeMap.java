package jsonvalues.mymaps.immutable;

import jsonvalues.JsElem;
import scala.collection.Map;
import scala.collection.immutable.AbstractMap;
import scala.collection.immutable.TreeMap;
import scala.math.Ordering;

public class ScalaTreeMap extends ScalaMap
{

    private static final TreeMap<String, JsElem> EMPTY_HASH_MAP = new TreeMap<>(Ordering.String$.MODULE$);

    public ScalaTreeMap()
    {
        super(EMPTY_HASH_MAP);
    }

    private ScalaTreeMap(final Map<String, JsElem> map)
    {
        super(map);
    }

    @Override
    protected ScalaMap of(final Map<String, JsElem> map)
    {
        return new ScalaTreeMap(map);
    }

    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaMap remove(final String key)
    {
        return of(((TreeMap<String, JsElem>) map).$minus(key));
    }

    public ScalaMap update(final String key,
                           final JsElem e
                          )
    {
        return of((TreeMap<String, JsElem>) (map).updated(key,
                                                              e
                                                             ));
    }

    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaMap tail(String head)
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty");
        final Map<String, JsElem> map = ((TreeMap<String, JsElem>) this.map).$minus(head);
        return of(map);
    }
}
