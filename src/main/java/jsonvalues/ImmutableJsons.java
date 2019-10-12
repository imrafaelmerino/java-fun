package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.checkerframework.checker.nullness.qual.KeyFor;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static java.util.Objects.requireNonNull;

/**
 Factory to create immutable jsons.
 */
public final class ImmutableJsons
{
    /**
     converts the given json using this factory to an immutable one, if it's not immutable, returning the same instance otherwise.
     @param json the given json
     @return an immutable json
     */
    @SuppressWarnings("squid:S1452") //Json requires a type and the real type is unknown
    public Json<?> toImmutable(Json<?> json)
    {
        if (json.isMutable()) return json.isObj() ? object.toImmutable(json.asJsObj()) : array.toImmutable(json.asJsArray());
        return json;
    }

    /**
     Tries to parse the string into an immutable json.
     @param str the string that will be parsed
     @return a {@link Try} computation
     */
    public Try parse(String str)
    {

        try (JsonParser parser = Jsons.factory.createParser(requireNonNull(str)))
        {
            final JsonToken event = parser.nextToken();
            if (event == START_ARRAY)
            {
                return new Try(new ImmutableJsArray(Jsons.immutable.array.emptySeq.parse(this,
                                                                                         parser
                                                                                        ),
                                                    this
                ));
            }
            return new Try(new ImmutableJsObj(Jsons.immutable.object.emptyMap.parse(this,
                                                                                    parser
                                                                                   ),
                                              this
            )
            );
        }


        catch (IOException e)
        {

            return new Try(new MalformedJson(e.getMessage()));

        }


    }

    /**
     Tries to parse the string into an immutable json, performing the specified transformations while the parsing.
     @param str     the string that will be parsed
     @param builder a builder with the transformations that will be applied during the parsing
     @return a {@link Try} computation
     */
    public Try parse(String str,
                     ParseBuilder builder
                    )
    {

        try (JsonParser parser = Jsons.factory.createParser(requireNonNull(str)))
        {
            final JsonToken event = parser.nextToken();
            if (event == START_ARRAY) return new Try(new ImmutableJsArray(Jsons.immutable.array.emptySeq.parse(this,
                                                                                                               parser,
                                                                                                               builder.create(),
                                                                                                               JsPath.empty()
                                                                                                                     .index(-1)

                                                                                                              ),
                                                                          this
            )
            );
            return new Try(new ImmutableJsObj(Jsons.immutable.object.emptyMap.parse(this,
                                                                                    parser,
                                                                                    builder.create(),
                                                                                    JsPath.empty()

                                                                                   ),
                                              this
            )
            );

        }
        catch (IOException e)
        {

            return new Try(new MalformedJson(e.getMessage()));

        }


    }

    /**
     represents a factory of immutable Json arrays
     */
    public class ImmutableJsArrays
    {

        final ImmutableJsArray empty;
        final ScalaImmutableVector emptySeq;

        ImmutableJsArrays()
        {

            emptySeq = new ScalaImmutableVector();
            empty = new ImmutableJsArray(emptySeq,
                                         ImmutableJsons.this
            );


        }


        public JsArray toImmutable(JsArray mutable)
        {
            JsArray acc = empty();
            for (JsElem elem : requireNonNull(mutable))
            {
                if (elem.isObj()) acc = acc.append(object.toImmutable(elem.asJsObj()));
                else if (elem.isArray()) acc = acc.append(array.toImmutable(elem.asJsArray()));
                else acc = acc.append(elem);
            }
            return acc;

        }

        /**
         Returns the immutable empty array. The same instance is always returned.
         @return the singleton immutable empty JsArray
         */
        public JsArray empty()
        {
            return empty;
        }

        public JsArray of(JsElem e)
        {
            if (requireNonNull(e).isJson(Json::isMutable)) throw UserError.immutableArgExpected(e);

            return empty().append(e);
        }

