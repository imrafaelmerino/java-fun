package jsonvalues;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 Represents the location of a first-level element in a json, either a Key in a JsObj or an Index in
 a JsArray.
 */
public interface Position extends Comparable<Position>
{
    /**
     {@inheritDoc}
     */
    int compareTo(final Position o);

    /**
     Casts this position into a Key, throwing an exception if it's an index.
     @return this position as a Key
     @throws UnsupportedOperationException if this position is an Index
     */
    Key asKey();

    /**
     Casts this position into an Index, throwing an exception if it's a Key.
     @return this position as an Index
     @throws UnsupportedOperationException if this position is an Key
     */
    Index asIndex();

    /**
     Returns true if this position is an index.
     @return true if this position is an index
     */
    boolean isIndex();

    /**
     Returns true if this position is an key.
     @return true if this position is an key
     */
    boolean isKey();

    /**
     Returns true if this position is a key and its name tested on a given predicate is true.
     @param predicate the give predicate
     @return true if this position is a key and its name tested on a given predicate is true
     */
    default boolean isKey(final Predicate<String> predicate)
    {
        if (isIndex()) return false;
        return Objects.requireNonNull(predicate)
                      .test(this.asKey().name);
    }

    /**
     Returns true if this position is an index and its index tested on a given predicate is true.
     @param predicate the give predicate
     @return true if this position is an index and its index tested on a given predicate is true
     */
    default boolean isIndex(final IntPredicate predicate)
    {
        if (isKey()) return false;
        return Objects.requireNonNull(predicate)
                      .test(this.asIndex().n);
    }

    /**
     Returns a new value applying pattern matching on the Position type.
     @param keyFn function to apply if this Position is a key
     @param indexFn function to apply if this Position is an index
     @param <T> the type of the object returned
     @return an object of type T
     */
    default <T> T match(final Function<String, T> keyFn,
                        final IntFunction<T> indexFn
                       )
    {

        return isKey() ? Objects.requireNonNull(keyFn)
                                .apply(((Key) this).name) : Objects.requireNonNull(indexFn)
                                                                   .apply(((Index) this).n);
    }
}
