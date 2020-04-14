package jsonvalues.spec;

import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.ARRAY_CONDITION;
import static jsonvalues.spec.ERROR_CODE.VALUE_CONDITION;

public class JsSpecs
{


  public static JsSpec spec(final JsObjSpec spec)
  {
    return spec(spec,
                false,
                true
               );
  }

  public static JsSpec nullableSpec(final JsObjSpec spec)
  {
    return spec(spec,
                true,
                false
               );
  }

  public static JsSpec optSpec(final JsObjSpec spec)
  {
    return spec(spec,
                false,
                false
               );
  }

  public static JsSpec optNullableSpec(final JsObjSpec spec)
  {
    return spec(spec,
                true,
                false
               );
  }


  public static JsSpec spec(final JsArraySpec spec
                           )
  {
    return spec(spec,
                false,
                true
               );
  }

  public static JsSpec optSpec(final JsArraySpec spec)
  {
    return spec(spec,
                false,
                false
               );
  }

  public static JsSpec nullableSpec(final JsArraySpec spec)
  {
    return spec(spec,
                true,
                true
               );
  }

  public static JsSpec optNullableSpec(final JsArraySpec spec)
  {
    return spec(spec,
                true,
                false
               );
  }


  public static JsSpec arrayOf(final JsObjSpec spec)
  {
    return new IsArrayOfObjSpec(false,
                                true,
                                false,
                                requireNonNull(spec)
    );
  }

  public static JsSpec nullableArrayOf(final JsObjSpec spec)
  {
    return new IsArrayOfObjSpec(true,
                                true,
                                false,
                                requireNonNull(spec)
    );
  }

  public static JsSpec optArrayOf(final JsObjSpec spec)
  {
    return new IsArrayOfObjSpec(false,
                                false,
                                false,
                                requireNonNull(spec)
    );
  }

  public static JsSpec optNullableArrayOf(final JsObjSpec spec)
  {
    return new IsArrayOfObjSpec(true,
                                false,
                                false,
                                requireNonNull(spec)
    );
  }


  public static JsSpec arrayOf(final JsObjSpec spec,
                               final boolean nullable,
                               final boolean required,
                               final boolean elemNullable
                              )
  {
    return new IsArrayOfObjSpec(nullable,
                                required,
                                elemNullable,
                                requireNonNull(spec)
    );
  }


  public static JsSpec any = new IsValue(true);

  public static JsSpec any(final Predicate<JsValue> predicate)
  {
    return any(predicate,
               true
              );
  }

  public static JsSpec optAny = new IsValue(false);

  public static JsSpec optAny(final Predicate<JsValue> predicate)
  {
    return any(predicate,
               false
              );
  }

  public static JsSpec str = new IsStr(true,
                                       false
  );

  public static JsSpec str(final Predicate<String> predicate)
  {
    return str(requireNonNull(predicate),
               true,
               false
              );
  }


  public static JsSpec nullableStr = new IsStr(true,
                                               true
  );

  public static JsSpec nullableStr(final Predicate<String> predicate)
  {
    return str(requireNonNull(predicate),
               true,
               true
              );
  }

  public static JsSpec optStr = new IsStr(false,
                                          false
  );

  public static JsSpec optStr(final Predicate<String> predicate)
  {
    return str(requireNonNull(predicate),
               false,
               false
              );
  }

  public static JsSpec optNullableStr = new IsStr(false,
                                                  true
  );

  public static JsSpec optNullableStr(final Predicate<String> predicate)
  {
    return str(requireNonNull(predicate),
               false,
               true
              );
  }


  public static JsSpec number = new IsNumber(true,
                                             false
  );

  public static JsSpec number(final Predicate<JsNumber> predicate)
  {
    return number(true,
                  false,
                  predicate
                 );
  }

  public static JsSpec optNumber = new IsNumber(false,
                                                false
  );

  public static JsSpec optNumber(final Predicate<JsNumber> predicate)
  {
    return number(false,
                  false,
                  predicate
                 );
  }

  public static JsSpec nullableNumber = new IsNumber(true,
                                                     true
  );

  public static JsSpec nullableNumber(final Predicate<JsNumber> predicate)
  {
    return number(true,
                  true,
                  predicate
                 );
  }

  public static JsSpec optNullableNumber = new IsNumber(false,
                                                        true
  );


  public static JsSpec optNullableNumber(final Predicate<JsNumber> predicate)
  {
    return number(false,
                  true,
                  predicate
                 );
  }