        /**
         Returns an immutable array from one or more pairs.
         @param pair a pair
         @param others more optional pairs
         @return an immutable JsArray
         @throws UserError if an elem of a pair is mutable

         */
        public JsArray of(final JsPair pair,
                          final JsPair... others
                         )
        {
            if (requireNonNull(pair).elem.isJson(Json::isMutable)) throw UserError.immutableArgExpected(pair.elem);
            JsArray arr = empty().put(pair.path,
                                      pair.elem
                                     );
            for (JsPair p : others)
            {
                if (requireNonNull(p).elem.isJson(Json::isMutable)) throw UserError.immutableArgExpected(p.elem);

                arr = arr.put(p.path,
                              p.elem
                             );
            }
            return arr;

        }


        /**
         Returns an immutable two-element array.
         @param e a JsElem
         @param e1 a JsElem
         @return an immutable two-element JsArray
         @throws UserError if an elem is a mutable Json
         */
        public JsArray of(final JsElem e,
                          final JsElem e1
                         )
        {
            if (requireNonNull(e1).isJson(Json::isMutable)) throw UserError.immutableArgExpected(e1);
            return of(e).append(e1);
        }

        /**
         Returns an immutable three-element array.
         @param e  a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @return an immutable three-element JsArray
         @throws UserError if an elem is a mutable Json
         */
        public JsArray of(final JsElem e,
                          final JsElem e1,
                          final JsElem e2
                         )
        {
            if (requireNonNull(e2).isJson(Json::isMutable)) throw UserError.immutableArgExpected(e2);

            return of(e,
                      e1
                     ).append(e2);
        }

        /**
         Returns an immutable four-element array.
         @param e a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @param e3 a JsElem
         @return an immutable four-element JsArray
         @throws UserError if an elem is a mutable Json
         */
        public JsArray of(final JsElem e,
                          final JsElem e1,
                          final JsElem e2,
                          final JsElem e3
                         )
        {
            if (requireNonNull(e3).isJson(Json::isMutable)) throw UserError.immutableArgExpected(e3);

            return of(e,
                      e1,
                      e2
                     ).append(e3);
        }

        /**
         Returns an immutable five-element array.
         @param e a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @param e3 a JsElem
         @param e4 a JsElem
         @return an immutable five-element JsArray
         @throws UserError if an elem is a mutable Json
         */
        //squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
        @SuppressWarnings("squid:S00107")
        public JsArray of(final JsElem e,
                          final JsElem e1,
                          final JsElem e2,
                          final JsElem e3,
                          final JsElem e4
                         )
        {
            if (requireNonNull(e4).isJson(Json::isMutable)) throw UserError.immutableArgExpected(e4);

            return of(e,
                      e1,
                      e2,
                      e3
                     ).append(e4);
        }

        /**
         Returns an immutable array.
         @param e a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @param e3 a JsElem
         @param e4 a JsElem
         @param rest more optional JsElem
         @return an immutable JsArray
         @throws UserError if an elem is a mutable Json
         */
        // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
        @SuppressWarnings("squid:S00107")
        public JsArray of(final JsElem e,
                          final JsElem e1,
                          final JsElem e2,
                          final JsElem e3,
                          final JsElem e4,
                          final JsElem... rest
                         )
        {
            JsArray result = of(e,
                                e1,
                                e2,
                                e3,
                                e4
                               );
            for (JsElem other : requireNonNull(rest))
            {
                if (requireNonNull(other).isJson(Json::isMutable)) throw UserError.immutableArgExpected(other);
                result = result.append(other);
            }
            return result;


        }

        /**
         returns an immutable json array from an iterable of json elements
         @param iterable the iterable of json elements
         @return an immutable json array
         */
        public JsArray ofIterable(Iterable<JsElem> iterable)
        {
            ImmutableSeq vector = emptySeq;
            for (JsElem e : requireNonNull(iterable))
            {
                if (requireNonNull(e).isJson(Json::isMutable)) throw UserError.immutableArgExpected(e);

                vector = vector.appendBack(e);
            }
            return new ImmutableJsArray(vector,
                                        ImmutableJsons.this
            );
        }

