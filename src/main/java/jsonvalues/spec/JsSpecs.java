package jsonvalues.spec;

import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.*;

public class JsSpecs
{

  /**
   A required and none nullable spec that specifies an array of objects that conform the given spec
   @param spec the given spec that every object in the array has to conform
   @return a spec
   */
  public static IsArrayOfObjSpec arrayOf(final JsObjSpec spec)
  {
    return new IsArrayOfObjSpec(false,
                                true,
                                requireNonNull(spec)
    );
  }


  /**
   Spec that is conforms by any value of a Json
   */
  public static JsSpec any = new IsValue(true);


  public static JsSpec str = new IsStr(true,
                                       false
  );

  public static JsSpec str(final Predicate<String> predicate)
  {
    return new IsStrSuchThat(true,
                             false,
                             s ->
                             {
                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                               return Optional.of(new Error(JsStr.of(s),
                                                            STRING_CONDITION
                               ));
                             }

    );
  }

  public static JsSpec number = new IsNumber(true,
                                             false
  );

  public static JsSpec number(final Predicate<JsNumber> predicate)
  {
    return new IsNumberSuchThat(true,
                                false,
                                s ->
                                {
                                  if (requireNonNull(predicate).test(s)) return Optional.empty();
                                  return Optional.of(new Error(s,
                                                               ERROR_CODE.NUMBER_CONDITION
                                  ));
                                }
    );
  }


  public static JsSpec bool = new IsBoolean(true,
                                            false
  );


  public static JsSpec decimal = new IsDecimal(true,
                                               false
  );


  public static JsSpec integral = new IsIntegral(true,
                                                 false
  );

  public static JsSpec longNumber = new IsLong(true,
                                               false
  );

  public static JsSpec intNumber = new IsInt(true,
                                             false
  );

  public static JsSpec TRUE = new IsTrue(true,
                                         false
  );

  public static JsSpec FALSE = new IsFalse(true,
                                           false
  );

  public static JsSpec obj = new IsObj(true,
                                       false
  );


  public static JsSpec array = new IsArray(true,
                                           false
  );


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


