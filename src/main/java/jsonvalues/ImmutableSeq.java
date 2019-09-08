package jsonvalues;

/**
 Represents an immutable data structure where elements of a JsArray are stored. Each immutable Json factory {@link ImmutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.ImmutableJsons#withSeq(Class)}.
 The default immutable implementation that {@link Jsons#immutable} uses is the <a href="https://www.scala-lang.org/api/2.12.0/scala/collection/immutable/Vector.html">immutable Scala Vector</a>.
 */
public interface ImmutableSeq extends MySeq<ImmutableSeq, ImmutableMap>
{

}
