package jsonvalues

import java.math.BigInteger

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import scala.collection.JavaConverters._


case class FreqTypeOfNumber(intFreq: Int = 1,
                            longFreq  : Int = 1,
                            doubleFreq: Int = 1,
                            bigIntFreq: Int = 1,
                            bigDecFreq: Int = 1
                           )

case class FreqTypeOfValue(strFreq: Int = 2,
                           numberFreq: Int = 2,
                           boolFreq  : Int = 1,
                           nullFreq  : Int = 1
                          )

/**
  * A json object {} is made up parse an unordered set parse name/x pairs. A x parse a pair can be an object, an vector or
  * a x
  *
  * @param objFreq   freq with which a pair x is an object
  * @param arrFreq   freq with which a pair x is an vector
  * @param valueFreq freq with which a pair x is a x
  */
case class FreqTypeOfPair(objFreq: Int = 2,
                          arrFreq     : Int = 2,
                          valueFreq   : Int = 10,
                          emptyObjFreq: Int = 3,
                          emptyArrFreq: Int = 3
                         )

case class FreqsTypeOfArr(numberFreq: Int = 10,
                          strFreq   : Int = 10,
                          boolFreq  : Int = 5,
                          valueFreq : Int = 10,
                          objFreq   : Int = 2,
                          intFreq   : Int = 10,
                          doubleFreq: Int = 10,
                          longFreq  : Int = 10
                         )

case class JsElemGens(strGen: Gen[String] = Gen.oneOf(Characters.ALPHABET),
                      intGen          : Gen[Int] = arbitrary[Int],
                      longGen         : Gen[Long] = arbitrary[Long],
                      doubleGen       : Gen[Double] = arbitrary[Double],
                      floatGen        : Gen[Float] = arbitrary[Float],
                      boolGen         : Gen[Boolean] = arbitrary[Boolean],
                      bigIntGen       : Gen[BigInteger] = arbitrary[BigInt].map(it => it.bigInteger),
                      bigDecGen       : Gen[java.math.BigDecimal] = arbitrary[BigDecimal].map(it => it.bigDecimal),
                      arrLengthGen    : Gen[Int] = Gen.choose(5,
                                                              8
                                                              ),
                      objSizeGen      : Gen[Int] = Gen.choose(5,
                                                              8
                                                              ),
                      keyGen          : Gen[String] = Gen.oneOf(Characters.ALPHABET),
                      freqTypeOfNumber: FreqTypeOfNumber = new FreqTypeOfNumber,
                      freqTypeOfValue : FreqTypeOfValue = new FreqTypeOfValue,
                      freqTypeOfPair  : FreqTypeOfPair = new FreqTypeOfPair,
                      freqTypeOfArr   : FreqsTypeOfArr = new FreqsTypeOfArr,
                     )
{

  val jsStrGen: Gen[JsStr] = strGen.map(it => JsStr.of(it))

  val jsIntGen: Gen[JsInt] = intGen.map(it => JsInt.of(it))

  val jsLongGen: Gen[JsLong] = longGen.map(it => JsLong.of(it))

  val jsDoubleGen: Gen[JsDouble] = doubleGen.map(it => JsDouble.of(it))

  val jsBigIntGen: Gen[JsBigInt] = bigIntGen.map(it => JsBigInt.of(it))

  val jsBigDecimalGen: Gen[JsBigDec] = bigDecGen.map(it => JsBigDec.of(it))

  val jsNumberGen: Gen[JsNumber] = Gen.frequency((freqTypeOfNumber.intFreq, jsIntGen),
                                                 (freqTypeOfNumber.longFreq, jsLongGen),
                                                 (freqTypeOfNumber.doubleFreq, jsDoubleGen),
                                                 (freqTypeOfNumber.bigDecFreq, jsBigIntGen),
                                                 (freqTypeOfNumber.bigIntFreq, jsBigDecimalGen)
                                                 )


  val jsBoolGen: Gen[JsBool] = boolGen.map(it => JsBool.of(it))


  val jsValueGen: Gen[JsElem] = Gen.frequency((freqTypeOfValue.strFreq, jsStrGen),
                                              (freqTypeOfValue.numberFreq, jsNumberGen),
                                              (freqTypeOfValue.boolFreq, jsBoolGen),
                                              (freqTypeOfValue.nullFreq, Gen.const(JsNull.NULL))
                                              )


  val jsArrNumberGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsElem](size,
                                                 jsNumberGen
                                                 )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))
  }

  val jsArrStrGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsElem](size,
                                                 jsStrGen
                                                 )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))

  }

  val jsArrBoolGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsElem](size,
                                                 jsBoolGen
                                                 )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))

  }

  val jsArrIntGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsElem](size,
                                                 jsIntGen
                                                 )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))

  }

  val jsArrDoubleGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsElem](size,
                                                 jsDoubleGen
                                                 )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))

  }


  val jsArrObjGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsObj](size,
                                                jsObjGen
                                                )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))

  }

  val jsArrLongGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsElem](size,
                                                 jsLongGen
                                                 )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))

  }

  val jsArrValueGen: Gen[JsArray] =
  {
    for
      {
      size <- arrLengthGen
      vector <- Gen.containerOfN[Vector, JsElem](size,
                                                 jsValueGen
                                                 )
    } yield new MyImmutableJsArray(new MyScalaVector(vector))

  }

  val jsArrGen: Gen[JsArray] = Gen.frequency((freqTypeOfArr.strFreq, jsArrStrGen),
                                             (freqTypeOfArr.numberFreq, jsArrNumberGen),
                                             (freqTypeOfArr.boolFreq, jsArrBoolGen),
                                             (freqTypeOfArr.valueFreq, jsArrValueGen),
                                             (freqTypeOfArr.objFreq, jsArrObjGen),
                                             (freqTypeOfArr.intFreq, jsArrIntGen),
                                             (freqTypeOfArr.longFreq, jsArrLongGen),
                                             (freqTypeOfArr.doubleFreq, jsArrDoubleGen)
                                             )

  val pairNameValueGen: Gen[(String, JsElem)] =
  {
    for
      {
      key <- keyGen
      value <- Gen.frequency((freqTypeOfPair.valueFreq, jsValueGen),
                             (freqTypeOfPair.arrFreq, jsArrGen),
                             (freqTypeOfPair.objFreq, jsObjGen),
                             (freqTypeOfPair.emptyObjFreq, JsObj.empty()),
                             (freqTypeOfPair.emptyArrFreq, JsArray.empty())
                             )
    } yield (key, value)
  }


  val jsObjGen: Gen[JsObj] =
  {
    for
      {
      size <- objSizeGen
      pairs <- Gen.containerOfN[Array, (String, JsElem)](size,
                                                         pairNameValueGen
                                                         )
    } yield JsObj.of(scala.collection.immutable.HashMap[String, JsElem](pairs: _*).asJava)

  }
  val jsElemGen: Gen[JsElem] = Gen.oneOf(jsValueGen,
                                         jsArrGen,
                                         jsObjGen
                                         )

  val jsonContainerGen: Gen[Json[_]] = Gen.oneOf(jsObjGen,
                                                 jsArrGen
                                                 )

  val _jsObjGen_ = jsObjGen.map(it => JsObj._parse_(it.toString).orElseThrow())
  val _jsArrGen_ = jsArrGen.map(it => JsArray._parse_(it.toString).orElseThrow())
  val _jsonContainerGen_ = Gen.oneOf(_jsObjGen_,
                                     _jsArrGen_
                                     )


}