        /**
         Returns an immutable array from one or more strings.
         @param str a string
         @param others more optional strings
         @return an immutable JsArray
         */
        public JsArray of(String str,
                          String... others
                         )
        {

            ImmutableSeq vector = emptySeq.appendBack(JsStr.of(str));
            for (String a : others)
            {
                vector = vector.appendBack(JsStr.of(a));
            }
            return new ImmutableJsArray(vector,
                                        ImmutableJsons.this
            );
        }


        /**
         Returns an immutable array from one or more integers.
         @param number an integer
         @param others more optional integers
         @return an immutable JsArray
         */
        public JsArray of(int number,
                          int... others
                         )
        {

            ImmutableSeq vector = emptySeq.appendBack(JsInt.of(number));
            for (int a : others)
            {
                vector = vector.appendBack(JsInt.of(a));
            }
            return new ImmutableJsArray(vector,
                                        ImmutableJsons.this
            );
        }

        /**
         Returns an immutable array from one or more booleans.
         @param bool an boolean
         @param others more optional booleans
         @return an immutable JsArray
         */
        public JsArray of(final boolean bool,
                          final boolean... others
                         )
        {
            ImmutableSeq vector = emptySeq.appendBack(JsBool.of(bool));
            for (boolean a : others)
            {
                vector = vector.appendBack(JsBool.of(a));
            }
            return new ImmutableJsArray(vector,
                                        ImmutableJsons.this
            );
        }


        /**
         Returns an immutable array from one or more longs.
         @param number a long
         @param others more optional longs
         @return an immutable JsArray
         */
        public JsArray of(final long number,
                          final long... others
                         )
        {

            ImmutableSeq vector = emptySeq.appendBack(JsLong.of(number));
            for (long a : others)
            {
                vector = vector.appendBack(JsLong.of(a));
            }
            return new ImmutableJsArray(vector,
                                        ImmutableJsons.this
            );
        }

        /**
         Returns an immutable array from one or more doubles.
         @param number a double
         @param others more optional doubles
         @return an immutable JsArray
         */
        public JsArray of(final double number,
                          final double... others
                         )
        {

            ImmutableSeq vector = emptySeq.appendBack(JsDouble.of(number));
            for (double a : others)
            {
                vector = vector.appendBack(JsDouble.of(a));
            }
            return new ImmutableJsArray(vector,
                                        ImmutableJsons.this
            );
        }


        /**
         Tries to parse the string into an immutable json array.
         @param str the string to be parsed
         @return a {@link TryArr} computation
         */
        public TryArr parse(final String str)
        {

            try (JsonParser parser = Jsons.factory.createParser(requireNonNull(str)))
            {
                final JsonToken keyEvent = parser.nextToken();
                if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
                return new TryArr(new ImmutableJsArray(emptySeq.parse(ImmutableJsons.this,
                                                                      parser
                                                                     ),
                                                       ImmutableJsons.this
                ));
            }
            catch (IOException e)
            {

                return new TryArr(new MalformedJson(e.getMessage()));
            }

        }

        public TryArr parse(final String str,
                            final ParseBuilder builder
                           )
        {

            try (JsonParser parser = Jsons.factory.createParser(requireNonNull(str)))
            {
                final JsonToken keyEvent = parser.nextToken();
                if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
                return new TryArr(new ImmutableJsArray(emptySeq.parse(ImmutableJsons.this,
                                                                      parser,
                                                                      requireNonNull(builder).create(),
                                                                      JsPath.fromIndex(-1)
                                                                     ),
                                                       ImmutableJsons.this
                ));
            }
            catch (IOException e)
            {

                return new TryArr(new MalformedJson(e.getMessage()));
            }

        }

    }

    /**
     represents a factory of immutable Json objects
     */
    public class ImmutableJsObjs
    {

        final ImmutableJsObj empty;
        final ScalaImmutableMap emptyMap;

