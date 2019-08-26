package jsonvalues;

import jsonvalues.JsArray.TYPE;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;
import static jsonvalues.MyJsParser.Event.START_OBJECT;
import static jsonvalues.MyScalaMap.EMPTY;

/**
 Represents a json object, which is an unordered set of name/element pairs. Two implementations are
 provided, an immutable which uses the persistent Scala HashMap, and a mutable which uses the conventional
 Java HashMap.
 */
@SuppressWarnings("squid:S1214") //serializable class, explicit declaration of serialVersionUID is fine
public interface JsObj extends Json<JsObj>, Iterable<Map.Entry<String, JsElem>>
{

    long serialVersionUID = 1L;

    /**
     Returns a mutable empty object.
     @return a mutable empty JsObj
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static JsObj _empty_()
    {
        return new MyMutableJsObj();
    }

    /**
     Returns a mutable one-pair object.
     @param key name of the key
     @param el JsElem to be associated to the key
     @return a mutable one-pair JsObj
     @throws UserError if the elem is an immutable Json

     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(final String key,
                      final JsElem el
                     )
    {
        if (requireNonNull(el).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el);
        return _empty_().put(JsPath.empty()
                                   .key(requireNonNull(key)),
                             el
                            );
    }

    /**
     Returns a mutable two-pair object.
     @param key1 name of a key
     @param el1 JsElem to be associated to the key1
     @param key2 name of a key
     @param el2 JsElem to be associated to the key2
     @return a two-pair mutable JsObj
     @throws UserError if an elem is an immutable Json
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2
                     )
    {
        if (requireNonNull(el2).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el2);

        return _of_(key1,
                    el1
                   ).put(JsPath.empty()
                               .key(requireNonNull(key2)),
                         el2
                        );
    }

    /**
     Returns a mutable three-pair object.
     @param key1 name of a key
     @param el1 JsElem to be associated to the key1
     @param key2 name of a key
     @param el2 JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @return a three-pair mutable JsObj
     @throws UserError if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100", "squid:S00107"})
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2,
                      final String key3,
                      final JsElem el3
                     )
    {
        if (requireNonNull(el3).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el3);
        return _of_(key1,
                    el1,
                    key2,
                    el2
                   ).put(JsPath.empty()
                               .key(requireNonNull(key3)),
                         el3
                        );
    }

    /**
     Returns a mutable four-pair object.
     @param key1 name of a key
     @param el1 JsElem to be associated to the key1
     @param key2 name of a key
     @param el2 JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4  JsElem to be associated to the key4
     @return a mutable four-pair JsObj
     @throws UserError if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100", "squid:S00107"})
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2,
                      final String key3,
                      final JsElem el3,
                      final String key4,
                      final JsElem el4
                     )
    {
        if (requireNonNull(el4).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el4);

        return _of_(key1,
                    el1,
                    key2,
                    el2,
                    key3,
                    el3
                   ).put(JsPath.empty()
                               .key(requireNonNull(key4)),
                         el4
                        );
    }

    /**
     Returns a mutable five-pair object.
     @param key1 name parse a key
     @param el1 JsElem to be associated to the key1
     @param key2 name parse a key
     @param el2 JsElem to be associated to the key2
     @param key3 name parse a key
     @param el3  JsElem to be associated to the key3
     @param key4 name parse a key
     @param el4  JsElem to be associated to the key4
     @param key5 name parse a key
     @param el5  JsElem to be associated to the key5
     @return a mutable five-element JsObj
     @throws UserError if an elem is an immutable Json

     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100", "squid:S00107"})
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2,
                      final String key3,
                      final JsElem el3,
                      final String key4,
                      final JsElem el4,
                      final String key5,
                      final JsElem el5
                     )
    {
        if (requireNonNull(el5).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el5);

        return _of_(key1,
                    el1,
                    key2,
                    el2,
                    key3,
                    el3,
                    key4,
                    el4
                   ).put(JsPath.empty()
                               .key(requireNonNull(key5)),
                         el5
                        );
    }

    /**
     Returns a mutable six-pair object.
     @param key1 name parse a key
     @param el1 JsElem to be associated to the key1
     @param key2 name parse a key
     @param el2 JsElem to be associated to the key2
     @param key3 name parse a key
     @param el3  JsElem to be associated to the key3
     @param key4 name parse a key
     @param el4  JsElem to be associated to the key4
     @param key5 name parse a key
     @param el5  JsElem to be associated to the key5
     @param key6 name parse a key
     @param el6  JsElem to be associated to the key6
     @return a mutable six-element JsObj
     @throws UserError if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100", "squid:S00107"})
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2,
                      final String key3,
                      final JsElem el3,
                      final String key4,
                      final JsElem el4,
                      final String key5,
                      final JsElem el5,
                      final String key6,
                      final JsElem el6
                     )
    {
        if (requireNonNull(el6).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el6);

        return _of_(key1,
                    el1,
                    key2,
                    el2,
                    key3,
                    el3,
                    key4,
                    el4,
                    key5,
                    el5
                   ).put(JsPath.empty()
                               .key(requireNonNull(key6)),
                         el6
                        );
    }


    /**
     Returns a mutable object from a map of elements.
     @param map the map of JsElem
     @return a mutable JsObj
     @throws UserError if an elem of the map is an immutable Json
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(final java.util.Map<String, JsElem> map)
    {
        requireNonNull(map).values()
                           .stream()
                           .filter(e -> e.isJson(Json::isImmutable))
                           .findFirst()
                           .ifPresent(UserError::mutableArgExpected);
        return new MyMutableJsObj(new MyJavaMap(map));
    }

    /**
     Tries to parse the string into a mutable json object.
     @param str the string to be parsed
     @return a {@link TryObj} computation

     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static TryObj _parse_(final String str)
    {
        try (MyJsParser parser = new MyJsParser(new StringReader(requireNonNull(str))))
        {
            MyJsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            MyJavaMap obj = new MyJavaMap();
            obj.parse(parser);
            return new TryObj(new MyMutableJsObj(obj));
        }

        catch (MalformedJson e)
        {

            return new TryObj(e);
        }


    }

    /**
     return true if this obj is equal to the given as a parameter. In the case of ARRAY_AS=LIST, this
     method is equivalent to JsObj.equals(Object).
     @param that the given array
     @param ARRAY_AS enum to specify if arrays are considered as lists or sets or multisets
     @return true if both objs are equals
     */
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    default boolean equals(final JsObj that,
                           final TYPE ARRAY_AS
                          )
    {
        if (isEmpty()) return that.isEmpty();
        if (that.isEmpty()) return isEmpty();
        return fields().stream()
                       .allMatch(field ->
                                 {
                                     final boolean exists = that.containsPath(JsPath.fromKey(field));
                                     if (!exists) return false;
                                     final JsElem elem = get(JsPath.fromKey(field));
                                     final JsElem thatElem = that.get(JsPath.fromKey(field));
                                     if (elem.isJson() && thatElem.isJson()) return elem.asJson()
                                                                                        .equals(thatElem,
                                                                                                ARRAY_AS
                                                                                               );
                                     return elem.equals(thatElem);
                                 }) && that.fields()
                                           .stream()
                                           .allMatch(f -> this.containsPath(JsPath.fromKey(f)));
    }


