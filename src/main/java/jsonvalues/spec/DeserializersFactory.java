package jsonvalues.spec;

import com.dslplatform.json.derializers.DeserializerException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.derializers.specs.SpecDeserializer;
import com.dslplatform.json.derializers.types.arrays.*;
import com.dslplatform.json.derializers.specs.JsArrayOfObjSpecDeserializer;
import com.dslplatform.json.derializers.specs.JsArraySpecDeserializer;
import com.dslplatform.json.derializers.specs.JsObjSpecDeserializer;
import com.dslplatform.json.derializers.specs.JsObjSpecWithRequiredKeysDeserializer;
import com.dslplatform.json.derializers.types.*;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.*;

class DeserializersFactory
{

  final static DeserializersFactory INSTANCE = new DeserializersFactory();

  private DeserializersFactory()
  {
    intParser = new JsIntDeserializer();
    longParser = new JsLongDeserializer();
    integralParser = new JsIntegralDeserializer();
    boolParser = new JsBoolDeserializer();
    decimalParser = new JsDecimalDeserializer();
    strParser = new JsStrDeserializer();
    numberParser = new JsNumberDeserializer();
    valueParser = new JsValueDeserializer();
    objParser = new JsObjDeserializer(valueParser);
    arrayOfValueParser = new JsArrayOfValueDeserializer(valueParser);
    valueParser.setArrayDeserializer(arrayOfValueParser);
    valueParser.setObjDeserializer(objParser);
    valueParser.setNumberDeserializer(numberParser);
    arrayOfIntParser = new JsArrayOfIntDeserializer(intParser);
    arrayOfLongParser = new JsArrayOfLongDeserializer(longParser);
    arrayOfDecimalParser = new JsArrayOfDecimalDeserializer(decimalParser);
    arrayOfIntegralParser = new JsArrayOfIntegralDeserializer(integralParser);
    arrayOfNumberParser = new JsArrayOfNumberDeserializer(numberParser);
    arrayOfObjParser = new JsArrayOfObjDeserializer(objParser);
    arrayOfStrParser = new JsArrayOfStringDeserializer(strParser);
    arrayOfBoolParser = new JsArrayOfBoolDeserializer(boolParser);
    newParseException =
      (reader, error) -> new DeserializerException(reader.newParseError(error.toString()));

  }

  private final JsIntDeserializer intParser;
  private final JsLongDeserializer longParser;
  private final JsIntegralDeserializer integralParser;
  private final JsBoolDeserializer boolParser;
  private final JsDecimalDeserializer decimalParser;
  private final JsStrDeserializer strParser;
  private final JsNumberDeserializer numberParser;
  private final JsValueDeserializer valueParser;
  private final JsObjDeserializer objParser;
  private final JsArrayOfValueDeserializer arrayOfValueParser;
  private final JsArrayOfIntDeserializer arrayOfIntParser;
  private final JsArrayOfLongDeserializer arrayOfLongParser;
  private final JsArrayOfDecimalDeserializer arrayOfDecimalParser;
  private final JsArrayOfIntegralDeserializer arrayOfIntegralParser;
  private final JsArrayOfNumberDeserializer arrayOfNumberParser;
  private final JsArrayOfObjDeserializer arrayOfObjParser;
  private final JsArrayOfStringDeserializer arrayOfStrParser;
  private final JsArrayOfBoolDeserializer arrayOfBoolParser;

  private final BiFunction<JsonReader<?>, Error, DeserializerException> newParseException;

  /**
   *
   * @param typeCondition     condition to check if the value has the expected type
   * @param converter         function to convert the value to the expected type
   * @param spec              the specification that the value has to conform
   * @param errorTypeSupplier if the value doesn't have the expected type,
   *                          the error produced by this supplier is thrown. It's considered an internal error
   *                          because if this happened, it would be because a development error
   * @return a function to test that a value has the expected type and conforms a given spec
   */
  private <R> Function<JsValue, Optional<Error>> testTypeAndSpec(Predicate<JsValue> typeCondition,
                                                                 Function<JsValue, R> converter,
                                                                 Function<R, Optional<Error>> spec,
                                                                 Supplier<InternalError> errorTypeSupplier
                                                                )
  {
    return value ->
    {
      if (typeCondition.test(value)) return spec.apply(converter.apply(value));
      else throw errorTypeSupplier.get();
    };
  }


