package jsonvalues;

import jsonvalues.JsParser.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.*;

import static java.util.Objects.requireNonNull;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.END_ARRAY;
import static jsonvalues.JsParser.Event.END_OBJECT;
import static jsonvalues.MyScalaImpl.Map.EMPTY;

class Functions
{

    private Functions()
    {
    }


    static JsElem get(final JsElem elem,
                      final JsPath path
                     )
    {
        assert elem != null;
        assert path != null;
        if (path.isEmpty()) return elem;
        if (elem.isNotJson() || elem.isNothing()) return JsNothing.NOTHING;
        return get(elem.asJson()
                       .get(path.head()),
                   path.tail()
                  );

    }

    /**
     Declarative way of implementing if(this.isArray()) return ifArr.apply(this.asJsArray()) else return ifNotArr.apply(this)
     @param ifArr the function to be applied if this JsElem is a JsArray
     @param ifNotArr the function to be applied if this JsElem is not a JsArray
     @param <T> the type of the object returned
     @return an object of type T
     */
    static <T> Function<JsElem, T> ifArrElse(final Function<? super JsArray, T> ifArr,
                                             final Function<? super JsElem, T> ifNotArr

                                            )
    {

        return elem -> elem.isArray() ? requireNonNull(ifArr).apply(elem.asJsArray()) : requireNonNull(ifNotArr).apply(elem);
    }

    /**
     Declarative way of implementing if(this.isBool()) return ifBoolean.get() else ifNotBoolean.get()
     @param ifBoolean the function to be applied if this JsElem is a JsBool
     @param ifNotBoolean the function to be applied if this JsElem is not a JsBool
     @param <T> the type of the object returned
     @return an object of type T
     */
    static <T> Function<JsElem, T> ifBoolElse(final Function<? super Boolean, T> ifBoolean,
                                              final Function<? super JsElem, T> ifNotBoolean
                                             )
    {
        return e -> e.isBool() ? requireNonNull(ifBoolean).apply(e.asJsBool().x) : requireNonNull(ifNotBoolean).apply(e);
    }

    /**
     Declarative way of returning an object based on the type of decimal number this element is
     @param ifDouble the function to be applied if this JsElem is a JsDouble
     @param ifBigDecimal the function to be applied if this JsElem is a JsBigDec
     @param ifOther the function to be applied if this JsElem is a not a decimal JsNumber
     @param <T> the type of the object returned
     @return an object of type T
     */
    static <T> Function<JsElem, T> ifDecimalElse(final DoubleFunction<T> ifDouble,
                                                 final Function<BigDecimal, T> ifBigDecimal,
                                                 final Function<? super JsElem, T> ifOther
                                                )
    {
        return elem ->
        {
            if (elem.isBigDec()) return requireNonNull(ifBigDecimal).apply(elem.asJsBigDec().x);
            if (elem.isDouble()) return requireNonNull(ifDouble).apply(elem.asJsDouble().x);
            return requireNonNull(ifOther).apply(elem);
        };

    }


    static <T> Function<JsElem, T> ifIntegralElse(final IntFunction<T> ifInt,
                                                  final LongFunction<T> ifLong,
                                                  final Function<BigInteger, T> ifBigInt,
                                                  final Function<? super JsElem, T> ifOther
                                                 )
    {
        return elem ->
        {
            if (elem.isLong()) return requireNonNull(ifLong).apply(elem.asJsLong().x);
            if (elem.isInt()) return requireNonNull(ifInt).apply(elem.asJsInt().x);
            if (elem.isBigInt()) return requireNonNull(ifBigInt).apply(elem.asJsBigInt().x);
            return requireNonNull(ifOther).apply(elem);
        };

    }

    static <T> Function<JsElem, T> ifJsonElse(final Function<? super JsObj, T> ifObj,
                                              final Function<? super JsArray, T> ifArr,
                                              final Function<? super JsElem, T> ifValue
                                             )
    {

        return elem ->
        {


            if (elem.isObj()) return ifObj.apply(elem.asJsObj());
            if (elem.isArray()) return ifArr.apply(elem.asJsArray());
            return ifValue.apply(elem);
        };
    }

    static <T> Function<JsElem, T> ifJsonElse(final Function<Json<?>, T> ifJson,
                                              final Function<JsElem, T> ifNotJson
                                             )
    {

        return elem -> requireNonNull(elem).isJson() ? requireNonNull(ifJson).apply(elem.asJson()) : requireNonNull(ifNotJson).apply(elem);
    }


    static <T> Function<JsElem, T> ifNothingElse(final Supplier<T> nothingSupplier,
                                                 final Function<JsElem, T> elseFn
                                                )
    {

        return elem -> elem.isNothing() ? requireNonNull(nothingSupplier).get() : requireNonNull(elseFn).apply(elem);
    }

