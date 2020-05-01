package jsonvalues.spec;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.parsers.specs.SpecParser;
import com.dslplatform.json.parsers.types.JsObjParser;
import com.dslplatform.json.parsers.types.arrays.*;
import com.dslplatform.json.parsers.specs.JsArrayOfObjSpecParser;
import com.dslplatform.json.parsers.specs.JsArraySpecParser;
import com.dslplatform.json.parsers.specs.JsObjSpecParser;
import com.dslplatform.json.parsers.specs.JsObjSpecWithRequiredKeysParser;
import com.dslplatform.json.parsers.types.*;
import com.dslplatform.json.parsers.types.arrays.JsArrayParser;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.*;

class ParserFactory
{

  final static ParserFactory INSTANCE = new ParserFactory();

  private ParserFactory()
  {
    intParser = new JsIntParser();
    longParser = new JsLongParser();
    integralParser = new JsIntegralParser();
    boolParser = new JsBoolParser();
    decimalParser = new JsDecimalParser();
    strParser = new JsStrParser();
    numberParser = new JsNumberParser();
    valueParser = new JsValueParser();
    objParser = new JsObjParser(valueParser);
    arrayOfValueParser = new JsArrayOfValueParser(valueParser);
    valueParser.setArrayDeserializer(arrayOfValueParser);
    valueParser.setObjDeserializer(objParser);
    valueParser.setNumberDeserializer(numberParser);
    arrayOfIntParser = new JsArrayOfIntParser(intParser);
    arrayOfLongParser = new JsArrayOfLongParser(longParser);
    arrayOfDecimalParser = new JsArrayOfDecimalParser(decimalParser);
    arrayOfIntegralParser = new JsArrayOfIntegralParser(integralParser);
    arrayOfNumberParser = new JsArrayOfNumberParser(numberParser);
    arrayOfObjParser = new JsArrayOfObjParser(objParser);
    arrayOfStrParser = new JsArrayOfStringParser(strParser);
    arrayOfBoolParser = new JsArrayOfBoolParser(boolParser);
    newParseException =
      (reader, error) -> new JsParserException(reader.newParseError(error.toString()));

  }

  private final JsIntParser intParser;
  private final JsLongParser longParser;
  private final JsIntegralParser integralParser;
  private final JsBoolParser boolParser;
  private final JsDecimalParser decimalParser;
  private final JsStrParser strParser;
  private final JsNumberParser numberParser;
  private final JsValueParser valueParser;
  private final JsObjParser objParser;
  private final JsArrayOfValueParser arrayOfValueParser;
  private final JsArrayOfIntParser arrayOfIntParser;
  private final JsArrayOfLongParser arrayOfLongParser;
  private final JsArrayOfDecimalParser arrayOfDecimalParser;
  private final JsArrayOfIntegralParser arrayOfIntegralParser;
  private final JsArrayOfNumberParser arrayOfNumberParser;
  private final JsArrayOfObjParser arrayOfObjParser;
  private final JsArrayOfStringParser arrayOfStrParser;
  private final JsArrayOfBoolParser arrayOfBoolParser;

  private final BiFunction<JsonReader<?>, Error, JsParserException> newParseException;

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


  private SpecParser getParser(AbstractParser parser,
                               boolean nullable
                              )
  {
    if (nullable)
      return parser::nullOrValue;
    else
      return parser::value;
  }

  private SpecParser getParser(JsArrayParser parser,
                               boolean nullable
                              )
  {
   if (nullable)
      return parser::nullOrArray;
    else return parser::array;
  }


  private SpecParser getParser(JsArrayParser parser,
                               Function<JsArray, Optional<Error>> p,
                               boolean nullable
                              )
  {
     if (nullable)
      return reader -> parser.nullOrArraySuchThat(reader,
                                                        p
                                                       );

    else return reader -> parser.arraySuchThat(reader,
                                                     p
                                                    );
  }


  SpecParser ofArrayOfObjSuchThat(Function<JsArray, Optional<Error>> p,
                                  boolean nullable
                                 )
  {
    return getParser(arrayOfObjParser,
                     p,
                     nullable
                    );
  }