    /**
     Tries to parse the string into an mutable object, performing the specified transformations while the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and
     applying them later.
     @param str  string to be parsed
     @param builder builder with the transformations that will be applied during the parsing
     @return a {@link TryObj} computation
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static TryObj _parse_(final String str,
                          final ParseBuilder builder
                         )
    {

        try (MyJsParser parser = new MyJsParser(new StringReader(requireNonNull(str))))
        {
            MyJsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            MyJavaMap obj = new MyJavaMap();
            obj.parse(parser,
                      builder.create(),
                      JsPath.empty()
                     );
            return new TryObj(new MyMutableJsObj(obj));
        }

        catch (MalformedJson e)
        {

            return new TryObj(e);
        }


    }

    /**
     Returns a collector that accumulates the pairs from a stream into an immutable object.
     @return a Collector which collects all the pairs of elements into an immutable JsObj, in encounter order
     */
    static Collector<JsPair, JsObj, JsObj> collector()
    {

        return Collector.of(jsonvalues.JsObj::_empty_,
                            (obj, pair) -> obj.put(pair.path,
                                                   pair.elem.isJson() ? pair.elem.asJson()
                                                                                 .toImmutable() : pair.elem
                                                  ),
                            (a, b) -> new OpCombinerObjs(a,
                                                         b
                            ).combine()
                             .get(),
                            jsonvalues.JsObj::toImmutable
                           );

    }


    /**
     Returns a collector that accumulates the pairs from a stream into an mutable object.
     @return a Collector which collects all the pairs of elements into an mutable JsObj, in encounter order
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static Collector<JsPair, JsObj, JsObj> _collector_()
    {

        return Collector.of(jsonvalues.JsObj::_empty_,
                            (obj, pair) -> obj.put(pair.path,
                                                   pair.elem.isJson() ? pair.elem.asJson()
                                                                                 .toMutable() : pair.elem
                                                  ),
                            (a, b) -> new OpCombinerObjs(a,
                                                         b
                            ).combine()
                             .get()

                           );

    }

    /**
     Returns the immutable empty object. The same instance is always returned.
     @return the singleton immutable empty JsObj
     */
    static JsObj empty()
    {
        return MyImmutableJsObj.EMPTY;
    }


