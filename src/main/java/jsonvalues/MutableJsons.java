package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.*;

/**
 Factory to create mutable jsons. New factories can be created with different map and seq implementations using
 the methods {@link MutableJsons#withMap(Class)} and {@link MutableJsons#withSeq(Class)}.
 */
public class MutableJsons
{
    /**
     converts the given json using this factory to an mutable one, if it's not mutable, returning the same instance otherwise.
     @param json the given json
     @return a mutable json
     */
    @SuppressWarnings("squid:S1452") //Json requires a type and the real type is unknown
    public Json<?> toMutable(Json<?> json)
    {
        if (json.isImmutable()) return json.isObj() ? object.toMutable(json.asJsObj()) : array.toMutable(json.asJsArray());
        return json;
    }

    /**
     Tries to parse the string into an immutable json.
     @param str the string that will be parsed
     @return a {@link Try} computation
     */
    public Try parse(String str)
    {
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {

            final JsParser.Event event = parser.next();
            if (event == START_ARRAY)
            {
                final MutableSeq vector = this.array.emptySeq();
                parse(vector,
                      parser
                     );
                return new Try(new MutableJsArray(vector,
                                                  this
                ));
            }
            final MutableMap map = this.object.emptyMap();
            parse(map,
                  parser
                 );
            return new Try(new MutableJsObj(map,
                                            this
            ));
        }

        catch (MalformedJson e)
        {

            return new Try(e);

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
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {
            final JsParser.Event event = parser.next();
            if (event == START_ARRAY)
            {
                final MutableSeq vector = this.array.emptySeq();
                parse(vector,
                      parser,
                      builder.create(),
                      JsPath.fromIndex(-1)
                     );
                return new Try(new MutableJsArray(vector,
                                                  this
                ));
            }
            final MutableMap map = this.object.emptyMap();
            parse(map,
                  parser,
                  builder.create(),
                  JsPath.empty()
                 );
            return new Try(new MutableJsObj(map,
                                            this
            ));
        }

        catch (MalformedJson e)
        {

            return new Try(e);

        }
    }


    private void parse(final MutableMap root,
                       final JsParser parser
                      ) throws MalformedJson
    {
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            JsParser.Event elem = parser.next();
            assert elem != null;
            switch (elem.code)
            {
                case 0:
                    root.update(key,
                                parser.getJsString()
                               );
                    break;
                case 1:
                    root.update(key,
                                parser.getJsNumber()
                               );
                    break;
                case 2:
                    root.update(key,
                                FALSE
                               );
                    break;
                case 3:
                    root.update(key,
                                TRUE
                               );
                    break;
                case 4:
                    root.update(key,
                                NULL
                               );
                    break;
                case 5:
                    final MutableMap obj = this.object.emptyMap();
                    parse(obj,
                          parser
                         );
                    root.update(key,
                                new MutableJsObj(obj,
                                                 this
                                )
                               );
                    break;
                case 6:
                    final MutableSeq arr = this.array.emptySeq();
                    parse(arr,
                          parser
                         );
                    root.update(key,
                                new MutableJsArray(arr,
                                                   this
                                )
                               );
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());


            }


        }
    }

    private void parse(final MutableMap root,
                       final JsParser parser,
                       final ParseBuilder.Options options,
                       final JsPath path
                      ) throws MalformedJson
    {
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            JsParser.Event elem = parser.next();
            assert elem != null;
            switch (elem.code)
            {
                case 0:
                    JsPair.of(currentPath,
                              parser.getJsString()
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap.apply(p)
                                                     )
                                    );

                    break;
                case 1:
                    JsPair.of(currentPath,
                              parser.getJsNumber()
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap.apply(p)
                                                     )
                                    );

                    break;
                case 2:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap
                                                      .apply(p)
                                                     )
                                    );

                    break;
                case 3:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap
                                                      .apply(p)
                                                     )
                                    );

                    break;
                case 4:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap
                                                      .apply(p)
                                                     )
                                    );

                    break;
                case 5:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MutableMap obj = this.object.emptyMap();
                        parse(obj,
                              parser,
                              options,
                              currentPath
                             );
                        root.update(key,
                                    new MutableJsObj(obj,
                                                     this
                                    )
                                   );
                    }
                    break;
                case 6:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MutableSeq arr = this.array.emptySeq();
                        parse(arr,
                              parser,
                              options,
                              currentPath.index(-1)
                             );
                        root.update(key,
                                    new MutableJsArray(arr,
                                                       this
                                    )
                                   );
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }


        }


    }

    private void parse(final MutableSeq root,
                       final JsParser parser
                      ) throws MalformedJson
    {
        JsParser.Event elem;
        while ((elem = parser.next()) != END_ARRAY)
        {
            assert elem != null;
            switch (elem.code)
            {
                case 0:
                    root.appendBack(parser.getJsString());
                    break;
                case 1:
                    root.appendBack(parser.getJsNumber());
                    break;
                case 2:
                    root.appendBack(FALSE);
                    break;
                case 3:
                    root.appendBack(TRUE);
                    break;
                case 4:
                    root.appendBack(NULL);
                    break;
                case 5:
                    final MutableMap obj = this.object.emptyMap();
                    parse(obj,
                          parser
                         );
                    root.appendBack(new MutableJsObj(obj,
                                                     this
                    ));
                    break;
                case 6:
                    final MutableSeq arr = this.array.emptySeq();
                    parse(arr,
                          parser
                         );
                    root.appendBack(new MutableJsArray(arr,
                                                       this
                    ));
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }
        }
    }

    private void parse(final MutableSeq root,
                       final JsParser parser,
                       final ParseBuilder.Options options,
                       final JsPath path
                      ) throws MalformedJson
    {
        JsParser.Event elem;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            assert elem != null;
            final JsPath currentPath = path.inc();
            switch (elem.code)
            {
                case 0:
                    JsPair.of(currentPath,
                              parser.getJsString()
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case 1:
                    JsPair.of(currentPath,
                              parser.getJsNumber()
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case 2:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case 3:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case 4:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case 5:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MutableMap obj = this.object.emptyMap();
                        parse(obj,
                              parser,
                              options,
                              currentPath
                             );
                        root.appendBack(new MutableJsObj(obj,
                                                         this
                        ));
                    }
                    break;

                case 6:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MutableSeq arr = this.array.emptySeq();
                        parse(arr,
                              parser,
                              options,
                              currentPath.index(-1)
                             );

                        root.appendBack(new MutableJsArray(arr,
                                                           this
                        ));
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }
        }
    }

    /**
     represents a factory of mutable Json arrays
     */
    public class MutableJsArrays
    {

        private final Class<? extends MutableSeq> vector;

        MutableJsArrays(final Class<? extends MutableSeq> seq)
        {
            this.vector = seq;

        }

        public JsArray toMutable(JsArray array)
        {
            JsArray acc = empty();
            array.forEach(MatchExp.accept(acc::append,
                                          obj -> acc.append(object.toMutable(obj)),
                                          arr -> acc.append(toMutable(arr))
                                         ));

            return acc;

        }

        /**
         Returns a collector that accumulates the pairs from a stream into a mutable array.
         @return a Collector which collects all the pairs of elements into a mutable JsArray, in encounter order
         */
        public Collector<JsPair, JsArray, JsArray> collector()
        {

            return Collector.of(this::empty,
                                (arr, pair) ->
                                {
                                    if (pair.elem.isObj()) arr.put(pair.path,
                                                                   object.toMutable(pair.elem.asJsObj())
                                                                  );
                                    else if (pair.elem.isArray()) arr.put(pair.path,
                                                                          array.toMutable(pair.elem.asJsArray())
                                                                         );
                                    else arr.put(pair.path,
                                                 pair.elem
                                                );
                                },
                                (a, b) -> new OpCombinerArrs(a,
                                                             b
                                ).combine()
                                 .get()
                               );

        }


        private MutableSeq emptySeq()
        {
            try
            {
                final MutableSeq seq = vector.getDeclaredConstructor()
                                             .newInstance();
                if (!seq.isEmpty()) throw UserError.defaultConstructorShouldCreateEmptyVector();
                return seq;
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                throw UserError.wrongVectorImplementation(e);
            }
        }

        /**
         Returns a mutable empty array.
         @return mutable empty JsArray
         */
        public JsArray empty()
        {
            return new MutableJsArray(emptySeq(),
                                      MutableJsons.this
            );
        }

        /**
         Tries to parse the string into a mutable json array.
         @param str the string to be parsed
         @return a {@link TryArr} computation
         */
        public TryArr parse(final String str)
        {
            try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
            {
                JsParser.Event keyEvent = parser.next();
                if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
                MutableSeq seq = emptySeq();
                MutableJsons.this.parse(seq,
                                        parser
                                       );
                return new TryArr(new MutableJsArray(seq,
                                                     MutableJsons.this
                ));
            }

            catch (MalformedJson e)
            {

                return new TryArr(e);
            }

        }

        /**
         Tries to parse the string into an mutable array, performing the specified transformations during the parsing.
         It's faster to do certain operations right while the parsing instead of doing the parsing and applying them later.
         @param str string to be parsed
         @param builder builder with the transformations that will be applied during the parsing
         @return a {@link TryArr} computation
         */
        public TryArr parse(final String str,
                            final ParseBuilder builder
                           )
        {
            try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
            {
                JsParser.Event keyEvent = parser.next();
                if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
                MutableSeq seq = emptySeq();
                MutableJsons.this.parse(seq,
                                        parser,
                                        builder.create(),
                                        JsPath.fromIndex(-1)
                                       );
                return new TryArr(new MutableJsArray(seq,
                                                     MutableJsons.this
                ));
            }

            catch (MalformedJson e)
            {

                return new TryArr(e);
            }

        }


        /**
         Returns a mutable array from one or more strings.
         @param str a string
         @param others more optional strings
         @return a mutable JsArray
         */
        public JsArray of(final String str,
                          final String... others
                         )
        {

            final MutableSeq v = emptySeq();
            v.appendBack(JsStr.of(str));
            for (String a : others)
            {
                v.appendBack(JsStr.of(a));
            }
            return new MutableJsArray(v,
                                      MutableJsons.this
            );
        }

        /**
         Returns a mutable one-element array.
         @param e a JsElem
         @return a mutable one-element JsArray
         @throws UserError if the elem is an immutable Json
         */
        public JsArray of(final JsElem e)
        {
            if (requireNonNull(e).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(e);

            return empty().append(e);
        }

        /**
         Returns an mutable two-element array.
         @param e a JsElem
         @param e1 a JsElem
         @return an mutable two-element JsArray
         @throws UserError if an elem is an immutable Json
         */
        public JsArray of(final JsElem e,
                          final JsElem e1
                         )
        {
            if (requireNonNull(e1).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(e1);
            return of(e).append(e1);
        }

        /**
         Returns an mutable three-element array.
         @param e  a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @return an mutable three-element JsArray
         @throws UserError if an elem is a immutable Json
         */
        public JsArray of(final JsElem e,
                          final JsElem e1,
                          final JsElem e2
                         )
        {
            if (requireNonNull(e2).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(e2);

            return of(e,
                      e1
                     ).append(e2);
        }

        /**
         Returns a mutable four-element array.
         @param e a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @param e3 a JsElem
         @return an mutable four-element JsArray
         @throws UserError if an elem is an immutable Json
         */
        public JsArray of(final JsElem e,
                          final JsElem e1,
                          final JsElem e2,
                          final JsElem e3
                         )
        {
            if (requireNonNull(e3).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(e3);

            return of(e,
                      e1,
                      e2
                     ).append(e3);
        }

        /**
         Returns an mutable five-element array.
         @param e a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @param e3 a JsElem
         @param e4 a JsElem
         @return an mutable five-element JsArray
         @throws UserError if an elem is a immutable Json
         */
        //squid:S00107: 5 args is ok in this case
        @SuppressWarnings("squid:S00107")
        public JsArray of(final JsElem e,
                          final JsElem e1,
                          final JsElem e2,
                          final JsElem e3,
                          final JsElem e4
                         )
        {
            if (requireNonNull(e4).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(e4);

            return of(e,
                      e1,
                      e2,
                      e3
                     ).append(e4);
        }

        /**
         Returns an mutable array.
         @param e a JsElem
         @param e1 a JsElem
         @param e2 a JsElem
         @param e3 a JsElem
         @param e4 a JsElem
         @param rest more optional JsElem
         @return an mutable JsArray
         @throws UserError if an elem is an immutable Json
         */
        //squid:S00107: 6 args is ok in this case
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
                if (requireNonNull(other).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(other);
                result = result.append(other);
            }
            return result;


        }

        /**
         Returns a mutable array from one or more integers.
         @param number an integer
         @param others more optional integers
         @return a mutable JsArray
         */
        public JsArray of(final int number,
                          final int... others
                         )
        {

            final MutableSeq seq = emptySeq();
            seq.appendBack(JsInt.of(number));
            for (int a : others)
            {
                seq.appendBack(JsInt.of(a));
            }
            return new MutableJsArray(seq,
                                      MutableJsons.this
            );
        }

        /**
         Returns a mutable array from one or more longs.
         @param number a long
         @param others more optional longs
         @return a mutable JsArray
         */
        public JsArray of(final long number,
                          final long... others
                         )
        {

            final MutableSeq seq = emptySeq();
            seq.appendBack(JsLong.of(number));
            for (long a : others)
            {
                seq.appendBack(JsLong.of(a));
            }
            return new MutableJsArray(seq,
                                      MutableJsons.this
            );
        }

        /**
         Returns a mutable array from one or more booleans.
         @param bool a boolean
         @param others more optional booleans
         @return a mutable JsArray
         */
        public JsArray of(final boolean bool,
                          final boolean... others
                         )
        {
            final MutableSeq seq = emptySeq();
            seq.appendBack(JsBool.of(bool));
            for (boolean a : others)
            {
                seq.appendBack(JsBool.of(a));
            }
            return new MutableJsArray(seq,
                                      MutableJsons.this
            );
        }

        /**
         Returns a mutable array from one or more doubles.
         @param number a double
         @param others more optional doubles
         @return a mutable JsArray
         */
        public JsArray of(final double number,
                          final double... others
                         )
        {
            final MutableSeq seq = emptySeq();
            emptySeq().appendBack(JsDouble.of(number));
            for (double a : others)
            {
                seq.appendBack(JsDouble.of(a));
            }
            return new MutableJsArray(seq,
                                      MutableJsons.this
            );
        }

        public JsArray ofIterable(Iterable<JsElem> iterable)
        {
            MutableSeq seq = emptySeq();
            for (JsElem e : iterable)
            {
                if (requireNonNull(e).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(e);

                seq.appendBack(e);
            }
            return new MutableJsArray(seq,
                                      MutableJsons.this
            );
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
            if (requireNonNull(pair).elem.isJson(Json::isImmutable)) throw UserError.mutableArgExpected(pair.elem);
            JsArray arr = empty().put(pair.path,
                                      pair.elem
                                     );
            for (JsPair p : others)
            {
                if (requireNonNull(p).elem.isJson(Json::isImmutable)) throw UserError.mutableArgExpected(p.elem);

                arr.put(p.path,
                        p.elem
                       );
            }
            return arr;

        }

    }

    /**
     represents a factory of mutable Json objects
     */
    public class MutableJsObjs
    {

        final Class<? extends MutableMap> map;

        MutableJsObjs(final Class<? extends MutableMap> map)
        {
            this.map = map;
        }

        MutableMap emptyMap()
        {
            try
            {
                final MutableMap mymap = map.getDeclaredConstructor()
                                            .newInstance();
                if (!mymap.isEmpty()) throw UserError.defaultConstructorShouldCreateEmptyMap();
                return mymap;
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                throw UserError.wrongMapImplementation(e);
            }
        }


        public JsObj toMutable(JsObj map)
        {
            if (map.isMutable()) return map;
            JsObj acc = empty();
            @SuppressWarnings("squid:S1905")// in return checkerframework does its job!
            final Set<@KeyFor("map") String> keys = (Set<@KeyFor("map") String>) map.fields();
            keys.forEach(key -> MatchExp.accept(val -> acc.put(JsPath.fromKey(key),
                                                               val
                                                              ),
                                                obj -> acc.put(JsPath.fromKey(key),
                                                               toMutable(obj)
                                                              ),
                                                arr -> acc.put(JsPath.fromKey(key),
                                                               array.toMutable(arr)
                                                              )
                                               )
                                        .accept(map.get(Key.of(key)))
                        );
            return acc;
        }


        /**
         Returns a collector that accumulates the pairs from a stream into an mutable object.
         @return a Collector which collects all the pairs of elements into an mutable JsObj, in encounter order
         */
        public Collector<JsPair, JsObj, JsObj> collector()
        {

            return Collector.of(this::empty,
                                (obj, pair) ->
                                {
                                    if (pair.elem.isObj()) obj.put(pair.path,
                                                                   object.toMutable(pair.elem.asJsObj())
                                                                  );
                                    else if (pair.elem.isArray()) obj.put(pair.path,
                                                                          array.toMutable(pair.elem.asJsArray())
                                                                         );
                                    else obj.put(pair.path,
                                                 pair.elem
                                                );
                                },
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
        public JsObj empty()
        {
            return new MutableJsObj(emptyMap(),
                                    MutableJsons.this
            );
        }


        /**
         Tries to parse the string into an immutable object.
         @param str the string to be parsed
         @return a {@link TryObj} computation
         */
        public TryObj parse(final String str)
        {
            try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
            {
                JsParser.Event keyEvent = parser.next();
                if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
                final MutableMap obj = emptyMap();
                MutableJsons.this.parse(obj,
                                        parser
                                       );
                return new TryObj(new MutableJsObj(obj,
                                                   MutableJsons.this
                ));
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
        public TryObj parse(final String str,
                            final ParseBuilder builder
                           )
        {
            try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
            {
                JsParser.Event keyEvent = parser.next();
                if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
                final MutableMap obj = emptyMap();
                MutableJsons.this.parse(obj,
                                        parser,
                                        builder.create(),
                                        JsPath.empty()
                                       );
                return new TryObj(new MutableJsObj(obj,
                                                   MutableJsons.this
                ));
            }
            catch (MalformedJson e)
            {
                return new TryObj(e);
            }
        }

        /**
         Returns a one-element mutable object.
         @param key name of a key
         @param el  JsElem to be associated to the key
         @return an mutable one-element JsObj
         @throws UserError if an elem is an immutable Json
         */
        public JsObj of(final String key,
                        final JsElem el
                       )
        {
            if (requireNonNull(el).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el);

            return empty().put(JsPath.empty()
                                     .key(requireNonNull(key)),
                               el
                              );
        }

        /**
         Returns a two-element mutable object.
         @param key1 name of a key
         @param el1  JsElem to be associated to the key1
         @param key2 name of a key
         @param el2  JsElem to be associated to the key2
         @return an mutable two-element JsObj
         @throws UserError if an elem is an immutable Json
         */
        public JsObj of(final String key1,
                        final JsElem el1,
                        final String key2,
                        final JsElem el2
                       )
        {
            if (requireNonNull(el2).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el2);

            return of(key1,
                      el1
                     ).put(JsPath.empty()
                                 .key(requireNonNull(key2)),
                           el2
                          );
        }

        /**
         Returns a three-element mutable object.
         @param key1 name of a key
         @param el1  JsElem to be associated to the key1
         @param key2 name of a key
         @param el2  JsElem to be associated to the key2
         @param key3 name of a key
         @param el3  JsElem to be associated to the key3
         @return an mutable three-element JsObj
         @throws UserError if an elem is an immutable Json
         */
        //squid:S00107: 6 args is ok in this case
        @SuppressWarnings("squid:S00107")
        public JsObj of(final String key1,
                        final JsElem el1,
                        final String key2,
                        final JsElem el2,
                        final String key3,
                        final JsElem el3
                       )
        {
            if (requireNonNull(el3).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el3);
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
         Returns a four-element mutable object.
         @param key1 name of a key
         @param el1  JsElem to be associated to the key1
         @param key2 name of a key
         @param el2  JsElem to be associated to the key2
         @param key3 name of a key
         @param el3  JsElem to be associated to the key3
         @param key4 name of a key
         @param el4 JsElem to be associated to the key4
         @return an mutable four-element JsObj
         @throws UserError if an elem is an immutable Json
         */
        //squid:S00107: 8 args is ok in this case
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
            if (requireNonNull(el4).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el4);

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
         Returns a five-element mutable object.
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
         @return an mutable five-element JsObj
         @throws UserError if an elem is an immutable Json
         */
        //squid:S00107: 10 args is ok in this case
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
            if (requireNonNull(el5).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el5);

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
         Returns a six-element mutable object.
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
         @return an mutable six-element JsObj
         @throws UserError if an elem is an immutable Json
         */
        //squid:S00107: 12 args is ok in this case
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
            if (requireNonNull(el6).isJson(Json::isImmutable)) throw UserError.mutableArgExpected(el6);

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
            if (requireNonNull(pair).elem.isJson(Json::isImmutable)) throw UserError.mutableArgExpected(pair.elem);
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


    }

    /**
     represents a factory of mutable Json arrays
     */
    public final MutableJsArrays array;
    /**
     represents a factory of mutable Json objects
     */
    public final MutableJsObjs object;


    MutableJsons(final Class<? extends MutableMap> map,
                 final Class<? extends MutableSeq> seq
                )
    {
        this.array = new MutableJsArrays(seq);
        this.object = new MutableJsObjs(map);
    }

    private MutableJsons(final MutableJsObjs obj,
                         final MutableJsArrays array
                        )
    {
        this.array = array;
        this.object = obj;
    }

    /**
     returns a new factory of mutable Json using as underlying data structure to store elements of Json objects
     the given as a parameter
     @param map the underlying data structure to store elements of Json objects in the factory
     @return a new factory of mutable Jsons
     */
    public MutableJsons withMap(final Class<? extends MutableMap> map)
    {

        return new MutableJsons(new MutableJsObjs(map),
                                this.array
        );
    }

    /**
     returns a new factory of mutable Json using as underlying data structure to store elements of Json arrays
     the given as a parameter
     @param seq the underlying data structure to store elements of Json arrays in the factory
     @return a new factory of mutable Jsons
     */
    public MutableJsons withSeq(final Class<? extends MutableSeq> seq)
    {

        return new MutableJsons(this.object,
                                new MutableJsArrays(seq)
        );
    }
}