        ImmutableJsObjs()
        {
            emptyMap = new ScalaImmutableMap();
            empty = new ImmutableJsObj(emptyMap,
                                       ImmutableJsons.this
            );
        }

        public JsObj toImmutable(JsObj mutable)
        {
            JsObj acc = empty();
            @SuppressWarnings("squid:S1905")// in return checkerframework does its job!
            final Set<@KeyFor("mutable") String> keys = (Set<@KeyFor("mutable") String>) requireNonNull(mutable).fields();
            for (String key : keys)
            {
                final JsPath path = JsPath.fromKey(key);
                final JsElem elem = mutable.get(path);
                if (elem.isObj()) acc = acc.put(path,
                                                object.toImmutable(elem.asJsObj())
                                               );
                else if (elem.isArray()) acc = acc.put(path,
                                                       array.toImmutable(elem.asJsArray())
                                                      );
                else acc = acc.put(path,
                                   elem
                                  );

            }

            return acc;

        }


        /**
         Returns the immutable empty object. The same instance is always returned.
         @return the singleton immutable empty JsObj
         */
        public JsObj empty()
        {
            return empty;
        }


        /**
         Tries to parse the string into an immutable object.
         @param str the string to be parsed
         @return a {@link TryObj} computation
         */
        public TryObj parse(final String str)
        {

            try (JsonParser parser = Jsons.factory.createParser(requireNonNull(str)))
            {
                JsonToken keyEvent = parser.nextToken();
                if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
                return new TryObj(new ImmutableJsObj(emptyMap.parse(ImmutableJsons.this,
                                                                    parser
                                                                   ),
                                                     ImmutableJsons.this
                ));
            }

            catch (IOException e)
            {
                return new TryObj(new MalformedJson(e.getMessage()));
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
        public TryObj parse(final String str,
                            final ParseBuilder builder
                           )
        {

            try (JsonParser parser = Jsons.factory.createParser(requireNonNull(str.getBytes())))
            {
                final JsonToken keyEvent = parser.nextToken();
                if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
                return new TryObj(new ImmutableJsObj(emptyMap.parse(ImmutableJsons.this,
                                                                    parser,
                                                                    requireNonNull(builder).create(),
                                                                    JsPath.empty()
                                                                   ),
                                                     ImmutableJsons.this
                )
                );


            }
            catch (IOException e)
            {
                return new TryObj(new MalformedJson(e.getMessage()));
            }
        }

        /**
         Returns a one-element immutable object.
         @param key name of a key
         @param el  JsElem to be associated to the key
         @return an immutable one-element JsObj
         @throws UserError if an elem is a mutable Json
         */
        public JsObj of(final String key,
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
        public JsObj of(final String key1,
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
         Returns a three-element immutable object.
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
        public JsObj of(final String key1,
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
        public JsObj of(final String key1,
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
        public JsObj of(final String key1,
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
        public JsObj of(final String key1,
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
         Returns an immutable object from one or more pairs.
         @param pair a pair
         @param others more optional pairs
         @return an immutable JsObject
         @throws UserError if an elem of a pair is mutable

         */
        public JsObj of(final JsPair pair,
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

        public JsObj ofIterable(Iterable<Map.Entry<String, JsElem>> xs)
        {
            JsObj acc = empty();
            for (Map.Entry<String, JsElem> x : requireNonNull(xs))
            {
                if (requireNonNull(requireNonNull(x).getValue()).isJson(Json::isMutable)) throw UserError.immutableArgExpected(x.getValue());

                acc = acc.put(JsPath.fromKey(x.getKey()),
                              x.getValue()
                             );
            }
            return acc;
        }

    }

    /**
     represents a factory of immutable Json arrays
     */
    public final ImmutableJsArrays array;
    /**
     represents a factory of immutable Json objects
     */
    public final ImmutableJsObjs object;


    ImmutableJsons()
    {

        this.array = new ImmutableJsArrays();
        this.object = new ImmutableJsObjs();
    }


}
