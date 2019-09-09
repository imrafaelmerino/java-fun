package jsonvalues;

/**
 Represents a mutable data structure where elements of a JsArray are stored. Each mutable Json factory {@link MutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.MutableJsons#withSeq(Class)}.
 The default mutable implementation that {@link Jsons#mutable} uses is the Java {@link java.util.ArrayList}

 */
public interface MutableSeq extends MySeq<MutableSeq, MutableMap>
{
    /**
     creates and returns a copy of this sequence
     @return a new instance
     */
    MutableSeq copy();
}
