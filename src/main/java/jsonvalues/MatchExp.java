package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.*;

import static java.util.Objects.requireNonNull;

/**
 Java doesn't support Pattern Matching but we can implement some matching expressions using high
 order functions.
 */
public final class MatchExp
{

    private MatchExp()
    {
    }

    /**
     Declarative way of consuming an element based on its type
     @param ifValue the consumer to be invoked if this JsElem is a JsValue
     @param ifObj the consumer to be invoked if this JsElem is a JsObj
     @param ifArray the consumer to be invoked if this JsElem is a JsArray
     @return consumer of a json element
     */
    public static Consumer<JsElem> accept(final Consumer<JsElem> ifValue,
                                          final Consumer<JsObj> ifObj,
                                          final Consumer<JsArray> ifArray
                                         )
    {
        requireNonNull(ifValue);
        requireNonNull(ifObj);
        requireNonNull(ifArray);
        return e ->
        {
            if (e.isNotJson()) ifValue.accept(e);
            if (e.isObj()) ifObj.accept(e.asJsObj());
            if (e.isArray()) ifArray.accept(e.asJsArray());
        };

    }

    /**
     return a matching expression to extract arrays out of json elements.
     @param ifArr the function to be applied if this JsElem is a JsArray
     @param ifNotArr the function to be applied if this JsElem is not a JsArray
     @param <T> the type of the object returned
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifArrElse(final Function<? super JsArray, T> ifArr,
                                                    final Function<? super JsElem, T> ifNotArr

                                                   )
    {

        return elem -> elem.isArray() ? requireNonNull(ifArr).apply(elem.asJsArray()) : requireNonNull(ifNotArr).apply(elem);
    }

    /**
     return a matching expression to extract booleans out of json elements.
     @param ifBoolean the function to be applied if this JsElem is a JsBool
     @param ifNotBoolean the function to be applied if this JsElem is not a JsBool
     @param <T> the type of the object returned
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifBoolElse(final Function<? super Boolean, T> ifBoolean,
                                                     final Function<? super JsElem, T> ifNotBoolean
                                                    )
    {
        return e -> e.isBool() ? requireNonNull(ifBoolean).apply(e.asJsBool().x) : requireNonNull(ifNotBoolean).apply(e);
    }

    /**
     return a matching expression to extract decimal numbers out of json elements.
     @param ifDouble the function to be applied if this JsElem is a JsDouble
     @param ifBigDecimal the function to be applied if this JsElem is a JsBigDec
     @param ifOther the function to be applied if this JsElem is a not a decimal JsNumber
     @param <T> the type of the object returned
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifDecimalElse(final DoubleFunction<T> ifDouble,
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

    /**
     return a matching expression to extract integral numbers out of json elements.
     @param ifInt function to be applied if the JsElem is a JsInt
     @param ifLong function to be applied if the JsElem is a JsLong
     @param ifBigInt function to be applied if the JsElem is a JsBigInt
     @param ifOther function to be applied if the JsElem is a not an integral number
     @param <T> the type of the result
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifIntegralElse(final IntFunction<T> ifInt,
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

    /**
     return a matching expression to extract objs and arrays out of json elements.
     @param ifObj function to be applied if the JsElem is a JsObj
     @param ifArr function to be applied if the JsElem is not a JsArr
     @param ifValue function to be applied if the JsElem is not a Json
     @param <T> the type of the result
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifJsonElse(final Function<? super JsObj, T> ifObj,
                                                     final Function<? super JsArray, T> ifArr,
                                                     final Function<? super JsElem, T> ifValue
                                                    )
    {

        return elem ->
        {


            if (elem.isObj()) return requireNonNull(ifObj).apply(elem.asJsObj());
            if (elem.isArray()) return requireNonNull(ifArr).apply(elem.asJsArray());
            return ifValue.apply(elem);
        };
    }

    /**
     return a matching expression to extract jsons out of json elements.
     @param ifJson function to be applied if the JsElem is a Json
     @param ifNotJson function to be applied if the JsElem is not a Json
     @param <T> the type of the result
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifJsonElse(final Function<Json<?>, T> ifJson,
                                                     final Function<JsElem, T> ifNotJson
                                                    )
    {

        return elem -> requireNonNull(elem).isJson() ? requireNonNull(ifJson).apply(elem.asJson()) : requireNonNull(ifNotJson).apply(elem);
    }

    /**
     return a matching expression to extract JsNothing out of json elements.
     @param nothingSupplier supplier to be invoked if the JsElem is JsNothing
     @param elseFn function to be applied if the JsElem is not JsNothing
     @param <T> the type of the result
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifNothingElse(final Supplier<T> nothingSupplier,
                                                        final Function<JsElem, T> elseFn
                                                       )
    {

        return elem -> elem.isNothing() ? requireNonNull(nothingSupplier).get() : requireNonNull(elseFn).apply(elem);
    }

    /**
     return a matching expression to extract json objects out of json elements.
     @param ifObj function to be applied if the JsElem is a JsObj
     @param ifNotObj function to be applied if the JsElem is not a JsObj
     @param <T> the type of the result
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifObjElse(final Function<? super JsObj, T> ifObj,
                                                    final Function<? super JsElem, T> ifNotObj
                                                   )
    {
        return elem ->
        {
            if (elem.isObj()) return requireNonNull(ifObj).apply(elem.asJsObj());
            else return requireNonNull(ifNotObj).apply(elem);
        };
    }

    /**
     declarative way of implementing an if-else using high order functions
     @param predicate the condition that will be tested on the json element
     @param ifTrue the function to be applied if the predicate is evaluated to true
     @param ifFalse the function to be applied if the predicate is evaluated to false
     @param <T> the type of the result
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifPredicateElse(final Predicate<JsElem> predicate,
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

    /**
     returns a matching expression to extract strings out of json elements.
     @param ifStr the function to be applied if the JsElem is a JsStr
     @param ifNotStr the function to be applied if the JsElem is not a JsStr
     @param <T> the type of the result
     @return a function that takes a JsElem and returns an object of type T
     */
    public static <T> Function<JsElem, T> ifStrElse(final Function<? super String, T> ifStr,
                                                    final Function<? super JsElem, T> ifNotStr
                                                   )
    {
        return elem -> elem.isStr() ? requireNonNull(ifStr).apply(elem.asJsStr().x) : requireNonNull(ifNotStr).apply(elem);
    }

}
