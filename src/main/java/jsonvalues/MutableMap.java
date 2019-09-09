package jsonvalues;

/**
 Represents a mutable data structure where pairs of a JsObj are stored. Each mutable Json factory {@link MutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.MutableJsons#withMap(Class)}.
 The default mutable implementation that {@link Jsons#mutable} uses is the Java {@link java.util.HashMap}
 */
public interface MutableMap extends MyMap<MutableMap, MutableSeq>

{

    /**
     creates and returns a copy of this map
     @return a new instance
     */
    MutableMap copy();
}