    /**
     Returns a set containing each key fo this object.
     @return a Set containing each key of this JsObj
     */
    Set<String> fields();

    /**
     Returns a pair with an arbitrary key of this object and its associated element. When using head
     and tail to process a JsObj, the key of the pair returned must be passed in to get the tail using
     the method {@link #tail(String)}.
     @return an arbitrary {@code Map.Entry<String,JsElem>} of this JsObj
     @throws UserError if this json object is empty
     */
    Map.Entry<String, JsElem> head();


    /**
     Returns an immutable one-element object.
     @param key  name of the key
     @param el  JsElem to be associated to the key
     @return an immutable one-element JsObj
     @throws UserError if the elem is a mutable Json
     */
    static JsObj of(final String key,
                    final JsElem el
                   )
    {
        if (requireNonNull(el).isJson(Json::isMutable)) throw UserError.immutableArgExpected(el);

        return empty().put(JsPath.empty()
                                 .key(requireNonNull(key)),
                           el
                          );
    }

    /**
     Returns a two-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @return an immutable two-element JsObj
     @throws UserError if an elem is a mutable Json

     */
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2
                   )
    {
        if (requireNonNull(el2).isJson(Json::isMutable)) throw UserError.immutableArgExpected(el2);

        return of(key1,
                  el1
                 ).put(JsPath.empty()
                             .key(requireNonNull(key2)),
                       el2
                      );
    }

    /**
     Returns a three-element immutable json object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @return an immutable three-element JsObj
     @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3
                   )
    {
        if (requireNonNull(el3).isJson(Json::isMutable)) throw UserError.immutableArgExpected(el3);

        return of(key1,
                  el1,
                  key2,
                  el2
                 ).put(JsPath.empty()
                             .key(requireNonNull(key3)),
                       el3
                      );
    }

    /**
     Returns a four-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4 JsElem to be associated to the key4
     @return an immutable four-element JsObj
     @throws UserError if an elem is a mutable Json

     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3,
                    final String key4,
                    final JsElem el4
                   )
    {
        if (requireNonNull(el4).isJson(Json::isMutable)) throw UserError.immutableArgExpected(el4);

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3
                 ).put(JsPath.empty()
                             .key(requireNonNull(key4)),
                       el4
                      );
    }

    /**
     Returns a five-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4 JsElem to be associated to the key4
     @param key5 name of a key
     @param el5 JsElem to be associated to the key5
     @return an immutable five-element JsObj
     @throws UserError if an elem is a mutable Json

     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3,
                    final String key4,
                    final JsElem el4,
                    final String key5,
                    final JsElem el5
                   )
    {
        if (requireNonNull(el5).isJson(Json::isMutable)) throw UserError.immutableArgExpected(el5);
        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4
                 ).put(JsPath.empty()
                             .key(requireNonNull(key5)),
                       el5
                      );
    }

    /**
     Returns a six-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4 JsElem to be associated to the key4
     @param key5 name of a key
     @param el5 JsElem to be associated to the key5
     @param key6 name of a key
     @param el6 JsElem to be associated to the key6
     @return an immutable six-element JsObj
     @throws UserError if an elem is a mutable Json

     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3,
                    final String key4,
                    final JsElem el4,
                    final String key5,
                    final JsElem el5,
                    final String key6,
                    final JsElem el6
                   )
    {
        if (requireNonNull(el6).isJson(Json::isMutable)) throw UserError.immutableArgExpected(el6);
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
                 ).put(JsPath.empty()
                             .key(requireNonNull(key6)),
                       el6
                      );
    }

    /**
     Returns a immutable object from a map of elements.
     @param map the map of JsElem
     @return an immutable JsObj
     @throws UserError if an elem of the map is a mutable Json
     */
    static JsObj of(final java.util.Map<String, JsElem> map)
    {
        if (requireNonNull(map).isEmpty()) return empty();
        requireNonNull(map).values()
                           .stream()
                           .filter(e -> e.isJson(Json::isMutable))
                           .findFirst()
                           .ifPresent(UserError::immutableArgExpected);
        return new MyImmutableJsObj(EMPTY.updateAll(map));
    }

    /**
     Tries to parse the string into an immutable object.
     @param str the string to be parsed
     @return a {@link TryObj} computation
     */
    static TryObj parse(final String str)
    {
        try (MyJsParser parser = new MyJsParser(new StringReader(requireNonNull(str))))
        {
            MyJsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            return new TryObj(new MyImmutableJsObj(EMPTY.parse(parser)));
        }
        catch (MalformedJson e)
        {
            return new TryObj(e);
        }
    }


    /**
     Tries to parse the string into an immutable object,  performing the specified transformations during the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and
     applying them later.
     @param str  string to be parsed
     @param builder builder with the transformations that will be applied during the parsing
     @return a {@link TryObj} computation
     */
    static TryObj parse(final String str,
                        final ParseBuilder builder
                       )
    {
        try (MyJsParser parser = new MyJsParser(new StringReader(requireNonNull(str))))
        {
            MyJsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            return new TryObj(new MyImmutableJsObj(EMPTY.parse(parser,
                                                               builder.create(),
                                                               JsPath.empty()
                                                              )));
        }
        catch (MalformedJson e)
        {
            return new TryObj(e);
        }
    }


    /**
     Returns a new object with all the entries of this json object except the one with the given key.
     @param key the given key, which associated pair will be excluded
     @return a new JsObj
     @throws UserError if this json object is empty
     */
    JsObj tail(final String key);

    /**
     {@code this.intersection(that, SET)} returns an array with the elements that exist in both {@code this} and {@code that}
     {@code this.intersection(that, MULTISET)} returns an array with the elements that exist in both {@code this} and {@code that},
     being duplicates allowed.
     {@code this.intersection(that, LIST)} returns an array with the elements that exist in both {@code this} and {@code that},
     and are located at the same position.
     @param that the other obj
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117")
    //  ARRAY_AS  should be a valid name
    JsObj intersection(final JsObj that,
                       final TYPE ARRAY_AS
                      );


    /**
     {@code this.intersection_(that)} behaves as {@code this.intersection(that, LIST)}, but for those elements
     that are containers of the same type and are located at the same position, the result is their
     intersection.  So this operation is kind of a 'recursive' intersection.
     @param that the other object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    // squid:S00100_ naming convention: xx_ traverses the whole json
    // squid:S00117 ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00117", "squid:S00100"})
    JsObj intersection_(final JsObj that,
                        final TYPE ARRAY_AS
                       );

    /**
     returns {@code this} json object plus those pairs from the given json object {@code that} which
     keys don't exist in {@code this}. Taking that into account, it's not a commutative operation unless
     the elements associated with the keys that exist in both json objects are equals.
     @param that the given json object
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    JsObj union(final JsObj that);

    /**
     behaves like the {@link JsObj#union(JsObj)} but, for those keys that exit in both {@code this}
     and {@code that} json objects,
     which associated elements are **containers of the same type**, the result is their union. In this
     case, we can specify if arrays are considered Sets, Lists, or MultiSets. So this operation is kind of a
     'recursive' union.
     @param that the given json object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */

    // squid:S00100:  naming convention: _xx_ returns immutable object
    // squid:squid:S00117: ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S00117"})
    JsObj union_(final JsObj that,
                 final TYPE ARRAY_AS
                );


    /**
     Returns an immutable object from one or more pairs.
     @param pair a pair
     @param others more optional pairs
     @return an immutable JsObject
     @throws UserError if an elem of a pair is mutable

     */
    static JsObj of(final JsPair pair,
                    final JsPair... others
                   )
    {
        if (requireNonNull(pair).elem.isJson(Json::isMutable)) throw UserError.immutableArgExpected(pair.elem);
        JsObj obj = empty().put(pair.path,
                                pair.elem
                               );
        for (JsPair p : others)
        {
            if (requireNonNull(p).elem.isJson(Json::isMutable)) throw UserError.immutableArgExpected(p.elem);

            obj = obj.put(p.path,
                          p.elem
                         );
        }
        return obj;

    }

    /**
     Returns a mutable object from one or more pairs.
     @param pair a pair
     @param others more optional pairs
     @return a mutable JsObject
     @throws UserError if an elem of a pair is immutable
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(JsPair pair,
                      JsPair... others
                     )
    {
        if (requireNonNull(pair).elem.isJson(Json::isImmutable)) throw UserError.mutableArgExpected(pair.elem);

        JsObj obj = _empty_().put(pair.path,
                                  pair.elem
                                 );
        for (JsPair p : others)
        {
            if (requireNonNull(p).elem.isJson(Json::isImmutable)) throw UserError.mutableArgExpected(p.elem);

            obj.put(p.path,
                    p.elem
                   );
        }
        return obj;
    }

    default <T> Trampoline<T> ifEmptyElse(final Trampoline<T> empty,
                                          final BiFunction<Map.Entry<String, JsElem>, JsObj, Trampoline<T>> fn
                                         )
    {


        if (this.isEmpty()) return empty;

        final Map.Entry<String, JsElem> head = this.head();

        final JsObj tail = this.tail(head.getKey());

        return fn.apply(head,
                        tail
                       );

    }

    @Override
    default boolean isObj()
    {
        return true;
    }

    @Override
    default boolean isArray()
    {
        return false;
    }

    @Override
    default TryPatch<JsObj> patch(final JsArray ops)
    {
        return Patch.of(this,
                        requireNonNull(ops)
                       );
    }
}

