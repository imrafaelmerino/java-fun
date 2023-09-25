package jsonvalues;


import fun.optic.Prism;
import jsonvalues.JsArray.TYPE;
import jsonvalues.spec.JsIO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static jsonvalues.JsArray.streamOfArr;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MatchExp.ifNothingElse;

/**
 * Represents a JSON object in a lightweight, immutable, and functional manner. A `JsObj` consists of a collection
 * of key-value pairs, where the keys are strings and the values can be various JSON data types such as strings,
 * numbers, booleans, arrays, or nested objects.
 *
 * <p>Instances of this class are immutable and persistent, meaning that once created, the contents of a `JsObj` cannot
 * be modified. Instead, operations on `JsObj` instances return new `JsObj` instances with the desired changes,
 * leaving the original object unchanged. This immutability makes `JsObj` a persistent data structure.
 *
 * <p>This class provides methods to access and manipulate JSON data stored within it, including querying values by key,
 * performing intersections with other `JsObj` instances, and converting the `JsObj` to a JSON string.
 *
 * <p>It also offers a convenient and functional way to work with JSON data in Java, making it easy to create and
 * manipulate JSON objects programmatically.
 *
 * <p>Example usage:
 * <pre>{@code
 * // Create a JsObj with key-value pairs
 * JsObj person = JsObj.of("name", "Alice", "age", 30, "city", "New York");
 *
 * // Access values by key
 * String name = person.getStr("name"); // "Alice"
 * int age = person.getInt("age");      // 30
 *
 * // Create a new JsObj with additional data
 * JsObj updatedPerson = person.set("isStudent", true);
 *
 * // Perform an intersection with another JsObj
 * JsObj otherPerson = JsObj.of("name", "Alice", "city", "London");
 * JsObj commonData = person.intersection(otherPerson, JsObj.TYPE.SET);
 * }</pre>
 */