  private SpecDeserializer getDeserializer(AbstractDeserializer deserializer,
                                           boolean nullable
                                          )
  {
    if (nullable)
      return deserializer::nullOrValue;
    else
      return deserializer::value;
  }

  private SpecDeserializer getDeserializer(JsArrayDeserializer deserializer,
                                           boolean nullable
                                          )
  {
   if (nullable)
      return deserializer::nullOrArray;
    else return deserializer::array;
  }


  private SpecDeserializer getDeserializer(JsArrayDeserializer deserializer,
                                           Function<JsArray, Optional<Error>> p,
                                           boolean nullable
                                          )
  {
     if (nullable)
      return reader -> deserializer.nullOrArraySuchThat(reader,
                                                        p
                                                       );

    else return reader -> deserializer.arraySuchThat(reader,
                                                     p
                                                    );
  }


  SpecDeserializer ofArrayOfObjSuchThat(Function<JsArray, Optional<Error>> p,
                                        boolean nullable
                                       )
  {
    return getDeserializer(arrayOfObjParser,
                           p,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfObjEachSuchThat(Function<JsObj, Optional<Error>> p,
                                            boolean nullable
                                           )
  {
     if (nullable) return reader -> arrayOfObjParser.nullOrArrayEachSuchThat(reader,
                                                                                 p
                                                                                );

    else return reader -> arrayOfObjParser.arrayEachSuchThat(reader,
                                                             p
                                                            );
  }

  SpecDeserializer ofArrayOfObjSpec(Vector<String> required,
                                    Map<String, SpecDeserializer> keyDeserializers,
                                    boolean nullable,
                                    boolean strict
                                   )
  {
    JsObjSpecDeserializer f = (required.isEmpty()) ?
      new JsObjSpecDeserializer(strict,keyDeserializers) :
      new JsObjSpecWithRequiredKeysDeserializer(required,
                                                keyDeserializers,strict);
    JsArrayOfObjSpecDeserializer deserializer = new JsArrayOfObjSpecDeserializer(f);
    if (nullable)
      return deserializer::nullOrArray;

    else
      return deserializer::array;

  }

  SpecDeserializer ofArrayOfObj(boolean nullable
                               )
  {
    return getDeserializer(arrayOfObjParser,
                           nullable
                          );
  }


  SpecDeserializer ofObjSuchThat(final Function<JsObj, Optional<Error>> predicate,
                                 final boolean nullable
                                )
  {

    if (nullable)
      return reader ->
      {
        JsValue value = objParser.nullOrValue(reader);
        if (value == JsNull.NULL) return value;
        else
        {
          testTypeAndSpec(JsValue::isObj,
                          JsValue::toJsObj,
                          predicate,
                          () -> InternalError.objWasExpected("JsObjDeserializer.nullOrValue didn't return wither null or a JsObj as expected.")
                         ).apply(value)
                          .ifPresent(e -> newParseException.apply(reader,
                                                                  e
                                                                 )
                                    );
          return value;
        }
      };


    else return reader ->
    {
      JsObj value = objParser.value(reader);
      final Optional<Error> result = predicate.apply(value);
      if (!result.isPresent()) return value;
      else throw newParseException.apply(reader,
                                         result.get()
                                        );
    };
  }


  SpecDeserializer ofArraySpec(Vector<SpecDeserializer> keyDeserializers,
                               boolean nullable
                              )
  {
    if (nullable)
      return reader -> new JsArraySpecDeserializer(keyDeserializers).nullOrArray(reader);
    else
      return reader -> new JsArraySpecDeserializer(keyDeserializers).array(reader);
  }

  SpecDeserializer ofObjSpec(Vector<String> required,
                             Map<String, SpecDeserializer> keyDeserializers,
                             boolean nullable,
                             boolean strict
                            )
  {
    return reader ->
    {
      if (required.isEmpty())
      {
        JsObjSpecDeserializer deserializer = new JsObjSpecDeserializer(strict,keyDeserializers);
        if (nullable) return deserializer.nullOrValue(reader);
        else return deserializer.value(reader);
      } else
      {
        JsObjSpecWithRequiredKeysDeserializer deserializer = new JsObjSpecWithRequiredKeysDeserializer(required,
                                                                                                       keyDeserializers,
                                                                                                       strict
        );
        if (nullable) return deserializer.nullOrValue(reader);
        else return deserializer.value(reader);
      }

    };
  }


  SpecDeserializer ofArrayOfValueSuchThat(Function<JsArray, Optional<Error>> p,
                                          boolean nullable
                                         )
  {
    return getDeserializer(arrayOfValueParser,
                           p,
                           nullable
                          );
  }

  SpecDeserializer ofObj(boolean nullable)
  {
    return getDeserializer(objParser,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfValue(boolean nullable
                                 )
  {
    return getDeserializer(arrayOfValueParser,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfValueEachSuchThat(Function<JsValue, Optional<Error>> p,
                                              boolean nullable
                                             )
  {
    if (nullable) return reader -> arrayOfValueParser.nullOrArrayEachSuchThat(reader,
                                                                                   p
                                                                                  );

    else return reader -> arrayOfValueParser.arrayEachSuchThat(reader,
                                                               p
                                                              );
  }

  SpecDeserializer ofValue()
  {
    return getDeserializer(valueParser,
                           true
                          );
  }

  SpecDeserializer ofValueSuchThat(Function<JsValue, Optional<Error>> predicate)
  {
    return reader ->
    {
      JsValue value = valueParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        final Optional<Error> result = predicate.apply(value);
        if (!result.isPresent()) return value;
        else throw newParseException.apply(reader,
                                           result.get()
                                          );
      }
    };
  }


  SpecDeserializer ofBool(boolean nullable)
  {
    return getDeserializer(boolParser,
                           nullable
                          );
  }

  SpecDeserializer ofTrue(boolean nullable)
  {
    if (nullable) return boolParser::nullOrTrue;
    else return boolParser::True;
  }

  SpecDeserializer ofFalse(boolean nullable)
  {
    if (nullable) return boolParser::nullOrFalse;
    else return boolParser::False;
  }

  SpecDeserializer ofArrayOfBool(boolean nullable
                                )
  {
    return getDeserializer(arrayOfBoolParser,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfBoolSuchThat(Function<JsArray, Optional<Error>> p,
                                         boolean nullable
                                        )
  {
    return getDeserializer(arrayOfBoolParser,
                           p,
                           nullable
                          );

  }

  SpecDeserializer ofArrayOfStrEachSuchThat(Function<String, Optional<Error>> p,
                                            boolean nullable
                                           )
  {
    if (nullable) return reader ->
      arrayOfStrParser.nullOrArrayEachSuchThat(reader,
                                               p
                                              );

    else return reader ->
        arrayOfStrParser.arrayEachSuchThat(reader,
                                           p
                                          );
  }

  SpecDeserializer ofArrayOfStrSuchThat(Function<JsArray, Optional<Error>> p,
                                        boolean nullable
                                       )
  {
    return getDeserializer(arrayOfStrParser,
                           p,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfStr(boolean nullable
                               )
  {
    return getDeserializer(arrayOfStrParser,
                           nullable
                          );
  }


  SpecDeserializer ofStr(boolean nullable)
  {
    return getDeserializer(strParser,
                           nullable
                          );
  }

  SpecDeserializer ofStrSuchThat(Function<String, Optional<Error>> predicate,
                                 boolean nullable
                                )
  {

    if (nullable) return reader ->
    {
      JsValue value = strParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(JsValue::isStr,
                        v -> v.toJsStr().value,
                        predicate,
                        () -> InternalError.stringWasExpected("JsStrDeserializer.nullOrValue didn't return neither null or a JsStr as expected.")
                       ).apply(value)
                        .ifPresent(e -> newParseException.apply(reader,
                                                                e
                                                               )
                                  );
        return value;
      }
    };
    else return reader ->
    {
      JsStr value = strParser.value(reader);

      final Optional<Error> result = predicate.apply(value.value);
      if (!result.isPresent()) return value;
      else throw newParseException.apply(reader,
                                         result.get()
                                        );
    };
  }

  SpecDeserializer ofArrayOfNumber(boolean nullable
                                  )
  {
    return getDeserializer(arrayOfNumberParser,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfNumberEachSuchThat(Function<JsNumber, Optional<Error>> p,
                                               boolean nullable
                                              )
  {
     if (nullable) return reader -> arrayOfNumberParser.nullOrArrayEachSuchThat(reader,
                                                                                    p
                                                                                   );

    else return reader -> arrayOfNumberParser.arrayEachSuchThat(reader,
                                                                p
                                                               );
  }

  SpecDeserializer ofArrayOfNumberSuchThat(Function<JsArray, Optional<Error>> p,
                                           boolean nullable
                                          )
  {
    return getDeserializer(arrayOfNumberParser,
                           p,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfIntegralSuchThat(Function<JsArray, Optional<Error>> p,
                                             boolean nullable
                                            )
  {
    return getDeserializer(arrayOfIntegralParser,
                           p,
                           nullable
                          );
  }

  SpecDeserializer ofNumber(boolean nullable)
  {
    return getDeserializer(numberParser,
                           nullable
                          );
  }

  SpecDeserializer ofNumberSuchThat(Function<JsNumber, Optional<Error>> predicate,
                                    boolean nullable
                                   )
  {

    if (nullable) return reader ->
    {
      JsValue value = numberParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(JsValue::isNumber,
                        v -> value.toJsNumber(),
                        predicate,
                        () -> InternalError.numberWasExpected("JsNumberDeserializer.nullOrValue didn't return neither null or a JsNumber as expected.")
                       ).apply(value)
                        .ifPresent(e -> newParseException.apply(reader,
                                                                e
                                                               )
                                  );
        return value;
      }

    };
    else return reader ->
    {
      JsNumber value = numberParser.value(reader);
      final Optional<Error> result = predicate.apply(value);
      if (!result.isPresent()) return value;
      else throw newParseException.apply(reader,
                                         result.get()
                                        );
    };

  }


  SpecDeserializer ofArrayOfIntegral(boolean nullable
                                    )
  {
    return getDeserializer(arrayOfIntegralParser,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfIntegralEachSuchThat(Function<BigInteger, Optional<Error>> p,
                                                 boolean nullable
                                                )

  {
    if (nullable) return reader ->
      arrayOfIntegralParser.nullOrArrayEachSuchThat(reader,
                                                    p
                                                   );

    else return reader ->
        arrayOfIntegralParser.arrayEachSuchThat(reader,
                                                p
                                               );
  }

  SpecDeserializer ofIntegral(boolean nullable)
  {
    return getDeserializer(integralParser,
                           nullable
                          );
  }

  SpecDeserializer ofIntegralSuchThat(Function<BigInteger, Optional<Error>> predicate,
                                      boolean nullable
                                     )
  {

    if (nullable) return reader ->
    {
      JsValue value = integralParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(JsValue::isBigInt,
                        v -> v.toJsBigInt().value,
                        predicate,
                        () -> InternalError.integralWasExpected("JsIntegralDeserializer.nullOrValue didn't return neither null or a JsBigInt as expected.")
                       ).apply(value)
                        .ifPresent(e -> newParseException.apply(reader,
                                                                e
                                                               )
                                  );
        return value;
      }
    };
    else return reader ->
    {
      JsBigInt integral = integralParser.value(reader);
      final Optional<Error> result = predicate.apply(integral.value);
      if (!result.isPresent()) return integral;
      else throw newParseException.apply(reader,
                                         result.get()
                                        );

    };
  }

  SpecDeserializer ofArrayOfDecimal(boolean nullable
                                   )
  {
    return getDeserializer(arrayOfDecimalParser,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfDecimalEachSuchThat(Function<BigDecimal, Optional<Error>> p,
                                                boolean nullable
                                               )
  {
     if (nullable) return reader ->
      arrayOfDecimalParser.nullOrArrayEachSuchThat(reader,
                                                   p
                                                  );

    else return reader ->
        arrayOfDecimalParser.arrayEachSuchThat(reader,
                                               p
                                              );
  }

  SpecDeserializer ofArrayOfDecimalSuchThat(Function<JsArray, Optional<Error>> p,
                                            boolean nullable
                                           )
  {
    return getDeserializer(arrayOfDecimalParser,
                           p,
                           nullable
                          );

  }


  SpecDeserializer ofArrayOfLong(boolean nullable
                                )
  {
    return getDeserializer(arrayOfLongParser,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfLongEachSuchThat(LongFunction<Optional<Error>> p,
                                             boolean nullable
                                            )

  {
    if (nullable) return reader ->
      arrayOfLongParser.nullOrArrayEachSuchThat(reader,
                                                p
                                               );

    else return reader -> arrayOfLongParser.arrayEachSuchThat(reader,
                                                              p
                                                             );
  }

  SpecDeserializer ofArrayOfLongSuchThat(Function<JsArray, Optional<Error>> p,
                                         boolean nullable
                                        )
  {
    return getDeserializer(arrayOfLongParser,
                           p,
                           nullable
                          );
  }


  SpecDeserializer ofDecimal(boolean nullable)
  {
    return getDeserializer(decimalParser,
                           nullable
                          );
  }

  SpecDeserializer ofDecimalSuchThat(Function<BigDecimal, Optional<Error>> predicate,
                                     boolean nullable
                                    )
  {

    if (nullable) return reader ->
    {
      JsValue value = decimalParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(JsValue::isDecimal,
                        v -> v.toJsBigDec().value,
                        predicate,
                        () -> InternalError.decimalWasExpected("JsDecimalDeserializer.nullOrValue didn't return neither null or a JsBigDec as expected.")
                       ).apply(value)
                        .ifPresent(e -> newParseException.apply(reader,
                                                                e
                                                               )
                                  );

        return value;
      }
    };
    else
      return reader ->
      {
        JsBigDec decimal = decimalParser.value(reader);
        final Optional<Error> result = predicate.apply(decimal.value);
        if (!result.isPresent()) return decimal;
        else throw newParseException.apply(reader,
                                           result.get()
                                          );

      };
  }


  SpecDeserializer ofLong(boolean nullable)
  {
    return getDeserializer(longParser,
                           nullable
                          );
  }

  SpecDeserializer ofLongSuchThat(LongFunction<Optional<Error>> predicate,
                                  boolean nullable
                                 )
  {

    if (nullable) return reader ->
    {
      JsValue value = longParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(JsValue::isLong,
                        v -> v.toJsLong().value,
                        predicate::apply,
                        () -> InternalError.longWasExpected("JsLongDeserializer.nullOrValue didn't return neither null or a JsLong as expected.")
                       ).apply(value)
                        .ifPresent(e -> newParseException.apply(reader,
                                                                e
                                                               )
                                  );
        return value;
      }
    };
    else return reader ->
    {
      JsLong value = longParser.value(reader);
      final Optional<Error> result = predicate.apply(value.value);
      if (!result.isPresent()) return value;
      else throw newParseException.apply(reader,
                                         result.get()
                                        );

    };
  }


  SpecDeserializer ofArrayOfInt(boolean nullable
                               )
  {
    return getDeserializer(arrayOfIntParser,
                           nullable
                          );
  }


  SpecDeserializer ofArrayOfIntSuchThat(Function<JsArray, Optional<Error>> p,
                                        boolean nullable
                                       )
  {
    return getDeserializer(arrayOfIntParser,
                           p,
                           nullable
                          );
  }

  SpecDeserializer ofArrayOfIntEachSuchThat(IntFunction<Optional<Error>> p,
                                            boolean nullable
                                           )
  {

    if(nullable) return reader -> arrayOfIntParser.nullOrArrayEachSuchThat(reader,
                                                                                 p
                                                                                );

    else return reader -> arrayOfIntParser.arrayEachSuchThat(reader,
                                                             p
                                                            );
  }


  SpecDeserializer ofInt(boolean nullable)
  {
    return getDeserializer(intParser,
                           nullable
                          );
  }

  SpecDeserializer ofIntSuchThat(IntFunction<Optional<Error>> predicate,
                                 boolean nullable
                                )
  {

    if (nullable) return reader ->
    {
      JsValue value = intParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(v -> value.isInt(),
                        v -> v.toJsInt().value,
                        predicate::apply,
                        () -> InternalError.integerWasExpected("JsIntDeserializer.nullOrValue didn't return neither null or a Int as expected.")
                       ).apply(value)
                        .ifPresent(e -> newParseException.apply(reader,
                                                                e
                                                               )
                                  );

        return value;
      }

    };
    else return reader ->
    {
      JsInt value = intParser.value(reader);
      final Optional<Error> result = predicate.apply(value.value);
      if (!result.isPresent()) return value;
      else throw newParseException.apply(reader,
                                         result.get()
                                        );

    };
  }


}