    static <T> Function<JsElem, T> ifObjElse(final Function<? super JsObj, T> ifObj,
                                             final Function<? super JsElem, T> ifNotObj
                                            )
    {
        return elem ->
        {
            if (elem.isObj()) return requireNonNull(ifObj).apply(elem.asJsObj());
            else return requireNonNull(ifNotObj).apply(elem);
        };
    }

    static <T> Function<JsElem, T> ifPredicateElse(final Predicate<JsElem> predicate,
                                                   final Function<JsElem, T> ifTrue,
                                                   final Function<JsElem, T> ifFalse
                                                  )
    {

        return elem ->
        {
            if (requireNonNull(predicate).test(elem)) return requireNonNull(ifTrue).apply(elem);
            return requireNonNull(ifFalse).apply(elem);
        };
    }

    static <T> Function<JsElem, T> ifStrElse(final Function<? super String, T> ifStr,
                                             final Function<? super JsElem, T> ifNotStr
                                            )
    {
        return elem -> elem.isStr() ? requireNonNull(ifStr).apply(elem.asJsStr().x) : requireNonNull(ifNotStr).apply(elem);
    }

    static <T> Function<JsElem, T> ifValueElse(final Function<JsElem, T> ifValue,
                                               final Function<JsObj, T> ifObj,
                                               final Function<JsArray, T> ifArray
                                              )
    {

        return e ->
        {
            if (e.isNotJson()) return requireNonNull(ifValue).apply(e);
            if (e.isObj()) return requireNonNull(ifObj).apply(e.asJsObj());
            else return requireNonNull(ifArray).apply(e.asJsArray());
        };

    }

    static Predicate<JsElem> isSameType(final JsElem that)
    {
        return e -> e.getClass() == requireNonNull(that).getClass();

    }