public non-sealed class JsObj implements Json<JsObj>, Iterable<JsObjPair> {
    /**
     * Using the {@code lens} field, you can apply various optical operations to {@code JsObj} instances
     * in a functional and declarative manner, making it easier to modify or query the content of a
     * {@code JsObj}
     */
    public static final JsOptics.JsObjLenses lens = JsOptics.obj.lens;
    /**
     * <p>Using the {@code optional} field, you can apply optional optics to {@code JsObj} instances in a functional
     * and declarative manner. These optics allow you to safely retrieve and modify fields that may or may not be present
     * in the {@code JsObj}, helping you avoid null pointer exceptions and handle optional values gracefully.
     */
    public static final JsOptics.JsObjOptional optional = JsOptics.obj.optional;
    /**
     * A Prism that allows you to view a {@code JsValue} as a {@code JsObj} if it represents an object, or as an empty optional if it's not an object.
     *
     * <p>Prisms are a functional optic that can be used to safely extract a specific type from a more general type, or to construct a more general type from a specific type. In this case, the {@code prism} field allows you to view a {@code JsValue} as a {@code JsObj} only if it represents an object, and returns an empty optional if the {@code JsValue} is not an object.
     *
     * <p>This Prism is useful for safely handling and processing JSON data, ensuring that you only work with objects when you expect them to be objects.
     */
    public static final Prism<JsValue, JsObj> prism =
            new Prism<>(s -> s.isObj() ?
                    Optional.of(s.toJsObj()) :
                    Optional.empty(),
                        o -> o
            );
    /**
     * the empty Json Object
     */
    static final JsObj EMPTY = new JsObj(HashMap.empty());
    @SuppressWarnings("squid:S3008")
    private static final JsPath EMPTY_PATH = JsPath.empty();
    private final HashMap map;
    private volatile int hashcode;
    @SuppressWarnings("squid:S3077")
    private volatile String str;

    JsObj(final HashMap myMap) {
        this.map = myMap;
    }

    /**
     * Returns the singleton empty JSON object
     *
     * @return the singleton empty JSON object
     */
    public static JsObj empty() {
        return EMPTY;
    }

    /**
     * Static factory method to create one-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    public static JsObj of(final String key,
                           final JsValue el
                          ) {

        return JsObj.EMPTY.set(JsPath.empty()
                                     .key(requireNonNull(key)),
                               el
                              );
    }

    /**
     * Static factory method to create one-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    public static JsObj of(final JsPath path,
                           final JsValue el
                          ) {

        return JsObj.EMPTY.set(requireNonNull(path),
                               requireNonNull(el)
                              );
    }

    /**
     * Static factory method to create two-element JSON object
     *
     * @return an immutable and persistent JSON object
     */

    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2
                          ) {

        return of(key1,
                  el1
                 ).set(JsPath.empty()
                             .key(requireNonNull(key2)),
                       el2
                      );
    }

    /**
     * Static factory method to create two-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2
                          ) {

        return of(path1,
                  el1
                 )
                .set(requireNonNull(path2),
                     requireNonNull(el2)
                    );
    }

    /**
     * Static factory method to create three-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3
                          ) {
        return of(key1,
                  el1,
                  key2,
                  el2
                 ).set(JsPath.empty()
                             .key(requireNonNull(key3)),
                       el3
                      );
    }

    /**
     * Static factory method to create three-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2
                 ).set(requireNonNull(path3),
                       requireNonNull(el3)
                      );
    }

    /**
     * Static factory method to create four-element JSON object
     *
     * @return an immutable and persistent JSON object
     */

    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3
                 ).set(JsPath.empty()
                             .key(requireNonNull(key4)),
                       el4
                      );
    }

    /**
     * Static factory method to create four-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3
                 ).set(requireNonNull(path4),
                       requireNonNull(el4)
                      );
    }

    /**
     * Static factory method to create five-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4
                 ).set(JsPath.empty()
                             .key(requireNonNull(key5)),
                       el5
                      );
    }

    /**
     * Static factory method to create five-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4
                 ).set(requireNonNull(path5),
                       requireNonNull(el5)
                      );
    }

    /**
     * Static factory method to create six-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5
                 ).set(JsPath.empty()
                             .key(requireNonNull(key6)),
                       el6
                      );
    }

    /**
     * Static factory method to create six-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5
                 ).set(requireNonNull(path6),
                       requireNonNull(el6)
                      );
    }

    /**
     * Static factory method to create seven-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6
                 ).set(JsPath.empty()
                             .key(requireNonNull(key7)),
                       el7
                      );
    }

    /**
     * Static factory method to create seven-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6
                 ).set(requireNonNull(path7),
                       requireNonNull(el7)
                      );
    }

    /**
     * Static factory method to create eight-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7
                 ).set(JsPath.empty()
                             .key(requireNonNull(key8)),
                       el8
                      );
    }

    /**
     * Static factory method to create eight-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7
                 ).set(requireNonNull(path8),
                       requireNonNull(el8)
                      );
    }

    /**
     * Static factory method to create nine-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8
                 ).set(JsPath.empty()
                             .key(requireNonNull(key9)),
                       el9
                      );
    }

    /**
     * Static factory method to create nine-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8
                 ).set(requireNonNull(path9),
                       requireNonNull(el9)
                      );
    }

    /**
     * Static factory method to create ten-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9
                 ).set(JsPath.empty()
                             .key(requireNonNull(key10)),
                       el10
                      );
    }

    /**
     * Static factory method to create ten-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9
                 ).set(requireNonNull(path10),
                       requireNonNull(el10)
                      );
    }


    /**
     * Static factory method to create eleven-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10
                 ).set(JsPath.empty()
                             .key(requireNonNull(key11)),
                       el11
                      );
    }

    /**
     * Static factory method to create eleven-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10
                 ).set(requireNonNull(path11),
                       requireNonNull(el11)
                      );
    }

    /**
     * Static factory method to create twelve-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11
                 ).set(JsPath.empty()
                             .key(requireNonNull(key12)),
                       el12
                      );
    }

    /**
     * Static factory method to create twelve-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11
                 ).set(requireNonNull(path12),
                       requireNonNull(el12)
                      );
    }


    /**
     * Static factory method to create thirteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12
                 ).set(JsPath.empty()
                             .key(requireNonNull(key13)),
                       el13
                      );
    }

    /**
     * Static factory method to create thirteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12
                 ).set(requireNonNull(path13),
                       requireNonNull(el13)
                      );
    }

    /**
     * Static factory method to create fourteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13
                 ).set(JsPath.empty()
                             .key(requireNonNull(key14)),
                       el14
                      );
    }

    /**
     * Static factory method to create fourteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13,
                           final JsPath path14,
                           final JsValue el14
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12,
                  path13,
                  el13
                 ).set(requireNonNull(path14),
                       requireNonNull(el14)
                      );
    }

    /**
     * Static factory method to create fifteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14
                 ).set(JsPath.empty()
                             .key(requireNonNull(key15)),
                       el15
                      );
    }

    /**
     * Static factory method to create fifteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13,
                           final JsPath path14,
                           final JsValue el14,
                           final JsPath path15,
                           final JsValue el15
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12,
                  path13,
                  el13,
                  path14,
                  el14
                 ).set(requireNonNull(path15),
                       requireNonNull(el15)
                      );
    }


    /**
     * Static factory method to create sixteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15
                 ).set(JsPath.empty()
                             .key(requireNonNull(key16)),
                       el16
                      );
    }

    /**
     * Static factory method to create sixteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13,
                           final JsPath path14,
                           final JsValue el14,
                           final JsPath path15,
                           final JsValue el15,
                           final JsPath path16,
                           final JsValue el16
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12,
                  path13,
                  el13,
                  path14,
                  el14,
                  path15,
                  el15
                 ).set(requireNonNull(path16),
                       requireNonNull(el16)
                      );
    }

    /**
     * Static factory method to create seventeen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16
                 ).set(JsPath.empty()
                             .key(requireNonNull(key17)),
                       el17
                      );
    }

    /**
     * Static factory method to create seventeen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13,
                           final JsPath path14,
                           final JsValue el14,
                           final JsPath path15,
                           final JsValue el15,
                           final JsPath path16,
                           final JsValue el16,
                           final JsPath path17,
                           final JsValue el17
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12,
                  path13,
                  el13,
                  path14,
                  el14,
                  path15,
                  el15,
                  path16,
                  el16
                 ).set(requireNonNull(path17),
                       requireNonNull(el17)
                      );
    }

    /**
     * Static factory method to create eighteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17,
                           final String key18,
                           final JsValue el18
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16,
                  key17,
                  el17
                 ).set(JsPath.empty()
                             .key(requireNonNull(key18)),
                       el18
                      );
    }

    /**
     * Static factory method to create eighteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13,
                           final JsPath path14,
                           final JsValue el14,
                           final JsPath path15,
                           final JsValue el15,
                           final JsPath path16,
                           final JsValue el16,
                           final JsPath path17,
                           final JsValue el17,
                           final JsPath path18,
                           final JsValue el18
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12,
                  path13,
                  el13,
                  path14,
                  el14,
                  path15,
                  el15,
                  path16,
                  el16,
                  path17,
                  el17
                 ).set(requireNonNull(path18),
                       requireNonNull(el18)
                      );
    }

    /**
     * Static factory method to create nineteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17,
                           final String key18,
                           final JsValue el18,
                           final String key19,
                           final JsValue el19
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16,
                  key17,
                  el17,
                  key18,
                  el18
                 ).set(JsPath.empty()
                             .key(requireNonNull(key19)),
                       el19
                      );
    }

    /**
     * Static factory method to create nineteen-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13,
                           final JsPath path14,
                           final JsValue el14,
                           final JsPath path15,
                           final JsValue el15,
                           final JsPath path16,
                           final JsValue el16,
                           final JsPath path17,
                           final JsValue el17,
                           final JsPath path18,
                           final JsValue el18,
                           final JsPath path19,
                           final JsValue el19
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12,
                  path13,
                  el13,
                  path14,
                  el14,
                  path15,
                  el15,
                  path16,
                  el16,
                  path17,
                  el17,
                  path18,
                  el18
                 ).set(requireNonNull(path19),
                       requireNonNull(el19)
                      );
    }

    /**
     * Static factory method to create twenty-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17,
                           final String key18,
                           final JsValue el18,
                           final String key19,
                           final JsValue el19,
                           final String key20,
                           final JsValue el20
                          ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16,
                  key17,
                  el17,
                  key18,
                  el18,
                  key19,
                  el19
                 ).set(JsPath.empty()
                             .key(requireNonNull(key20)),
                       el20
                      );
    }

    /**
     * Static factory method to create twenty-element JSON object
     *
     * @return an immutable and persistent JSON object
     */
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final JsPath path1,
                           final JsValue el1,
                           final JsPath path2,
                           final JsValue el2,
                           final JsPath path3,
                           final JsValue el3,
                           final JsPath path4,
                           final JsValue el4,
                           final JsPath path5,
                           final JsValue el5,
                           final JsPath path6,
                           final JsValue el6,
                           final JsPath path7,
                           final JsValue el7,
                           final JsPath path8,
                           final JsValue el8,
                           final JsPath path9,
                           final JsValue el9,
                           final JsPath path10,
                           final JsValue el10,
                           final JsPath path11,
                           final JsValue el11,
                           final JsPath path12,
                           final JsValue el12,
                           final JsPath path13,
                           final JsValue el13,
                           final JsPath path14,
                           final JsValue el14,
                           final JsPath path15,
                           final JsValue el15,
                           final JsPath path16,
                           final JsValue el16,
                           final JsPath path17,
                           final JsValue el17,
                           final JsPath path18,
                           final JsValue el18,
                           final JsPath path19,
                           final JsValue el19,
                           final JsPath path20,
                           final JsValue el20
                          ) {
        return of(path1,
                  el1,
                  path2,
                  el2,
                  path3,
                  el3,
                  path4,
                  el4,
                  path5,
                  el5,
                  path6,
                  el6,
                  path7,
                  el7,
                  path8,
                  el8,
                  path9,
                  el9,
                  path10,
                  el10,
                  path11,
                  el11,
                  path12,
                  el12,
                  path13,
                  el13,
                  path14,
                  el14,
                  path15,
                  el15,
                  path16,
                  el16,
                  path17,
                  el17,
                  path18,
                  el18,
                  path19,
                  el19
                 ).set(requireNonNull(path20),
                       requireNonNull(el20)
                      );
    }


    /**
     * Parses the given string into an immutable and persistent JSON object.
     *
     * @param str the string to be parsed
     * @return a JsOb object
     * @throws JsParserException if the string doesn't represent a json object
     */
    public static JsObj parse(final String str) throws JsParserException {
        return JsIO.INSTANCE.parseToJsObj(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Parses the given array of bytes into an immutable and persistent JSON object.
     *
     * @param bytes the array of bytes
     * @return a JsObj object
     * @throws JsParserException if the string doesn't represent a json object
     */
    public static JsObj parse(final byte[] bytes) throws JsParserException {
        return JsIO.INSTANCE.parseToJsObj(bytes);
    }

    static Stream<JsPair> streamOfObj(final JsObj obj,
                                      final JsPath path
                                     ) {

        requireNonNull(path);
        return requireNonNull(obj).ifEmptyElse(() -> Stream.of(new JsPair(path,
                                                                          obj
                                               )),
                                               () -> obj.keySet()
                                                        .stream()
                                                        .map(key -> new JsPair(path.key(key),
                                                                               get(obj,
                                                                                   Key.of(key)
                                                                                  )
                                                        ))
                                                        .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                              pair.path()
                                                                                                             ),
                                                                                             a -> streamOfArr(a,
                                                                                                              pair.path()
                                                                                                             ),
                                                                                             e -> Stream.of(pair)
                                                                                            )
                                                                                 .apply(pair.value()))
                                              );

    }

    private static JsValue get(final JsObj obj,
                               final Position position
                              ) {
        return requireNonNull(position).match(key -> obj.map.getOrElse(key,
                                                                       NOTHING
                                                                      ),
                                              index -> NOTHING
                                             );
    }

    /**
     * Sets the specified key to the given JSON value. If the key already exists, its value will be updated.
     * If the key doesn't exist, a new key-value pair will be added to the JSON object.
     *
     * @param key   The key to set.
     * @param value The JSON value to associate with the key.
     * @return A new `JsObj` with the updated or added key-value pair.
     */
    public JsObj set(final String key,
                     final JsValue value
                    ) {
        requireNonNull(key);
        return ifNothingElse(() -> this.delete(key),
                             elem -> new JsObj(map.put(key,
                                                       elem
                                                      ))
                            ).apply(requireNonNull(value));
    }

    /**
     * Deletes the specified key from the JSON object. If the key doesn't exist, this method has no effect.
     *
     * @param key The key to delete.
     * @return A new `JsObj` with the specified key removed.
     */
    public JsObj delete(final String key) {
        if (!map.containsKey(requireNonNull(key))) return this;
        return new JsObj(map.remove(key));
    }

    @Override
    public boolean containsValue(final JsValue el) {
        return map.containsValue(el);
    }

    /**
     * Returns a `Set` of all keys present in the JSON object.
     *
     * @return A `Set` of all keys in the JSON object.
     */
    public Set<String> keySet() {
        Set<String> keys = new HashSet<>();
        Iterator<String> iter = map.keySet();
        while (iter.hasNext()) keys.add(iter.next());
        return keys;
    }

    @Override
    public JsValue get(final JsPath path) {
        if (path.isEmpty()) return this;
        final JsValue e = get(this,
                              path.head()
                             );
        final JsPath tail = path.tail();
        if (tail.isEmpty()) return e;
        if (e.isPrimitive()) return NOTHING;
        return e.toJson()
                .get(tail);
    }

    @Override
    public JsObj filterValues(final BiPredicate<? super JsPath, ? super JsPrimitive> filter) {
        return OpFilterObjElems.filter(this,
                                       JsPath.empty(),
                                       requireNonNull(filter)
                                      );

    }

    @Override
    public JsObj filterValues(final Predicate<? super JsPrimitive> filter) {
        return OpFilterObjElems.filter(this,
                                       requireNonNull(filter)
                                      );
    }

    @Override
    public JsObj filterKeys(final BiPredicate<? super JsPath, ? super JsValue> filter) {
        return OpFilterObjKeys.filter(this,
                                      JsPath.empty(),
                                      filter
                                     );
    }

    @Override
    public JsObj filterKeys(final Predicate<? super String> filter) {
        return OpFilterObjKeys.filter(this,
                                      filter
                                     );
    }


    @Override
    public JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter) {
        return OpFilterObjObjs.filter(this,
                                      JsPath.empty(),
                                      requireNonNull(filter)
                                     );

    }

    @Override
    public JsObj filterObjs(final Predicate<? super JsObj> filter) {
        return OpFilterObjObjs.filter(this,
                                      requireNonNull(filter)
                                     );
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public JsObj mapValues(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapObjElems.map(this,
                                 requireNonNull(fn),
                                 EMPTY_PATH
                                );
    }

    @Override
    public JsObj mapValues(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapObjElems.map(this,
                                 requireNonNull(fn)
                                );
    }


    @Override
    public JsObj mapKeys(final BiFunction<? super JsPath, ? super JsValue, String> fn) {
        return OpMapObjKeys.map(this,
                                requireNonNull(fn),
                                EMPTY_PATH
                               );
    }

    @Override
    public JsObj mapKeys(final Function<? super String, String> fn) {
        return OpMapObjKeys.map(this,
                                requireNonNull(fn)
                               );
    }

    @Override
    public JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, ? extends JsValue> fn) {
        return OpMapObjObjs.map(this,
                                requireNonNull(fn),
                                JsPath.empty()
                               );
    }

    @Override
    public JsObj mapObjs(final Function<? super JsObj, ? extends JsValue> fn) {
        return OpMapObjObjs.map(this,
                                requireNonNull(fn)
                               );
    }

    @Override
    public JsObj set(final JsPath path,
                     final JsValue value,
                     final JsValue padElement

                    ) {
        requireNonNull(value);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head ->
                          {
                              final JsPath tail = path.tail();

                              return tail.isEmpty() ?
                                      ifNothingElse(() -> this.delete(head),
                                                    elem -> new JsObj(map.put(head,
                                                                              elem
                                                                             ))
                                                   )
                                              .apply(value) :
                                      isReplaceWithEmptyJson(map).test(head,
                                                                       tail
                                                                      ) ?
                                              new JsObj(map.put(head,
                                                                tail.head()
                                                                    .match(key -> JsObj.EMPTY.set(tail,
                                                                                                  value,
                                                                                                  padElement
                                                                                                 ),
                                                                           index -> JsArray.EMPTY.set(tail,
                                                                                                      value,
                                                                                                      padElement
                                                                                                     )
                                                                          )
                                                               )) :
                                              new JsObj(map.put(head,
                                                                map.get(head)
                                                                   .get()
                                                                   .toJson()
                                                                   .set(tail,
                                                                        value,
                                                                        padElement
                                                                       )
                                                               ));
                          },
                          index -> this

                         );

    }

    @Override
    public JsObj set(final JsPath path,
                     final JsValue element
                    ) {
        return ifNothingElse(() -> this.delete(path),
                             e -> set(path,
                                      e,
                                      NULL
                                     )
                            ).apply(requireNonNull(element));
    }


    @Override
    public <R> Optional<R> reduce(final BinaryOperator<R> op,
                                  final BiFunction<? super JsPath, ? super JsPrimitive, R> map,
                                  final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                                 ) {
        return OpMapReduce.reduceObj(this,
                                     JsPath.empty(),
                                     requireNonNull(predicate),
                                     map,
                                     op,
                                     Optional.empty()
                                    );

    }

    @Override
    public <R> Optional<R> reduce(final BinaryOperator<R> op,
                                  final Function<? super JsPrimitive, R> map,
                                  final Predicate<? super JsPrimitive> predicate
                                 ) {
        return OpMapReduce.reduceObj(this,
                                     requireNonNull(predicate),
                                     map,
                                     op,
                                     Optional.empty()
                                    );
    }

    @Override
    public JsObj delete(final JsPath path) {
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key ->
                          {
                              if (!map.containsKey(key)) return this;
                              final JsPath tail = path.tail();
                              return tail.isEmpty() ?
                                      new JsObj(map.remove(key)) :
                                      MatchExp.ifJsonElse(json -> new JsObj(map.put(key,
                                                                                    json.delete(tail)
                                                                                   )),
                                                          e -> this
                                                         )
                                              .apply(map.get(key)
                                                        .get());
                          },
                          index -> this
                         );


    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Stream<JsPair> stream() {
        return streamOfObj(this,
                           JsPath.empty()
                          );
    }

    /**
     * Returns a stream of `(key, value)` pairs representing the properties of the JSON object at the first level.
     * This method operates on the top-level properties and does not traverse recursively into nested JSON objects.
     *
     * @return A stream of `(key, value)` pairs, where each pair consists of a top-level property key and its corresponding value.
     */
    public Stream<JsObjPair> streamOfKeys() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(),
                                                             size(),
                                                             Spliterator.DISTINCT
                                                            ),
                                    false
                                   );
    }

    /**
     * Checks if this `JsObj` is equal to another `JsObj` considering how arrays are treated (as lists, sets, or multisets).
     *
     * @param that     The `JsObj` to compare with.
     * @param ARRAY_AS Enum to specify how arrays are considered.
     * @return `true` if both `JsObj` instances are equal; otherwise, `false`.
     */
    public boolean equals(final JsObj that,
                          final TYPE ARRAY_AS
                         ) {
        if (isEmpty()) return that.isEmpty();
        if (that.isEmpty()) return isEmpty();
        return keySet().stream()
                       .allMatch(field ->
                                 {
                                     final boolean exists = that.containsKey(field);
                                     if (!exists) return false;
                                     final JsValue elem = get(field);
                                     final JsValue thatElem = that.get(field);
                                     if (elem.isJson() && thatElem.isJson())
                                         return elem.toJson()
                                                    .equals(thatElem,
                                                            ARRAY_AS
                                                           );
                                     return elem.equals(thatElem);
                                 }) && that.keySet()
                                           .stream()
                                           .allMatch(this::containsKey);
    }

    /**
     * Checks if the specified key exists in the JSON object.
     *
     * @param key The key to check for existence.
     * @return `true` if the key exists; otherwise, `false`.
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
     * Retrieves the JSON value associated with the specified key.
     *
     * @param key The key to retrieve the JSON value for.
     * @return The JSON value associated with the key or a default value if the key does not exist.
     */
    public JsValue get(final String key) {
        return map.getOrElse(requireNonNull(key),
                             NOTHING
                            );
    }

    /**
     * Returns the array located at the given key or null if it doesn't exist, or it's not an array.
     *
     * @param key the key
     * @return the JsArray located at the given key or null
     */
    public JsArray getArray(final String key) {
        return JsArray.prism.getOptional.apply(get(requireNonNull(key)))
                                        .orElse(null);

    }

    /**
     * Returns the array located at the given key or the default value provided if it
     * doesn't exist, or it's not an array.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the JsArray located at the given key or the default value provided
     */
    public JsArray getArray(final String key,
                            final Supplier<JsArray> orElse
                           ) {
        return JsArray.prism.getOptional.apply(get(requireNonNull(key)))
                                        .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the number located at the given key as a big decimal or null if it doesn't exist or it's
     * not a decimal number.
     *
     * @param key the key
     * @return the BigDecimal located at the given key or null
     */
    public BigDecimal getBigDec(final String key) {
        return JsBigDec.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);


    }

    /**
     * Returns the number located at the given key as a big decimal or the default value provided
     * if it doesn't exist, or it's not a decimal number.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the BigDecimal located at the given key or the default value provided
     */
    public BigDecimal getBigDec(final String key,
                                final Supplier<BigDecimal> orElse
                               ) {
        return JsBigDec.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));


    }

    /**
     * Returns the bytes located at the given key or null if it doesn't exist, or it's
     * not an array of bytes.
     *
     * @param key the key
     * @return the bytes located at the given key or null
     */
    public byte[] getBinary(final String key) {
        return JsBinary.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);


    }

    /**
     * Returns the bytes located at the given key  or the default value provided if it doesn't exist,
     * or it's not an array of bytes.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the bytes located at the given key or the default value provided
     */
    public byte[] getBinary(final String key,
                            final Supplier<byte[]> orElse
                           ) {
        return JsBinary.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));


    }

    /**
     * Returns the big integer located at the given key as a big integer or null if it doesn't
     * exist, or it's not an integral number.
     *
     * @param key the key
     * @return the BigInteger located at the given key or null
     */
    public BigInteger getBigInt(final String key) {
        return JsBigInt.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);

    }

    /**
     * Returns the big integer located at the given key as a big integer or the default value provided
     * if it doesn't exist, or it's not an integral number.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the BigInteger located at the given key or null
     */
    public BigInteger getBigInt(final String key,
                                final Supplier<BigInteger> orElse
                               ) {
        return JsBigInt.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the instant located at the given key or null if it doesn't exist, or it's
     * not an instant.
     *
     * @param key the key
     * @return the instant located at the given key or null
     */
    public Instant getInstant(final String key) {
        return JsInstant.prism.getOptional.apply(get(requireNonNull(key)))
                                          .orElse(null);


    }

    /**
     * Returns the instant located at the given key or the default value provided if it doesn't
     * exist, or it's not an instant.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the instant located at the given key or null
     */
    public Instant getInstant(final String key,
                              final Supplier<Instant> orElse
                             ) {
        return JsInstant.prism.getOptional.apply(get(requireNonNull(key)))
                                          .orElseGet(requireNonNull(orElse));


    }

    /**
     * Returns the boolean located at the given key or null if it doesn't exist.
     *
     * @param key the key
     * @return the Boolean located at the given key or null
     */
    public Boolean getBool(final String key) {
        return JsBool.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElse(null);

    }

    /**
     * Returns the boolean located at the given key or the default value provided if it doesn't exist.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the Boolean located at the given key or null
     */
    public Boolean getBool(final String key,
                           final Supplier<Boolean> orElse
                          ) {
        return JsBool.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the number located at the given key as a double or null if it
     * doesn't exist, or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     * to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     * the precision of the BigDecimal
     *
     * @param key the key
     * @return the decimal number located at the given key or null
     */
    public Double getDouble(final String key) {
        return JsDouble.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);

    }

    /**
     * Returns the number located at the given key as a double or the default value provided if it
     * doesn't exist, or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     * to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     * the precision of the BigDecimal
     *
     * @param key    the key
     * @param orElse the default value
     * @return the decimal number located at the given key or null
     */
    public Double getDouble(final String key,
                            final Supplier<Double> orElse
                           ) {
        return JsDouble.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the integral number located at the given key as an integer or null if it
     * doesn't exist, or it's not an integral number or it's an integral number but doesn't fit in an integer.
     *
     * @param key the key
     * @return the integral number located at the given key or null
     */
    public Integer getInt(final String key) {
        return JsInt.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElse(null);

    }

    /**
     * Returns the integral number located at the given key as an integer or the default value provided if it
     * doesn't exist, or it's not an integral number or it's an integral number but doesn't fit in an integer.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the integral number located at the given key or null
     */
    public Integer getInt(final String key,
                          final Supplier<Integer> orElse
                         ) {
        return JsInt.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the integral number located at the given key as a long or null if it
     * doesn't exist, or it's not an integral number or it's an integral number but doesn't fit in a long.
     *
     * @param key the key
     * @return the integral number located at the given key or null
     */
    public Long getLong(final String key) {
        return JsLong.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElse(null);

    }

    /**
     * Returns the integral number located at the given key as a long or the default value provided
     * if it doesn't exist, or it's not an integral number or it's an integral number but doesn't fit
     * in a long.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the integral number located at the given key or the default value provided
     */
    public Long getLong(final String key,
                        final Supplier<Long> orElse
                       ) {
        return JsLong.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the json object located at the given key or null if it doesn't exist or it's not an object.
     *
     * @param key the key
     * @return the json object located at the given key or null
     */
    public JsObj getObj(final String key) {
        return JsObj.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElse(null);

    }

    /**
     * Returns the json object located at the given key or the default value provided
     * if it doesn't exist, or it's not an object.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the json object located at the given key or the default value
     */
    public JsObj getObj(final String key,
                        final Supplier<JsObj> orElse
                       ) {
        return JsObj.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the string located at the given key or null if it doesn't exist or it's not an string.
     *
     * @param key the key
     * @return the string located at the given key or null
     */
    public String getStr(final String key) {
        return JsStr.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElse(null);
    }

    /**
     * Returns the string located at the given key or the default value provided if it doesn't
     * exist, or it's not an string.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the string located at the given key or null
     */
    public String getStr(final String key,
                         final Supplier<String> orElse
                        ) {
        return JsStr.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElseGet(requireNonNull(orElse));
    }


    @Override
    public int hashCode() {
        int result = hashcode;
        if (result == 0) {
            for (var next : map) {
                result += next.key().hashCode() ^ next.value().hashCode();
            }
            hashcode = result;
        }
        return result;
    }

    @Override
    public boolean equals(final Object o) {

        if (o == this)
            return true;
        if (!(o instanceof JsObj m))
            return false;
        if (m.size() != size())
            return false;

        try {
            for (JsObjPair e : this) {
                var key = e.key();
                var value = e.value();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }

        return true;
    }


    @Override
    public String toString() {
        String result = str;
        if (result == null)
            str = result = new String(JsIO.INSTANCE.serialize(this),
                                      StandardCharsets.UTF_8
            );
        return result;
    }


    @Override
    public boolean isObj() {
        return true;
    }


    @Override
    public JsObj intersection(final JsObj that,
                              final TYPE ARRAY_AS
                             ) {

        return intersection(this,
                            requireNonNull(that),
                            requireNonNull(ARRAY_AS)
                           );
    }

    private BiPredicate<String, JsPath> isReplaceWithEmptyJson(final HashMap pmap) {
        return (head, tail) ->
                (!pmap.containsKey(head) || pmap.get(head)
                                                .filter(JsValue::isPrimitive)
                                                .isPresent())
                        ||
                        (
                                tail.head()
                                    .isKey() && pmap.get(head)
                                                    .filter(JsValue::isArray)
                                                    .isPresent()
                        )
                        ||
                        (tail.head()
                             .isIndex() && pmap.get(head)
                                               .filter(JsValue::isObj)
                                               .isPresent());
    }

    @Override
    public Iterator<JsObjPair> iterator() {

        Iterator<HashArrayMappedTrieModule.LeafNode> iterator = map.iterator();

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsObjPair next() {
                var next = iterator.next();
                return new JsObjPair(next.key(), next.value());
            }
        };

    }


    @Override
    public JsObj union(final JsObj that,
                       final TYPE ARRAY_AS
                      ) {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return ifEmptyElse(() -> that,
                           () -> that.ifEmptyElse(() -> this,
                                                  () -> union(this,
                                                              that,
                                                              ARRAY_AS
                                                             )
                                                 )
                          );

    }


    private JsObj intersection(final JsObj a,
                               final JsObj b,
                               final JsArray.TYPE ARRAY_AS
                              ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;
        JsObj result = JsObj.empty();
        for (var aVal : a) {

            if (b.containsKey(aVal.key())) {
                JsValue bVal = b.get(aVal.key());

                if (bVal.equals(aVal.value())) result = result.set(aVal.key(),
                                                                   aVal.value()
                                                                  );
                else if (bVal.isJson() && bVal.isSameType(aVal.value())) {
                    result = result.set(aVal.key(),
                                        OpIntersectionJsons.intersectionAll(aVal.value().toJson(),
                                                                            bVal.toJson(),
                                                                            ARRAY_AS
                                                                           )
                                       );
                }
            }

        }

        return result;

    }


    private JsObj union(final JsObj a,
                        final JsObj b,
                        final JsArray.TYPE ARRAY_AS
                       ) {

        if (b.isEmpty()) return a;
        JsObj result = a;
        for (var bVal : b) {
            if (!a.containsKey(bVal.key()))
                result = result.set(bVal.key(),
                                    bVal.value()
                                   );
            JsValue aVal = a.get(bVal.key());
            if (aVal.isJson() && aVal.isSameType(bVal.value())) {
                Json<?> aJson = aVal.toJson();
                Json<?> bJson = bVal.value().toJson();

                result = result.set(bVal.key(),
                                    OpUnionJsons.unionAll(aJson,
                                                          bJson,
                                                          ARRAY_AS
                                                         )
                                   );
            }
        }

        return result;

    }


}