  public static JsSpec arrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {
    return new IsArrayOfIntSuchThat(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ARRAY_CONDITION
                                      ));
                                    },
                                    true,
                                    false
    );

  }


  public static JsSpec arrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return new IsArrayOfTestedDecimal(s ->
                                      {
                                        if (requireNonNull(predicate).test(s)) return Optional.empty();
                                        return Optional.of(new Error(JsBigDec.of(s),
                                                                     DECIMAL_CONDITION
                                        ));
                                      },
                                      true,
                                      false
    );
  }

  public static JsSpec arrayOfDecSuchThat(final Predicate<JsArray> predicate
                                         )
  {
    return new IsArrayOfDecimalSuchThat(s ->
                                        {
                                          if (requireNonNull(predicate).test(s)) return Optional.empty();
                                          return Optional.of(new Error(s,
                                                                       ARRAY_CONDITION
                                          ));
                                        },
                                        true,
                                        false
    );
  }

  public static JsSpec arrayOfIntegral(final Predicate<BigInteger> predicate
                                      )
  {
    return new IsArrayOfTestedIntegral(s ->
                                       {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(JsBigInt.of(s),
                                                                      INTEGRAL_CONDITION
                                         ));
                                       },
                                       true,
                                       false
    );
  }


  public static JsSpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {
    return new IsArrayOfIntegralSuchThat(s ->
                                         {
                                           if (requireNonNull(predicate).test(s)) return Optional.empty();
                                           return Optional.of(new Error(s,
                                                                        ARRAY_CONDITION)
                                                             );
                                         },
                                         true,
                                         false
    );
  }

  public static JsSpec arrayOfNumber(final Predicate<JsNumber> predicate)
  {
    return new IsArrayOfTestedNumber(s ->
                                     {
                                       if (requireNonNull(predicate).test(s)) return Optional.empty();
                                       return Optional.of(new Error(s,
                                                                    ERROR_CODE.NUMBER_CONDITION)
                                                         );
                                     },
                                     true,
                                     false
    );
  }

  public static JsSpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate
                                            )
  {
    return new IsArrayOfNumberSuchThat(s ->
                                       {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(s,
                                                                      ARRAY_CONDITION)
                                                           );
                                       },
                                       true,
                                       false
    );
  }

  public static JsSpec arrayOfObj(final Predicate<JsObj> predicate
                                 )
  {
    return new IsArrayOfTestedObj(s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(s,
                                                                 OBJ_CONDITION)
                                                      );
                                  },
                                  true,
                                  false
    );

  }

  public static JsSpec arrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {
    return new IsArrayOfObjSuchThat(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ARRAY_CONDITION)
                                                        );
                                    },
                                    true,
                                    false
    );

  }


  public static JsSpec intNumber(
    final IntPredicate predicate
                                )
  {
    return new IsIntSuchThat(true,
                             false,
                             s ->
                             {
                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                               return Optional.of(new Error(JsInt.of(s),
                                                            INT_CONDITION)
                                                 );
                             }
    );
  }

  public static JsSpec arrayOfStrSuchThat(final Predicate<JsArray> predicate
                                         )
  {
    return new IsArrayOfStrSuchThat(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   ARRAY_CONDITION
                                      ));
                                    },
                                    true,
                                    false
    );
  }

  public static JsSpec array(final Predicate<JsValue> predicate)
  {
    return new IsArrayOfTestedValue(s ->
                                    {
                                      if (requireNonNull(predicate).test(s)) return Optional.empty();
                                      return Optional.of(new Error(s,
                                                                   VALUE_CONDITION
                                      ));
                                    },
                                    true,
                                    false
    );
  }


  public static JsSpec arrayOfLong(final LongPredicate predicate
                                  )
  {
    return new IsArrayOfTestedLong(s ->
                                   {
                                     if (requireNonNull(predicate).test(s)) return Optional.empty();
                                     return Optional.of(new Error(JsLong.of(s),
                                                                  LONG_CONDITION)
                                                       );
                                   },
                                   true,
                                   false
    );
  }

  public static JsSpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate
                                          )
  {
    return new IsArrayOfBoolSuchThat(s ->
                                     {
                                       if (requireNonNull(predicate).test(s)) return Optional.empty();
                                       return Optional.of(new Error(s,
                                                                    ARRAY_CONDITION)
                                                         );
                                     },
                                     true,
                                     false
    );
  }

  public static JsSpec longNumber(
    final LongPredicate predicate
                                 )
  {
    return new IsLongSuchThat(true,
                              false,
                              s ->
                              {
                                if (requireNonNull(predicate).test(s)) return Optional.empty();
                                return Optional.of(new Error(JsLong.of(s),
                                                             LONG_CONDITION)
                                                  );
                              }
    );
  }

  public static JsSpec decimal(
    final Predicate<BigDecimal> predicate
                              )
  {
    return new IsDecimalSuchThat(true,
                                 false,
                                 s ->
                                 {
                                   if (requireNonNull(predicate).test(s)) return Optional.empty();
                                   return Optional.of(new Error(JsBigDec.of(s),
                                                                DECIMAL_CONDITION)
                                                     );
                                 }
    );
  }


  public static JsSpec integral(
    final Predicate<BigInteger> predicate
                               )
  {
    return new IsIntegralSuchThat(true,
                                  false,
                                  s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(JsBigInt.of(s),
                                                                 INTEGRAL_CONDITION)
                                                      );
                                  }
    );
  }

  public static JsSpec arrayOfStr(final Predicate<String> predicate
                                 )
  {

    return new IsArrayOfTestedStr(s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(JsStr.of(s),
                                                                 STRING_CONDITION)
                                                      );
                                  },
                                  true,
                                  false
    );
  }

  /**returns a spec that conforms any value that is evaluated to true on the predicate.
   * When the type is not specified by the spec, positive numbers are parsed as Long by default,
   * which has to be taken into account in order to define any condition.
   * @param predicate the predicate
   * @return a spec
   */
  public static JsSpec any(final Predicate<JsValue> predicate
                          )
  {
    return new IsValueSuchThat(true,
                               v ->
                               {
                                 if (requireNonNull(predicate).test(v)) return Optional.empty();
                                 return Optional.of(new Error(v,
                                                              VALUE_CONDITION)
                                                   );
                               }
    );
  }

  public static JsSpec obj(final Predicate<JsObj> predicate)
  {
    return new IsObjSuchThat(true,
                             false,
                             s ->
                             {
                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                               return Optional.of(new Error(s,
                                                            OBJ_CONDITION)
                                                 );
                             }
    );
  }

  public static JsSpec arrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {
    return new IsArrayOfLongSuchThat(s ->
                                     {
                                       if (requireNonNull(predicate).test(s)) return Optional.empty();
                                       return Optional.of(new Error(s,
                                                                    ARRAY_CONDITION)
                                                         );
                                     },
                                     true,
                                     false
    );
  }

  public static JsArrayPredicate arraySuchThat(final Predicate<JsArray> predicate)
  {
    return new IsArraySuchThat(s ->
                               {
                                 if (requireNonNull(predicate).test(s)) return Optional.empty();
                                 return Optional.of(new Error(s,
                                                              ARRAY_CONDITION)
                                                   );
                               },
                               true,
                               false
    );

  }

  public static JsSpec arrayOfInt(final IntPredicate predicate)
  {
    return new IsArrayOfTestedInt(s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(JsInt.of(s),
                                                                 INT_CONDITION)
                                                      );
                                  },
                                  true,
                                  false
    );

  }

}
