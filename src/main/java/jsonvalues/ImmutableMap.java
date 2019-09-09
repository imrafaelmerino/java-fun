package jsonvalues;

/**
 Represents an immutable data structure where pairs of a JsObj are stored. Each immutable Json factory {@link ImmutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.ImmutableJsons#withMap(Class)}.
 The default immutable implementation that {@link Jsons#immutable} uses is the <a href="https://scala-lang.org/files/archive/api/2.12.0/scala/collection/seq/HashMap.html">immutable Scala HashMap</a>.
 */
public interface ImmutableMap extends MyMap<ImmutableMap, ImmutableSeq>
{
}