    static MyScalaImpl.Vector parse(final MyScalaImpl.Vector root,
                                    final JsParser parser
                                   ) throws MalformedJson
    {

        Event elem;
        MyScalaImpl.Vector newRoot = root;
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
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
                    final MyScalaImpl.Map newObj = parse(EMPTY,
                                                         parser
                                                        );
                    newRoot = newRoot.appendBack(new JsObjImmutableImpl(newObj));
                    break;

                case START_ARRAY:
                    final MyScalaImpl.Vector newVector = parse(MyScalaImpl.Vector.EMPTY,
                                                               parser
                                                              );

                    newRoot = newRoot.appendBack(new JsArrayImmutableImpl(newVector));
                    break;
                default:
                    throw unexpectedEventError(parser.currentEvent);
            }
        }

        return newRoot;


    }

    static MyScalaImpl.Vector parse(final MyScalaImpl.Vector root,
                                    final JsParser parser,
                                    final ParseOptions.Options options,
                                    final JsPath path
                                   ) throws MalformedJson
    {
        Event elem;
        MyScalaImpl.Vector newRoot = root;
        JsPair pair;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
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
                        newRoot = newRoot.appendBack(new JsObjImmutableImpl(parse(EMPTY,
                                                                                  parser,
                                                                                  options,
                                                                                  currentPath
                                                                                 )));
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new JsArrayImmutableImpl(parse(MyScalaImpl.Vector.EMPTY,
                                                                                    parser,
                                                                                    options,
                                                                                    currentPath.index(-1)
                                                                                   )));
                    }

                    break;

                default:
                    throw unexpectedEventError(elem);


            }


        }

        return newRoot;
    }

    static void parse(MyJavaImpl.Vector root,
                      final JsParser parser
                     ) throws MalformedJson
    {
        Event elem;
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    root.appendBack(parser.getJsString());
                    break;
                case VALUE_NUMBER:
                    root.appendBack(parser.getJsNumber());
                    break;
                case VALUE_FALSE:
                    root.appendBack(FALSE);
                    break;
                case VALUE_TRUE:
                    root.appendBack(TRUE);
                    break;
                case VALUE_NULL:
                    root.appendBack(NULL);
                    break;
                case START_OBJECT:
                    final MyJavaImpl.Map obj = new MyJavaImpl.Map();

                    parse(obj,
                          parser
                         );
                    root.appendBack(new JsObjMutableImpl(obj));
                    break;

                case START_ARRAY:
                    final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                    parse(arr,
                          parser
                         );

                    root.appendBack(new JsArrayMutableImpl(arr));
                    break;
                default:
                    throw unexpectedEventError(elem);
            }
        }
    }

    static void parse(final MyJavaImpl.Vector root,
                      final JsParser parser,
                      final ParseOptions.Options options,
                      final JsPath path
                     ) throws MalformedJson
    {
        Event elem;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
            final JsPath currentPath = path.inc();
            switch (elem)
            {
                case VALUE_STRING:
                    JsPair.of(currentPath,
                              parser.getJsString()
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case VALUE_NUMBER:
                    JsPair.of(currentPath,
                              parser.getJsNumber()
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case VALUE_FALSE:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    )
                    ;
                    break;
                case VALUE_TRUE:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case VALUE_NULL:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> root.appendBack(options.elemMap.apply(p))
                                    );
                    break;
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                        parse(obj,
                              parser,
                              options,
                              currentPath
                             );
                        root.appendBack(new JsObjMutableImpl(obj));
                    }
                    break;

                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                        parse(arr,
                              parser,
                              options,
                              currentPath.index(-1)
                             );

                        root.appendBack(new JsArrayMutableImpl(arr));
                    }
                    break;
                default:
                    throw unexpectedEventError(elem);
            }
        }
    }

    static MyScalaImpl.Map parse(MyScalaImpl.Map root,
                                 final JsParser parser
                                ) throws MalformedJson
    {
        MyScalaImpl.Map newRoot = root;
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);
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
                    final MyScalaImpl.Map newObj = parse(EMPTY,
                                                         parser
                                                        );
                    newRoot = newRoot.update(key,
                                             new JsObjImmutableImpl(newObj)
                                            );
                    break;
                case START_ARRAY:
                    final MyScalaImpl.Vector newArr = parse(MyScalaImpl.Vector.EMPTY,
                                                            parser
                                                           );
                    newRoot = newRoot.update(key,
                                             new JsArrayImmutableImpl(newArr)
                                            );
                    break;
                default:
                    throw unexpectedEventError(elem);
            }
        }
        return newRoot;


    }

    static MyScalaImpl.Map parse(final MyScalaImpl.Map root,
                                 final JsParser parser,
                                 final ParseOptions.Options options,
                                 final JsPath path
                                ) throws MalformedJson
    {

        MyScalaImpl.Map newRoot = root;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          parser.getJsString()
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_NUMBER:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          parser.getJsNumber()
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_TRUE:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          TRUE
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_FALSE:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          FALSE
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_NULL:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          NULL
                                                         ),
                                                key
                                               );
                    break;

                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
                                                 new JsObjImmutableImpl(parse(EMPTY,
                                                                              parser,
                                                                              options,
                                                                              currentPath
                                                                             ))
                                                );
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
                                                 new JsArrayImmutableImpl(parse(MyScalaImpl.Vector.EMPTY,
                                                                                parser,
                                                                                options,
                                                                                currentPath.index(-1)
                                                                               ))
                                                );
                    }
                    break;
                default:
                    throw unexpectedEventError(parser.currentEvent);


            }


        }

        return newRoot;
    }

    static void parse(final MyJavaImpl.Map root,
                      final JsParser parser
                     ) throws MalformedJson
    {
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    root.update(key,
                                parser.getJsString()
                               );
                    break;
                case VALUE_NUMBER:
                    root.update(key,
                                parser.getJsNumber()
                               );
                    break;
                case VALUE_FALSE:
                    root.update(key,
                                FALSE
                               );
                    break;
                case VALUE_TRUE:
                    root.update(key,
                                TRUE
                               );
                    break;
                case VALUE_NULL:
                    root.update(key,
                                NULL
                               );
                    break;
                case START_OBJECT:
                    final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                    parse(obj,
                          parser
                         );
                    root.update(key,
                                new JsObjMutableImpl(obj)
                               );
                    break;
                case START_ARRAY:
                    final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                    parse(arr,
                          parser
                         );
                    root.update(key,
                                new JsArrayMutableImpl(arr)
                               );
                    break;
                default:
                    throw unexpectedEventError(elem);
            }


        }
    }

    static void parse(final MyJavaImpl.Map root,
                      final JsParser parser,
                      final ParseOptions.Options options,
                      final JsPath path
                     ) throws MalformedJson
    {
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);

            switch (elem)
            {
                case VALUE_STRING:
                    JsPair.of(currentPath,
                              parser.getJsString()
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap.apply(p)
                                                     )
                                    );

                    break;
                case VALUE_NUMBER:
                    JsPair.of(currentPath,
                              parser.getJsNumber()
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap.apply(p)
                                                     )
                                    );

                    break;
                case VALUE_FALSE:
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
                case VALUE_TRUE:
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
                case VALUE_NULL:
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
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                        parse(obj,
                              parser,
                              options,
                              currentPath
                             );
                        root.update(key,
                                    new JsObjMutableImpl(obj)
                                   );
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                        parse(arr,
                              parser,
                              options,
                              currentPath.index(-1)
                             );
                        root.update(key,
                                    new JsArrayMutableImpl(arr)
                                   );
                    }
                    break;
                default:
                    throw unexpectedEventError(elem);
            }


        }
    }


    private static IllegalStateException unexpectedEventError(final @Nullable Event elem)
    {

        return new IllegalStateException(String.format("Unexpected event during parsing: %s",
                                                       elem
                                                      ));
    }


    private static MyScalaImpl.Map updateIfCondition(Predicate<? super JsPair> condition,
                                                     Function<? super JsPair, ? extends JsElem> elemMap,
                                                     MyScalaImpl.Map pmap,
                                                     final JsPair pair,
                                                     final String key
                                                    )
    {

        return (condition.test(pair)) ? pmap.update(key,
                                                    elemMap.apply(pair)
                                                   ) : pmap;
    }


}
