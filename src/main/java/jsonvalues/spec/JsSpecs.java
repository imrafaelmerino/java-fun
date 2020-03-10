package jsonvalues.spec;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

public class JsSpecs
{

  public static JsPredicate isStr = value ->
    value.isStr()  ? Optional.empty() : Optional.of(new Error(value,STRING_EXPECTED));

  public static JsPredicate isLong = value ->
    value.isLong()  ? Optional.empty() : Optional.of(new Error(value,LONG_EXPECTED));

  public static JsPredicate isDecimal = value ->
    value.isDecimal()  ? Optional.empty() : Optional.of(new Error(value,DECIMAL_EXPECTED));

  public static JsPredicate isInt = value ->
    value.isInt()  ? Optional.empty() : Optional.of(new Error(value,INT_EXPECTED));

  public static JsPredicate isIntegral = value ->
    value.isIntegral()  ? Optional.empty() : Optional.of(new Error(value,INTEGRAL_EXPECTED));

  public static JsPredicate isBoolean = value ->
    value.isBool()  ? Optional.empty() : Optional.of(new Error(value,BOOLEAN_EXPECTED));

  public static JsPredicate isTrue= value ->
    value.isTrue()  ? Optional.empty() : Optional.of(new Error(value,TRUE_EXPECTED));

  public static JsPredicate isFalse = value ->
    value.isBool()  ? Optional.empty() : Optional.of(new Error(value,FALSE_EXPECTED));

  public static JsPredicate isObj = value ->
    value.isObj()  ? Optional.empty() : Optional.of(new Error(value,OBJ_EXPECTED));

  public static JsPredicate isArray = value ->
    value.isArray()  ? Optional.empty() : Optional.of(new Error(value,ARRAY_EXPECTED));

  public static JsPredicate isStr(boolean required, boolean nullable) {
    return value -> {
      final Optional<Error> error = isStr.test(value);
      if(error.isPresent()) return error;
      if(value.isNull() && !nullable) return Optional.of(new Error(value,ERROR_CODE.NULL));
      if(value.isNothing() && required) return Optional.of(new Error(value,REQUIRED));
      return Optional.empty();
    };
  }

  public static JsPredicate isStr(Predicate<String> predicate) {
    return value ->
    {
      if(predicate.test(value.toJsStr().value)) return Optional.empty();
      else return  Optional.of(new Error(value, STRING_CONDITION));
    };
  }

  public static JsPredicate isInt(IntPredicate predicate) {
    return value ->
    {
      if(predicate.test(value.toJsInt().value)) return Optional.empty();
      else return  Optional.of(new Error(value, INT_CONDITION));
    };
  }

  public static JsPredicate isLong(LongPredicate predicate) {
    return value ->
    {
      if(predicate.test(value.toJsLong().value)) return Optional.empty();
      else return  Optional.of(new Error(value, LONG_CONDITION));
    };
  }

  public static JsPredicate isDecimal(Predicate<BigDecimal> predicate) {
    return value ->
    {
      if(predicate.test(value.toJsBigDec().value)) return Optional.empty();
      else return  Optional.of(new Error(value, DECIMAL_CONDITION));
    };
  }

  public static JsPredicate isIntegral(Predicate<BigInteger> predicate) {
    return value ->
    {
      if(predicate.test(value.toJsBigInt().value)) return Optional.empty();
      else return  Optional.of(new Error(value, INTEGRAL_CONDITION));
    };
  }
}