  public static JsSpec bool = new IsBoolean(true,
                                            false
  );

  public static JsSpec nullableBool = new IsBoolean(true,
                                                    true
  );

  public static JsSpec optBool = new IsBoolean(false,
                                               false
  );

  public static JsSpec optNullableBool = new IsBoolean(false,
                                                       true
  );

  public static JsSpec decimal = new IsDecimal(true,
                                               false
  );

  public static JsSpec decimal(final Predicate<BigDecimal> predicate)
  {
    return decimal(true,
                   false,
                   predicate
                  );
  }

  public static JsSpec nullableDecimal = new IsDecimal(true,
                                                       true
  );

  public static JsSpec nullableDecimal(final Predicate<BigDecimal> predicate)
  {
    return decimal(true,
                   true,
                   predicate
                  );
  }

  public static JsSpec optDecimal = new IsDecimal(false,
                                                  false
  );

  public static JsSpec optDecimal(final Predicate<BigDecimal> predicate)
  {
    return decimal(false,
                   false,
                   predicate
                  );
  }

  public static JsSpec optNullableDecimal = new IsDecimal(false,
                                                          true
  );

  public static JsSpec optNullableDecimal(final Predicate<BigDecimal> predicate)
  {
    return decimal(false,
                   true,
                   predicate
                  );
  }

  public static JsSpec integral = new IsIntegral(true,
                                                 false
  );

  public static JsSpec integral(final Predicate<BigInteger> predicate)
  {
    return integral(true,
                    false,
                    predicate
                   );
  }

  public static JsSpec nullableIntegral = new IsIntegral(true,
                                                         true
  );

  public static JsSpec nullableIntegral(final Predicate<BigInteger> predicate)
  {
    return integral(true,
                    true,
                    predicate
                   );
  }

  public static JsSpec optNullableIntegral = new IsIntegral(false,
                                                            true
  );

  public static JsSpec optIntegral = new IsIntegral(false,
                                                    false
  );

  public static JsSpec optIntegral(final Predicate<BigInteger> predicate)
  {
    return integral(false,
                    false,
                    predicate
                   );
  }

  public static JsSpec optNullableIntegral(final Predicate<BigInteger> predicate)
  {
    return integral(false,
                    true,
                    predicate
                   );
  }

  public static JsSpec longNumber = new IsLong(true,
                                               false
  );

  public static JsSpec longNumber(final LongPredicate predicate)
  {
    return longNumber(true,
                      false,
                      predicate
                     );
  }

  public static JsSpec nullableLongNumber = new IsLong(true,
                                                       true
  );

  public static JsSpec nullableLongNumber(final LongPredicate predicate)
  {
    return longNumber(true,
                      true,
                      predicate
                     );
  }

  public static JsSpec optLongNumber = new IsLong(false,
                                                  false
  );

  public static JsSpec optLongNumber(final LongPredicate predicate)
  {
    return longNumber(false,
                      false,
                      predicate
                     );
  }

  public static JsSpec optNullableLongNumber = new IsLong(false,
                                                          true
  );

  public static JsSpec optNullableLongNumber(final LongPredicate predicate)
  {
    return longNumber(false,
                      true,
                      predicate
                     );
  }

  public static JsSpec intNumber = new IsInt(true,
                                             false
  );

  public static JsSpec intNumber(final IntPredicate predicate)
  {
    return intNumber(true,
                     false,
                     predicate
                    );
  }

  public static JsSpec nullableIntNumber = new IsInt(true,
                                                     true
  );

  public static JsSpec nullableIntNumber(final IntPredicate predicate)
  {
    return intNumber(true,
                     true,
                     predicate
                    );
  }

  public static JsSpec optIntNumber = new IsInt(false,
                                                false
  );

  public static JsSpec optIntNumber(final IntPredicate predicate)
  {
    return intNumber(false,
                     false,
                     predicate
                    );
  }

  public static JsSpec optNullableIntNumber = new IsInt(false,
                                                        true
  );

  public static JsSpec optNullableIntNumber(final IntPredicate predicate)
  {
    return intNumber(false,
                     true,
                     predicate
                    );
  }

  public static JsSpec TRUE = new IsTrue(true,
                                         false
  );

  public static JsSpec FALSE = new IsFalse(true,
                                           false
  );

  public static JsSpec obj = new IsObj(true,
                                       false
  );


  public static JsSpec obj(final Predicate<JsObj> predicate)
  {
    return obj(true,
               false,
               predicate
              );
  }

  public static JsSpec nullableObj = new IsObj(true,
                                               true
  );

