package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import io.vavr.Tuple2;

class JsParser
{
  static Tuple2<Boolean, SpecDeserializer> getDeserializer(final JsPredicate spec)
  {
    if (spec instanceof IsValue)
    {
      final IsValue isValue = (IsValue) spec;
      return new Tuple2<>(isValue.required,
                          DeserializersFactory.INSTANCE.ofValue()
      );
    } else if (spec instanceof IsValueSuchThat)
    {
      final IsValueSuchThat isValue = (IsValueSuchThat) spec;
      return new Tuple2<>(isValue.required,
                          DeserializersFactory.INSTANCE.ofValueSuchThat(isValue.predicate)
      );
    } else if (spec instanceof JsPrimitivePredicate)
    {
      if (spec instanceof JsStrPredicate)
      {
        if (spec instanceof IsStr)
        {
          final IsStr isStr = (IsStr) spec;
          return new Tuple2<>(isStr.required,
                              DeserializersFactory.INSTANCE.ofStr(isStr.nullable)
          );

        } else if (spec instanceof IsStrSuchThat)
        {
          final IsStrSuchThat isStrSuchThat = (IsStrSuchThat) spec;
          return new Tuple2<>(isStrSuchThat.required,
                              DeserializersFactory.INSTANCE.ofStrSuchThat(isStrSuchThat.predicate,
                                                                          isStrSuchThat.nullable
                                                                         )
          );

        }
      } else if (spec instanceof JsIntPredicate)
      {
        if (spec instanceof IsInt)
        {
          final IsInt isInt = (IsInt) spec;
          return new Tuple2<>(isInt.required,
                              DeserializersFactory.INSTANCE.ofInt(isInt.nullable)
          );

        } else if (spec instanceof IsIntSuchThat)
        {
          final IsIntSuchThat isIntSuchThat = (IsIntSuchThat) spec;
          return new Tuple2<>(isIntSuchThat.required,
                              DeserializersFactory.INSTANCE.ofIntSuchThat(isIntSuchThat.predicate,
                                                                          isIntSuchThat.nullable
                                                                         )
          );
        }
      } else if (spec instanceof JsLongPredicate)
      {
        if (spec instanceof IsLong)
        {
          final IsLong isLong = (IsLong) spec;
          return new Tuple2<>(isLong.required,
                              DeserializersFactory.INSTANCE.ofLong(isLong.nullable)
          );
        } else if (spec instanceof IsLongSuchThat)
        {
          final IsLongSuchThat isLongSuchThat = (IsLongSuchThat) spec;
          return new Tuple2<>(isLongSuchThat.required,
                              DeserializersFactory.INSTANCE.ofLongSuchThat(isLongSuchThat.predicate,
                                                                           isLongSuchThat.nullable
                                                                          )
          );
        }
      } else if (spec instanceof JsDecimalPredicate)
      {
        if (spec instanceof IsDecimal)
        {
          final IsDecimal isDecimal = (IsDecimal) spec;
          return new Tuple2<>(isDecimal.required,
                              DeserializersFactory.INSTANCE.ofDecimal(isDecimal.nullable)
          );
        } else if (spec instanceof IsDecimalSuchThat)
        {
          final IsDecimalSuchThat isDecimalSuchThat = (IsDecimalSuchThat) spec;
          return new Tuple2<>(isDecimalSuchThat.required,
                              DeserializersFactory.INSTANCE.ofDecimalSuchThat(isDecimalSuchThat.predicate,
                                                                              isDecimalSuchThat.nullable
                                                                             )
          );
        }
      } else if (spec instanceof JsNumberPredicate)
      {
        if (spec instanceof IsNumber)
        {
          final IsNumber isNumber = (IsNumber) spec;
          return new Tuple2<>(isNumber.required,
                              DeserializersFactory.INSTANCE.ofNumber(isNumber.nullable)
          );
        } else if (spec instanceof IsNumberSuchThat)
        {
          final IsNumberSuchThat isNumberSuchThat = (IsNumberSuchThat) spec;
          return new Tuple2<>(isNumberSuchThat.required,
                              DeserializersFactory.INSTANCE.ofNumberSuchThat(isNumberSuchThat.predicate,
                                                                             isNumberSuchThat.nullable
                                                                            )
          );
        }
      } else if (spec instanceof JsIntegralPredicate)
      {
        if (spec instanceof IsIntegral)
        {
          final IsIntegral isIntegral = (IsIntegral) spec;
          return new Tuple2<>(isIntegral.required,
                              DeserializersFactory.INSTANCE.ofIntegral(isIntegral.nullable)
          );
        } else if (spec instanceof IsIntegralSuchThat)
        {
          final IsIntegralSuchThat isIntegralSuchThat = (IsIntegralSuchThat) spec;
          return new Tuple2<>(isIntegralSuchThat.required,
                              DeserializersFactory.INSTANCE.ofIntegralSuchThat(isIntegralSuchThat.predicate,
                                                                               isIntegralSuchThat.nullable
                                                                              )
          );
        }
      } else if (spec instanceof JsBoolPredicate)
      {
        if (spec instanceof IsBoolean)
        {
          final IsBoolean isBoolean = (IsBoolean) spec;
          return new Tuple2<>(isBoolean.required,
                              DeserializersFactory.INSTANCE.ofBool(isBoolean.nullable)
          );
        } else if (spec instanceof IsTrue)
        {
          final IsTrue isTrue = (IsTrue) spec;
          return new Tuple2<>(isTrue.required,
                              DeserializersFactory.INSTANCE.ofTrue(isTrue.nullable)
          );
        } else if (spec instanceof IsFalse)
        {
          final IsFalse isFalse = (IsFalse) spec;
          return new Tuple2<>(isFalse.required,
                              DeserializersFactory.INSTANCE.ofFalse(isFalse.nullable)
          );
        }
      }
    } else if (spec instanceof JsonPredicate)
    {
      if (spec instanceof JsArrayPredicate)
      {
        if (spec instanceof IsArray)
        {
          IsArray isArray = ((IsArray) spec);
          return new Tuple2<>(isArray.required,
                              DeserializersFactory.INSTANCE.ofArrayOfValue(isArray.nullable
                                                                          )
          );
        } else if (spec instanceof IsArraySuchThat)
        {
          IsArraySuchThat isArray = ((IsArraySuchThat) spec);
          return new Tuple2<>(isArray.required,
                              DeserializersFactory.INSTANCE.ofArrayOfValueSuchThat(isArray.predicate,
                                                                                   isArray.nullable
                                                                                  )
          );
        } else if (spec instanceof IsArrayOfInt)
        {


          IsArrayOfInt isArrayOfInt = ((IsArrayOfInt) spec);
          return new Tuple2<>(isArrayOfInt.required,
                              DeserializersFactory.INSTANCE.ofArrayOfInt(isArrayOfInt.nullable
                                                                        )
          );


        } else if (spec instanceof IsArrayOfTestedInt)
        {
          IsArrayOfTestedInt isArrayOfInt = ((IsArrayOfTestedInt) spec);
          return new Tuple2<>(isArrayOfInt.required,
                              DeserializersFactory.INSTANCE.ofArrayOfIntEachSuchThat(isArrayOfInt.predicate,
                                                                                     isArrayOfInt.nullable
                                                                                    )
          );
        } else if (spec instanceof IsArrayOfIntSuchThat)
        {
          IsArrayOfIntSuchThat isArrayOfInt = ((IsArrayOfIntSuchThat) spec);
          return new Tuple2<>(isArrayOfInt.required,
                              DeserializersFactory.INSTANCE.ofArrayOfIntSuchThat(isArrayOfInt.predicate,
                                                                                 isArrayOfInt.nullable
                                                                                )
          );

        } else if (spec instanceof IsArrayOfObj)
        {


          IsArrayOfObj isArrayOfObj = ((IsArrayOfObj) spec);
          return new Tuple2<>(isArrayOfObj.required,
                              DeserializersFactory.INSTANCE.ofArrayOfObj(isArrayOfObj.nullable
                                                                        )
          );


        } else if (spec instanceof IsArrayOfTestedObj)
        {
          IsArrayOfTestedObj isArrayOfObj = ((IsArrayOfTestedObj) spec);
          return new Tuple2<>(isArrayOfObj.required,
                              DeserializersFactory.INSTANCE.ofArrayOfObjEachSuchThat(isArrayOfObj.predicate,
                                                                                     isArrayOfObj.nullable
                                                                                    )
          );
        } else if (spec instanceof IsArrayOfObjSuchThat)
        {
          IsArrayOfObjSuchThat isArrayOfObj = ((IsArrayOfObjSuchThat) spec);
          return new Tuple2<>(isArrayOfObj.required,
                              DeserializersFactory.INSTANCE.ofArrayOfObjSuchThat(isArrayOfObj.predicate,
                                                                                 isArrayOfObj.nullable
                                                                                )
          );

        } else if (spec instanceof IsArrayOfIntegral)
        {


          IsArrayOfIntegral isArrayOfIntegral = ((IsArrayOfIntegral) spec);
          return new Tuple2<>(isArrayOfIntegral.required,
                              DeserializersFactory.INSTANCE.ofArrayOfIntegral(isArrayOfIntegral.nullable
                                                                             )
          );

        } else if (spec instanceof IsArrayOfTestedIntegral)
        {
          IsArrayOfTestedIntegral isArrayOfIntegral = ((IsArrayOfTestedIntegral) spec);
          return new Tuple2<>(isArrayOfIntegral.required,
                              DeserializersFactory.INSTANCE.ofArrayOfIntegralEachSuchThat(isArrayOfIntegral.predicate,
                                                                                          isArrayOfIntegral.nullable
                                                                                         )
          );
        } else if (spec instanceof IsArrayOfIntegralSuchThat)
        {
          IsArrayOfIntegralSuchThat isArrayOfIntegral = ((IsArrayOfIntegralSuchThat) spec);
          return new Tuple2<>(isArrayOfIntegral.required,
                              DeserializersFactory.INSTANCE.ofArrayOfIntegralSuchThat(isArrayOfIntegral.predicate,
                                                                                      isArrayOfIntegral.nullable
                                                                                     )
          );

        } else if (spec instanceof IsArrayOfLong)
        {


          IsArrayOfLong isArrayOfLong = ((IsArrayOfLong) spec);
          return new Tuple2<>(isArrayOfLong.required,
                              DeserializersFactory.INSTANCE.ofArrayOfLong(isArrayOfLong.nullable
                                                                         )
          );

        } else if (spec instanceof IsArrayOfTestedLong)
        {
          IsArrayOfTestedLong isArrayOfLong = ((IsArrayOfTestedLong) spec);
          return new Tuple2<>(isArrayOfLong.required,
                              DeserializersFactory.INSTANCE.ofArrayOfLongEachSuchThat(isArrayOfLong.predicate,
                                                                                      isArrayOfLong.nullable
                                                                                     )
          );
        } else if (spec instanceof IsArrayOfLongSuchThat)
        {
          IsArrayOfLongSuchThat isArrayOfLong = ((IsArrayOfLongSuchThat) spec);
          return new Tuple2<>(isArrayOfLong.required,
                              DeserializersFactory.INSTANCE.ofArrayOfLongSuchThat(isArrayOfLong.predicate,
                                                                                  isArrayOfLong.nullable
                                                                                 )
          );

        } else if (spec instanceof IsArrayOfStr)
        {


          IsArrayOfStr isArrayOfStr = ((IsArrayOfStr) spec);
          return new Tuple2<>(isArrayOfStr.required,
                              DeserializersFactory.INSTANCE.ofArrayOfStr(isArrayOfStr.nullable
                                                                        )
          );

        } else if (spec instanceof IsArrayOfTestedStr)
        {
          IsArrayOfTestedStr isArrayOfStr = ((IsArrayOfTestedStr) spec);
          return new Tuple2<>(isArrayOfStr.required,
                              DeserializersFactory.INSTANCE.ofArrayOfStrEachSuchThat(isArrayOfStr.predicate,
                                                                                     isArrayOfStr.nullable
                                                                                    )
          );
        } else if (spec instanceof IsArrayOfStrSuchThat)
        {
          IsArrayOfStrSuchThat isArrayOfStr = ((IsArrayOfStrSuchThat) spec);
          return new Tuple2<>(isArrayOfStr.required,
                              DeserializersFactory.INSTANCE.ofArrayOfStrSuchThat(isArrayOfStr.predicate,
                                                                                 isArrayOfStr.nullable
                                                                                )
          );

        } else if (spec instanceof IsArrayOfDecimal)
        {


          IsArrayOfDecimal isArrayOfDecimal = ((IsArrayOfDecimal) spec);
          return new Tuple2<>(isArrayOfDecimal.required,
                              DeserializersFactory.INSTANCE.ofArrayOfDecimal(isArrayOfDecimal.nullable
                                                                            )
          );

        } else if (spec instanceof IsArrayOfTestedDecimal)
        {
          IsArrayOfTestedDecimal isArrayOfDecimal = ((IsArrayOfTestedDecimal) spec);
          return new Tuple2<>(isArrayOfDecimal.required,
                              DeserializersFactory.INSTANCE.ofArrayOfDecimalEachSuchThat(isArrayOfDecimal.predicate,
                                                                                         isArrayOfDecimal.nullable
                                                                                        )
          );
        } else if (spec instanceof IsArrayOfDecimalSuchThat)
        {
          IsArrayOfDecimalSuchThat isArrayOfDecimal = ((IsArrayOfDecimalSuchThat) spec);
          return new Tuple2<>(isArrayOfDecimal.required,
                              DeserializersFactory.INSTANCE.ofArrayOfDecimalSuchThat(isArrayOfDecimal.predicate,
                                                                                     isArrayOfDecimal.nullable
                                                                                    )
          );

        } else if (spec instanceof IsArrayOfNumber)
        {


          IsArrayOfNumber isArrayOfNumber = ((IsArrayOfNumber) spec);
          return new Tuple2<>(isArrayOfNumber.required,
                              DeserializersFactory.INSTANCE.ofArrayOfNumber(isArrayOfNumber.nullable
                                                                           )
          );

        } else if (spec instanceof IsArrayOfTestedNumber)
        {
          IsArrayOfTestedNumber isArrayOfNumber = ((IsArrayOfTestedNumber) spec);
          return new Tuple2<>(isArrayOfNumber.required,
                              DeserializersFactory.INSTANCE.ofArrayOfNumberEachSuchThat(isArrayOfNumber.predicate,
                                                                                        isArrayOfNumber.nullable
                                                                                       )
          );
        } else if (spec instanceof IsArrayOfNumberSuchThat)
        {
          IsArrayOfNumberSuchThat isArrayOfNumber = ((IsArrayOfNumberSuchThat) spec);
          return new Tuple2<>(isArrayOfNumber.required,
                              DeserializersFactory.INSTANCE.ofArrayOfNumberSuchThat(isArrayOfNumber.predicate,
                                                                                    isArrayOfNumber.nullable
                                                                                   )
          );

        } else if (spec instanceof IsArrayOfBool)
        {


          IsArrayOfBool isArrayOfBool = ((IsArrayOfBool) spec);
          return new Tuple2<>(isArrayOfBool.required,
                              DeserializersFactory.INSTANCE.ofArrayOfBool(isArrayOfBool.nullable
                                                                         )
          );

        }

      } else if (spec instanceof JsObjPredicate)
      {
        if (spec instanceof IsObj)
        {
          final IsObj isObj = (IsObj) spec;
          return new Tuple2<>(isObj.required,
                              DeserializersFactory.INSTANCE.ofObj(isObj.nullable)
          );
        } else if (spec instanceof IsObjSuchThat)
        {
          final IsObjSuchThat isObjSuchThat = (IsObjSuchThat) spec;
          return new Tuple2<>(isObjSuchThat.required,
                              DeserializersFactory.INSTANCE.ofObjSuchThat(isObjSuchThat.predicate,
                                                                          isObjSuchThat.nullable
                                                                         )
          );

        }
      }
    }
    throw new RuntimeException("Deserializer not found for the spec " + spec.getClass()
                                                                            .getName());
  }

}
