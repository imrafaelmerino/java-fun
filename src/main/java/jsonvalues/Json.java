package jsonvalues;

import com.dslplatform.json.serializers.SerializerException;
import jsonvalues.JsArray.TYPE;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

import static com.dslplatform.json.MyDslJson.INSTANCE;
import static java.util.Objects.requireNonNull;

/**
 <pre>
 Represents a json of type T, where T is the type of the container, either a JsObj or a JsArray.
 A json of any type can be modeled as a set of pairs {@link JsPair}=({@link JsPath}, {@link JsValue}), where:
 - a JsElem is a {@link JsBool} or {@link JsStr} or {@link JsNumber} or {@link JsNull}, or another {@link Json} like {@link JsObj} or {@link JsArray},
 what makes the data structure recursive.
 - a JsPath represents the location of the element in the json.
 For example, the json
 {
 "a":1, "x":{ "c": true, "d":null, e: [false, 1, "hi"] }
 }
 can be seen as the following set:
 Set[(a,1), (x.c,true), (x.d,null), (x.e.0,false), (x.e.1,1), (x.e.2,"hi"), (_,NOTHING)]
 where _, which means any other JsPath, and the special element {@link JsNothing#NOTHING}, makes the
 function {@link #get(JsPath)} total (defined for every possible path). Moreover, inserting JsNothing
 in a json doesn't change the json, which is very convenient when passing functions as parameters to
 put data in:
 //all the logic goes into the supplier{@code
Supplier<JsElem> supplier = ()-> (doesnt-put-anything-condition) ? JsNothing.NOTHING : JsInt.of(2);
json.putIfAbsent(path,supplier)
}
 Another way to see a json is like a stream of pairs, which opens the door to doing all the operations
 that were introduced in Java 8 (map, filter, reduce, etc). For this purpose the methods {@link #streamAll()}
 or {@link #stream()} are provided.
 There are one convention on method names:
 -Methods that are suffixed with underscore traverse the whole json recursively.
 All the methods throw a NullPointerException when any of the params passed in is null. The exception
 <code>UserError</code> is thrown when the user calls a method inappropriately:
 for example calling the method <code>asJsStr</code> in a <code>JsNull</code> instance or calling the
 method head in an empty array, etc. Normally, when that happens, a previous check is missing.
 </pre>

 @param <T> Type of container: either an object or an array
 @author Rafael Merino Garcia
 @see JsObj to work with jsons that are objects
 @see JsArray to work with jsons that are arrays */
public interface Json<T extends Json<T>> extends JsValue {

    /**
     Converts the string representation of this Json to a pretty print version

     @return pretty print version of the string representation of this Json
     */
    default String toPrettyString() {
        return INSTANCE.toPrettyString(this);
    }


    /**
     Returns true if this json contains the given element in the first level.

     @param element the give element JsElem whose presence in this JsArray is to be tested
     @return true if this JsArray contains the  JsElem
     */
    boolean containsValue(final JsValue element);


    /**
     Returns true if an element exists in this json at the given path.

     @param path the JsPath
     @return true if a JsElem exists at the JsPath
     */
    default boolean containsPath(final JsPath path) {
        return get(requireNonNull(path)).isNotNothing();

    }

    /**
     Returns the element located at the given path or {@link JsNothing} if it doesn't exist.

     @param path the JsPath object of the element that will be returned
     @return the JsElem located at the given JsPath or JsNothing if it doesn't exist
     */
    JsValue get(final JsPath path);

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    default boolean equals(final JsValue elem,
                           final TYPE ARRAY_AS
                          ) {
        if (elem == null || getClass() != elem.getClass()) return false;
        if (isObj()) return toJsObj().equals(elem.toJsObj(),
                                             ARRAY_AS
                                            );
        if (isArray()) return toJsArray().equals(elem.toJsArray(),
                                                 ARRAY_AS
                                                );
        return false;

    }

    /**
     Filters the pairs of elements in the first level of this json, removing those that don't ifPredicateElse
     the predicate.

     @param filter the predicate which takes as the input every JsPair in the first level of this json
     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
     @see #filterAllValues(Predicate) how to filter the pair of elements of the whole json and not only the first level
     */
    T filterValues(final Predicate<? super JsPair> filter);

    /**
     Filters all the pairs of elements of this json, removing those that don't ifPredicateElse the predicate.

     @param filter the predicate which takes as the input every JsPair of this json
     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
     @see #filterValues(Predicate) how to filter the pairs of values of only the first level
     */
    T filterAllValues(final Predicate<? super JsPair> filter);

    /**
     Filters the keys in the first level of this json, removing those that don't ifPredicateElse the predicate.

     @param filter the predicate which takes as the input every JsPair in the first level of this json
     @return same this instance if all the keys satisfy the predicate or a new filtered json of the same type T
     @see #filterAllKeys(Predicate) how to filter the keys of the whole json and not only the first level
     */
    T filterKeys(final Predicate<? super JsPair> filter);