  public static JsSpec nullableObj(final Predicate<JsObj> predicate)
  {
    return obj(true,
               true,
               predicate
              );
  }

  public static JsSpec optObj = new IsObj(false,
                                          false
  );

  public static JsSpec optObj(final Predicate<JsObj> predicate)
  {
    return obj(false,
               false,
               predicate
              );
  }

  public static JsSpec optNullableObj(final Predicate<JsObj> predicate)
  {
    return obj(false,
               true,
               predicate
              );
  }

  public static JsSpec optNullableObj = new IsObj(false,
                                                  true
  );

  public static JsSpec array = new IsArray(true,
                                           false
  );

  public static JsSpec nullableArray = new IsArray(true,
                                                   true
  );

  public static JsSpec optArray = new IsArray(false,
                                              false
  );

  public static JsSpec optNullableArray = new IsArray(false,
                                                      true
  );

  public static JsArrayPredicate array(final Predicate<JsArray> predicate)
  {

    return array(true,
                 false,
                 predicate
                );
  }


  public static JsArrayPredicate nullableArray(final Predicate<JsArray> predicate)
  {

    return array(true,
                 true,
                 predicate
                );
  }


  public static JsArrayPredicate optNullableArray(final Predicate<JsArray> predicate)
  {

    return array(false,
                 true,
                 predicate
                );
  }

  public static JsArrayPredicate optArray(final Predicate<JsArray> predicate)
  {

    return array(false,
                 false,
                 predicate
                );
  }

  public static JsSpec arrayOfLong = new IsArrayOfLong(true,
                                                       false
  );

  public static JsSpec nullableArrayOfLong = new IsArrayOfLong(true,
                                                               true
  );

  public static JsSpec optArrayOfLong = new IsArrayOfLong(false,
                                                          false
  );

  public static JsSpec optNullableArrayOfLong = new IsArrayOfLong(false,
                                                                  true
  );

  public static JsSpec arrayOfInt = new IsArrayOfInt(true,
                                                     false
  );

  public static JsSpec nullableArrayOfInt = new IsArrayOfInt(false,
                                                             false
  );

  public static JsSpec optArrayOfInt = new IsArrayOfInt(false,
                                                        false
  );

  public static JsSpec optNullableArrayOfInt = new IsArrayOfInt(false,
                                                                true
  );

  public static JsSpec arrayOfStr = new IsArrayOfStr(true,
                                                     false
  );

  public static JsSpec nullableArrayOfStr = new IsArrayOfStr(true,
                                                             true
  );

  public static JsSpec optArrayOfStr = new IsArrayOfStr(false,
                                                        false
  );

  public static JsSpec optNullableArrayOfStr = new IsArrayOfStr(false,
                                                                true
  );

  public static JsSpec arrayOfBool = new IsArrayOfBool(true,
                                                       false
  );

  public static JsSpec nullableArrayOfBool = new IsArrayOfBool(true,
                                                               true
  );

  public static JsSpec optArrayOfBool = new IsArrayOfBool(false,
                                                          false
  );

  public static JsSpec optNullableArrayOfBool = new IsArrayOfBool(false,
                                                                  true
  );

  public static JsSpec arrayOfDec = new IsArrayOfDecimal(true,
                                                         false
  );

  public static JsSpec nullableArrayOfDec = new IsArrayOfDecimal(true,
                                                                 true
  );
  public static JsSpec optArrayOfDec = new IsArrayOfDecimal(false,
                                                            false
  );

  public static JsSpec optNullableArrayOfDec = new IsArrayOfDecimal(false,
                                                                    true
  );

  public static JsSpec arrayOfNumber = new IsArrayOfNumber(true,
                                                           false
  );

  public static JsSpec nullableArrayOfNumber = new IsArrayOfNumber(true,
                                                                   true
  );

  public static JsSpec optArrayOfNumber = new IsArrayOfNumber(false,
                                                              false
  );

  public static JsSpec optNullableArrayOfNumber = new IsArrayOfNumber(false,
                                                                      true
  );

  public static JsSpec arrayOfIntegral = new IsArrayOfIntegral(true,
                                                               false
  );

  public static JsSpec nullableArrayOfIntegral = new IsArrayOfIntegral(true,
                                                                       true
  );

  public static JsSpec optArrayOfIntegral = new IsArrayOfIntegral(true,
                                                                  false
  );

  public static JsSpec optNullableArrayOfIntegral = new IsArrayOfIntegral(true,
                                                                          true
  );

