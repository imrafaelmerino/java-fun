package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import io.vavr.collection.Vector;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.dslplatform.json.MyDslJson.INSTANCE;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;
import static jsonvalues.JsArray.TYPE.LIST;
import static jsonvalues.JsArray.TYPE.MULTISET;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsObj.streamOfObj;
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;


/**
 Represents a json array, which is an ordered list of elements.
 */
public class JsArray implements Json<JsArray>, Iterable<JsValue> {
    public static final int TYPE_ID = 4;
    /**
     lenses defined for a Json array
     */
    public static final JsOptics.JsArrayLenses lens = JsOptics.array.lens;
    /**
     optionals defined for a Json array
     */
    public static final JsOptics.JsArrayOptionals optional = JsOptics.array.optional;

    public static final JsArray EMPTY = new JsArray(Vector.empty());
    /**
     prism between the sum type JsValue and JsArray
     */
    public static final Prism<JsValue, JsArray> prism =
            new Prism<>(
                    s -> s.isArray() ? Optional.of(s.toJsArray()) : Optional.empty(),
                    a -> a
            );
    private final Vector<JsValue> seq;
    private volatile int hashcode;
    //squid:S3077: doesn't make any sense, volatile is perfectly valid here an as a matter of fact
    //is a recommendation from Effective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")

    private volatile String str;


    JsArray(Vector<JsValue> seq) {
        this.seq = seq;
    }

    public JsArray() {
        this.seq = Vector.empty();
    }

    public static JsArray empty() {
        return EMPTY;
    }

    /**
     Returns an immutable array from one or more pairs.

     @param pair   a pair
     @param others more optional pairs
     @return an immutable JsArray
     @throws UserError if an elem of a pair is mutable
     */
    public static JsArray of(final JsPair pair,
                             final JsPair... others
                            ) {
        JsArray arr = JsArray.EMPTY.set(pair.path,
                                        pair.value
                                       );
        for (JsPair p : others) {

            arr = arr.set(p.path,
                          p.value
                         );
        }
        return arr;

    }

    /**
     Returns an immutable array.

     @param e    a JsValue
     @param e1   a JsValue
     @param e2   a JsValue
     @param e3   a JsValue
     @param e4   a JsValue
     @param rest more optional JsValue
     @return an immutable JsArray
     @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2,
                             final JsValue e3,
                             final JsValue e4,
                             final JsValue... rest
                            ) {
        JsArray result = of(e,
                            e1,
                            e2,
                            e3,
                            e4
                           );
        for (JsValue other : requireNonNull(rest)) {
            result = result.append(other);
        }
        return result;


    }

    /**
     Returns an immutable five-element array.

     @param e  a JsValue
     @param e1 a JsValue
     @param e2 a JsValue
     @param e3 a JsValue
     @param e4 a JsValue
     @return an immutable five-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    //squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2,
                             final JsValue e3,
                             final JsValue e4
                            ) {

        return of(e,
                  e1,
                  e2,
                  e3
                 ).append(e4);
    }

    /**
     Adds one or more elements, starting from the first, to the back of this array.

     @param e      the JsValue to be added to the back.
     @param others more optional JsValue to be added to the back
     @return a new JsArray
     */
    public final JsArray append(final JsValue e,
                                final JsValue... others
                               ) {
        Vector<JsValue> acc = this.seq.append(requireNonNull(e));
        for (JsValue other : requireNonNull(others)) acc = acc.append(requireNonNull(other));
        return new JsArray(acc);
    }

