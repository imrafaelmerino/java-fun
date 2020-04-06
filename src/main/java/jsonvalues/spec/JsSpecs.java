package jsonvalues.spec;

import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public class JsSpecs
{

  public static JsSpec isStr = new IsStr(true,
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

  public static JsSpec any = (JsPredicate) value -> Optional.empty();

  public static JsSpec isArrayOfStr(final Predicate<String> predicate)
  {
    return isArrayOfString(predicate,
                           true,
                           false
                          );
  }

  public static JsSpec isArrayOfStrSuchThat(final Predicate<JsArray> predicate)
  {

    return new IsArrayOfStrSuchThat(s ->
                                    {
                                      if (predicate.test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ERROR_CODE.STRING_CONDITION
                                      ));
                                    },
                                    true,
                                    false
    );
  }

  public static JsSpec isArrayOfString(final Predicate<String> predicate,
                                       boolean required,
                                       boolean nullable
                                      )
  {

    return new IsArrayOfTestedStr(s ->
                                  {
                                    if (predicate.test(s)) return Optional.empty();
                                    return Optional.of(new Error(s,
                                                                 ERROR_CODE.STRING_CONDITION
                                    ));
                                  },
                                  required,
                                  nullable
    );
  }

  public static JsSpec isArrayOfStrSuchThat(final Predicate<JsArray> predicate,
                                            boolean required,
                                            boolean nullable
                                           )
  {
    return new IsArrayOfStrSuchThat(s ->
                                    {
                                      if (predicate.test(s)) return Optional.empty();
                                      return Optional.of(new Error(String.valueOf(s),
                                                                   ERROR_CODE.STRING_CONDITION
                                      ));
                                    },
                                    required,
                                    nullable
    );
  }

  public static JsSpec isArrayOfLong(final LongPredicate predicate)
  {

    return isArrayOfLong(predicate,
                         true,
                         false
                        );
  }

  public static JsSpec isArrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {

    return new IsArrayOfLongSuchThat(s ->
                                     {
                                       if (predicate.test(s)) return Optional.empty();
                                       return Optional.of(new Error(s,
                                                                    ERROR_CODE.LONG_CONDITION
                                       ));
                                     },
                                     true,
                                     false
    );
  }

  public static JsSpec isArrayOfLongSuchThat(final Predicate<JsArray> predicate,
                                             boolean required,
                                             boolean nullable
                                            )
  {
    return new IsArrayOfLongSuchThat(s ->
                                     {
                                       if (predicate.test(s)) return Optional.empty();
                                       return Optional.of(new Error(String.valueOf(s),
                                                                    ERROR_CODE.ARRAY_CONDITION
                                       ));
                                     },
                                     required,
                                     nullable
    );
  }

  public static JsSpec isArrayOfLong(final LongPredicate predicate,
                                     boolean required,
                                     boolean nullable
                                    )
  {
    return new IsArrayOfTestedLong(s ->
                                   {
                                     if (predicate.test(s)) return Optional.empty();
                                     return Optional.of(new Error(String.valueOf(s),
                                                                  ERROR_CODE.LONG_CONDITION
                                     ));
                                   },
                                   required,
                                   nullable
    );
  }

  public static JsSpec isArrayOfInt(final IntPredicate predicate)
  {

    return isArrayOfInt(predicate,
                        true,
                        false
                       );
  }

  public static JsSpec isArrayOfInt(final IntPredicate predicate,
                                    boolean required,
                                    boolean nullable
                                   )
  {
    return new IsArrayOfTestedInt(s ->
                                  {
                                    if (predicate.test(s)) return Optional.empty();
                                    return Optional.of(new Error(String.valueOf(s),
                                                                 ERROR_CODE.INT_CONDITION
                                    ));
                                  },
                                  required,
                                  nullable
    );
  }

  public static JsSpec isArrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfIntSuchThat(predicate,
                                true,
                                false
                               );
  }

  public static JsSpec isArrayOfIntSuchThat(final Predicate<JsArray> predicate,
                                            boolean required,
                                            boolean nullable
                                           )
  {
    return new IsArrayOfIntSuchThat(s ->
                                    {
                                      if (predicate.test(s)) return Optional.empty();
                                      return Optional.of(new Error(String.valueOf(s),
                                                                   ERROR_CODE.INT_CONDITION
                                      ));
                                    },
                                    required,
                                    nullable
    );
  }

  public static JsSpec isArrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return isArrayOfDec(predicate,
                        true,
                        false
                       );
  }

  public static JsSpec isArrayOfDec(final Predicate<BigDecimal> predicate,
                                    boolean required,
                                    boolean nullable
                                   )
  {
    return new IsArrayOfTestedDecimal(s ->
                                      {
                                        if (predicate.test(s)) return Optional.empty();
                                        return Optional.of(new Error(String.valueOf(s),
                                                                     ERROR_CODE.INTEGRAL_CONDITION
                                        ));
                                      },
                                      required,
                                      nullable
    );
  }

  public static JsSpec isArrayOfDecSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfDecSuchThat(predicate,
                                true,
                                false
                               );
  }

  public static JsSpec isArrayOfDecSuchThat(final Predicate<JsArray> predicate,
                                            boolean required,
                                            boolean nullable
                                           )
  {
    return new IsArrayOfDecimalSuchThat(s ->
                                        {
                                          if (predicate.test(s)) return Optional.empty();
                                          return Optional.of(new Error(String.valueOf(s),
                                                                       ERROR_CODE.DECIMAL_CONDITION
                                          ));
                                        },
                                        required,
                                        nullable
    );
  }

  public static JsSpec isArrayOfIntegral(final Predicate<BigInteger> predicate)
  {
    return isArrayOfIntegral(predicate,
                             true,
                             false
                            );
  }

  public static JsSpec isArrayOfIntegral(final Predicate<BigInteger> predicate,
                                         boolean required,
                                         boolean nullable
                                        )
  {
    return new IsArrayOfTestedIntegral(s ->
                                       {
                                         if (predicate.test(s)) return Optional.empty();
                                         return Optional.of(new Error(String.valueOf(s),
                                                                      ERROR_CODE.INTEGRAL_CONDITION
                                         ));
                                       },
                                       required,
                                       nullable
    );
  }

  public static JsSpec isArrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfIntegralSuchThat(predicate,
                                     true,
                                     false
                                    );
  }

  public static JsSpec isArrayOfIntegralSuchThat(final Predicate<JsArray> predicate,
                                                 boolean required,
                                                 boolean nullable
                                                )
  {
    return new IsArrayOfIntegralSuchThat(s ->
                                         {
                                           if (predicate.test(s)) return Optional.empty();
                                           return Optional.of(new Error(String.valueOf(s),
                                                                        ERROR_CODE.ARRAY_CONDITION
                                           ));
                                         },
                                         required,
                                         nullable
    );
  }

  public static JsSpec isArrayOfNumber(final Predicate<JsNumber> predicate)
  {

    return isArrayOfNumber(predicate,
                           true,
                           false
                          );
  }

  public static JsSpec isArrayOfNumber(final Predicate<JsNumber> predicate,
                                       boolean required,
                                       boolean nullable
                                      )
  {
    return new IsArrayOfTestedNumber(s ->
                                     {
                                       if (predicate.test(s)) return Optional.empty();
                                       return Optional.of(new Error(String.valueOf(s),
                                                                    ERROR_CODE.ARRAY_CONDITION
                                       ));
                                     },
                                     required,
                                     nullable
    );
  }

  public static JsSpec isArrayOfNumberSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfNumberSuchThat(predicate,
                                   true,
                                   false
                                  );
  }

  public static JsSpec isArrayOfNumberSuchThat(final Predicate<JsArray> predicate,
                                               boolean required,
                                               boolean nullable
                                              )
  {
    return new IsArrayOfNumberSuchThat(s ->
                                       {
                                         if (predicate.test(s)) return Optional.empty();
                                         return Optional.of(new Error(String.valueOf(s),
                                                                      ERROR_CODE.ARRAY_CONDITION
                                         ));
                                       },
                                       required,
                                       nullable
    );
  }

  public static JsSpec isArrayOfObj(final Predicate<JsObj> predicate,
                                    boolean required,
                                    boolean nullable
                                   )
  {
    return isArrayOfObj(predicate,
                        required,
                        nullable
                       );
  }

  public static JsSpec isArrayOfObj(final Predicate<JsObj> predicate)
  {
    return new IsArrayOfTestedObj(s ->
                                  {
                                    if (predicate.test(s)) return Optional.empty();
                                    return Optional.of(new Error(String.valueOf(s),
                                                                 ERROR_CODE.ARRAY_CONDITION
                                    ));
                                  },
                                  true,
                                  false
    );
  }

  public static JsSpec isArrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {

    return isArrayOfObjSuchThat(predicate,
                                true,
                                false
                               );
  }

  public static JsSpec isArrayOfObjSuchThat(final Predicate<JsArray> predicate,
                                            boolean required,
                                            boolean nullable
                                           )
  {
    return new IsArrayOfObjSuchThat(s ->
                                    {
                                      if (predicate.test(s)) return Optional.empty();
                                      return Optional.of(new Error(String.valueOf(s),
                                                                   ERROR_CODE.ARRAY_CONDITION
                                      ));
                                    },
                                    required,
                                    nullable
    );
  }

  public static JsSpec isStr(boolean required,
                             boolean nullable
                            )
  {
    return new IsStr(required,
                     nullable
    );
  }

  public static JsSpec isStr(Predicate<String> predicate)
  {
    return isStr(predicate,
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
                               if (predicate.test(s)) return Optional.empty();
                               return Optional.of(new Error(String.valueOf(s),
                                                            ERROR_CODE.STRING_CONDITION
                               ));
                             }

    );
  }

  public static JsSpec isInt(boolean required,
                             boolean nullable
                            )
  {
    return new IsInt(required,
                     nullable
    );
  }

  public static JsSpec isInt(IntPredicate predicate)
  {
    return isInt(true,
                 false,
                 predicate
                );
  }

  public static JsSpec isInt(boolean required,
                             boolean nullable,
                             IntPredicate predicate
                            )
  {
    return new IsIntSuchThat(required,
                             nullable,
                             s ->
                             {
                               if (predicate.test(s)) return Optional.empty();
                               return Optional.of(new Error(String.valueOf(s),
                                                            ERROR_CODE.INT_CONDITION
                               ));
                             }
    );
  }

  public static JsSpec isLong(boolean required,
                              boolean nullable
                             )
  {
    return new IsLong(required,
                      nullable
    );
  }


  public static JsSpec isLong(LongPredicate predicate)
  {
    return isLong(true,
                  false,
                  predicate
                 );
  }

  public static JsSpec isLong(boolean required,
                              boolean nullable,
                              LongPredicate predicate
                             )
  {
    return new IsLongSuchThat(required,
                              nullable,
                              s ->
                              {
                                if (predicate.test(s)) return Optional.empty();
                                return Optional.of(new Error(String.valueOf(s),
                                                             ERROR_CODE.LONG_CONDITION
                                ));
                              }
    );
  }

  public static JsSpec isDecimal(boolean required,
                                 boolean nullable,
                                 Predicate<BigDecimal> predicate
                                )
  {
    return new IsDecimalSuchThat(required,
                                 nullable,
                                 s ->
                                 {
                                   if (predicate.test(s)) return Optional.empty();
                                   return Optional.of(new Error(String.valueOf(s),
                                                                ERROR_CODE.DECIMAL_CONDITION
                                   ));
                                 }
    );
  }

  public static JsSpec isDecimal(Predicate<BigDecimal> predicate)
  {
    return isDecimal(true,
                     false,
                     predicate
                    );
  }

  public static JsSpec isDecimal(boolean required,
                                 boolean nullable
                                )
  {
    return new IsDecimal(required,
                         nullable
    );
  }

  public static JsSpec isIntegral(Predicate<BigInteger> predicate)
  {
    return isIntegral(true,
                      false,
                      predicate
                     );
  }

  public static JsSpec isIntegral(boolean required,
                                  boolean nullable,
                                  Predicate<BigInteger> predicate
                                 )
  {
    return new IsIntegralSuchThat(required,
                                  nullable,
                                  s ->
                                  {
                                    if (predicate.test(s)) return Optional.empty();
                                    return Optional.of(new Error(String.valueOf(s),
                                                                 ERROR_CODE.INTEGRAL_CONDITION
                                    ));
                                  }
    );
  }

  public static JsSpec isIntegral(boolean required,
                                  boolean nullable
                                 )
  {
    return new IsIntegral(required,
                          nullable
    );
  }


  public static JsSpec isObj(Predicate<JsObj> predicate)
  {
    return isObj(true,
                 false,
                 predicate
                );
  }

  public static JsSpec isObj(boolean required,
                             boolean nullable,
                             Predicate<JsObj> predicate
                            )
  {
    return new IsObjSuchThat(required,
                             nullable,
                             s ->
                             {
                               if (predicate.test(s)) return Optional.empty();
                               return Optional.of(new Error(String.valueOf(s),
                                                            ERROR_CODE.OBJ_CONDITION
                               ));
                             }
    );
  }

  public static JsSpec isObj(boolean required,
                             boolean nullable
                            )
  {
    return new IsObj(required,
                     nullable
    );
  }

  public static JsArrayPredicate isArray(Predicate<JsArray> predicate)
  {

    return isArray(true,
                   false,
                   predicate
                  );
  }

  public static JsArrayPredicate isArray(boolean required,
                                         boolean nullable,
                                         Predicate<JsArray> predicate
                                        )
  {
    return new IsArraySuchThat(required,
                               nullable,
                               s ->
                               {
                                 if (predicate.test(s)) return Optional.empty();
                                 return Optional.of(new Error(String.valueOf(s),
                                                              ERROR_CODE.ARRAY_CONDITION
                                 ));
                               }
    );
  }

  public static JsArrayPredicate isArray(boolean required,
                                         boolean nullable
                                        )
  {
    return new IsArray(required,
                       nullable
    );
  }

  public static JsArrayPredicate isArrayOfLong(boolean required,
                                               boolean nullable
                                              )
  {
    return new IsArrayOfLong(required,
                             nullable
    );
  }

  public static JsArrayPredicate isArrayOfInt(boolean required,
                                              boolean nullable
                                             )
  {
    return new IsArrayOfInt(required,
                            nullable
    );
  }

  public static JsArrayPredicate isArrayOfStr(boolean required,
                                              boolean nullable
                                             )
  {
    return new IsArrayOfStr(required,
                            nullable
    );
  }

  public static JsArrayPredicate isArrayOfBool(boolean required,
                                               boolean nullable
                                              )
  {
    return new IsArrayOfBool(required,
                             nullable
    );
  }

  public static JsArrayPredicate isArrayOfObj(boolean required,
                                              boolean nullable
                                             )
  {
    return new IsArrayOfObj(required,
                            nullable
    );
  }

  public static JsArrayPredicate isArrayOfDec(boolean required,
                                              boolean nullable
                                             )
  {
    return new IsArrayOfDecimal(required,
                                nullable
    );
  }

  public static JsArrayPredicate isArrayOfIntegral(boolean required,
                                                   boolean nullable
                                                  )
  {
    return new IsArrayOfIntegral(required,
                                 nullable
    );
  }

  public static JsArrayPredicate isArrayOfNumber(boolean required,
                                                 boolean nullable
                                                )
  {
    return new IsArrayOfNumber(required,
                               nullable
    );
  }
}
