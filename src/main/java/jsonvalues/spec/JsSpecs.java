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

  public static JsSpec conforms(final JsObjSpec spec
                               )
  {
    return conforms(spec,
                    false,
                    true
                   );
  }

  public static JsSpec conforms(final JsObjSpec spec,
                                final boolean nullable,
                                final boolean required
                               )
  {
    return new IsObjSpec(nullable,
                         required,
                         requireNonNull(spec)
    );
  }

  public static JsSpec conforms(final JsArraySpec spec
                               )
  {
    return conforms(spec,
                    false,
                    true
                   );
  }

  public static JsSpec conforms(final JsArraySpec spec,
                                final boolean nullable,
                                final boolean required
                               )
  {
    return new IsArraySpec(requireNonNull(spec),
                           nullable,
                           required
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


  /**returns a spec that conforms any value that is evaluated to true on the predicate.
   * When the type is not specified by the spec, positive numbers are parsed as Long by default,
   * which has to be taken into account in order to define any condition.
   * @param predicate the predicate
   * @param required if true, the value is mandatory
   * @return a spec
   */
  public static JsSpec any(final Predicate<JsValue> predicate,
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

  public static JsSpec any = any(true);

  /**
   * returns a spec that any value conforms
   *
   * @param required if true, the value is mandatory
   * @return a spec
   */
  public static JsSpec any(boolean required)
  {
    return new IsValue(required);
  }

  public static JsSpec isStr = new IsStr(true,
                                         false
  );

  public static JsSpec isNumber = new IsNumber(true,
                                         false
  );

  public static JsSpec isBool = new IsBoolean(true,
                                              false
  );

  public static JsSpec isLong = new IsLong(true,
                                           false
  );

  public static JsSpec isDecimal = new IsDecimal(true,
                                                 false
  );

  public static JsSpec isInt = new IsInt(true,
                                         false
  );

  public static JsSpec isIntegral = new IsIntegral(true,
                                                   false
  );

  public static JsSpec isTrue = new IsTrue(true,
                                           false
  );

  public static JsSpec isFalse = new IsFalse(true,
                                             false
  );

  public static JsSpec isObj = new IsObj(true,
                                         false
  );

  public static JsSpec isArray = new IsArray(true,
                                             false
  );

  public static JsSpec isArrayOfLong = new IsArrayOfLong(true,
                                                         false
  );

  public static JsSpec isArrayOfInt = new IsArrayOfInt(true,
                                                       false
  );

  public static JsSpec isArrayOfStr = new IsArrayOfStr(true,
                                                       false
  );

  public static JsSpec isArrayOfBool = new IsArrayOfBool(true,
                                                         false
  );

  public static JsSpec isArrayOfDec = new IsArrayOfDecimal(true,
                                                           false
  );

  public static JsSpec isArrayOfNumber = new IsArrayOfNumber(true,
                                                             false
  );

  public static JsSpec isArrayOfIntegral = new IsArrayOfIntegral(true,
                                                                 false
  );

  public static JsSpec isArrayOfObj = new IsArrayOfObj(true,
                                                       false
  );


  public static JsSpec isArrayOfStr(final Predicate<String> predicate)
  {
    return isArrayOfString(requireNonNull(predicate),
                           true,
                           false
                          );
  }

  public static JsSpec isArrayOfStrSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfStrSuchThat(predicate,true,false);
  }

  public static JsSpec isArrayOfString(final Predicate<String> predicate,
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

  public static JsSpec isArrayOfStrSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfLong(final LongPredicate predicate)
  {

    return isArrayOfLong(requireNonNull(predicate),
                         true,
                         false
                        );
  }


  public static JsSpec isArrayOfBoolSuchThat(final Predicate<JsArray> predicate)
  {
    return isArrayOfBoolSuchThat(predicate,true,false);
  }

  public static JsSpec isArrayOfBoolSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfLongSuchThat(predicate,true,false);
  }

  public static JsSpec isArrayOfLongSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfLong(final LongPredicate predicate,
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

  public static JsSpec isArrayOfInt(final IntPredicate predicate)
  {

    return isArrayOfInt(requireNonNull(predicate),
                        true,
                        false
                       );
  }

  public static JsSpec isArrayOfInt(final IntPredicate predicate,
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

  public static JsSpec isArrayOfArray = new IsArrayOfArray(true, false);

  public static JsSpec isArrayOfArray(final Predicate<JsArray> predicate) {
    return isArrayOfArray(predicate,true,false);
  }

  public static JsSpec isArrayOfArray(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfIntSuchThat(requireNonNull(predicate),
                                true,
                                false
                               );
  }

  public static JsSpec isArrayOfIntSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return isArrayOfDec(requireNonNull(predicate),
                        true,
                        false
                       );
  }

  public static JsSpec isArrayOfDec(final Predicate<BigDecimal> predicate,
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

  public static JsSpec isArrayOfDecSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfDecSuchThat(requireNonNull(predicate),
                                true,
                                false
                               );
  }

  public static JsSpec isArrayOfDecSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfIntegral(final Predicate<BigInteger> predicate)
  {
    return isArrayOfIntegral(requireNonNull(predicate),
                             true,
                             false
                            );
  }

  public static JsSpec isArrayOfIntegral(final Predicate<BigInteger> predicate,
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

  public static JsSpec isArrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfIntegralSuchThat(requireNonNull(predicate),
                                     true,
                                     false
                                    );
  }

  public static JsSpec isArrayOfIntegralSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfNumber(final Predicate<JsNumber> predicate)
  {

    return isArrayOfNumber(requireNonNull(predicate),
                           true,
                           false
                          );
  }

  public static JsSpec isArrayOfNumber(final Predicate<JsNumber> predicate,
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

  public static JsSpec isArrayOfNumberSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfNumberSuchThat(requireNonNull(predicate),
                                   true,
                                   false
                                  );
  }

  public static JsSpec isArrayOfNumberSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isArrayOfObj(final Predicate<JsObj> predicate,
                                    final boolean required,
                                    final boolean nullable
                                   )
  {
    return isArrayOfObj(requireNonNull(predicate),
                        required,
                        nullable
                       );
  }

  public static JsSpec isArrayOfObj(final Predicate<JsObj> predicate)
  {
    return new IsArrayOfTestedObj(s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(s,
                                                                 ERROR_CODE.OBJ_CONDITION
                                    ));
                                  },
                                  true,
                                  false
    );
  }

  public static JsSpec isArrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfObjSuchThat(requireNonNull(predicate),
                                true,
                                false
                               );
  }

  public static JsSpec isArrayOfObjSuchThat(final Predicate<JsArray> predicate,
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

  public static JsSpec isStr(final boolean required,
                             final boolean nullable
                            )
  {
    return new IsStr(required,
                     nullable
    );
  }

  public static JsSpec isStr(final Predicate<String> predicate)
  {
    return isStr(requireNonNull(predicate),
                 true,
                 false
                );
  }

  public static JsSpec isStr(final Predicate<String> predicate,
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

  public static JsSpec isInt(final boolean required,
                             final boolean nullable
                            )
  {
    return new IsInt(required,
                     nullable
    );
  }

  public static JsSpec isInt(final IntPredicate predicate)
  {
    return isInt(true,
                 false,
                 predicate
                );
  }

  public static JsSpec isInt(final boolean required,
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

  public static JsSpec isLong(final boolean required,
                              final boolean nullable
                             )
  {
    return new IsLong(required,
                      nullable
    );
  }


  public static JsSpec isLong(final LongPredicate predicate)
  {
    return isLong(true,
                  false,
                  predicate
                 );
  }

  public static JsSpec isLong(final boolean required,
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

  public static JsSpec isDecimal(final boolean required,
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

  public static JsSpec isDecimal(final Predicate<BigDecimal> predicate)
  {
    return isDecimal(true,
                     false,
                     predicate
                    );
  }

  public static JsSpec isDecimal(final boolean required,
                                 final boolean nullable
                                )
  {
    return new IsDecimal(required,
                         nullable
    );
  }

  public static JsSpec isIntegral(final Predicate<BigInteger> predicate)
  {
    return isIntegral(true,
                      false,
                      predicate
                     );
  }

  public static JsSpec isIntegral(final boolean required,
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

  public static JsSpec isIntegral(final boolean required,
                                  final boolean nullable
                                 )
  {
    return new IsIntegral(required,
                          nullable
    );
  }


  public static JsSpec isObj(final Predicate<JsObj> predicate)
  {
    return isObj(true,
                 false,
                 predicate
                );
  }

  public static JsSpec isObj(final boolean required,
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

  public static JsSpec isObj(final boolean required,
                             final boolean nullable
                            )
  {
    return new IsObj(required,
                     nullable
    );
  }

  public static JsArrayPredicate isArray(final Predicate<JsArray> predicate)
  {

    return isArray(true,
                   false,
                   predicate
                  );
  }

  public static JsArrayPredicate isArray(final boolean required,
                                         final boolean nullable,
                                         final Predicate<JsArray> predicate
                                        )
  {
    return new IsArraySuchThat(required,
                               nullable,
                               s ->
                               {
                                 if (requireNonNull(predicate).test(s)) return Optional.empty();
                                 return Optional.of(new Error(s,
                                                              ARRAY_CONDITION
                                 ));
                               }
    );
  }

  public static JsArrayPredicate isArray(final boolean required,
                                         final boolean nullable
                                        )
  {
    return new IsArray(required,
                       nullable
    );
  }

  public static JsArrayPredicate isArrayOfLong(final boolean required,
                                               final boolean nullable
                                              )
  {
    return new IsArrayOfLong(required,
                             nullable
    );
  }

  public static JsArrayPredicate isArrayOfInt(final boolean required,
                                              final boolean nullable
                                             )
  {
    return new IsArrayOfInt(required,
                            nullable
    );
  }

  public static JsArrayPredicate isArrayOfStr(final boolean required,
                                              final boolean nullable
                                             )
  {
    return new IsArrayOfStr(required,
                            nullable
    );
  }

  public static JsArrayPredicate isArrayOfBool(final boolean required,
                                               final boolean nullable
                                              )
  {
    return new IsArrayOfBool(required,
                             nullable
    );
  }

  public static JsArrayPredicate isArrayOfObj(final boolean required,
                                              final boolean nullable
                                             )
  {
    return new IsArrayOfObj(required,
                            nullable
    );
  }

  public static JsArrayPredicate isArrayOfDec(final boolean required,
                                              final boolean nullable
                                             )
  {
    return new IsArrayOfDecimal(required,
                                nullable
    );
  }

  public static JsArrayPredicate isArrayOfIntegral(final boolean required,
                                                   final boolean nullable
                                                  )
  {
    return new IsArrayOfIntegral(required,
                                 nullable
    );
  }

  public static JsArrayPredicate isArrayOfNumber(final boolean required,
                                                 final boolean nullable
                                                )
  {
    return new IsArrayOfNumber(required,
                               nullable
    );
  }
}
