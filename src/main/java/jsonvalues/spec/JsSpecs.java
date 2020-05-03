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
  public static JsArraySpec arrayOf(final JsObjSpec spec)
  {
    return new JsArrayOfJsObjSpec(false,
                                  true,
                                  requireNonNull(spec)
    );
  }


  /**
   Spec that is conforms by any value of a Json
   */
  public static JsSpec any = new AnySpec(true);


  public static JsSpec str = new JsStrSpec(true,
                                           false
  );

  public static JsSpec str(final Predicate<String> predicate)
  {
    return new JsStrSuchThatSpec(true,
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

  public static JsSpec number = new JsNumberSpec(true,
                                                 false
  );

  public static JsSpec number(final Predicate<JsNumber> predicate)
  {
    return new JsNumberSuchThatSpec(true,
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


  public static JsSpec bool = new JsBooleanSpec(true,
                                                false
  );


  public static JsSpec decimal = new JsDecimalSpec(true,
                                                   false
  );


  public static JsSpec integral = new JsIntegralSpec(true,
                                                     false
  );

  public static JsSpec longInteger = new JsLongSpec(true,
                                                    false
  );

  public static JsSpec integer = new JsIntSpec(true,
                                               false
  );

  public static JsSpec TRUE = new JsTrueConstantSpec(true,
                                                     false
  );

  public static JsSpec FALSE = new JsFalseConstantSpec(true,
                                                       false
  );

  public static JsSpec obj = new IsJsObjSpec(true,
                                             false
  );


  public static JsArraySpec array = new JsArrayOfValueSpec(true,
                                                           false
  );


  public static JsArraySpec arrayOfLong = new JsArrayOfLongSpec(true,
                                                                false
  );


  public static JsArraySpec arrayOfInt = new JsArrayOfIntSpec(true,
                                                              false
  );


  public static JsArraySpec arrayOfStr = new JsArrayOfStrSpec(true,
                                                              false
  );


  public static JsArraySpec arrayOfBool = new JsArrayOfBoolSpec(true,
                                                                false
  );


  public static JsArraySpec arrayOfDec = new JsArrayOfDecimalSpec(true,
                                                                  false
  );


  public static JsArraySpec arrayOfNumber = new JsArrayOfNumberSpec(true,
                                                                    false
  );


  public static JsArraySpec arrayOfIntegral = new JsArrayOfIntegralSpec(true,
                                                                        false
  );


  public static JsArraySpec arrayOfObj = new JsArrayOfObjSpec(true,
                                                              false
  );


  public static JsArraySpec arrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {
    return new JsArrayOfIntSuchThatSpec(s ->
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


  public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate)
  {

    return new JsArrayOfTestedDecimalSpec(s ->
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

  public static JsArraySpec arrayOfDecSuchThat(final Predicate<JsArray> predicate
                                              )
  {
    return new JsArrayOfDecimalSuchThatSpec(s ->
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

  public static JsArraySpec arrayOfIntegral(final Predicate<BigInteger> predicate
                                           )
  {
    return new JsArrayOfTestedIntegralSpec(s ->
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


  public static JsArraySpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate)
  {
    return new JsArrayOfIntegralSuchThatSpec(s ->
                                             {
                                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                                               return Optional.of(new Error(s,
                                                                            ARRAY_CONDITION
                                                                  )
                                                                 );
                                             },
                                             true,
                                             false
    );
  }

  public static JsArraySpec arrayOfNumber(final Predicate<JsNumber> predicate)
  {
    return new JsArrayOfTestedNumberSpec(s ->
                                         {
                                           if (requireNonNull(predicate).test(s)) return Optional.empty();
                                           return Optional.of(new Error(s,
                                                                        ERROR_CODE.NUMBER_CONDITION
                                                              )
                                                             );
                                         },
                                         true,
                                         false
    );
  }

  public static JsArraySpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate
                                                 )
  {
    return new JsArrayOfNumberSuchThatSpec(s ->
                                           {
                                             if (requireNonNull(predicate).test(s)) return Optional.empty();
                                             return Optional.of(new Error(s,
                                                                          ARRAY_CONDITION
                                                                )
                                                               );
                                           },
                                           true,
                                           false
    );
  }

  public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate
                                      )
  {
    return new JsArrayOfTestedObjSpec(s ->
                                      {
                                        if (requireNonNull(predicate).test(s)) return Optional.empty();
                                        return Optional.of(new Error(s,
                                                                     OBJ_CONDITION
                                                           )
                                                          );
                                      },
                                      true,
                                      false
    );

  }

  public static JsArraySpec arrayOfObjSuchThat(final Predicate<JsArray> predicate)
  {
    return new JsArrayOfObjSuchThatSpec(s ->
                                        {
                                          if (requireNonNull(predicate).test(s)) return Optional.empty();
                                          return Optional.of(new Error(s,
                                                                       ARRAY_CONDITION
                                                             )
                                                            );
                                        },
                                        true,
                                        false
    );

  }


  public static JsSpec integer(
    final IntPredicate predicate
                              )
  {
    return new JsIntSuchThatSpec(true,
                                 false,
                                 s ->
                                 {
                                   if (requireNonNull(predicate).test(s)) return Optional.empty();
                                   return Optional.of(new Error(JsInt.of(s),
                                                                INT_CONDITION
                                                      )
                                                     );
                                 }
    );
  }

  public static JsArraySpec arrayOfStrSuchThat(final Predicate<JsArray> predicate
                                              )
  {
    return new JsArrayOfStrSuchThatSpec(s ->
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

  public static JsArraySpec array(final Predicate<JsValue> predicate)
  {
    return new JsArrayOfTestedValueSpec(s ->
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


  public static JsArraySpec arrayOfLong(final LongPredicate predicate
                                       )
  {
    return new JsArrayOfTestedLongSpec(s ->
                                       {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(JsLong.of(s),
                                                                      LONG_CONDITION
                                                            )
                                                           );
                                       },
                                       true,
                                       false
    );
  }

  public static JsArraySpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate
                                               )
  {
    return new JsArrayOfBoolSuchThatSpec(s ->
                                         {
                                           if (requireNonNull(predicate).test(s)) return Optional.empty();
                                           return Optional.of(new Error(s,
                                                                        ARRAY_CONDITION
                                                              )
                                                             );
                                         },
                                         true,
                                         false
    );
  }

  public static JsSpec longInteger(
    final LongPredicate predicate
                                  )
  {
    return new JsLongSuchThatSpec(true,
                                  false,
                                  s ->
                                  {
                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                    return Optional.of(new Error(JsLong.of(s),
                                                                 LONG_CONDITION
                                                       )
                                                      );
                                  }
    );
  }

  public static JsSpec decimal(
    final Predicate<BigDecimal> predicate
                              )
  {
    return new JsDecimalSuchThatSpec(true,
                                     false,
                                     s ->
                                     {
                                       if (requireNonNull(predicate).test(s)) return Optional.empty();
                                       return Optional.of(new Error(JsBigDec.of(s),
                                                                    DECIMAL_CONDITION
                                                          )
                                                         );
                                     }
    );
  }


  public static JsSpec integral(
    final Predicate<BigInteger> predicate
                               )
  {
    return new JsIntegralSuchThatSpec(true,
                                      false,
                                      s ->
                                      {
                                        if (requireNonNull(predicate).test(s)) return Optional.empty();
                                        return Optional.of(new Error(JsBigInt.of(s),
                                                                     INTEGRAL_CONDITION
                                                           )
                                                          );
                                      }
    );
  }

  public static JsArraySpec arrayOfStr(final Predicate<String> predicate
                                      )
  {

    return new JsArrayOfTestedStrSpec(s ->
                                      {
                                        if (requireNonNull(predicate).test(s)) return Optional.empty();
                                        return Optional.of(new Error(JsStr.of(s),
                                                                     STRING_CONDITION
                                                           )
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
    return new AnySuchThatSpec(true,
                               v ->
                               {
                                 if (requireNonNull(predicate).test(v)) return Optional.empty();
                                 return Optional.of(new Error(v,
                                                              VALUE_CONDITION
                                                    )
                                                   );
                               }
    );
  }

  public static JsSpec obj(final Predicate<JsObj> predicate)
  {
    return new JsObjSuchThatSpec(true,
                                 false,
                                 s ->
                                 {
                                   if (requireNonNull(predicate).test(s)) return Optional.empty();
                                   return Optional.of(new Error(s,
                                                                OBJ_CONDITION
                                                      )
                                                     );
                                 }
    );
  }

  public static JsArraySpec arrayOfLongSuchThat(final Predicate<JsArray> predicate)
  {
    return new JsArrayOfLongSuchThatSpec(s ->
                                         {
                                           if (requireNonNull(predicate).test(s)) return Optional.empty();
                                           return Optional.of(new Error(s,
                                                                        ARRAY_CONDITION
                                                              )
                                                             );
                                         },
                                         true,
                                         false
    );
  }

  public static JsArraySpec arraySuchThat(final Predicate<JsArray> predicate)
  {
    return new JsArraySuchThatSpec(s ->
                                   {
                                     if (requireNonNull(predicate).test(s)) return Optional.empty();
                                     return Optional.of(new Error(s,
                                                                  ARRAY_CONDITION
                                                        )
                                                       );
                                   },
                                   true,
                                   false
    );

  }

  public static JsArraySpec arrayOfInt(final IntPredicate predicate)
  {
    return new JsArrayOfTestedIntSpec(s ->
                                      {
                                        if (requireNonNull(predicate).test(s)) return Optional.empty();
                                        return Optional.of(new Error(JsInt.of(s),
                                                                     INT_CONDITION
                                                           )
                                                          );
                                      },
                                      true,
                                      false
    );

  }

  public static JsTupleSpec tuple(JsSpec spec,
                                  JsSpec... others
                                 )
  {
    return JsTupleSpec.of(spec,
                          others);
  }

}