  public static JsSpec arrayOfObj = new IsArrayOfObj(true,
                                                     false
  );

  public static JsSpec nullableArrayOfObj = new IsArrayOfObj(true,
                                                             true
  );

  public static JsSpec optArrayOfObj = new IsArrayOfObj(true,
                                                        false
  );

  public static JsSpec optNullableArrayOfObj = new IsArrayOfObj(true,
                                                                true
  );



  public static JsSpec arrayOfStr(final Predicate<String> predicate)
  {
    return arrayOfStr(requireNonNull(predicate),
                         true,
                         false
                        );
  }

  public static JsSpec nullableArrayOfStr(final Predicate<String> predicate)
  {
    return arrayOfStr(requireNonNull(predicate),
                         true,
                         true
                        );
  }

  public static JsSpec optArrayOfStr(final Predicate<String> predicate)
  {
    return arrayOfStr(requireNonNull(predicate),
                         false,
                         false
                        );
  }

  public static JsSpec optNullableArrayOfStr(final Predicate<String> predicate)
  {
    return arrayOfStr(requireNonNull(predicate),
                         false,
                         true
                        );
  }

  public static JsSpec arrayOfStrSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfStrSuchThat(predicate,
                              true,
                              false
                             );
  }

  public static JsSpec nullableArrayOfStrSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfStrSuchThat(predicate,
                              true,
                              true
                             );
  }

  public static JsSpec optArrayOfStrSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfStrSuchThat(predicate,
                              false,
                              false
                             );
  }

  public static JsSpec optNullableArrayOfStrSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfStrSuchThat(predicate,
                              false,
                              true
                             );
  }




  public static JsSpec arrayOfLong(final LongPredicate predicate)
  {

    return arrayOfLong(requireNonNull(predicate),
                       true,
                       false
                      );
  }

  public static JsSpec nullableArrayOfLong(final LongPredicate predicate)
  {

    return arrayOfLong(requireNonNull(predicate),
                       true,
                       true
                      );
  }

  public static JsSpec optArrayOfLong(final LongPredicate predicate)
  {

    return arrayOfLong(requireNonNull(predicate),
                       false,
                       false
                      );
  }

  public static JsSpec optNullableArrayOfLong(final LongPredicate predicate)
  {

    return arrayOfLong(requireNonNull(predicate),
                       false,
                       true
                      );
  }


  public static JsSpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate)
  {
    return arrayOfBoolSuchThat(predicate,
                               true,
                               false
                              );
  }

  public static JsSpec nullableArrayOfBoolSuchThat(final Predicate<JsArray> predicate)
  {
    return arrayOfBoolSuchThat(predicate,
                               true,
                               true
                              );
  }

  public static JsSpec optNullableArrayOfBoolSuchThat(final Predicate<JsArray> predicate)
  {
    return arrayOfBoolSuchThat(predicate,
                               false,
                               true
                              );
  }

  public static JsSpec optArrayOfBoolSuchThat(final Predicate<JsArray> predicate)
  {
    return arrayOfBoolSuchThat(predicate,
                               false,
                               false
                              );
  }



  public static JsSpec arrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfLongSuchThat(predicate,
                               true,
                               false
                              );
  }

  public static JsSpec nullableArrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfLongSuchThat(predicate,
                               true,
                               true
                              );
  }

  public static JsSpec optArrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfLongSuchThat(predicate,
                               false,
                               false
                              );
  }

  public static JsSpec optNullableArrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfLongSuchThat(predicate,
                               false,
                               true
                              );
  }


  public static JsSpec arrayOfInt(final IntPredicate predicate)
{

  return arrayOfInt(requireNonNull(predicate),
                    true,
                    false
                   );
}

  public static JsSpec optArrayOfInt(final IntPredicate predicate)
  {

    return arrayOfInt(requireNonNull(predicate),
                      false,
                      false
                     );
  }

  public static JsSpec nullableArrayOfInt(final IntPredicate predicate)
  {

    return arrayOfInt(requireNonNull(predicate),
                      true,
                      true
                     );
  }

  public static JsSpec optNullableArrayOfInt(final IntPredicate predicate)
  {

    return arrayOfInt(requireNonNull(predicate),
                      false,
                      true
                     );
  }



  public static JsSpec arrayOfArray = new IsArrayOfArray(true,
                                                         false
  );

  public static JsSpec nullableArrayOfArray = new IsArrayOfArray(true,
                                                         true
  );

  public static JsSpec optArrayOfArray = new IsArrayOfArray(false,
                                                         false
  );

  public static JsSpec optNullableArrayOfArray = new IsArrayOfArray(false,
                                                            true
  );

  public static JsSpec arrayOfArray(final Predicate<JsArray> predicate)
  {
    return arrayOfArray(predicate,
                        true,
                        false
                       );
  }

  public static JsSpec nullableArrayOfArray(final Predicate<JsArray> predicate)
  {
    return arrayOfArray(predicate,
                        true,
                        true
                       );
  }

  public static JsSpec optNullableArrayOfArray(final Predicate<JsArray> predicate)
  {
    return arrayOfArray(predicate,
                        false,
                        true
                       );
  }

  public static JsSpec optArrayOfArray(final Predicate<JsArray> predicate)
  {
    return arrayOfArray(predicate,
                        false,
                        false
                       );
  }


  public static JsSpec arrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntSuchThat(requireNonNull(predicate),
                              true,
                              false
                             );
  }

  public static JsSpec nullableArrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntSuchThat(requireNonNull(predicate),
                              true,
                              true
                             );
  }
  public static JsSpec optNullableArrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntSuchThat(requireNonNull(predicate),
                              false,
                              true
                             );
  }

  public static JsSpec optArrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntSuchThat(requireNonNull(predicate),
                              false,
                              false
                             );
  }

  private static JsSpec arrayOfIntSuchThat(final Predicate<JsArray> predicate,
                                          final boolean required,
                                          final boolean nullable
                                         )
  {
    return new IsArrayOfIntSuchThat(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ARRAY_CONDITION
                                      ));
                                    },
                                    required,
                                    nullable
    );
  }

  public static JsSpec arrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return arrayOfDec(requireNonNull(predicate),
                      true,
                      false
                     );
  }

  public static JsSpec optArrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return arrayOfDec(requireNonNull(predicate),
                      false,
                      false
                     );
  }

  public static JsSpec nullableAptArrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return arrayOfDec(requireNonNull(predicate),
                      true,
                      true
                     );
  }

  public static JsSpec optNullableAptArrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return arrayOfDec(requireNonNull(predicate),
                      false,
                      true
                     );
  }

  private static JsSpec arrayOfDec(final Predicate<BigDecimal> predicate,
                                  final boolean required,
                                  final boolean nullable
                                 )
  {
    return new IsArrayOfTestedDecimal(s ->
                                      {
                                        if (requireNonNull(predicate).test(s)) return Optional.empty();
                                        return Optional.of(new Error(JsBigDec.of(s),
                                                                     ERROR_CODE.DECIMAL_CONDITION
                                        ));
                                      },
                                      required,
                                      nullable
    );
  }

  public static JsSpec arrayOfDecSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfDecSuchThat(requireNonNull(predicate),
                              true,
                              false
                             );
  }


  public static JsSpec nullableArrayOfDecSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfDecSuchThat(requireNonNull(predicate),
                              true,
                              true
                             );
  }

  public static JsSpec optNullableArrayOfDecSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfDecSuchThat(requireNonNull(predicate),
                              false,
                              true
                             );
  }

  public static JsSpec optArrayOfDecSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfDecSuchThat(requireNonNull(predicate),
                              false,
                              false
                             );
  }

  private static JsSpec arrayOfDecSuchThat(final Predicate<JsArray> predicate,
                                          final boolean required,
                                          final boolean nullable
                                         )
  {
    return new IsArrayOfDecimalSuchThat(s ->
                                        {
                                          if (requireNonNull(predicate).test(s)) return Optional.empty();
                                          return Optional.of(new Error(s,
                                                                       ARRAY_CONDITION
                                          ));
                                        },
                                        required,
                                        nullable
    );
  }

  public static JsSpec arrayOfIntegral(final Predicate<BigInteger> predicate)
{
  return arrayOfIntegral(requireNonNull(predicate),
                         true,
                         false
                        );
}

  public static JsSpec nullableArrayOfIntegral(final Predicate<BigInteger> predicate)
  {
    return arrayOfIntegral(requireNonNull(predicate),
                           true,
                           true
                          );
  }

  public static JsSpec optArrayOfIntegral(final Predicate<BigInteger> predicate)
  {
    return arrayOfIntegral(requireNonNull(predicate),
                           false,
                           false
                          );
  }

  public static JsSpec optNullableArrayOfIntegral(final Predicate<BigInteger> predicate)
  {
    return arrayOfIntegral(requireNonNull(predicate),
                           false,
                           true
                          );
  }

  private static JsSpec arrayOfIntegral(final Predicate<BigInteger> predicate,
                                       final boolean required,
                                       final boolean nullable
                                      )
  {
    return new IsArrayOfTestedIntegral(s ->
                                       {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(JsBigInt.of(s),
                                                                      ERROR_CODE.INTEGRAL_CONDITION
                                         ));
                                       },
                                       required,
                                       nullable
    );
  }

  public static JsSpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntegralSuchThat(requireNonNull(predicate),
                                   true,
                                   false
                                  );
  }

  public static JsSpec nullableArrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntegralSuchThat(requireNonNull(predicate),
                                   true,
                                   true
                                  );
  }

  public static JsSpec optArrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntegralSuchThat(requireNonNull(predicate),
                                   false,
                                   false
                                  );
  }

  public static JsSpec optNullableArrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntegralSuchThat(requireNonNull(predicate),
                                   false,
                                   true
                                  );
  }

  private static JsSpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate,
                                               final boolean required,
                                               final boolean nullable
                                              )
  {
    return new IsArrayOfIntegralSuchThat(s ->
                                         {
                                           if (requireNonNull(predicate).test(s)) return Optional.empty();
                                           return Optional.of(new Error(s,
                                                                        ARRAY_CONDITION
                                           ));
                                         },
                                         required,
                                         nullable
    );
  }

  public static JsSpec arrayOfNumber(final Predicate<JsNumber> predicate)
  {

    return arrayOfNumber(requireNonNull(predicate),
                         true,
                         false
                        );
  }

  public static JsSpec optArrayOfNumber(final Predicate<JsNumber> predicate)
  {

    return arrayOfNumber(requireNonNull(predicate),
                         false,
                         false
                        );
  }

  public static JsSpec nullableArrayOfNumber(final Predicate<JsNumber> predicate)
  {

    return arrayOfNumber(requireNonNull(predicate),
                         true,
                         true
                        );
  }

  public static JsSpec optNullableArrayOfNumber(final Predicate<JsNumber> predicate)
  {

    return arrayOfNumber(requireNonNull(predicate),
                         false,
                         true
                        );
  }



  public static JsSpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfNumberSuchThat(requireNonNull(predicate),
                                 true,
                                 false
                                );
  }

  public static JsSpec nullableArrayOfNumberSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfNumberSuchThat(requireNonNull(predicate),
                                 true,
                                 true
                                );
  }

  public static JsSpec optArrayOfNumberSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfNumberSuchThat(requireNonNull(predicate),
                                 false,
                                 false
                                );
  }

  public static JsSpec optNullableArrayOfNumberSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfNumberSuchThat(requireNonNull(predicate),
                                 false,
                                 true
                                );
  }

  private static JsSpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate,
                                             final boolean required,
                                             final boolean nullable
                                            )
  {
    return new IsArrayOfNumberSuchThat(s ->
                                       {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(s,
                                                                      ARRAY_CONDITION
                                         ));
                                       },
                                       required,
                                       nullable
    );
  }

  private static JsSpec arrayOfObj(final boolean required,
                                  final boolean nullable,
                                  final Predicate<JsObj> predicate
                                 )
  {
    return new IsArrayOfTestedObj(s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(s,
                                                                 ERROR_CODE.OBJ_CONDITION
                                    ));
                                  },
                                  required,
                                  nullable
    );

  }

  public static JsSpec arrayOfObj(final Predicate<JsObj> predicate)
  {
    return arrayOfObj(true,
                      false,
                      requireNonNull(predicate)
                     );
  }

  public static JsSpec nullableArrayOfObj(final Predicate<JsObj> predicate)
  {
    return arrayOfObj(true,
                      true,
                      requireNonNull(predicate)
                     );
  }

  public static JsSpec optNullableArrayOfObj(final Predicate<JsObj> predicate)
  {
    return arrayOfObj(false,
                      true,
                      requireNonNull(predicate)
                     );
  }

  public static JsSpec optArrayOfObj(final Predicate<JsObj> predicate)
  {
    return arrayOfObj(false,
                      false,
                      requireNonNull(predicate)
                     );
  }

  public static JsSpec arrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfObjSuchThat(requireNonNull(predicate),
                              true,
                              false
                             );
  }

  public static JsSpec nullableArrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfObjSuchThat(requireNonNull(predicate),
                              true,
                              true
                             );
  }


  public static JsSpec optNullableArrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfObjSuchThat(requireNonNull(predicate),
                              false,
                              true
                             );
  }

  public static JsSpec optArrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfObjSuchThat(requireNonNull(predicate),
                              false,
                              false
                             );
  }

  private static JsSpec arrayOfObjSuchThat(final Predicate<JsArray> predicate,
                                          final boolean required,
                                          final boolean nullable
                                         )
  {
    return new IsArrayOfObjSuchThat(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ARRAY_CONDITION
                                      ));
                                    },
                                    required,
                                    nullable
    );
  }


  private static JsSpec str(final Predicate<String> predicate,
                            final boolean required,
                            final boolean nullable
                           )
  {
    return new IsStrSuchThat(required,
                             nullable,
                             s ->
                             {
                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                               return Optional.of(new Error(JsStr.of(s),
                                                            ERROR_CODE.STRING_CONDITION
                               ));
                             }

    );
  }


  private static JsSpec number(final boolean required,
                               final boolean nullable,
                               final Predicate<JsNumber> predicate
                              )
  {
    return new IsNumberSuchThat(required,
                                nullable,
                                s ->
                                {
                                  if (requireNonNull(predicate).test(s)) return Optional.empty();
                                  return Optional.of(new Error(s,
                                                               ERROR_CODE.NUMBER_CONDITION
                                  ));
                                }
    );
  }


  private static JsSpec intNumber(final boolean required,
                                  final boolean nullable,
                                  final IntPredicate predicate
                                 )
  {
    return new IsIntSuchThat(required,
                             nullable,
                             s ->
                             {
                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                               return Optional.of(new Error(JsInt.of(s),
                                                            ERROR_CODE.INT_CONDITION
                               ));
                             }
    );
  }

  private static JsSpec arrayOfStrSuchThat(final Predicate<JsArray> predicate,
                                           final boolean required,
                                           final boolean nullable
                                          )
  {
    return new IsArrayOfStrSuchThat(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ARRAY_CONDITION
                                      ));
                                    },
                                    required,
                                    nullable
    );
  }


  private static JsSpec arrayOfLong(final LongPredicate predicate,
                                    final boolean required,
                                    final boolean nullable
                                   )
  {
    return new IsArrayOfTestedLong(s ->
                                   {
                                     if (requireNonNull(predicate).test(s)) return Optional.empty();
                                     return Optional.of(new Error(JsLong.of(s),
                                                                  ERROR_CODE.LONG_CONDITION
                                     ));
                                   },
                                   required,
                                   nullable
    );
  }

  private static JsSpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate,
                                            final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfBoolSuchThat(s ->
                                     {
                                       if (requireNonNull(predicate).test(s)) return Optional.empty();
                                       return Optional.of(new Error(s,
                                                                    ARRAY_CONDITION
                                       ));
                                     },
                                     required,
                                     nullable
    );
  }

  private static JsSpec longNumber(final boolean required,
                                   final boolean nullable,
                                   final LongPredicate predicate
                                  )
  {
    return new IsLongSuchThat(required,
                              nullable,
                              s ->
                              {
                                if (requireNonNull(predicate).test(s)) return Optional.empty();
                                return Optional.of(new Error(JsLong.of(s),
                                                             ERROR_CODE.LONG_CONDITION
                                ));
                              }
    );
  }

  private static JsSpec decimal(final boolean required,
                                final boolean nullable,
                                final Predicate<BigDecimal> predicate
                               )
  {
    return new IsDecimalSuchThat(required,
                                 nullable,
                                 s ->
                                 {
                                   if (requireNonNull(predicate).test(s)) return Optional.empty();
                                   return Optional.of(new Error(JsBigDec.of(s),
                                                                ERROR_CODE.DECIMAL_CONDITION
                                   ));
                                 }
    );
  }

  private static JsSpec spec(final JsArraySpec spec,
                             final boolean nullable,
                             final boolean required
                            )
  {
    return new IsArraySpec(requireNonNull(spec),
                           nullable,
                           required
    );
  }


  private static JsSpec integral(final boolean required,
                                 final boolean nullable,
                                 final Predicate<BigInteger> predicate
                                )
  {
    return new IsIntegralSuchThat(required,
                                  nullable,
                                  s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(JsBigInt.of(s),
                                                                 ERROR_CODE.INTEGRAL_CONDITION
                                    ));
                                  }
    );
  }

  private static JsSpec arrayOfStr(final Predicate<String> predicate,
                                      final boolean required,
                                      final boolean nullable
                                     )
  {

    return new IsArrayOfTestedStr(s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(JsStr.of(s),
                                                                 ERROR_CODE.STRING_CONDITION
                                    ));
                                  },
                                  required,
                                  nullable
    );
  }

  /**returns a spec that conforms any value that is evaluated to true on the predicate.
   * When the type is not specified by the spec, positive numbers are parsed as Long by default,
   * which has to be taken into account in order to define any condition.
   * @param predicate the predicate
   * @param required if true, the value is mandatory
   * @return a spec
   */
  private static JsSpec any(final Predicate<JsValue> predicate,
                            final boolean required
                           )
  {
    return new IsValueSuchThat(required,
                               v ->
                               {
                                 if (requireNonNull(predicate).test(v)) return Optional.empty();
                                 return Optional.of(new Error(v,
                                                              VALUE_CONDITION
                                 ));
                               }
    );
  }

  private static JsSpec spec(final JsObjSpec spec,
                             final boolean nullable,
                             final boolean required
                            )
  {
    return new IsObjSpec(nullable,
                         required,
                         requireNonNull(spec)
    );
  }


  private static JsSpec obj(final boolean required,
                            final boolean nullable,
                            final Predicate<JsObj> predicate
                           )
  {
    return new IsObjSuchThat(required,
                             nullable,
                             s ->
                             {
                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                               return Optional.of(new Error(s,
                                                            ERROR_CODE.OBJ_CONDITION
                               ));
                             }
    );
  }

  private static JsSpec arrayOfLongSuchThat(final Predicate<JsArray> predicate,
                                            final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfLongSuchThat(s ->
                                     {
                                       if (requireNonNull(predicate).test(s)) return Optional.empty();
                                       return Optional.of(new Error(s,
                                                                    ARRAY_CONDITION
                                       ));
                                     },
                                     required,
                                     nullable
    );
  }


  private static JsArrayPredicate array(final boolean required,
                                        final boolean nullable,
                                        final Predicate<JsArray> predicate
                                       )
  {
    return new IsArraySuchThat(s ->
                               {
                                 if (requireNonNull(predicate).test(s)) return Optional.empty();
                                 return Optional.of(new Error(s,
                                                              ERROR_CODE.ARRAY_CONDITION
                                 ));
                               },
                               required,
                               nullable
    );

  }

  private static JsSpec arrayOfInt(final IntPredicate predicate,
                                   final boolean required,
                                   final boolean nullable
                                  )
  {
    return new IsArrayOfTestedInt(s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(JsInt.of(s),
                                                                 ERROR_CODE.INT_CONDITION
                                    ));
                                  },
                                  required,
                                  nullable
    );
  }

  private static JsArrayPredicate arrayOfLong(final boolean required,
                                             final boolean nullable
                                            )
  {
    return new IsArrayOfLong(required,
                             nullable
    );
  }

  private static JsArrayPredicate arrayOfInt(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfInt(required,
                            nullable
    );
  }

  private static JsArrayPredicate arrayOfStr(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfStr(required,
                            nullable
    );
  }

  private static JsArrayPredicate arrayOfBool(final boolean required,
                                             final boolean nullable
                                            )
  {
    return new IsArrayOfBool(required,
                             nullable
    );
  }

  private static JsArrayPredicate arrayOfObj(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfObj(required,
                            nullable
    );
  }

  private static JsArrayPredicate arrayOfDec(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfDecimal(required,
                                nullable
    );
  }

  private static JsArrayPredicate arrayOfIntegral(final boolean required,
                                                 final boolean nullable
                                                )
  {
    return new IsArrayOfIntegral(required,
                                 nullable
    );
  }

  private static JsArrayPredicate arrayOfNumber(final boolean required,
                                               final boolean nullable
                                              )
  {
    return new IsArrayOfNumber(required,
                               nullable
    );
  }

  private static JsSpec arrayOfArray(final Predicate<JsArray> predicate,
                                     final boolean required,
                                     final boolean nullable
                                    )
  {
    return new IsArrayOfTestedArray(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ARRAY_CONDITION
                                      ));
                                    },
                                    required,
                                    nullable
    );
  }

  private static JsSpec arrayOfNumber(final Predicate<JsNumber> predicate,
                                      final boolean required,
                                      final boolean nullable
                                     )
  {
    return new IsArrayOfTestedNumber(s ->
                                     {
                                       if (requireNonNull(predicate).test(s)) return Optional.empty();
                                       return Optional.of(new Error(s,
                                                                    ERROR_CODE.NUMBER_CONDITION
                                       ));
                                     },
                                     required,
                                     nullable
    );
  }
}
