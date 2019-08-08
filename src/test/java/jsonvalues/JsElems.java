package jsonvalues;


import java.math.BigDecimal;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

public class JsElems
{

    private JsElems()
    {
    }

    /**
     Returns a function that maps a json element if it's a json double, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsDouble
     @return the same this JsElem or a new JsDouble with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfDouble(DoubleUnaryOperator fn)
    {
        return element ->
        {
            if (element.isDouble()) return element.asJsDouble()
                                                  .map(requireNonNull(fn));
            return element;
        };
    }

    /**
     Returns a function that maps a json element if it's a big decimal (or a double), returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsBigDecimal or JsDouble
     @return the same this JsElem or a new JsBigDecimal with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfBigDecimal(UnaryOperator<BigDecimal> fn)
    {
        return element ->
        {
            if (element.isBigDec() || element.isDouble()) return JsBigDec.of(fn.apply(element.asJsBigDec().x));
            return element;
        };
    }

    /**
     Returns a function that maps a json element if it's a json integer, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsInt
     @return the same JsElem or a new JsInt with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfInt(final IntUnaryOperator fn)
    {
        return element ->
        {
            if (element.isInt()) return element.asJsInt()
                                               .map(requireNonNull(fn));
            return element;
        };
    }


    /**
     Returns a function that maps a json element if it's a json string, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsStr
     @return the same JsElem or a new JsStr with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfStr(final UnaryOperator<String> fn)
    {
        return element ->
        {
            if (element.isStr()) return element.asJsStr()
                                               .map(requireNonNull(fn));
            return element;
        };
    }



}
