package jsonvalues;

/**
 Represents an immutable data structure where pairs of a JsObj are stored.
 */
abstract class ImmutableMap extends MyMap<ImmutableMap>
{

    /**
     removes the key associated with this map. It will be called by the library only if the key exists
     @param key the given key
     @return a map with the key removed
     */
    abstract ImmutableMap remove(final String key);

    /**
     updates the element associated with the key with a new element. It will be called by the library only if the key exists,
     @param key the given key
     @param je the new element
     @return a map with the key element updated
     */
    abstract ImmutableMap update(final String key,
                                 final JsElem je
                                );


}
