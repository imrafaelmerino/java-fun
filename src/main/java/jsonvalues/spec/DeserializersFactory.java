package jsonvalues.spec;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import com.dslplatform.json.derializers.ValueDeserializer;
import com.dslplatform.json.derializers.arrays.*;
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

public class DeserializersFactory
{


  private static final JsIntDeserializer intParser = new JsIntDeserializer();
  private static final JsLongDeserializer longParser = new JsLongDeserializer();

  private static final JsIntegralDeserializer integralParser = new JsIntegralDeserializer();
  private static final JsBoolDeserializer boolParser = new JsBoolDeserializer();
  private static final JsDecimalDeserializer decimalParser = new JsDecimalDeserializer();
  private static final JsStrDeserializer strParser = new JsStrDeserializer();
  private static final JsNumberDeserializer numberParser = new JsNumberDeserializer();

  private static final JsValueDeserializer valueParser = new JsValueDeserializer();
  private static final JsObjDeserializer objParser = new JsObjDeserializer(valueParser);
  private static final JsArrayOfValueDeserializer arrayOfValueParser = new JsArrayOfValueDeserializer(valueParser);

  static
  {
    valueParser.setArrayDeserializer(arrayOfValueParser);
    valueParser.setObjDeserializer(objParser);
    valueParser.setNumberDeserializer(numberParser);
  }

  private static final JsArrayOfIntDeserializer arrayOfIntParser = new JsArrayOfIntDeserializer(intParser);
  private static final JsArrayOfLongDeserializer arrayOfLongParser = new JsArrayOfLongDeserializer(longParser);
  private static final JsArrayOfDecimalDeserializer arrayOfDecimalParser = new JsArrayOfDecimalDeserializer(decimalParser);
  private static final JsArrayOfIntegralDeserializer arrayOfIntegralParser = new JsArrayOfIntegralDeserializer(integralParser);
  private static final JsArrayOfNumberDeserializer arrayOfNumberParser = new JsArrayOfNumberDeserializer(numberParser);
  private static final JsArrayOfObjDeserializer arrayOfObjParser = new JsArrayOfObjDeserializer(objParser);
  private static final JsArrayOfStringDeserializer arrayOfStrParser = new JsArrayOfStringDeserializer(strParser);
  private static final JsArrayOfBoolDeserializer arrayOfBoolParser = new JsArrayOfBoolDeserializer(boolParser);

  private static final BiFunction<JsonReader<?>, Error, ParsingException> newParseException =
    (reader, error) -> reader.newParseError(error.toString());

