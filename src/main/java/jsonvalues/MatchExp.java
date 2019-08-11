package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.*;

import static java.util.Objects.requireNonNull;

public class MatchExp
{

    private MatchExp()
    {
    }

    /**
     Declarative way of consuming an element based on its type
     @param ifValue the consumer to be invoked if this JsElem is a JsValue
     @param ifObj the consumer to be invoked if this JsElem is a JsObj
     @param ifArray the consumer to be invoked if this JsElem is a JsArray
     */
    static Consumer<JsElem> accept(final Consumer<JsElem> ifValue,
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
     Declarative way of implementing if(this.isArray()) return ifArr.apply(this.asJsArray()) else return ifNotArr.apply(this)
     @param ifArr the function to be applied if this JsElem is a JsArray
     @param ifNotArr the function to be applied if this JsElem is not a JsArray
     @param <T> the type of the object returned
     @return an object of type T
     */
    public static <T> Function<JsElem, T> ifArrElse(final Function<? super JsArray, T> ifArr,
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
    public static <T> Function<JsElem, T> ifBoolElse(final Function<? super Boolean, T> ifBoolean,
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

    public static <T> Function<JsElem, T> ifJsonElse(final Function<Json<?>, T> ifJson,
                                                     final Function<JsElem, T> ifNotJson
                                                    )
    {

        return elem -> requireNonNull(elem).isJson() ? requireNonNull(ifJson).apply(elem.asJson()) : requireNonNull(ifNotJson).apply(elem);
    }

    public static <T> Function<JsElem, T> ifNothingElse(final Supplier<T> nothingSupplier,
                                                        final Function<JsElem, T> elseFn
                                                       )
    {

        return elem -> elem.isNothing() ? requireNonNull(nothingSupplier).get() : requireNonNull(elseFn).apply(elem);
    }

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

    public static <T> Function<JsElem, T> ifStrElse(final Function<? super String, T> ifStr,
                                                    final Function<? super JsElem, T> ifNotStr
                                                   )
    {
        return elem -> elem.isStr() ? requireNonNull(ifStr).apply(elem.asJsStr().x) : requireNonNull(ifNotStr).apply(elem);
    }

    public static Predicate<JsElem> isSameType(final JsElem that)
    {
        return e -> e.getClass() == requireNonNull(that).getClass();

    }
}
