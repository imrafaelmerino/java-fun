package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsObj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public class JsSpecs
{

  public static JsSpec isStr = new IsStr(true,false);
  public static JsSpec isBool = new IsBoolean(true,false);

  public static JsSpec isLong = new IsLong(true, false);

  public static JsSpec isDecimal = new IsDecimal(true, false);

  public static JsSpec isInt = new IsInt(true,false);

  public static JsSpec isIntegral = new IsIntegral(true, false);

  public static JsSpec isTrue= new IsTrue(true,false);

  public static JsSpec isFalse = new IsFalse(true,false);

  public static JsSpec isObj = new IsObj(true,false);

  public static JsSpec isArray = new IsArray(true,false);

  public static JsSpec isStr(boolean required, boolean nullable) {
    return new IsStr(required, nullable);
  }

  public static JsSpec isStr(Predicate<String> predicate) {
    return new IsStrSuchThat(true,
                             false,
                             predicate);
  }

  public static JsSpec isStr(boolean required,
                             boolean nullable,
                             Predicate<String> predicate) {
    return new IsStrSuchThat(required,
                             nullable,
                             predicate);
  }

  public static JsSpec isInt(boolean required, boolean nullable) {
    return new IsInt(required, nullable);
  }

  public static JsSpec isInt(IntPredicate predicate) {
    return new IsIntSuchThat(true,false,predicate);
  }

  public static JsSpec isInt(boolean required,boolean nullable,IntPredicate predicate) {
    return new IsIntSuchThat(required,nullable,predicate);
  }

  public static JsSpec isLong(boolean required, boolean nullable) {
    return new IsLong(required, nullable);
  }


  public static JsSpec isLong(LongPredicate predicate) {
    return new IsLongSuchThat(true,
                              false,
                              predicate);
  }
  public static JsSpec isLong(boolean required,boolean nullable,LongPredicate predicate) {
    return new IsLongSuchThat(required,
                              nullable,
                              predicate);
  }

  public static JsSpec isDecimal(boolean required,boolean nullable,Predicate<BigDecimal> predicate) {
    return new IsDecimalSuchThat(required,
                                 nullable,
                                 predicate);
  }

  public static JsSpec isDecimal(Predicate<BigDecimal> predicate) {
    return new IsDecimalSuchThat(true,
                                 false,
                                 predicate);
  }

  public static JsSpec isDecimal(boolean required, boolean nullable) {
    return new IsDecimal(required,nullable);
  }

  public static JsSpec isIntegral(Predicate<BigInteger> predicate) {
    return new IsIntegralSuchThat(true,
                                  false,
                                  predicate);
  }

  public static JsSpec isIntegral(boolean required, boolean nullable,Predicate<BigInteger> predicate) {
    return new IsIntegralSuchThat(required,
                                  nullable,
                                  predicate);
  }

  public static JsSpec isIntegral(boolean required, boolean nullable) {
    return new IsIntegral(required,
                          nullable);
  }


  public static JsSpec isObj(Predicate<JsObj> predicate) {
    return new IsObjSuchThat(true,
                     false,
                     predicate);
  }

  public static JsSpec isObj(boolean required, boolean nullable,Predicate<JsObj> predicate) {
    return new IsObjSuchThat(required,
                             nullable,
                             predicate);
  }

  public static JsSpec isObj(boolean required, boolean nullable) {
    return new IsObj(required, nullable);
  }

  public static JsArrayPredicate isArray(Predicate<JsArray> predicate) {
    return new IsArraySuchThat(true,false,predicate);
  }

  public static JsArrayPredicate isArray(boolean required, boolean nullable,Predicate<JsArray> predicate) {
    return new IsArraySuchThat(required,nullable,predicate);
  }

  public static JsArrayPredicate isArray(boolean required, boolean nullable) {
    return new IsArray(required,nullable);
  }
}
