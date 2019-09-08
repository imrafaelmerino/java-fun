package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

interface MyMap<M extends MyMap<M, V>, V extends MySeq<V, M>>
{
    /**
     returns true if the map contains the key
     @param key the given key
     @return true if the map contains the key, false if not
     */
    boolean contains(String key);

    /**
     returns the keys of this map
     @return set of keys
     */
    Set<String> keys();

    /**
     return the element associated with the key. It will be called by the library only if the key exists, so
     an error can be thrown if it doesn't
     @param key the given key
     @return the element associated with the key or JsNothing if it doesn't exist
     */
    JsElem get(String key);

    /**
     return the element associated with the key wrapped into an optional. It's called by the library without
     checking the existence of the key.
     @param key the given key
     @return the element associated with the key or Optional.empty() if it doesn't exist
     */
    Optional<JsElem> getOptional(String key);

    /**
     an entry of this map. A map is unordered, so any element could be the head. It's only called
     by the library if the map is not empty.
     @return an entry of this map
     */
    Map.Entry<String, JsElem> head();

    /**
     returns true if this map is empty
     @return true if empty, false otherwise
     */
    boolean isEmpty();

    /**
     returns an iterator of this map.
     @return an iterator of this map
     */
    Iterator<Map.Entry<String, JsElem>> iterator();

    /**
     returns the size of the map
     @return the size of the map
     */
    int size();

    /**
     the tail of the map given a head. It's only called by the library if the map is not empty.
     @param head the element returned by {@link #head()}
     @return the tail of the map given a head
     */
    M tail(String head);

    /**
     It creates an empty map of the same type of this instance
     @return an empty map of the same type
     */
    M empty();

    /**
     removes the key associated with this map. It will be called by the library only if the key exists
     @param key the given key
     @return a map with the key removed
     */
    M remove(String key);

    /**
     updates the element associated with the key with a new element. It will be called by the library only if the key exists,
     @param key the given key
     @param je the new element
     @return a map with the key element updated
     */
    M update(String key,
             JsElem je
            );

    /**
     returns an empty seq. Every data structure defined to model a Json object has its dual to model a Json Array
     and has to be defined when creating a Json factory.
     @return en empty seq of the dual data structure to model JsArray
     */
    V emptyArray();

}
