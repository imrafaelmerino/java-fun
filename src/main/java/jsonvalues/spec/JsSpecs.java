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
  public static JsSpec optAny =new IsValue(false);

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

  public static JsArrayPredicate array(final Predicate<JsArray> predicate)
  {

    return array(true,
                 false,
                 predicate
                );
  }

  public static JsSpec nullableArray = new IsArray(true,
                                           true
  );

  public static JsArrayPredicate nullableArray(final Predicate<JsArray> predicate)
  {

    return array(true,
                 true,
                 predicate
                );
  }

  public static JsSpec optNullableArray = new IsArray(false,
                                                   true
  );

  public static JsArrayPredicate optNullableArray(final Predicate<JsArray> predicate)
  {

    return array(false,
                 true,
                 predicate
                );
  }

  public static JsSpec optArray = new IsArray(false,
                                           false
  );

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

  public static JsSpec arrayOfInt = new IsArrayOfInt(true,
                                                     false
  );

  public static JsSpec arrayOfStr = new IsArrayOfStr(true,
                                                     false
  );

  public static JsSpec arrayOfBool = new IsArrayOfBool(true,
                                                       false
  );

  public static JsSpec arrayOfDec = new IsArrayOfDecimal(true,
                                                         false
  );

  public static JsSpec arrayOfNumber = new IsArrayOfNumber(true,
                                                           false
  );

  public static JsSpec arrayOfIntegral = new IsArrayOfIntegral(true,
                                                               false
  );

  public static JsSpec arrayOfObj = new IsArrayOfObj(true,
                                                     false
  );


  public static JsSpec arrayOfStr(final Predicate<String> predicate)
  {
    return arrayOfString(requireNonNull(predicate),
                         true,
                         false
                        );
  }

  public static JsSpec arrayOfStrSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfStrSuchThat(predicate,
                              true,
                              false
                             );
  }

  public static JsSpec arrayOfString(final Predicate<String> predicate,
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

  public static JsSpec arrayOfStrSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfLong(final LongPredicate predicate)
  {

    return arrayOfLong(requireNonNull(predicate),
                       true,
                       false
                      );
  }


  public static JsSpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate)
  {
    return arrayOfBoolSuchThat(predicate,
                               true,
                               false
                              );
  }

  public static JsSpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfLongSuchThat(predicate,
                               true,
                               false
                              );
  }

  public static JsSpec arrayOfLongSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfLong(final LongPredicate predicate,
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

  public static JsSpec arrayOfInt(final IntPredicate predicate)
  {

    return arrayOfInt(requireNonNull(predicate),
                      true,
                      false
                     );
  }

  public static JsSpec arrayOfInt(final IntPredicate predicate,
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

  public static JsSpec arrayOfArray = new IsArrayOfArray(true,
                                                         false
  );

  public static JsSpec arrayOfArray(final Predicate<JsArray> predicate)
  {
    return arrayOfArray(predicate,
                        true,
                        false
                       );
  }

  public static JsSpec arrayOfArray(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfIntSuchThat(requireNonNull(predicate),
                              true,
                              false
                             );
  }

  public static JsSpec arrayOfIntSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfDec(final Predicate<BigDecimal> predicate,
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

  public static JsSpec arrayOfDecSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfIntegral(final Predicate<BigInteger> predicate,
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

  public static JsSpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfNumber(final Predicate<JsNumber> predicate,
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

  public static JsSpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfNumberSuchThat(requireNonNull(predicate),
                                 true,
                                 false
                                );
  }

  public static JsSpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec arrayOfObj(final boolean required,
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

  public static JsSpec arrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {

    return arrayOfObjSuchThat(requireNonNull(predicate),
                              true,
                              false
                             );
  }

  public static JsSpec arrayOfObjSuchThat(final Predicate<JsArray> predicate,
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



  public static JsArrayPredicate arrayOfLong(final boolean required,
                                             final boolean nullable
                                            )
  {
    return new IsArrayOfLong(required,
                             nullable
    );
  }

  public static JsArrayPredicate arrayOfInt(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfInt(required,
                            nullable
    );
  }

  public static JsArrayPredicate arrayOfStr(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfStr(required,
                            nullable
    );
  }

  public static JsArrayPredicate arrayOfBool(final boolean required,
                                             final boolean nullable
                                            )
  {
    return new IsArrayOfBool(required,
                             nullable
    );
  }

  public static JsArrayPredicate arrayOfObj(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfObj(required,
                            nullable
    );
  }

  public static JsArrayPredicate arrayOfDec(final boolean required,
                                            final boolean nullable
                                           )
  {
    return new IsArrayOfDecimal(required,
                                nullable
    );
  }

  public static JsArrayPredicate arrayOfIntegral(final boolean required,
                                                 final boolean nullable
                                                )
  {
    return new IsArrayOfIntegral(required,
                                 nullable
    );
  }

  public static JsArrayPredicate arrayOfNumber(final boolean required,
                                               final boolean nullable
                                              )
  {
    return new IsArrayOfNumber(required,
                               nullable
    );
  }
}
