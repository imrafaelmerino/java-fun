package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

interface MyMap<M extends MyMap<M, V>, V extends MySeq<V, M>>
{
    boolean contains(String key);

    Set<String> fields();

    JsElem get(String key);

    Optional<JsElem> getOptional(String key);

    Map.Entry<String, JsElem> head();

    boolean isEmpty();

    Iterator<Map.Entry<String, JsElem>> iterator();

    int size();

    M tail(String head);

    M empty();

    M remove(String key);

    M update(String key,
             JsElem je
            );

    M updateAll(java.util.Map<String, JsElem> map);

    V emptyArray();

}