    /**
     Returns an immutable four-element array.

     @param e  a JsValue
     @param e1 a JsValue
     @param e2 a JsValue
     @param e3 a JsValue
     @return an immutable four-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2,
                             final JsValue e3
                            ) {
        return of(e,
                  e1,
                  e2
                 ).append(e3);
    }

    /**
     Returns an immutable three-element array.

     @param e  a JsValue
     @param e1 a JsValue
     @param e2 a JsValue
     @return an immutable three-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2
                            ) {

        return of(e,
                  e1
                 ).append(e2);
    }

    /**
     Returns an immutable two-element array.

     @param e  a JsValue
     @param e1 a JsValue
     @return an immutable two-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    public static JsArray of(final JsValue e,
                             final JsValue e1
                            ) {
        return of(e).append(e1);
    }

    public static JsArray of(JsValue e) {

        return JsArray.EMPTY.append(e);
    }

    /**
     Returns an immutable array from one or more strings.

     @param str    a string
     @param others more optional strings
     @return an immutable JsArray
     */
    public static JsArray of(String str,
                             String... others
                            ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsStr.of(str));
        for (String a : others) {
            vector = vector.append(JsStr.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     Returns an immutable array from one or more integers.

     @param number an integer
     @param others more optional integers
     @return an immutable JsArray
     */
    public static JsArray of(int number,
                             int... others
                            ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsInt.of(number));
        for (int a : others) {
            vector = vector.append(JsInt.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     Returns an immutable array from one or more booleans.

     @param bool   an boolean
     @param others more optional booleans
     @return an immutable JsArray
     */
    public static JsArray of(final boolean bool,
                             final boolean... others
                            ) {
        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsBool.of(bool));
        for (boolean a : others) {
            vector = vector.append(JsBool.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     Returns an immutable array from one or more longs.

     @param number a long
     @param others more optional longs
     @return an immutable JsArray
     */
    public static JsArray of(final long number,
                             final long... others
                            ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsLong.of(number));
        for (long a : others) {
            vector = vector.append(JsLong.of(a));
        }
        return new JsArray(vector

        );
    }

    /**
     Returns an immutable array from one or more big integers.

     @param number a big integer
     @param others more optional big integers
     @return an immutable JsArray
     */
    public static JsArray of(final BigInteger number,
                             final BigInteger... others
                            ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsBigInt.of(number));
        for (BigInteger a : others) {
            vector = vector.append(JsBigInt.of(a));
        }
        return new JsArray(vector);
    }

    /**
     Returns an immutable array from one or more doubles.

     @param number a double
     @param others more optional doubles
     @return an immutable JsArray
     */
    public static JsArray of(final double number,
                             final double... others
                            ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsDouble.of(number));
        for (double a : others) {
            vector = vector.append(JsDouble.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     returns an immutable json array from an iterable of json elements

     @param iterable the iterable of json elements
     @return an immutable json array
     */
    public static JsArray ofIterable(Iterable<? extends JsValue> iterable) {
        Vector<JsValue> vector = Vector.empty();
        for (JsValue e : requireNonNull(iterable)) {
            vector = vector.append(e);
        }
        return new JsArray(vector

        );
    }

    /**
     Tries to parse the string into an immutable json array.

     @param str the string to be parsed
     @return a JsArray
     @throws MalformedJson if the string doesnt represent a json array
     */
    public static JsArray parse(final String str) {

        try (JsonParser parser = JacksonFactory.INSTANCE.createParser(requireNonNull(str))) {
            final JsonToken keyEvent = parser.nextToken();
            if (START_ARRAY != keyEvent) throw MalformedJson.expectedArray(str);
            return new JsArray(parse(parser
                                    ));
        } catch (IOException e) {

            throw new MalformedJson(e.getMessage());
        }

    }

    static Vector<JsValue> parse(final JsonParser parser
                                ) throws IOException {
        Vector<JsValue> root = Vector.empty();
        while (true) {
            JsonToken token = parser.nextToken();
            JsValue   elem;
            switch (token.id()) {
                case JsonTokenId.ID_END_ARRAY:
                    return root;
                case JsonTokenId.ID_START_OBJECT:
                    elem = new JsObj(JsObj.parse(parser)
                    );
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    elem = new JsArray(parse(parser
                                            )
                    );
                    break;
                case JsonTokenId.ID_STRING:
                    elem = JsStr.of(parser.getValueAsString());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    elem = JsNumber.of(parser);
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    elem = JsBigDec.of(parser.getDecimalValue());
                    break;
                case JsonTokenId.ID_TRUE:
                    elem = TRUE;
                    break;
                case JsonTokenId.ID_FALSE:
                    elem = FALSE;
                    break;
                case JsonTokenId.ID_NULL:
                    elem = NULL;
                    break;
                default:
                    throw InternalError.tokenNotExpected(token.name());
            }
            root = root.append(elem);
        }
    }

    static Stream<JsPair> streamOfArr(final JsArray array,
                                      final JsPath path
                                     ) {


        requireNonNull(path);
        return requireNonNull(array).ifEmptyElse(() -> Stream.of(JsPair.of(path,
                                                                           array
                                                                          )),
                                                 () -> range(0,
                                                             array.size()
                                                            ).mapToObj(pair -> JsPair.of(path.index(pair),
                                                                                         array.get(Index.of(pair))
                                                                                        ))
                                                             .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                                   pair.path
                                                                                                                  ),
                                                                                                  a -> streamOfArr(a,
                                                                                                                   pair.path
                                                                                                                  ),
                                                                                                  e -> Stream.of(pair)
                                                                                                 )
                                                                                      .apply(pair.value)
                                                                     )
                                                );


    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private static JsArray intersection(JsArray a,
                                        JsArray b,
                                        JsArray.TYPE ARRAY_AS
                                       ) {
        switch (ARRAY_AS) {
            case SET:
                return intersectionAsSet(a,
                                         b
                                        );
            case LIST:
                return intersectionAsList(a,
                                          b
                                         );
            case MULTISET:
                return intersectionAsMultiSet(a,
                                              b
                                             );
            default:
                throw InternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }

    }

    private static JsArray intersectionAsList(JsArray a,
                                              JsArray b
                                             ) {

        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (int i = 0; i < a.size(); i++) {
            JsValue aVal = a.get(i);
            JsValue bVal = b.get(i);
            if (aVal.isNothing() || bVal.isNothing()) return result;
            if (aVal.equals(bVal)) result = result.append(aVal);
        }

        return result;

    }

    private static JsArray intersectionAsMultiSet(final JsArray a,
                                                  final JsArray b
                                                 ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (JsValue it : a) {
            if (b.containsValue(it))
                result = result.append(it);
        }

        return result;
    }

    private static JsArray intersectionAsSet(final JsArray a,
                                             final JsArray b
                                            ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (JsValue it : a) {
            if (b.containsValue(it) && !result.containsValue(it)) result = result.append(it);
        }
        return result;
    }

    private static JsArray unionAsList(final JsArray a,
                                       final JsArray b
                                      ) {
        if (a.isEmpty()) return b;
        JsArray result = a;
        for (int i = a.size(); i < b.size(); i++) {
            result = result.append(b.get(i));
        }
        return result;
    }

    private static JsArray unionAsMultiSet(final JsArray a,
                                           final JsArray b
                                          ) {
        return a.appendAll(b);

    }

    private static JsArray unionAsSet(final JsArray a,
                                      final JsArray b
                                     ) {
        if (b.isEmpty()) return a;
        if (a.isEmpty()) return b;

        JsArray result = JsArray.empty();
        for (final JsValue value : a) {
            if (!result.containsValue(value)) result = result.append(value);
        }
        for (final JsValue value : b) {
            if (!result.containsValue(value)) result = result.append(value);
        }

        return result;
    }

    /**
     Adds all the elements of the given array, starting from the head, to the back of this array.

     @param array the JsArray of elements to be added to the back
     @return a new JsArray
     */
    public final JsArray appendAll(final JsArray array) {
        return appendAllBack(this,
                             requireNonNull(array)
                            );


    }

    private JsArray appendAllBack(JsArray arr1,
                                  final JsArray arr2
                                 ) {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return arr1;
        if (arr1.isEmpty()) return arr2;
        for (final JsValue value : arr2) {
            arr1 = arr1.append(value);
        }
        return arr1;
    }

    private JsArray appendAllFront(JsArray arr1,
                                   JsArray arr2
                                  ) {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return arr1;
        if (arr1.isEmpty()) return arr2;
        for (final JsValue value : arr1) {
            arr2 = arr2.append(value);
        }
        return arr2;
    }

    /**
     Returns true if this array is equal to the given as a parameter. In the case of ARRAY_AS=LIST,
     this method is equivalent to JsArray.equals(Object).

     @param array    the given array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return true if both arrays are equals according to ARRAY_AS parameter
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public boolean equals(final JsArray array,
                          final TYPE ARRAY_AS
                         ) {
        if (ARRAY_AS == LIST) return this.equals(array);
        if (isEmpty()) return array.isEmpty();
        if (array.isEmpty()) return false;
        return IntStream.range(0,
                               size()
                              )
                        .mapToObj(i -> get(Index.of(i)))
                        .allMatch(elem ->
                                  {
                                      if (!array.containsValue(elem)) return false;
                                      if (ARRAY_AS == MULTISET) return times(elem) == array.times(elem);
                                      return true;
                                  }) && IntStream.range(0,
                                                        array.size()
                                                       )
                                                 .mapToObj(i -> array.get(Index.of(i)))
                                                 .allMatch(this::containsValue);
    }

    /**
     Returns the integral number located at the given index as an integer or null if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.

     @param index the index
     @return the integral number located at the given index or null
     */
    public Integer getInt(final int index) {

        if (index == -1 && !seq.isEmpty())
            return JsInt.prism.getOptional.apply(seq.last())
                                          .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsInt.prism.getOptional.apply(seq.get(index))
                                             .orElse(null);

    }


    /**
     Returns the long number located at the given index as an long or null if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an long.

     @param index the index
     @return the long number located at the given index or null
     */
    public Long getLong(final int index) {

        if (index == -1 && !seq.isEmpty()) return JsLong.prism.getOptional.apply(seq.last())
                                                                          .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsLong.prism.getOptional.apply(seq.get(index))
                                              .orElse(null);

    }

    /**
     Returns the string located at the given index  or null if it doesn't exist.

     @param index the index
     @return the string located at the given index or null
     */
    public String getStr(final int index) {

        if (index == -1 && !seq.isEmpty()) return JsStr.prism.getOptional.apply(seq.last())
                                                                         .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsStr.prism.getOptional.apply(seq.get(index))
                                             .orElse(null);

    }

    /**
     Returns the instant located at the given index or null if it doesn't exist or it's not an instant.
     If the element is an instant formatted as a string, it's returned as an instant as well.

     @param index the given index
     @return an instant
     */
    public Instant getInstant(final int index) {

        if (index == -1 && !seq.isEmpty()) return JsInstant.prism.getOptional.apply(seq.last())
                                                                             .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsInstant.prism.getOptional.apply(seq.get(index))
                                                 .orElse(null);

    }

    /**
     Returns the array of bytes located at the given index or null if it doesn't exist or it's not an array of bytes.
     If the element is a string in base64, it's returned as an array of bytes as well.

     @param index the given index
     @return an array of bytes
     */
    public byte[] getBinary(final int index) {

        if (index == -1 && !seq.isEmpty()) return JsBinary.prism.getOptional.apply(seq.last())
                                                                            .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsBinary.prism.getOptional.apply(seq.get(index))
                                                .orElse(null);

    }

    /**
     Returns the boolean located at the given index  or null if it doesn't exist.

     @param index the index
     @return the boolean located at the given index or null
     */
    public Boolean getBool(final int index) {

        if (index == -1 && !seq.isEmpty()) return JsBool.prism.getOptional.apply(seq.last())
                                                                          .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsBool.prism.getOptional.apply(seq.get(index))
                                              .orElse(null);

    }


    /**
     Returns the number located at the given index as a double or null if it
     doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     the precision of the BigDecimal

     @param index the index
     @return the double number located at the given index or null
     */
    public Double getDouble(final int index) {

        if (index == -1 && !seq.isEmpty()) return JsDouble.prism.getOptional.apply(seq.last())
                                                                            .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsDouble.prism.getOptional.apply(seq.get(index))
                                                .orElse(null);

    }


    /**
     Returns the number located at the given index as a big decimal or null if
     it doesn't exist or it's not a decimal number.

     @param index the index
     @return the decimal number located at the given index or null
     */
    public BigDecimal getBigDec(final int index) {
        if (index == -1 && !seq.isEmpty()) return JsBigDec.prism.getOptional.apply(seq.last())
                                                                            .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsBigDec.prism.getOptional.apply(seq.get(index))
                                                .orElse(null);
    }

    /**
     Returns the number located at the given index as a big integer or null if
     it doesn't exist or it's not an integral number.

     @param index the index
     @return the integral number located at the given index or null
     */
    public BigInteger getBigInt(final int index) {
        if (index == -1 && !seq.isEmpty())
            return JsBigInt.prism.getOptional.apply(seq.last())
                                             .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsBigInt.prism.getOptional.apply(seq.get(index))
                                                .orElse(null);
    }

    /**
     Returns the object located at the given index  or null if it doesn't exist or it's not a json object.

     @param index the index
     @return the object located at the given index or null
     */
    public JsObj getObj(final int index) {
        if (index == -1 && !seq.isEmpty()) return JsObj.prism.getOptional.apply(seq.last())
                                                                         .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsObj.prism.getOptional.apply(seq.get(index))
                                             .orElse(null);
    }

    /**
     Returns the array located at the given index or null if it doesn't exist or it's not a json array.

     @param index the index
     @return the array located at the given index or null
     */
    public JsArray getArray(final int index) {
        if (index == -1 && !seq.isEmpty()) return JsArray.prism.getOptional.apply(seq.last())
                                                                           .orElse(null);
        return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
               null : JsArray.prism.getOptional.apply(seq.get(index))
                                               .orElse(null);
    }

    private JsValue get(final Position pos) {
        return requireNonNull(pos).match(key -> JsNothing.NOTHING,
                                         index ->
                                         {
                                             if (index == -1 && !this.seq.isEmpty()) return this.seq.last();
                                             return (this.seq.isEmpty() || index < 0 || index > this.seq.size() - 1) ?
                                                    JsNothing.NOTHING : this.seq.get(index);
                                         }
                                        );
    }

    @Override
    public final boolean containsValue(final JsValue el) {
        return seq.contains(requireNonNull(el));
    }

    @Override
    public JsValue get(final JsPath path) {
        if (path.isEmpty()) return this;
        final JsValue e    = get(path.head());
        final JsPath  tail = path.tail();
        if (tail.isEmpty()) return e;
        if (e.isPrimitive()) return NOTHING;
        return e.toJson()
                .get(tail);
    }

    public final JsArray filterValues(final BiPredicate<? super Integer, ? super JsPrimitive> filter) {
        return OpFilterArrElems.filter(this,
                                       requireNonNull(filter)
                                      );
    }

    @Override
    public JsArray filterValues(final Predicate<? super JsPrimitive> filter) {
        return OpFilterArrElems.filter(this,
                                       requireNonNull(filter)
                                      );
    }

    @Override
    public final JsArray filterAllValues(final BiPredicate<? super JsPath, ? super JsPrimitive> filter) {
        return OpFilterArrElems.filterAll(this,
                                          JsPath.empty(),
                                          requireNonNull(filter)
                                         );
    }

    @Override
    public JsArray filterAllValues(final Predicate<? super JsPrimitive> filter) {
        return OpFilterArrElems.filterAll(this,
                                          requireNonNull(filter)
                                         );
    }

    public final JsArray filterKeys(final BiPredicate<? super String, ? super JsValue> filter) {
        return this;
    }

    @Override
    public JsArray filterKeys(final Predicate<? super String> filter) {
        return this;
    }

    @Override
    public final JsArray filterAllKeys(final BiPredicate<? super JsPath, ? super JsValue> filter) {
        return OpFilterArrKeys.filterAll(this,
                                         JsPath.empty(),
                                         filter
                                        );
    }

    @Override
    public JsArray filterAllKeys(final Predicate<? super String> filter) {
        return OpFilterArrKeys.filterAll(this,
                                         filter
                                        );
    }

    public final JsArray filterObjs(final BiPredicate<? super Integer, ? super JsObj> filter) {
        return OpFilterArrObjs.filter(this,
                                      requireNonNull(filter)
                                     );
    }

    @Override
    public JsArray filterObjs(final Predicate<? super JsObj> filter) {
        return OpFilterArrObjs.filter(this,
                                      requireNonNull(filter)
                                     );
    }

    @Override
    public final JsArray filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter) {
        return OpFilterArrObjs.filterAll(this,
                                         JsPath.empty(),
                                         requireNonNull(filter)
                                        );
    }

    @Override
    public JsArray filterAllObjs(final Predicate<? super JsObj> filter) {
        return OpFilterArrObjs.filterAll(this,
                                         requireNonNull(filter)
                                        );
    }

    @Override
    public final boolean isEmpty() {
        return seq.isEmpty();
    }

    public final JsArray mapValues(final BiFunction<? super Integer, ? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapArrElems.map(this,
                                 requireNonNull(fn)
                                );

    }

    @Override
    public JsArray mapValues(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapArrElems.map(this,
                                 requireNonNull(fn)
                                );
    }

    @Override
    public JsArray mapAllValues(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapArrElems.mapAll(this,
                                    requireNonNull(fn),
                                    JsPath.empty()
                                          .index(-1)
                                   );
    }

    @Override
    public JsArray mapAllValues(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapArrElems.mapAll(this,
                                    requireNonNull(fn)
                                   );
    }

    public final JsArray mapKeys(final BiFunction<? super String, ? super JsValue, String> fn) {
        return this;
    }

    @Override
    public JsArray mapKeys(final Function<? super String, String> fn) {
        return this;
    }

    @Override
    public final JsArray mapAllKeys(final BiFunction<? super JsPath, ? super JsValue, String> fn) {
        return OpMapArrKeys.mapAll(this,
                                   requireNonNull(fn),
                                   JsPath.empty()
                                         .index(-1)
                                  );
    }

    @Override
    public JsArray mapAllKeys(final Function<? super String, String> fn) {
        return OpMapArrKeys.mapAll(this,
                                   requireNonNull(fn)
                                  );
    }

    public final JsArray mapObjs(final BiFunction<? super Integer, ? super JsObj, JsValue> fn) {
        return OpMapArrObjs.map(this,
                                requireNonNull(fn)
                               );
    }

    @Override
    public JsArray mapObjs(final Function<? super JsObj, JsValue> fn) {
        return OpMapArrObjs.map(this,
                                requireNonNull(fn)

                               );
    }

    @Override
    public final JsArray mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn) {
        return OpMapArrObjs.mapAll(this,
                                   requireNonNull(fn),
                                   JsPath.empty()

                                  );
    }

    @Override
    public JsArray mapAllObjs(final Function<? super JsObj, JsValue> fn) {
        return OpMapArrObjs.mapAll(this,
                                   requireNonNull(fn)
                                  );
    }

    @Override
    public JsArray set(final JsPath path,
                       final JsValue element) {
        return set(path,
                   element,
                   NULL
                  );
    }

    public JsArray set(final int index,
                       final JsValue element) {
        return set(index,
                   element,
                   NULL
                  );
    }

    public final JsArray set(final int index,
                             final JsValue value,
                             final JsValue padElement
                            ) {

        requireNonNull(value);

        return ifNothingElse(() -> this.delete(index),
                             elem -> new JsArray(nullPadding(index,
                                                             seq,
                                                             elem,
                                                             padElement
                                                            ))
                            )
                .apply(value);

    }

    @Override
    public final JsArray set(final JsPath path,
                             final JsValue value,
                             final JsValue padElement
                            ) {

        requireNonNull(value);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();

                              return tail.isEmpty() ? ifNothingElse(() -> this.delete(index),
                                                                    elem -> new JsArray(nullPadding(index,
                                                                                                    seq,
                                                                                                    elem,
                                                                                                    padElement
                                                                                                   ))
                                                                   )
                                      .apply(value) :
                                     putEmptyJson(seq).test(index,
                                                            tail
                                                           ) ?
                                     new JsArray(nullPadding(index,
                                                             seq,
                                                             tail.head()
                                                                 .match(key -> JsObj.EMPTY
                                                                                .set(tail,
                                                                                     value,
                                                                                     padElement

                                                                                    ),
                                                                        i -> JsArray.EMPTY
                                                                                .set(tail,
                                                                                     value,
                                                                                     padElement
                                                                                    )
                                                                       ),
                                                             padElement
                                                            )) :

                                     new JsArray(seq.update(index,
                                                            seq.get(index)
                                                               .toJson()
                                                               .set(tail,
                                                                    value,
                                                                    padElement
                                                                   )
                                                           ));

                          }

                         );

    }

