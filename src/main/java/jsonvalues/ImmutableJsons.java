package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.*;

/**
 Factory to create immutable jsons. New factories can be created with different map and seq implementations using
 the methods {@link ImmutableJsons#withMap(Class)} and {@link ImmutableJsons#withSeq(Class)}.
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
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {

            final JsParser.Event event = parser.next();
            if (event == START_ARRAY)
            {
                return new Try(new ImmutableJsArray(parse(array.emptySeq,
                                                          parser
                                                         ),
                                                    this
                ));
            }
            return new Try(new ImmutableJsObj(parse(object.emptyMap,
                                                    parser
                                                   ),
                                              this
            )
            );
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
            if (event == START_ARRAY) return new Try(new ImmutableJsArray(parse(array.emptySeq,
                                                                                parser,
                                                                                builder.create(),
                                                                                JsPath.empty()
                                                                                      .index(-1)

                                                                               ),
                                                                          this
            )
            );
            return new Try(new ImmutableJsObj(parse(object.emptyMap,
                                                    parser,
                                                    builder.create(),
                                                    JsPath.empty()

                                                   ),
                                              this
            )
            );
        }

        catch (MalformedJson e)
        {

            return new Try(e);

        }


    }

    private ImmutableSeq parse(final ImmutableSeq root,
                               final JsParser parser
                              ) throws MalformedJson
    {
        JsParser.Event elem;
        ImmutableSeq newRoot = root;
        while ((elem = parser.next()) != END_ARRAY)
        {
            switch (elem)
            {
                case VALUE_STRING:
                    newRoot = newRoot.appendBack(parser.getJsString());
                    break;
                case VALUE_NUMBER:
                    newRoot = newRoot.appendBack(parser.getJsNumber());
                    break;
                case VALUE_FALSE:
                    newRoot = newRoot.appendBack(FALSE);
                    break;
                case VALUE_TRUE:
                    newRoot = newRoot.appendBack(TRUE);
                    break;
                case VALUE_NULL:
                    newRoot = newRoot.appendBack(NULL);
                    break;
                case START_OBJECT:
                    final ImmutableMap newObj = parse(this.object.emptyMap,
                                                      parser
                                                     );
                    newRoot = newRoot.appendBack(new ImmutableJsObj(newObj,
                                                                    this
                    ));
                    break;
                case START_ARRAY:
                    final ImmutableSeq newSeq = parse(this.array.emptySeq,
                                                      parser
                                                     );
                    newRoot = newRoot.appendBack(new ImmutableJsArray(newSeq,
                                                                      this
                    ));
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());
            }
        }
        return newRoot;

    }

    private ImmutableMap parse(final ImmutableMap root,
                               final JsParser parser,
                               final ParseBuilder.Options options,
                               final JsPath path
                              ) throws MalformedJson
    {

        ImmutableMap newRoot = root;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            JsParser.Event elem = parser.next();
            final JsPair pair;
            assert elem != null;
            switch (elem)
            {
                case VALUE_STRING:
                    pair = JsPair.of(currentPath,
                                     parser.getJsString()
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case VALUE_NUMBER:
                    pair = JsPair.of(currentPath,
                                     parser.getJsNumber()
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case VALUE_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case VALUE_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case VALUE_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;

                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
                                                 new ImmutableJsObj(parse(this.object.emptyMap,
                                                                          parser,
                                                                          options,
                                                                          currentPath
                                                                         ),
                                                                    this
                                                 )
                                                );
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
                                                 new ImmutableJsArray(parse(this.array.emptySeq,
                                                                            parser,
                                                                            options,
                                                                            currentPath.index(-1)
                                                                           ),
                                                                      this
                                                 )
                                                );
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());
            }
        }
        return newRoot;
    }

    private ImmutableSeq parse(final ImmutableSeq root,
                               final JsParser parser,
                               final ParseBuilder.Options options,
                               final JsPath path
                              ) throws MalformedJson
    {
        JsParser.Event elem;
        ImmutableSeq newRoot = root;
        JsPair pair;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            assert elem != null;
            final JsPath currentPath = path.inc();
            switch (elem)
            {
                case VALUE_STRING:

                    pair = JsPair.of(currentPath,
                                     parser.getJsString()
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case VALUE_NUMBER:
                    pair = JsPair.of(currentPath,
                                     parser.getJsNumber()
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;


                    break;

                case VALUE_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case VALUE_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case VALUE_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new ImmutableJsObj(parse(this.object.emptyMap,
                                                                              parser,
                                                                              options,
                                                                              currentPath
                                                                             ),
                                                                        this
                                                     )
                                                    );
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new ImmutableJsArray(parse(this.array.emptySeq,
                                                                                parser,
                                                                                options,
                                                                                currentPath.index(-1)
                                                                               ),
                                                                          this
                                                     )
                                                    );
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());


            }
        }
        return newRoot;
    }


    private ImmutableMap parse(final ImmutableMap root,
                               final JsParser parser
                              ) throws MalformedJson
    {
        ImmutableMap newRoot = root;
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            JsParser.Event elem = parser.next();
            switch (elem)
            {
                case VALUE_STRING:
                    newRoot = newRoot.update(key,
                                             parser.getJsString()
                                            );
                    break;
                case VALUE_NUMBER:
                    newRoot = newRoot.update(key,
                                             parser.getJsNumber()
                                            );
                    break;
                case VALUE_FALSE:
                    newRoot = newRoot.update(key,
                                             FALSE
                                            );
                    break;
                case VALUE_TRUE:
                    newRoot = newRoot.update(key,
                                             TRUE
                                            );
                    break;
                case VALUE_NULL:
                    newRoot = newRoot.update(key,
                                             NULL
                                            );
                    break;
                case START_OBJECT:
                    final ImmutableMap newObj = parse(this.object.emptyMap,
                                                      parser
                                                     );
                    newRoot = newRoot.update(key,
                                             new ImmutableJsObj(newObj,
                                                                this
                                             )
                                            );
                    break;
                case START_ARRAY:
                    final ImmutableSeq newArr = parse(this.array.emptySeq,
                                                      parser
                                                     );
                    newRoot = newRoot.update(key,
                                             new ImmutableJsArray(newArr,
                                                                  this
                                             )
                                            );
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());


            }
        }
        return newRoot;


    }

    /**
     represents a factory of immutable Json arrays
     */
    public class ImmutableJsArrays
    {

        private final ImmutableJsArray empty;
        private final ImmutableSeq emptySeq;

        ImmutableJsArrays(final Class<? extends ImmutableSeq> vector)
        {
            try
            {
                emptySeq = requireNonNull(vector).getDeclaredConstructor()
                                                 .newInstance();
                if (!emptySeq.isEmpty()) throw UserError.defaultConstructorShouldCreateEmptyVector();
                empty = new ImmutableJsArray(emptySeq,
                                             ImmutableJsons.this
                );
            }
            catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
            {
                throw UserError.wrongVectorImplementation(e);
            }


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
            try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
            {
                JsParser.Event keyEvent = parser.next();
                if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
                return new TryArr(new ImmutableJsArray(ImmutableJsons.this.parse(emptySeq,
                                                                                 parser
                                                                                ),
                                                       ImmutableJsons.this
                ));
            }

            catch (MalformedJson e)
            {

                return new TryArr(e);
            }

        }

        public TryArr parse(final String str,
                            final ParseBuilder builder
                           )
        {
            try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
            {
                JsParser.Event keyEvent = parser.next();
                if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
                return new TryArr(new ImmutableJsArray(ImmutableJsons.this.parse(emptySeq,
                                                                                 parser,
                                                                                 requireNonNull(builder).create(),
                                                                                 JsPath.fromIndex(-1)
                                                                                ),
                                                       ImmutableJsons.this
                ));
            }

            catch (MalformedJson e)
            {

                return new TryArr(e);
            }

        }

    }

    /**
     represents a factory of immutable Json objects
     */
    public class ImmutableJsObjs
    {

        private final ImmutableJsObj empty;
        private final ImmutableMap emptyMap;

        ImmutableJsObjs(final Class<? extends ImmutableMap> map)
        {
            try
            {
                emptyMap = requireNonNull(map).getDeclaredConstructor()
                                              .newInstance();
                if (!emptyMap.isEmpty()) throw UserError.defaultConstructorShouldCreateEmptyMap();
                empty = new ImmutableJsObj(emptyMap,
                                           ImmutableJsons.this
                );
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                throw UserError.wrongMapImplementation(e);
            }
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
            try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
            {
                JsParser.Event keyEvent = parser.next();
                if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
                return new TryObj(new ImmutableJsObj(ImmutableJsons.this.parse(this.emptyMap,
                                                                               parser
                                                                              ),
                                                     ImmutableJsons.this
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
                return new TryObj(new ImmutableJsObj(ImmutableJsons.this.parse(this.emptyMap,
                                                                               parser,
                                                                               requireNonNull(builder).create(),
                                                                               JsPath.empty()
                                                                              ),
                                                     ImmutableJsons.this
                )
                );
            }
            catch (MalformedJson e)
            {
                return new TryObj(e);
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


    ImmutableJsons(final Class<? extends ImmutableMap> map,
                   final Class<? extends ImmutableSeq> seq
                  )
    {
        this.array = new ImmutableJsArrays(requireNonNull(seq));
        this.object = new ImmutableJsObjs(requireNonNull(map));
    }

    private ImmutableJsons(final ImmutableJsObjs obj,
                           final ImmutableJsArrays array
                          )
    {
        this.array = requireNonNull(array);
        this.object = requireNonNull(obj);
    }

    /**
     returns a new factory of immutable Jsons using as underlying data structure to store elements of Json objects
     the given as a parameter
     @param map the underlying data structure to store elements of Json objects in the factory
     @return a new factory of immutable Jsons
     */
    public ImmutableJsons withMap(final Class<? extends ImmutableMap> map)
    {

        return new ImmutableJsons(new ImmutableJsObjs(requireNonNull(map)),
                                  this.array
        );
    }

    /**
     returns a new factory of immutable Jsons using as underlying data structure to store elements of Json arrays
     the given as a parameter
     @param seq the underlying data structure to store elements of Json arrays in the factory
     @return a new factory of immutable Jsons
     */
    public ImmutableJsons withSeq(final Class<? extends ImmutableSeq> seq)
    {

        return new ImmutableJsons(this.object,
                                  new ImmutableJsArrays(requireNonNull(seq))
        );
    }
}