    /**
     Filters all the keys of this json, removing those that don't ifPredicateElse the predicate.

     @param filter the predicate which takes as the input every JsPair of this json
     @return same this instance if all the keys satisfy the predicate or a new filtered json of the same type T
     @see #filterKeys(Predicate) how to filter the keys of only the first level
     */
    T filterAllKeys(final Predicate<? super JsPair> filter);

    /**
     Filters the pair of jsons in the first level of this json, removing those that don't ifPredicateElse
     the predicate.

     @param filter the predicate which takes as the input every JsPair in the first level of this json
     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
     @see #filterAllObjs(BiPredicate) how to filter the pair of jsons of the whole json and not only the first level
     */
    T filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter
                );

    /**
     Filters all the pair of jsons of this json, removing those that don't ifPredicateElse the predicate.

     @param filter the predicate which takes as the input every JsPair of this json
     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
     @see #filterObjs(BiPredicate) how to filter the pair of jsons of only the first level
     */
    T filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter);

    /**
     Returns the array located at the given path or null if it doesn't exist or it's not an array.

     @param path the path
     @return the JsArray located at the given JsPath or null
     */
    default JsArray getArray(final JsPath path) {
        return JsArray.prism.getOptional.apply(get(requireNonNull(path)))
                                        .orElse(null);

    }


    /**
     Returns the number located at the given path as a big decimal or null if
     it doesn't exist or it's not a decimal number.

     @param path the path
     @return the number located at the given JsPath or null
     */
    default BigDecimal getBigDec(final JsPath path) {
        return JsBigDec.prism.getOptional.apply(get(requireNonNull(path)))
                                         .orElse(null);

    }


    /**
     Returns the number located at the given path as a big integer or null if it doesn't
     exist or it's not an integral number.

     @param path the path
     @return the BigInteger located at the given JsPath or null
     */
    default BigInteger getBigInt(final JsPath path) {
        return JsBigInt.prism.getOptional.apply(get(requireNonNull(path)))
                                         .orElse(null);

    }

    /**
     Returns the boolean located at the given path or null if it doesn't exist.

     @param path the path
     @return the Boolean located at the given JsPath or null
     */
    default Boolean getBool(final JsPath path) {
        return JsBool.prism.getOptional.apply(get(requireNonNull(path)))
                                       .orElse(null);

    }


    /**
     Returns the decimal number located at the given path as a double or null if it
     doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     the precision of the BigDecimal

     @param path the path
     @return the decimal number located at the given JsPath or null
     */
    default Double getDouble(final JsPath path) {
        return JsDouble.prism.getOptional.apply(get(requireNonNull(path)))
                                         .orElse(null);

    }


    /**
     Returns the integral number located at the given path as an integer or null if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.

     @param path the path
     @return the integral number located at the given JsPath or null
     */
    default Integer getInt(final JsPath path) {
        return JsInt.prism.getOptional.apply(get(requireNonNull(path)))
                                      .orElse(null);

    }


    /**
     Returns the integral number located at the given path as a long or null if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in a long.

     @param path the path
     @return the integral number located at the given JsPath or null
     */
    default Long getLong(final JsPath path) {
        return JsLong.prism.getOptional.apply(get(requireNonNull(path)))
                                       .orElse(null);

    }

    /**
     Returns the object located at the given path or null if it doesn't exist or it's
     not an object.

     @param path the path
     @return the JsObj located at the given JsPath or null
     */
    default JsObj getObj(final JsPath path) {
        return JsObj.prism.getOptional.apply(get(path))
                                      .orElse(null);
    }


    /**
     Returns the string located at the given path or null if it doesn't exist or it's
     not an string.

     @param path the path
     @return the JsStr located at the given path or null
     */
    default String getStr(final JsPath path) {
        return JsStr.prism.getOptional.apply(get(path))
                                      .orElse(null);
    }

    /**
     Declarative way of implementing if(this.isEmpty()) return emptySupplier.get() else return
     nonEmptySupplier.get()

     @param emptySupplier    Supplier that will produce the result if this json is empty
     @param nonemptySupplier Supplier that will produce the result if this json is not empty
     @param <A>              the type of the result
     @return an object of type A
     */
    default <A> A ifEmptyElse(final Supplier<A> emptySupplier,
                              final Supplier<A> nonemptySupplier
                             ) {

        return this.isEmpty() ? requireNonNull(emptySupplier).get() : requireNonNull(nonemptySupplier).get();
    }

    /**
     return true if there's no element in this json

     @return true if empty, false otherwise
     */
    boolean isEmpty();

    /**
     return true if this json it not empty

     @return false if empty, true otherwise
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default T map(final UnaryOperator<T> fn) {
        //this is an instance of T (recursive type)
        @SuppressWarnings("unchecked") T o = fn.apply((T) this);
        return o;
    }

    /**
     Maps the values in the first level of this json.

     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapObjs(BiFunction) to map jsons
     @see #mapKeys(Function) to map keys of json objects
     @see #mapAllValues(Function) to map all the values and not only the first level
     */
    T mapValues(final Function<? super JsPair, ? extends JsValue> fn);


    /**
     Maps all the values of this json.

     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapAllObjs(BiFunction) to map jsons
     @see #mapAllKeys(Function) to map keys of json objects
     @see #mapValues(Function) to map only the first level
     */
    T mapAllValues(final Function<? super JsPair, ? extends JsValue> fn);


    /**
     Maps the keys in the first level of this json.

     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapValues(Function) to map values
     @see #mapObjs(BiFunction) to map jsons
     @see #mapAllKeys(Function) to map all the keys and not only the first level
     */
    T mapKeys(final Function<? super JsPair, String> fn);


    /**
     Maps all the keys of this json.

     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapAllValues(Function) to map values
     @see #mapAllObjs(BiFunction) to map jsons
     @see #mapKeys(Function) to map only the first level
     */
    T mapAllKeys(final Function<? super JsPair, String> fn);


    /**
     Maps the jsons in the first level of this json.

     @param fn the  mapping function
     @return a new mapped json of the same type T
     @see #mapValues(Function) to map values
     @see #mapKeys(Function) to map keys of json objects
     @see #mapAllObjs(BiFunction) to map all the jsons and not only the first level
     */
    T mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn);


    /**
     Maps all the jsons of this json.

     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapAllValues(Function) to map values
     @see #mapAllKeys(Function) to map keys of json objects
     @see #mapObjs(BiFunction) to map only the first level
     */
    T mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn);


    /**
     Inserts the element at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull} empty
     indexes in arrays when necessary.
     <p>
     The same instance is returned when the head of the path is a key and this is an array or the head
     of the path is an index and this is an object or the element is {@link JsNothing}

     @param path    the JsPath object where the element will be inserted at
     @param element the JsElem that will be inserted
     @return the same instance or a new json of the same type T
     */
    T set(final JsPath path,
          final JsValue element);


    /**
     Performs a reduction on the values that satisfy the predicate in the first level of this json. The reduction is performed mapping
     each value with the mapping function and then applying the operator

     @param op        the operator upon two objects of type R
     @param map       the mapping function which produces an object of type R from a JsValue
     @param predicate the predicate that determines what JsValue will be mapped and reduced
     @param <R>       the type of the operands of the operator
     @return an {@link Optional} describing the of of the reduction
     @see #reduceAll(BinaryOperator, Function, Predicate) to apply the reduction in all the Json and not only in the first level
     */
    <R> Optional<R> reduce(final BinaryOperator<R> op,
                           final Function<? super JsPair, R> map,
                           final Predicate<? super JsPair> predicate
                          );

    /**
     Performs a reduction on the values of this json that satisfy the predicate. The reduction is performed mapping
     each value with the mapping function and then applying the operator

     @param op        the operator upon two objects of type R
     @param map       the mapping function which produces an object of type R from a JsValue
     @param predicate the predicate that determines what JsValue will be mapped and reduced
     @param <R>       the type of the operands of the operator
     @return an {@link Optional} describing the result of the reduction
     @see #reduce(BinaryOperator, Function, Predicate) to apply the reduction only in the first level
     */
    <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                              final Function<? super JsPair, R> map,
                              final Predicate<? super JsPair> predicate
                             );

    /**
     Removes the element in this json located at the given path, if it exists, returning the same this
     instance otherwise

     @param path the given JsPath object
     @return a json of the same type T
     */
    T delete(final JsPath path);

    /**
     Returns the number of elements in the first level of this json

     @return the number of elements in the first level of this json
     */
    int size();

    /**
     Returns the number of all the elements in this json

     @return the number of all the elements in this json
     */
    default int sizeAll() {
        return streamAll().mapToInt(p -> 1)
                          .reduce(0,
                                  Integer::sum
                                 );
    }

    /**
     Returns a stream over all the pairs of elements in this json object.

     @return a {@code Stream} over all the JsPairs in this json
     */
    @SuppressWarnings("squid:S00100")
    Stream<JsPair> streamAll();

    default long times(final JsValue e) {
        return stream().filter(p -> p.value.equals(requireNonNull(e)))
                       .count();
    }

    /**
     Returns a stream over the pairs of elements in the first level of this json object.

     @return a {@code Stream} over all the JsPairs in the first level of this json
     */
    Stream<JsPair> stream();

    default long timesAll(final JsValue e) {
        return streamAll().filter(p -> p.value.equals(requireNonNull(e)))
                          .count();
    }

    /**
     Serializes this Json into the given output stream, no returning anything

     @param ouputstream the output stream
     */
    default void serialize(final OutputStream ouputstream) throws SerializerException {
        INSTANCE.serialize(this,
                           requireNonNull(ouputstream)
                          );
    }

    /**
     Serialize this Json into an array of bytes. When possible,
     it's more efficient to work on byte level that with strings

     @return this Json serialized into an array of bytes
     */
    default byte[] serialize() throws SerializerException {
        return INSTANCE.serialize(this);
    }


}
