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


  public static JsSpec conforms(final JsObjSpec spec)
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

  public static JsSpec any(final Predicate<JsValue> predicate
                          )
  {
    return any(predicate,
               true);
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

  public static JsSpec str = new IsStr(true,
                                       false
  );

  public static JsSpec number = new IsNumber(true,
                                             false
  );

  public static JsSpec bool = new IsBoolean(true,
                                            false
  );

  public static JsSpec longNumber = new IsLong(true,
                                               false
  );

  public static JsSpec decimal = new IsDecimal(true,
                                               false
  );

  public static JsSpec intNumber = new IsInt(true,
                                             false
  );

  public static JsSpec integral = new IsIntegral(true,
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

  public static JsSpec str(final boolean required,
                           final boolean nullable
                          )
  {
    return new IsStr(required,
                     nullable
    );
  }

  public static JsSpec str(final Predicate<String> predicate)
  {
    return str(requireNonNull(predicate),
               true,
               false
              );
  }

  public static JsSpec str(final Predicate<String> predicate,
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

  public static JsSpec intNumber(final boolean required,
                                 final boolean nullable
                                )
  {
    return new IsInt(required,
                     nullable
    );
  }

  public static JsSpec bool(final boolean required,
                            final boolean nullable
                           )
  {
    return new IsBoolean(required,
                         nullable
    );
  }

  public static JsSpec number(final Predicate<JsNumber> predicate)
  {
    return number(true,
                  false,
                  predicate
                 );
  }

  public static JsSpec number(final boolean required,
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

  public static JsSpec number(final boolean required,
                              final boolean nullable
                             )
  {
    return new IsNumber(required,
                        nullable
    );
  }

  public static JsSpec intNumber(final IntPredicate predicate)
  {
    return intNumber(true,
                     false,
                     predicate
                    );
  }

  public static JsSpec intNumber(final boolean required,
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

  public static JsSpec longNumber(final boolean required,
                                  final boolean nullable
                                 )
  {
    return new IsLong(required,
                      nullable
    );
  }


  public static JsSpec longNumber(final LongPredicate predicate)
  {
    return longNumber(true,
                      false,
                      predicate
                     );
  }

  public static JsSpec longNumber(final boolean required,
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

  public static JsSpec decimal(final boolean required,
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

  public static JsSpec decimal(final Predicate<BigDecimal> predicate)
  {
    return decimal(true,
                   false,
                   predicate
                  );
  }

  public static JsSpec decimal(final boolean required,
                               final boolean nullable
                              )
  {
    return new IsDecimal(required,
                         nullable
    );
  }

  public static JsSpec integral(final Predicate<BigInteger> predicate)
  {
    return integral(true,
                    false,
                    predicate
                   );
  }

  public static JsSpec integral(final boolean required,
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

  public static JsSpec integral(final boolean required,
                                final boolean nullable
                               )
  {
    return new IsIntegral(required,
                          nullable
    );
  }


  public static JsSpec obj(final Predicate<JsObj> predicate)
  {
    return obj(true,
               false,
               predicate
              );
  }

  public static JsSpec obj(final boolean required,
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

  public static JsSpec obj(final boolean required,
                           final boolean nullable
                          )
  {
    return new IsObj(required,
                     nullable
    );
  }

  public static JsArrayPredicate array(final Predicate<JsArray> predicate)
  {

    return array(true,
                 false,
                 predicate
                );
  }

  public static JsArrayPredicate array(final boolean required,
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

  public static JsArrayPredicate array(final boolean required,
                                       final boolean nullable
                                      )
  {
    return new IsArray(required,
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
