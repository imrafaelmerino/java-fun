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
  public static ArrayOfJsObjSpec arrayOf(final JsObjSpec spec)
  {
    return new ArrayOfJsObjSpec(false,
                                true,
                                requireNonNull(spec)
    );
  }


  /**
   Spec that is conforms by any value of a Json
   */
  public static JsSpec any = new AnySpec(true);


  public static JsSpec str = new StrSpec(true,
                                         false
  );

  public static JsSpec str(final Predicate<String> predicate)
  {
    return new StrSuchThatSpec(true,
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

  public static JsSpec number = new NumberSpec(true,
                                               false
  );

  public static JsSpec number(final Predicate<JsNumber> predicate)
  {
    return new NumberSuchThatSpec(true,
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


  public static JsSpec bool = new BooleanSpec(true,
                                              false
  );


  public static JsSpec decimal = new DecimalSpec(true,
                                                 false
  );


  public static JsSpec integral = new IntegralSpec(true,
                                                   false
  );

  public static JsSpec longNum = new LongSpec(true,
                                              false
  );

  public static JsSpec intNum = new IntSpec(true,
                                            false
  );

  public static JsSpec TRUE = new TrueConstantSpec(true,
                                                   false
  );

  public static JsSpec FALSE = new FalseConstantSpec(true,
                                                     false
  );

  public static JsSpec obj = new ObjSpec(true,
                                         false
  );


  public static JsSpec array = new ArraySpec(true,
                                             false
  );


  public static JsSpec arrayOfLong = new ArrayOfLongSpec(true,
                                                         false
  );


  public static JsSpec arrayOfInt = new ArrayOfIntSpec(true,
                                                       false
  );


  public static JsSpec arrayOfStr = new ArrayOfStrSpec(true,
                                                       false
  );


  public static JsSpec arrayOfBool = new ArrayOfBoolSpec(true,
                                                         false
  );


  public static JsSpec arrayOfDec = new ArrayOfDecimalSpec(true,
                                                           false
  );


  public static JsSpec arrayOfNumber = new ArrayOfNumberSpec(true,
                                                             false
  );


  public static JsSpec arrayOfIntegral = new ArrayOfIntegralSpec(true,
                                                                 false
  );


  public static JsSpec arrayOfObj = new ArrayOfObjSpec(true,
                                                       false
  );


  public static JsSpec arrayOfIntSuchThat(final Predicate<JsArray> predicate)
  {
    return new ArrayOfIntSuchThatSpec(s ->
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

    return new ArrayOfTestedDecimalSpec(s ->
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
    return new ArrayOfDecimalSuchThatSpec(s ->
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
    return new ArrayOfTestedIntegralSpec(s ->
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
    return new ArrayOfIntegralSuchThatSpec(s ->
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
    return new ArrayOfTestedNumberSpec(s ->
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
    return new ArrayOfNumberSuchThatSpec(s ->
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
    return new ArrayOfTestedObjSpec(s ->
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
    return new ArrayOfObjSuchThatSpec(s ->
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
    return new IntSuchThatSpec(true,
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
    return new ArrayOfStrSuchThatSpec(s ->
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
    return new ArrayOfTestedValueSpec(s ->
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
    return new ArrayOfTestedLongSpec(s ->
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
    return new ArrayOfBoolSuchThatSpec(s ->
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
    return new LongSuchThatSpec(true,
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
    return new DecimalSuchThatSpec(true,
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
    return new IntegralSuchThatSpec(true,
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

    return new ArrayOfTestedStrSpec(s ->
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
    return new AnySuchThatSpec(true,
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
    return new ObjSuchThatSpec(true,
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
    return new ArrayOfLongSuchThatSpec(s ->
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
    return new ArraySuchThatSpec(s ->
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
    return new ArrayOfTestedIntSpec(s ->
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
