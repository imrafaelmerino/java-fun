package jsonvalues.mymaps.immutable;

import jsonvalues.JsElem;
import scala.collection.Map;
import scala.collection.immutable.AbstractMap;
import scala.collection.immutable.HashMap;

public final class ScalaHashMap extends ScalaMap
{
    private static final HashMap<String, JsElem> EMPTY_HASH_MAP = new HashMap<>();

    public ScalaHashMap()
    {
        super(EMPTY_HASH_MAP);
    }

    private ScalaHashMap(final AbstractMap<String, JsElem> map)
    {
        super(map);
    }

    @Override
    protected ScalaMap of(final Map<String, JsElem> map)
    {
        return new ScalaHashMap((AbstractMap<String, JsElem>) map);
    }


    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaMap remove(final String key)
    {
        final HashMap<String, JsElem> map =((HashMap<String,JsElem>) this.map).$minus(key);
        return of(map);
    }

    public ScalaMap update(final String key,
                           final JsElem e
                          )
    {
        return of((HashMap<String, JsElem>) (map).updated(key,
                                                              e
                                                             ));
    }

    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaMap tail(String head)
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty");
        final Map<String, JsElem> map = ((HashMap<String, JsElem>) this.map).$minus(head);
        return of(map);
    }
}
