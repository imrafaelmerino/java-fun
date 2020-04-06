package jsonvalues.spec;

import com.dslplatform.json.derializers.ValueDeserializer;
import io.vavr.Tuple2;

public class JsParser
{
  static Tuple2<Boolean, ValueDeserializer> getDeserializer(JsPredicate spec)
  {
    if (spec instanceof JsPrimitivePredicate)
    {
      if (spec instanceof JsStrPredicate)
      {
        if (spec instanceof IsStr)
        {
          final IsStr isStr = (IsStr) spec;
          return new Tuple2<>(isStr.required,
                              DeserializersFactory.ofStr(isStr.nullable)
          );

        } else if (spec instanceof IsStrSuchThat)
        {
          final IsStrSuchThat isStrSuchThat = (IsStrSuchThat) spec;
          return new Tuple2<>(isStrSuchThat.required,
                              DeserializersFactory.ofStrSuchThat(isStrSuchThat.predicate,
                                                                 isStrSuchThat.nullable
                                                                )
          );

        }
      }
      else if (spec instanceof JsIntPredicate)
      {
        if (spec instanceof IsInt)
        {
          final IsInt isInt = (IsInt) spec;
          return new Tuple2<>(isInt.required,
                              DeserializersFactory.ofInt(isInt.nullable)
          );

        } else if (spec instanceof IsIntSuchThat)
        {
          final IsIntSuchThat isIntSuchThat = (IsIntSuchThat) spec;
          return new Tuple2<>(isIntSuchThat.required,
                              DeserializersFactory.ofIntSuchThat(isIntSuchThat.predicate,
                                                                 isIntSuchThat.nullable
                                                                )
          );
        }
      }
      else if (spec instanceof JsLongPredicate)
      {
        if (spec instanceof IsLong)
        {
          final IsLong isLong = (IsLong) spec;
          return new Tuple2<>(isLong.required,
                              DeserializersFactory.ofLong(isLong.nullable)
          );
        } else if (spec instanceof IsLongSuchThat)
        {
          final IsLongSuchThat isLongSuchThat = (IsLongSuchThat) spec;
          return new Tuple2<>(isLongSuchThat.required,
                              DeserializersFactory.ofLongSuchThat(isLongSuchThat.predicate,
                                                                  isLongSuchThat.nullable
                                                                 )
          );
        }
      }
      else if (spec instanceof JsDecimalPredicate)
      {
        if (spec instanceof IsDecimal)
        {
          final IsDecimal isDecimal = (IsDecimal) spec;
          return new Tuple2<>(isDecimal.required,
                              DeserializersFactory.ofDecimal(isDecimal.nullable)
          );
        } else if (spec instanceof IsDecimalSuchThat)
        {
          final IsDecimalSuchThat isDecimalSuchThat = (IsDecimalSuchThat) spec;
          return new Tuple2<>(isDecimalSuchThat.required,
                              DeserializersFactory.ofDecimalSuchThat(isDecimalSuchThat.predicate,
                                                                     isDecimalSuchThat.nullable
                                                                    )
          );
        }
      }
      else if (spec instanceof JsNumberPredicate)
      {
        if (spec instanceof IsNumber)
        {
          final IsNumber isNumber = (IsNumber) spec;
          return new Tuple2<>(isNumber.required,
                              DeserializersFactory.ofNumber(isNumber.nullable)
          );
        } else if (spec instanceof IsNumberSuchThat)
        {
          final IsNumberSuchThat isNumberSuchThat = (IsNumberSuchThat) spec;
          return new Tuple2<>(isNumberSuchThat.required,
                              DeserializersFactory.ofNumberSuchThat(isNumberSuchThat.predicate,
                                                                    isNumberSuchThat.nullable
                                                                   )
          );
        }
      }
      else if (spec instanceof JsIntegralPredicate)
      {
        if (spec instanceof IsIntegral)
        {
          final IsIntegral isIntegral = (IsIntegral) spec;
          return new Tuple2<>(isIntegral.required,
                              DeserializersFactory.ofIntegral(isIntegral.nullable)
          );
        } else if (spec instanceof IsIntegralSuchThat)
        {
          final IsIntegralSuchThat isIntegralSuchThat = (IsIntegralSuchThat) spec;
          return new Tuple2<>(isIntegralSuchThat.required,
                              DeserializersFactory.ofIntegralSuchThat(isIntegralSuchThat.predicate,
                                                                      isIntegralSuchThat.nullable
                                                                     )
          );
        }
      }
      else if (spec instanceof JsBoolPredicate)
      {
        if (spec instanceof IsBoolean)
        {
          final IsBoolean isBoolean = (IsBoolean) spec;
          return new Tuple2<>(isBoolean.required,
                              DeserializersFactory.ofBool(isBoolean.nullable)
          );
        } else if (spec instanceof IsTrue)
        {
          final IsTrue isTrue = (IsTrue) spec;
          return new Tuple2<>(isTrue.required,
                              DeserializersFactory.ofTrue(isTrue.nullable)
          );
        } else if (spec instanceof IsFalse)
        {
          final IsFalse isFalse = (IsFalse) spec;
          return new Tuple2<>(isFalse.required,
                              DeserializersFactory.ofTrue(isFalse.nullable)
          );
        }
      }
    }
    else if (spec instanceof JsonPredicate)
    {
      if (spec instanceof JsArrayPredicate)
      {
        if (spec instanceof JsArrayOfIntPredicate)
        {

          if (spec instanceof IsArrayOfInt)
          {
            IsArrayOfInt isArrayOfInt = ((IsArrayOfInt) spec);
            return new Tuple2<>(isArrayOfInt.required,
                                DeserializersFactory.ofArrayOfInt(isArrayOfInt.nullable,
                                                                  isArrayOfInt.elemNullable
                                                                 )
            );

          } else if (spec instanceof IsArrayOfTestedInt)
          {
            IsArrayOfTestedInt isArrayOfInt = ((IsArrayOfTestedInt) spec);
            return new Tuple2<>(isArrayOfInt.required,
                                DeserializersFactory.ofArrayOfIntEachSuchThat(isArrayOfInt.predicate,
                                                                              isArrayOfInt.nullable,
                                                                              isArrayOfInt.elemNullable
                                                                             )
            );
          } else if (spec instanceof IsArrayOfIntSuchThat)
          {
            IsArrayOfIntSuchThat isArrayOfInt = ((IsArrayOfIntSuchThat) spec);
            return new Tuple2<>(isArrayOfInt.required,
                                DeserializersFactory.ofArrayOfIntSuchThat(isArrayOfInt.predicate,
                                                                          isArrayOfInt.nullable,
                                                                          isArrayOfInt.elemNullable
                                                                         )
            );

          }
        }
        else if (spec instanceof JsArrayOfObjPredicate)
        {

          if (spec instanceof IsArrayOfObj)
          {
            IsArrayOfObj isArrayOfObj = ((IsArrayOfObj) spec);
            return new Tuple2<>(isArrayOfObj.required,
                                DeserializersFactory.ofArrayOfObj(isArrayOfObj.nullable,
                                                                  isArrayOfObj.elemNullable
                                                                 )
            );
          } else if (spec instanceof IsArrayOfTestedObj)
          {
            IsArrayOfTestedObj isArrayOfObj = ((IsArrayOfTestedObj) spec);
            return new Tuple2<>(isArrayOfObj.required,
                                DeserializersFactory.ofArrayOfObjEachSuchThat(isArrayOfObj.predicate,
                                                                              isArrayOfObj.nullable,
                                                                              isArrayOfObj.elemNullable
                                                                             )
            );
          } else if (spec instanceof IsArrayOfObjSuchThat)
          {
            IsArrayOfObjSuchThat isArrayOfObj = ((IsArrayOfObjSuchThat) spec);
            return new Tuple2<>(isArrayOfObj.required,
                                DeserializersFactory.ofArrayOfObjSuchThat(isArrayOfObj.predicate,
                                                                          isArrayOfObj.nullable,
                                                                          isArrayOfObj.elemNullable
                                                                         )
            );

          }
        }
        else if (spec instanceof JsArrayOfIntegralPredicate)
        {

          if (spec instanceof IsArrayOfIntegral)
          {
            IsArrayOfIntegral isArrayOfIntegral = ((IsArrayOfIntegral) spec);
            return new Tuple2<>(isArrayOfIntegral.required,
                                DeserializersFactory.ofArrayOfIntegral(isArrayOfIntegral.nullable,
                                                                       isArrayOfIntegral.elemNullable
                                                                      )
            );
          } else if (spec instanceof IsArrayOfTestedIntegral)
          {
            IsArrayOfTestedIntegral isArrayOfIntegral = ((IsArrayOfTestedIntegral) spec);
            return new Tuple2<>(isArrayOfIntegral.required,
                                DeserializersFactory.ofArrayOfIntegralEachSuchThat(isArrayOfIntegral.predicate,
                                                                                   isArrayOfIntegral.nullable,
                                                                                   isArrayOfIntegral.elemNullable
                                                                                  )
            );
          } else if (spec instanceof IsArrayOfIntegralSuchThat)
          {
            IsArrayOfIntegralSuchThat isArrayOfIntegral = ((IsArrayOfIntegralSuchThat) spec);
            return new Tuple2<>(isArrayOfIntegral.required,
                                DeserializersFactory.ofArrayOfIntegralSuchThat(isArrayOfIntegral.predicate,
                                                                               isArrayOfIntegral.nullable,
                                                                               isArrayOfIntegral.elemNullable
                                                                              )
            );

          }
        }
        else if (spec instanceof JsArrayOfLongPredicate)
        {

          if (spec instanceof IsArrayOfLong)
          {
            IsArrayOfLong isArrayOfLong = ((IsArrayOfLong) spec);
            return new Tuple2<>(isArrayOfLong.required,
                                DeserializersFactory.ofArrayOfLong(isArrayOfLong.nullable,
                                                                   isArrayOfLong.elemNullable
                                                                  )
            );
          } else if (spec instanceof IsArrayOfTestedLong)
          {
            IsArrayOfTestedLong isArrayOfLong = ((IsArrayOfTestedLong) spec);
            return new Tuple2<>(isArrayOfLong.required,
                                DeserializersFactory.ofArrayOfLongEachSuchThat(isArrayOfLong.predicate,
                                                                               isArrayOfLong.nullable,
                                                                               isArrayOfLong.elemNullable
                                                                              )
            );
          } else if (spec instanceof IsArrayOfLongSuchThat)
          {
            IsArrayOfLongSuchThat isArrayOfLong = ((IsArrayOfLongSuchThat) spec);
            return new Tuple2<>(isArrayOfLong.required,
                                DeserializersFactory.ofArrayOfLongSuchThat(isArrayOfLong.predicate,
                                                                           isArrayOfLong.nullable,
                                                                           isArrayOfLong.elemNullable
                                                                          )
            );

          }
        }
        else if (spec instanceof JsArrayOfStrPredicate)
        {

          if (spec instanceof IsArrayOfStr)
          {
            IsArrayOfStr isArrayOfStr = ((IsArrayOfStr) spec);
            return new Tuple2<>(isArrayOfStr.required,
                                DeserializersFactory.ofArrayOfStr(isArrayOfStr.nullable,
                                                                  isArrayOfStr.elemNullable
                                                                 )
            );
          } else if (spec instanceof IsArrayOfTestedStr)
          {
            IsArrayOfTestedStr isArrayOfStr = ((IsArrayOfTestedStr) spec);
            return new Tuple2<>(isArrayOfStr.required,
                                DeserializersFactory.ofArrayOfStrEachSuchThat(isArrayOfStr.predicate,
                                                                              isArrayOfStr.nullable,
                                                                              isArrayOfStr.elemNullable
                                                                             )
            );
          } else if (spec instanceof IsArrayOfStrSuchThat)
          {
            IsArrayOfStrSuchThat isArrayOfStr = ((IsArrayOfStrSuchThat) spec);
            return new Tuple2<>(isArrayOfStr.required,
                                DeserializersFactory.ofArrayOfStrSuchThat(isArrayOfStr.predicate,
                                                                          isArrayOfStr.nullable,
                                                                          isArrayOfStr.elemNullable
                                                                         )
            );

          }
        }
        else if (spec instanceof JsArrayOfDecimalPredicate)
        {

          if (spec instanceof IsArrayOfDecimal)
          {
            IsArrayOfDecimal isArrayOfDecimal = ((IsArrayOfDecimal) spec);
            return new Tuple2<>(isArrayOfDecimal.required,
                                DeserializersFactory.ofArrayOfDecimal(isArrayOfDecimal.nullable,
                                                                      isArrayOfDecimal.elemNullable
                                                                     )
            );
          } else if (spec instanceof IsArrayOfTestedDecimal)
          {
            IsArrayOfTestedDecimal isArrayOfDecimal = ((IsArrayOfTestedDecimal) spec);
            return new Tuple2<>(isArrayOfDecimal.required,
                                DeserializersFactory.ofArrayOfDecimalEachSuchThat(isArrayOfDecimal.predicate,
                                                                                  isArrayOfDecimal.nullable,
                                                                                  isArrayOfDecimal.elemNullable
                                                                                 )
            );
          } else if (spec instanceof IsArrayOfDecimalSuchThat)
          {
            IsArrayOfDecimalSuchThat isArrayOfDecimal = ((IsArrayOfDecimalSuchThat) spec);
            return new Tuple2<>(isArrayOfDecimal.required,
                                DeserializersFactory.ofArrayOfDecimalSuchThat(isArrayOfDecimal.predicate,
                                                                              isArrayOfDecimal.nullable,
                                                                              isArrayOfDecimal.elemNullable
                                                                             )
            );

          }
        }
        else if (spec instanceof JsArrayOfNumberPredicate)
        {

          if (spec instanceof IsArrayOfNumber)
          {
            IsArrayOfNumber isArrayOfNumber = ((IsArrayOfNumber) spec);
            return new Tuple2<>(isArrayOfNumber.required,
                                DeserializersFactory.ofArrayOfNumber(isArrayOfNumber.nullable,
                                                                     isArrayOfNumber.elemNullable
                                                                    )
            );
          } else if (spec instanceof IsArrayOfTestedNumber)
          {
            IsArrayOfTestedNumber isArrayOfNumber = ((IsArrayOfTestedNumber) spec);
            return new Tuple2<>(isArrayOfNumber.required,
                                DeserializersFactory.ofArrayOfNumberEachSuchThat(isArrayOfNumber.predicate,
                                                                                 isArrayOfNumber.nullable,
                                                                                 isArrayOfNumber.elemNullable
                                                                                )
            );
          } else if (spec instanceof IsArrayOfNumberSuchThat)
          {
            IsArrayOfNumberSuchThat isArrayOfNumber = ((IsArrayOfNumberSuchThat) spec);
            return new Tuple2<>(isArrayOfNumber.required,
                                DeserializersFactory.ofArrayOfNumberSuchThat(isArrayOfNumber.predicate,
                                                                             isArrayOfNumber.nullable,
                                                                             isArrayOfNumber.elemNullable
                                                                            )
            );

          }
        }
        else if (spec instanceof JsArrayOfBoolPredicate)
        {

          if (spec instanceof IsArrayOfBool)
          {
            IsArrayOfBool isArrayOfBool = ((IsArrayOfBool) spec);
            return new Tuple2<>(isArrayOfBool.required,
                                DeserializersFactory.ofArrayOfNumber(isArrayOfBool.nullable,
                                                                     isArrayOfBool.elemNullable
                                                                    )
            );
          }
        }

      }
      else if (spec instanceof JsObjPredicate)
      {
        if (spec instanceof IsObj)
        {
          final IsObj isObj = (IsObj) spec;
          return new Tuple2<>(isObj.required,DeserializersFactory.ofObj(isObj.nullable));
        }
        else if (spec instanceof IsObjSuchThat)
        {
          final IsObjSuchThat isObjSuchThat = (IsObjSuchThat) spec;
          return new Tuple2<>(isObjSuchThat.required,DeserializersFactory.ofObjSuchThat(isObjSuchThat.predicate,
                                                                                        isObjSuchThat.nullable)
          );

        }
      }
    }
   throw new RuntimeException("Deserializer not found for the spec "+spec.getClass().getName());
  }

}