  /**
   *
   * @param typeCondition     condition to check if the value has the expected type
   * @param converter         function to convert the value to the expected type
   * @param spec              the specification that the value has to conform
   * @param errorTypeSupplier if the value doesn't have the expected type,
   *                          the error produced by this supplier is thrown. It's considered an internal error
   *                          because if this happened, it would be because a development error
   * @tparam R the type of the value (primitive type or JsObj or JsArray or Json)
   * @return a function to test that a value has the expected type and conforms a given spec
   */
  static <R> Function<JsValue, Optional<Error>> testTypeAndSpec(Predicate<JsValue> typeCondition,
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


  private static ValueDeserializer getDeserializer(JsTypeDeserializer deserializer,
                                                   boolean nullable
                                                  )
  {
    if (nullable)
      return deserializer::nullOrValue;
    else
      return deserializer::value;
  }

  private static ValueDeserializer getDeserializer(JsArrayDeserializer deserializer,
                                                   boolean nullable,
                                                   boolean elemNullable
                                                  )
  {

    if (nullable && elemNullable)
      return deserializer::nullOrArrayWithNull;
    else if (nullable)
      return deserializer::nullOrArray;
    else if (elemNullable)
      return deserializer::arrayWithNull;
    else return deserializer::array;
  }


  private static ValueDeserializer getDeserializer(JsArrayDeserializer deserializer,
                                                   Function<JsArray, Optional<Error>> p,
                                                   boolean nullable,
                                                   boolean elemNullable
                                                  )
  {
    if (nullable && elemNullable)
      return reader -> deserializer.nullOrArrayWithNullSuchThat(reader,
                                                                p
                                                               );
    else if (nullable)
      return reader -> deserializer.nullOrArraySuchThat(reader,
                                                        p
                                                       );
    else if (elemNullable)
      return reader -> deserializer.arrayWithNullSuchThat(reader,
                                                          p
                                                         );
    else return reader -> deserializer.arraySuchThat(reader,
                                                     p
                                                    );
  }


  static ValueDeserializer ofArrayOfObjSuchThat(Function<JsArray, Optional<Error>> p,
                                                boolean nullable,
                                                boolean elemNullable
                                               )
  {
    return getDeserializer(arrayOfObjParser,
                           p,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfObjEachSuchThat(Function<JsObj, Optional<Error>> p,
                                                    boolean nullable,
                                                    boolean elemNullable
                                                   )
  {
    if (nullable && elemNullable) return reader -> arrayOfObjParser.nullOrArrayWithNullEachSuchThat(reader,
                                                                                                    p
                                                                                                   );
    else if (nullable) return reader -> arrayOfObjParser.nullOrArrayEachSuchThat(reader,
                                                                                 p
                                                                                );
    else if (elemNullable) return reader -> arrayOfObjParser.arrayWithNullEachSuchThat(reader,
                                                                                       p
                                                                                      );
    else return reader -> arrayOfObjParser.arrayEachSuchThat(reader,
                                                             p
                                                            );
  }

  static ValueDeserializer ofArrayOfObjSpec(Vector<String> required,
                                            Map<String, ValueDeserializer> keyDeserializers,
                                            boolean nullable,
                                            boolean elemNullable
                                           )
  {
    JsObjSpecDeserializer f = (required.isEmpty()) ? new JsObjSpecDeserializer(keyDeserializers) : new JsObjSpecWithRequiredKeysDeserializer(required,
                                                                                                                                             keyDeserializers
    );
    JsArrayOfObjSpecDeserializer deserializer = new JsArrayOfObjSpecDeserializer(f);
    if (nullable && elemNullable)
      return deserializer::nullOrArrayWithNull;
    else if (nullable)
      return deserializer::nullOrArray;
    else if (elemNullable)
      return deserializer::arrayWithNull;
    else
      return deserializer::array;

  }

  static ValueDeserializer ofArrayOfObj(boolean nullable,
                                        boolean elemNullable
                                       )
  {
    return getDeserializer(arrayOfObjParser,
                           nullable,
                           elemNullable
                          );
  }


  static ValueDeserializer ofObjSuchThat(final Function<JsObj, Optional<Error>> predicate,
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


  static ValueDeserializer ofArraySpec(Vector<ValueDeserializer> keyDeserializers,
                                       boolean nullable
                                      )
  {
    if (nullable)
      return reader -> new JsArraySpecDeserializer(keyDeserializers).nullOrArray(reader);
    else
      return reader -> new JsArraySpecDeserializer(keyDeserializers).array(reader);
  }

  static ValueDeserializer ofObjSpec(Vector<String> required,
                                     Map<String, ValueDeserializer> keyDeserializers,
                                     boolean nullable
                                    )
  {
    return reader ->
    {
      if (required.isEmpty())
      {
        JsObjSpecDeserializer deserializer = new JsObjSpecDeserializer(keyDeserializers);
        if (nullable) return deserializer.nullOrValue(reader);
        else return deserializer.value(reader);
      } else
      {
        JsObjSpecWithRequiredKeysDeserializer deserializer = new JsObjSpecWithRequiredKeysDeserializer(required,
                                                                                                       keyDeserializers
        );
        if (nullable) return deserializer.nullOrValue(reader);
        else return deserializer.value(reader);
      }

    };
  }


  static ValueDeserializer ofArrayOfValueSuchThat(Function<JsArray, Optional<Error>> p,
                                                  boolean nullable,
                                                  boolean elemNullable
                                                 )
  {
    return getDeserializer(arrayOfValueParser,
                           p,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofObj(boolean nullable)
  {
    return getDeserializer(objParser,
                           nullable
                          );
  }

  static ValueDeserializer ofArrayOfValue(boolean nullable,
                                          boolean elemNullable
                                         )
  {
    return getDeserializer(arrayOfValueParser,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfValueEachSuchThat(Function<JsValue, Optional<Error>> p,
                                                      boolean nullable,
                                                      boolean elemNullable
                                                     )
  {
    if (nullable && elemNullable) return reader -> arrayOfValueParser.nullOrArrayWithNullEachSuchThat(reader,
                                                                                                      p
                                                                                                     );
    else if (nullable) return reader -> arrayOfValueParser.nullOrArrayEachSuchThat(reader,
                                                                                   p
                                                                                  );
    else if (elemNullable) return reader -> arrayOfValueParser.arrayWithNullEachSuchThat(reader,
                                                                                         p
                                                                                        );
    else return reader -> arrayOfValueParser.arrayEachSuchThat(reader,
                                                               p
                                                              );
  }

  static ValueDeserializer ofValue()
  {
    return getDeserializer(valueParser,
                           true
                          );
  }

  static ValueDeserializer ofValueSuchThat(Function<JsValue, Optional<Error>> predicate)
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


  static ValueDeserializer ofBool(boolean nullable)
  {
    return getDeserializer(boolParser,
                           nullable
                          );
  }

  static ValueDeserializer ofTrue(boolean nullable)
  {
    if (nullable) return boolParser::nullOrTrue;
    else return boolParser::True;
  }

  static ValueDeserializer ofFalse(boolean nullable)
  {
    if (nullable) return boolParser::nullOrFalse;
    else return boolParser::False;
  }

  static ValueDeserializer ofArrayOfBool(boolean nullable,
                                         boolean elemNullable
                                        )
  {
    return getDeserializer(arrayOfBoolParser,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfBoolSuchThat(Function<JsArray, Optional<Error>> p,
                                                 boolean nullable,
                                                 boolean elemNullable
                                                )
  {
    return getDeserializer(arrayOfBoolParser,
                           p,
                           nullable,
                           elemNullable
                          );

  }

  static ValueDeserializer ofArrayOfStrEachSuchThat(Function<String, Optional<Error>> p,
                                                    boolean nullable,
                                                    boolean elemNullable
                                                   )
  {
    if (nullable && elemNullable) return reader ->
      arrayOfStrParser.nullOrArrayWithNullEachSuchThat(reader,
                                                       p
                                                      );
    else if (nullable) return reader ->
      arrayOfStrParser.nullOrArrayEachSuchThat(reader,
                                               p
                                              );
    else if (elemNullable) return reader ->
      arrayOfStrParser.arrayWithNullEachSuchThat(reader,
                                                 p
                                                );
    else return reader ->
        arrayOfStrParser.arrayEachSuchThat(reader,
                                           p
                                          );
  }

  static ValueDeserializer ofArrayOfStrSuchThat(Function<JsArray, Optional<Error>> p,
                                                boolean nullable,
                                                boolean elemNullable
                                               )
  {
    return getDeserializer(arrayOfStrParser,
                           p,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfStr(boolean nullable,
                                        boolean elemNullable
                                       )
  {
    return getDeserializer(arrayOfStrParser,
                           nullable,
                           elemNullable
                          );
  }


  static ValueDeserializer ofStr(boolean nullable)
  {
    return getDeserializer(strParser,
                           nullable
                          );
  }

  static ValueDeserializer ofStrSuchThat(Function<String, Optional<Error>> predicate,
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

  static ValueDeserializer ofArrayOfNumber(boolean nullable,
                                           boolean elemNullable
                                          )
  {
    return getDeserializer(arrayOfNumberParser,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfNumberEachSuchThat(Function<JsNumber, Optional<Error>> p,
                                                       boolean nullable,
                                                       boolean elemNullable
                                                      )
  {
    if (nullable && elemNullable) return reader -> arrayOfNumberParser.nullOrArrayWithNullEachSuchThat(reader,
                                                                                                       p
                                                                                                      );
    else if (nullable) return reader -> arrayOfNumberParser.nullOrArrayEachSuchThat(reader,
                                                                                    p
                                                                                   );
    else if (elemNullable) return reader -> arrayOfNumberParser.arrayWithNullEachSuchThat(reader,
                                                                                          p
                                                                                         );
    else return reader -> arrayOfNumberParser.arrayEachSuchThat(reader,
                                                                p
                                                               );
  }

  static ValueDeserializer ofArrayOfNumberSuchThat(Function<JsArray, Optional<Error>> p,
                                                   boolean nullable,
                                                   boolean elemNullable
                                                  )
  {
    return getDeserializer(arrayOfNumberParser,
                           p,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfIntegralSuchThat(Function<JsArray, Optional<Error>> p,
                                                     boolean nullable,
                                                     boolean elemNullable
                                                    )
  {
    return getDeserializer(arrayOfIntegralParser,
                           p,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofNumber(boolean nullable)
  {
    return getDeserializer(numberParser,
                           nullable
                          );
  }

  static ValueDeserializer ofNumberSuchThat(Function<JsNumber, Optional<Error>> predicate,
                                            boolean nullable
                                           )
  {

    if (nullable) return reader ->
    {
      JsValue value = numberParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(v -> v.isNumber(),
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


  static ValueDeserializer ofArrayOfIntegral(boolean nullable,
                                             boolean elemNullable
                                            )
  {
    return getDeserializer(arrayOfIntegralParser,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfIntegralEachSuchThat(Function<BigInteger, Optional<Error>> p,
                                                         boolean nullable,
                                                         boolean elemNullable
                                                        )

  {
    if (nullable && elemNullable) return reader ->
      arrayOfIntegralParser.nullOrArrayWithNullEachSuchThat(reader,
                                                            p
                                                           );
    else if (nullable) return reader ->
      arrayOfIntegralParser.nullOrArrayEachSuchThat(reader,
                                                    p
                                                   );
    else if (elemNullable) return reader ->
      arrayOfIntegralParser.arrayWithNullEachSuchThat(reader,
                                                      p
                                                     );
    else return reader ->
        arrayOfIntegralParser.arrayEachSuchThat(reader,
                                                p
                                               );
  }

  static ValueDeserializer ofIntegral(boolean nullable)
  {
    return getDeserializer(integralParser,
                           nullable
                          );
  }

  static ValueDeserializer ofIntegralSuchThat(Function<BigInteger, Optional<Error>> predicate,
                                              boolean nullable
                                             )
  {

    if (nullable) return reader ->
    {
      JsValue value = integralParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(v -> v.isBigInt(),
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

  static ValueDeserializer ofArrayOfDecimal(boolean nullable,
                                            boolean elemNullable
                                           )
  {
    return getDeserializer(arrayOfDecimalParser,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfDecimalEachSuchThat(Function<BigDecimal, Optional<Error>> p,
                                                        boolean nullable,
                                                        boolean elemNullable
                                                       )
  {
    if (nullable && elemNullable) return reader ->
      arrayOfDecimalParser.nullOrArrayWithNullEachSuchThat(reader,
                                                           p
                                                          );
    else if (nullable) return reader ->
      arrayOfDecimalParser.nullOrArrayEachSuchThat(reader,
                                                   p
                                                  );
    else if (elemNullable) return reader ->
      arrayOfDecimalParser.arrayWithNullEachSuchThat(reader,
                                                     p
                                                    );
    else return reader ->
        arrayOfDecimalParser.arrayEachSuchThat(reader,
                                               p
                                              );
  }

  static ValueDeserializer ofArrayOfDecimalSuchThat(Function<JsArray, Optional<Error>> p,
                                                    boolean nullable,
                                                    boolean elemNullable
                                                   )
  {
    return getDeserializer(arrayOfDecimalParser,
                           p,
                           nullable,
                           elemNullable
                          );

  }


  static ValueDeserializer ofArrayOfLong(boolean nullable,
                                         boolean elemNullable
                                        )
  {
    return getDeserializer(arrayOfLongParser,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfLongEachSuchThat(LongFunction<Optional<Error>> p,
                                                     boolean nullable,
                                                     boolean elemNullable
                                                    )

  {
    if (nullable && elemNullable) return reader ->
      arrayOfLongParser.nullOrArrayWithNullEachSuchThat(reader,
                                                        p
                                                       );
    else if (nullable) return reader ->
      arrayOfLongParser.nullOrArrayEachSuchThat(reader,
                                                p
                                               );
    else if (elemNullable) return reader ->
      arrayOfLongParser.arrayWithNullEachSuchThat(reader,
                                                  p
                                                 );
    else return reader -> arrayOfLongParser.arrayEachSuchThat(reader,
                                                              p
                                                             );
  }

  static ValueDeserializer ofArrayOfLongSuchThat(Function<JsArray, Optional<Error>> p,
                                                 boolean nullable,
                                                 boolean elemNullable
                                                )
  {
    return getDeserializer(arrayOfLongParser,
                           p,
                           nullable,
                           elemNullable
                          );
  }


  static ValueDeserializer ofDecimal(boolean nullable)
  {
    return getDeserializer(decimalParser,
                           nullable
                          );
  }

  static ValueDeserializer ofDecimalSuchThat(Function<BigDecimal, Optional<Error>> predicate,
                                             boolean nullable
                                            )
  {

    if (nullable) return reader ->
    {
      JsValue value = decimalParser.nullOrValue(reader);
      if (value == JsNull.NULL) return value;
      else
      {
        testTypeAndSpec(v -> v.isDecimal(),
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


  static ValueDeserializer ofLong(boolean nullable)
  {
    return getDeserializer(longParser,
                           nullable
                          );
  }

  static ValueDeserializer ofLongSuchThat(LongFunction<Optional<Error>> predicate,
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


  static ValueDeserializer ofArrayOfInt(boolean nullable,
                                        boolean elemNullable
                                       )
  {
    return getDeserializer(arrayOfIntParser,
                           nullable,
                           elemNullable
                          );
  }


  static ValueDeserializer ofArrayOfIntSuchThat(Function<JsArray, Optional<Error>> p,
                                                boolean nullable,
                                                boolean elemNullable
                                               )
  {
    return getDeserializer(arrayOfIntParser,
                           p,
                           nullable,
                           elemNullable
                          );
  }

  static ValueDeserializer ofArrayOfIntEachSuchThat(IntFunction<Optional<Error>> p,
                                                    boolean nullable,
                                                    boolean elemNullable
                                                   )
  {
    if (nullable && elemNullable)
      return reader -> arrayOfIntParser.nullOrArrayWithNullEachSuchThat(reader,
                                                                        p
                                                                       );
    else if (nullable) return reader -> arrayOfIntParser.nullOrArrayEachSuchThat(reader,
                                                                                 p
                                                                                );
    else if (elemNullable) return reader -> arrayOfIntParser.arrayWithNullEachSuchThat(reader,
                                                                                       p
                                                                                      );
    else return reader -> arrayOfIntParser.arrayEachSuchThat(reader,
                                                             p
                                                            );
  }


  public static ValueDeserializer ofInt(boolean nullable)
  {
    return getDeserializer(intParser,
                           nullable
                          );
  }

  public static ValueDeserializer ofIntSuchThat(IntFunction<Optional<Error>> predicate,
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