    public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                        final BiFunction<? super Integer, ? super JsPrimitive, R> map,
                                        final BiPredicate<? super Integer, ? super JsPrimitive> predicate
                                       ) {
        return OpMapReduce.reduceArr(this,
                                     requireNonNull(predicate),
                                     map,
                                     op
                                    );


    }

    @Override
    public <R> Optional<R> reduce(final BinaryOperator<R> op,
                                  final Function<? super JsPrimitive, R> map,
                                  final Predicate<? super JsPrimitive> predicate) {
        return OpMapReduce.reduceArr(this,
                                     requireNonNull(predicate),
                                     map,
                                     op,
                                     Optional.empty()
                                    );
    }

    @Override
    public final <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                           final BiFunction<? super JsPath, ? super JsPrimitive, R> map,
                                           final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                                          ) {
        return OpMapReduce.reduceAllArr(this,
                                        JsPath.fromIndex(-1),
                                        requireNonNull(predicate),
                                        map,
                                        op,
                                        Optional.empty()
                                       );

    }

    @Override
    public <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                     final Function<? super JsPrimitive, R> map,
                                     final Predicate<? super JsPrimitive> predicate) {
        return OpMapReduce.reduceAllArr(this,
                                        requireNonNull(predicate),
                                        map,
                                        op,
                                        Optional.empty()
                                       );
    }

    @Override
    public final JsArray delete(final JsPath path) {
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              final int maxIndex = seq.size() - 1;
                              if (index < -1 || index > maxIndex) return this;
                              final JsPath tail = path.tail();
                              return tail.isEmpty() ? new JsArray(index == -1 ? seq.removeAt(maxIndex) : seq.removeAt(index)) :
                                     ifJsonElse(json -> new JsArray(seq.update(index,
                                                                               json.delete(tail)
                                                                              )),
                                                e -> this
                                               )
                                             .apply(seq.get(index));
                          }

                         );


    }

    @Override
    public final int size() {
        return seq.size();
    }

    @Override
    public Stream<JsPair> streamAll() {
        return streamOfArr(this,
                           JsPath.empty()
                          );
    }

    @Override
    public Stream<JsPair> stream() {
        return IntStream.range(0,
                               size()
                              )
                        .mapToObj(i ->
                                  {
                                      final JsPath path = JsPath.fromIndex(i);
                                      return JsPair.of(path,
                                                       get(path)
                                                      );
                                  });

    }

    private boolean yContainsX(final Vector<JsValue> x,
                               final Vector<JsValue> y
                              ) {
        for (int i = 0; i < x.size(); i++) {
            if (!Objects.equals(x.get(i),
                                y.get(i)
                               ))
                return false;

        }
        return true;

    }

    /**
     returns the element located at the specified index or JsNothing if it doesn't exist. It never throws
     an exception, it's a total function.

     @param i the index
     @return a JsValue
     */
    public JsValue get(final int i) {
        try {
            if (i == -1 && !this.seq.isEmpty()) return this.seq.last();
            return seq.get(i);
        } catch (IndexOutOfBoundsException e) {
            return NOTHING;
        }
    }

    /**
     equals method is inherited, so it's implemented. The purpose of this method is to cache
     the hashcode once calculated. the object is immutable and it won't change
     Single-check idiom  Item 83 from Effective Java
     */
    @Override
    @SuppressWarnings("squid:S1206")
    public final int hashCode() {
        int result = hashcode;
        if (result == 0)
            hashcode = result = seq.hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object that) {
        if (!(that instanceof JsArray)) return false;
        if (this == that) return true;
        final Vector<JsValue> thatSeq   = ((JsArray) that).seq;
        final boolean         thatEmpty = thatSeq.isEmpty();
        final boolean         thisEmpty = isEmpty();
        if (thatEmpty && thisEmpty) return true;
        if (this.size() != thatSeq.size()) return false;
        return yContainsX(seq,
                          thatSeq
                         ) && yContainsX(thatSeq,
                                         seq
                                        );

    }

    /**
     // Single-check idiom  Item 83 from effective java
     */
    @Override
    public final String toString() {
        String result = str;
        if (result == null)
            str = result = new String(INSTANCE.serialize(this));

        return result;
    }

    /**
     Returns the first element of this array.

     @return the first JsValue of this JsArray
     @throws UserError if this JsArray is empty
     */
    public final JsValue head() {
        return seq.head();
    }

    /**
     Returns a json array consisting of all elements of this array except the first one.

     @return a JsArray consisting of all the elements of this JsArray except the head
     @throws UserError if this JsArray is empty.
     */
    public final JsArray tail() {
        return new JsArray(seq.tail());
    }

    /**
     Returns all the elements of this array except the last one.

     @return JsArray with all the JsValue except the last one
     @throws UserError if this JsArray is empty
     */
    public final JsArray init() {
        return new JsArray(seq.init());
    }

    /**
     {@code this.intersection(that, SET)} returns an array with the elements that exist in both {@code this}
     and {@code that}.
     {@code this.intersection(that, MULTISET)} returns an array with the elements that exist in both
     {@code this} and {@code that}, being duplicates allowed.
     {@code this.intersection(that, LIST)} returns an array with the elements that exist in both {@code this}
     and {@code that} and are located at the same position.

     @param that     the other array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public final JsArray intersection(final JsArray that,
                                      final TYPE ARRAY_AS
                                     ) {
        return intersection(this,
                            requireNonNull(that),
                            requireNonNull(ARRAY_AS)
                           );
    }

    /**
     {@code this.intersectionAll(that)} behaves as {@code this.intersection(that, LIST)}, but for those
     elements that are containers of the same type and are located at the same position, the result
     is their intersection. So this operation is kind of a recursive intersection

     @param that the other array
     @return a JsArray of the same type as the inputs (mutable or immutable)
     */
    public JsArray intersectionAll(final JsArray that) {
        return intersectionAll(this,
                               requireNonNull(that)
                              );
    }

    @Override
    public int id() {
        return TYPE_ID;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public final Iterator<JsValue> iterator() {
        return seq.iterator();
    }

    /**
     Returns the last element of this array.

     @return the last JsValue of this JsArray
     @throws UserError if this JsArray is empty
     */
    public final JsValue last() {

        return seq.last();
    }

    /**
     Adds one or more elements, starting from the last, to the front of this array.

     @param e      the JsValue to be added to the front.
     @param others more optional JsValue to be added to the front
     @return a new JsArray
     */
    public final JsArray prepend(final JsValue e,
                                 final JsValue... others
                                ) {
        Vector<JsValue> acc = seq;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++) {
            final JsValue other = others[othersLength - 1 - i];
            acc = acc.prepend(requireNonNull(other));
        }
        return new JsArray(acc.prepend(requireNonNull(e)));
    }

    /**
     Adds all the elements of the array, starting from the last, to the front of this array.

     @param array the JsArray of elements to be added to the front
     @return a new JsArray
     */
    public final JsArray prependAll(final JsArray array) {
        return appendAllFront(this,
                              requireNonNull(array)
                             );

    }

    @SuppressWarnings("squid:S1602")
    // curly braces makes IntelliJ to format the code in a more legible way
    private BiPredicate<Integer, JsPath> putEmptyJson(final Vector<JsValue> pseq) {
        return (index, tail) ->
        {
            return index > pseq.size() - 1 || pseq.isEmpty() || pseq.get(index)
                                                                    .isPrimitive()
                    ||
                    (tail.head()
                         .isKey() && pseq.get(index)
                                         .isArray()
                    )
                    ||
                    (tail.head()
                         .isIndex() && pseq.get(index)
                                           .isObj()
                    );
        };
    }

    public final JsArray delete(final int index) {
        if (index < -1) throw new IllegalArgumentException("index must be >= -1");
        final int maxIndex = seq.size() - 1;
        if (index > maxIndex) return this;
        return new JsArray(index == -1 ? seq.removeAt(maxIndex) : seq.removeAt(index));
    }

    /**
     {@code this.union(that, SET)} returns {@code this} plus those elements from {@code that} that
     don't exist in {@code this}.
     {@code this.union(that, MULTISET)} returns {@code this} plus those elements from {@code that}
     appended to the back.
     {@code this.union(that, LIST)} returns {@code this} plus those elements from {@code that} which
     position is {@code >= this.size()}.

     @param that     the other array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new json array of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public final JsArray union(final JsArray that,
                               final JsArray.TYPE ARRAY_AS
                              ) {
        return union(this,
                     requireNonNull(that),
                     requireNonNull(ARRAY_AS)
                    );
    }

    /**
     returns {@code this} plus those elements from {@code that} which position is  {@code >= this.size()},
     and, at the positions where a container of the same type exists in both {@code this} and {@code that},
     the result is their union. This operations doesn't make any sense if arrays are not considered lists,
     because there is no notion of order.

     @param that the other array
     @return a new JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00100")
    public final JsArray unionAll(final JsArray that
                                 ) {
        return unionAll(this,
                        requireNonNull(that)
                       );
    }

    private JsArray intersectionAll(final JsArray a,
                                    final JsArray b
                                   ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();

        for (int i = 0; i < a.size(); i++) {
            final JsValue head      = a.get(i);
            final JsValue otherHead = b.get(i);
            if (head.isJson() && head.isSameType(otherHead)) {
                final Json<?> obj  = head.toJson();
                final Json<?> obj1 = otherHead.toJson();
                result = result.set(i,
                                    OpIntersectionJsons.intersectionAll(obj,
                                                                        obj1,
                                                                        JsArray.TYPE.LIST
                                                                       )
                                   );


            }
            else if (head.equals(otherHead))
                result = result.set(i,
                                    head
                                   );

        }

        return result;
    }

    private Vector<JsValue> nullPadding(final int index,
                                        final Vector<JsValue> arr,
                                        final JsValue e,
                                        final JsValue pad
                                       ) {
        assert arr != null;
        assert e != null;

        return nullPaddingTrampoline(index,
                                     arr,
                                     e,
                                     pad
                                    );
    }

    private Vector<JsValue> nullPaddingTrampoline(final int i,
                                                  Vector<JsValue> arr,
                                                  final JsValue e,
                                                  final JsValue pad
                                                 ) {

        if (i == arr.size()) return arr.append(e);

        if (i == -1) return arr.update(seq.size() - 1,
                                       e
                                      );

        if (i < arr.size()) return arr.update(i,
                                              e
                                             );
        for (int j = arr.size(); j < i; j++) {
            arr = arr.append(pad);
        }
        return arr.append(e);

    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private JsArray union(JsArray a,
                          JsArray b,
                          JsArray.TYPE ARRAY_AS
                         ) {
        switch (ARRAY_AS) {
            case SET:
                return unionAsSet(a,
                                  b
                                 );
            case LIST:
                return unionAsList(a,
                                   b
                                  );
            case MULTISET:
                return unionAsMultiSet(a,
                                       b
                                      );
            default:
                throw InternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }
    }

    private JsArray unionAll(final JsArray a,
                             final JsArray b
                            ) {
        if (b.isEmpty()) return a;
        if (a.isEmpty()) return b;
        JsArray result = a;
        for (int i = 0; i < b.size(); i++) {
            final JsValue head      = a.get(i);
            final JsValue otherHead = b.get(i);
            if (head.isJson() && head.isSameType(otherHead)) {
                final Json<?> obj  = head.toJson();
                final Json<?> obj1 = otherHead.toJson();
                result = result.set(i,
                                    OpUnionJsons.unionAll(obj,
                                                          obj1,
                                                          JsArray.TYPE.LIST
                                                         )
                                   );

            }
            else if(!otherHead.isNothing() && head.isNothing()) result = result.append(otherHead);
        }

        return result;


    }


    /**
     Type of arrays: SET, MULTISET or LIST.
     */
    public enum TYPE {
        /**
         The order of data items does not matter (or is undefined) but duplicate data items are not
         permitted.
         */
        SET,
        /**
         The order of data matters and duplicate data items are permitted.
         */
        LIST,
        /**
         The order of data items does not matter, but in this
         case duplicate data items are permitted.
         */
        MULTISET
    }


}