  SpecParser ofArrayOfObjEachSuchThat(Function<JsObj, Optional<Error>> p,
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

  SpecParser ofArrayOfObjSpec(Vector<String> required,
                              Map<String, SpecParser> keyDeserializers,
                              boolean nullable,
                              boolean strict
                             )
  {
    JsObjSpecParser f = (required.isEmpty()) ?
      new JsObjSpecParser(strict, keyDeserializers) :
      new JsObjSpecWithRequiredKeysParser(required,
                                          keyDeserializers, strict);
    JsArrayOfObjSpecParser parser = new JsArrayOfObjSpecParser(f);
    if (nullable)
      return parser::nullOrArray;

    else
      return parser::array;

  }

  SpecParser ofArrayOfObj(boolean nullable)
  {
    return getParser(arrayOfObjParser,
                     nullable
                    );
  }


  SpecParser ofObjSuchThat(final Function<JsObj, Optional<Error>> predicate,
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


  SpecParser ofArraySpec(Vector<SpecParser> keyDeserializers,
                         boolean nullable
                        )
  {
    if (nullable)
      return reader -> new JsArraySpecParser(keyDeserializers).nullOrArray(reader);
    else
      return reader -> new JsArraySpecParser(keyDeserializers).array(reader);
  }

  SpecParser ofObjSpec(Vector<String> required,
                       Map<String, SpecParser> keyDeserializers,
                       boolean nullable,
                       boolean strict
                      )
  {
    return reader ->
    {
      if (required.isEmpty())
      {
        JsObjSpecParser parser = new JsObjSpecParser(strict, keyDeserializers);
        if (nullable) return parser.nullOrValue(reader);
        else return parser.value(reader);
      } else
      {
        JsObjSpecWithRequiredKeysParser parser = new JsObjSpecWithRequiredKeysParser(required,
                                                                                           keyDeserializers,
                                                                                           strict
        );
        if (nullable) return parser.nullOrValue(reader);
        else return parser.value(reader);
      }

    };
  }


  SpecParser ofArrayOfValueSuchThat(Function<JsArray, Optional<Error>> p,
                                    boolean nullable
                                   )
  {
    return getParser(arrayOfValueParser,
                     p,
                     nullable
                    );
  }

  SpecParser ofObj(boolean nullable)
  {
    return getParser(objParser,
                     nullable
                    );
  }

  SpecParser ofArrayOfValue(boolean nullable)
  {
    return getParser(arrayOfValueParser,
                     nullable
                    );
  }

  SpecParser ofArrayOfValueEachSuchThat(Function<JsValue, Optional<Error>> p,
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

  SpecParser ofValue()
  {
    return getParser(valueParser,
                     true
                    );
  }

  SpecParser ofValueSuchThat(Function<JsValue, Optional<Error>> predicate)
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


  SpecParser ofBool(boolean nullable)
  {
    return getParser(boolParser,
                     nullable
                    );
  }

  SpecParser ofTrue(boolean nullable)
  {
    if (nullable) return boolParser::nullOrTrue;
    else return boolParser::True;
  }

  SpecParser ofFalse(boolean nullable)
  {
    if (nullable) return boolParser::nullOrFalse;
    else return boolParser::False;
  }

  SpecParser ofArrayOfBool(boolean nullable
                          )
  {
    return getParser(arrayOfBoolParser,
                     nullable
                    );
  }

  SpecParser ofArrayOfBoolSuchThat(Function<JsArray, Optional<Error>> p,
                                   boolean nullable
                                  )
  {
    return getParser(arrayOfBoolParser,
                     p,
                     nullable
                    );

  }

  SpecParser ofArrayOfStrEachSuchThat(Function<String, Optional<Error>> p,
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

  SpecParser ofArrayOfStrSuchThat(Function<JsArray, Optional<Error>> p,
                                  boolean nullable
                                 )
  {
    return getParser(arrayOfStrParser,
                     p,
                     nullable
                    );
  }

  SpecParser ofArrayOfStr(boolean nullable
                         )
  {
    return getParser(arrayOfStrParser,
                     nullable
                    );
  }


  SpecParser ofStr(boolean nullable)
  {
    return getParser(strParser,
                     nullable
                    );
  }

  SpecParser ofStrSuchThat(Function<String, Optional<Error>> predicate,
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

  SpecParser ofArrayOfNumber(boolean nullable
                            )
  {
    return getParser(arrayOfNumberParser,
                     nullable
                    );
  }

  SpecParser ofArrayOfNumberEachSuchThat(Function<JsNumber, Optional<Error>> p,
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

  SpecParser ofArrayOfNumberSuchThat(Function<JsArray, Optional<Error>> p,
                                     boolean nullable
                                    )
  {
    return getParser(arrayOfNumberParser,
                     p,
                     nullable
                    );
  }

  SpecParser ofArrayOfIntegralSuchThat(Function<JsArray, Optional<Error>> p,
                                       boolean nullable
                                      )
  {
    return getParser(arrayOfIntegralParser,
                     p,
                     nullable
                    );
  }

  SpecParser ofNumber(boolean nullable)
  {
    return getParser(numberParser,
                     nullable
                    );
  }

  SpecParser ofNumberSuchThat(Function<JsNumber, Optional<Error>> predicate,
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


  SpecParser ofArrayOfIntegral(boolean nullable
                              )
  {
    return getParser(arrayOfIntegralParser,
                     nullable
                    );
  }

  SpecParser ofArrayOfIntegralEachSuchThat(Function<BigInteger, Optional<Error>> p,
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

  SpecParser ofIntegral(boolean nullable)
  {
    return getParser(integralParser,
                     nullable
                    );
  }

  SpecParser ofIntegralSuchThat(Function<BigInteger, Optional<Error>> predicate,
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

  SpecParser ofArrayOfDecimal(boolean nullable
                             )
  {
    return getParser(arrayOfDecimalParser,
                     nullable
                    );
  }

  SpecParser ofArrayOfDecimalEachSuchThat(Function<BigDecimal, Optional<Error>> p,
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

  SpecParser ofArrayOfDecimalSuchThat(Function<JsArray, Optional<Error>> p,
                                      boolean nullable
                                     )
  {
    return getParser(arrayOfDecimalParser,
                     p,
                     nullable
                    );

  }


  SpecParser ofArrayOfLong(boolean nullable
                          )
  {
    return getParser(arrayOfLongParser,
                     nullable
                    );
  }

  SpecParser ofArrayOfLongEachSuchThat(LongFunction<Optional<Error>> p,
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

  SpecParser ofArrayOfLongSuchThat(Function<JsArray, Optional<Error>> p,
                                   boolean nullable
                                  )
  {
    return getParser(arrayOfLongParser,
                     p,
                     nullable
                    );
  }


  SpecParser ofDecimal(boolean nullable)
  {
    return getParser(decimalParser,
                     nullable
                    );
  }

  SpecParser ofDecimalSuchThat(Function<BigDecimal, Optional<Error>> predicate,
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


  SpecParser ofLong(boolean nullable)
  {
    return getParser(longParser,
                     nullable
                    );
  }

  SpecParser ofLongSuchThat(LongFunction<Optional<Error>> predicate,
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


  SpecParser ofArrayOfInt(boolean nullable
                         )
  {
    return getParser(arrayOfIntParser,
                     nullable
                    );
  }


  SpecParser ofArrayOfIntSuchThat(Function<JsArray, Optional<Error>> p,
                                  boolean nullable
                                 )
  {
    return getParser(arrayOfIntParser,
                     p,
                     nullable
                    );
  }

  SpecParser ofArrayOfIntEachSuchThat(IntFunction<Optional<Error>> p,
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


  SpecParser ofInt(boolean nullable)
  {
    return getParser(intParser,
                     nullable
                    );
  }

  SpecParser ofIntSuchThat(IntFunction<Optional<Error>> predicate,
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
