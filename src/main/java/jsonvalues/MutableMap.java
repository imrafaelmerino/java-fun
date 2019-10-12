package jsonvalues;

/**
 Represents a mutable data structure where pairs of a JsObj are stored.
 It's an abstract class and not an interface for performance reason. The bytecode instruction invokeinterface
 is slower than invokedinamic, and from the point of view of the design, it's not a public class that
 will be extended by anyone
 */
@SuppressWarnings("squid:S1610")
abstract class MutableMap extends MyMap<MutableMap>

{

    /**
     creates and returns a copy of this map
     @return a new instance
     */
    abstract MutableMap copy();


    /**
     removes the key associated with this map. It will be called by the library only if the key exists
     @param key the given key
     */
    abstract void remove(final String key);

    /**
     updates the element associated with the key with a new element. It will be called by the library only if the key exists,
     @param key the given key
     @param je the new element
     */
    abstract void update(final String key,
                         final JsElem je
                        );


}

