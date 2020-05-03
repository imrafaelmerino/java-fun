package jsonvalues.specifications.immutable.array

import java.math.BigInteger
import java.util._
import java.util.function.BiFunction

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll

import scala.util.Try

class PutGetMergeRemoveSpec extends BasePropSpec
{


  val doubleInt: BiFunction[JsValue, JsValue, JsInt] = ScalaToJava.bifunction((a,
                                                                               b
                                                                            ) => JsInt.of(a.toJsInt().value + b.toJsInt().value)
                                                                              )


  property("inserted string in an empty array has to be returned by getStr function")
  {

    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[String]
                  )
           { (path,
              str
             ) =>
             JsArray.empty().put(path,
                                 JsStr.of(str)
                                 ).getOptStr(path).get() == str
           }
           )
  }

  property("inserted boolean is an empty array has to be returned only by getBool function")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Boolean]
                  )
           { (path,
              bool
             )
           =>

             val obj = JsArray.empty().put(path,
                                           JsBool.of(bool)
                                           )
             obj.getOptBool(path).get() == bool
             obj.getOptInt(path) == OptionalInt.empty()
             obj.getOptLong(path) == OptionalLong.empty()
             obj.getOptStr(path) == Optional.empty()
             obj.getOptObj(path) == Optional.empty()
             obj.getOptArray(path) == Optional.empty()
             obj.getOptBigDec(path) == Optional.empty()
             obj.getOptBigInt(path) == Optional.empty()
             obj.getOptDouble(path) == OptionalDouble.empty()
             obj.get(path) == JsBool.of(bool)

           }
           )
  }

  property("inserted integer in an empty array has to be returned by getInt,getLong,getBigInt function")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Int]
                  )
           { (path,
              n
             )
           =>
             val arr = JsArray.empty().put(path,
                                           JsInt.of(n)
                                           )
             arr.getOptInt(path).getAsInt == n
             arr.getOptBool(path) == Optional.empty()
             arr.getOptLong(path).getAsLong == n
             arr.getOptBigInt(path).get() == BigInteger.valueOf(n)
             arr.getOptObj(path) == Optional.empty()
             arr.getOptArray(path) == Optional.empty()
             arr.getOptBigDec(path) == Optional.empty()
             arr.getOptDouble(path) == OptionalDouble.empty()
             arr.get(path) == JsInt.of(n)

           }
           )
  }


  property("inserted long in an empty array has to be returned by getInt(if fits),getLong,getBigInt and get function")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Long]
                  )
           { (path,
              n
             )
           =>


             val arr = JsArray.empty().put(path,
                                           JsLong.of(n)
                                           )

             arr.getOptInt(path) == Try.apply(OptionalInt.of(Math.toIntExact(n))).getOrElse(OptionalInt.empty())

             arr.getOptBool(path) == Optional.empty()
             arr.getOptLong(path).getAsLong == n
             arr.getOptBigInt(path).get() == BigInteger.valueOf(n)
             arr.getOptObj(path) == Optional.empty()
             arr.getOptArray(path) == Optional.empty()
             arr.getOptBigDec(path) == Optional.empty()
             arr.getOptDouble(path) == OptionalDouble.empty()
             arr.get(path) == JsLong.of(n)

           }
           )
  }


  property("inserted bigint in an empty array has to be returned by getInt(if fits),getLong(if fits),getBigInt and get functions")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[BigInt]
                  )
           { (path,
              n
             )
           =>

             val arr = JsArray.empty().put(path,
                                           JsBigInt.of(n.bigInteger)
                                           )
             arr.getOptInt(path) == Try.apply(OptionalInt.of(n.bigInteger.intValueExact())).getOrElse(OptionalInt.empty())
             arr.getOptBool(path) == Optional.empty()
             arr.getOptLong(path) == Try.apply(OptionalLong.of(n.bigInteger.longValueExact())).getOrElse(OptionalLong.empty())
             arr.getOptBigInt(path).get() == n.bigInteger
             arr.getOptObj(path) == Optional.empty()
             arr.getOptArray(path) == Optional.empty()
             arr.getOptBigDec(path) == Optional.empty()
             arr.getOptDouble(path) == OptionalDouble.empty()
             arr.get(path) == JsBigInt.of(n.bigInteger)

           }
           )
  }




  property("put if present replaces elements with null")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            js.streamAll().allMatch(
                                   it =>
                                   {
                                     val elemToNull: function.Function[_ >: JsValue, _ <: JsValue] = _ => JsNull.NULL
                                     js.putIfPresent(it.path,
                                                     elemToNull
                                                     ).get(it.path).equals(JsNull.NULL)
                                   }
                                   )


          }
          )
  }

  property("put if absent never inserts when element containsPath")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            js.streamAll().allMatch(
                                   it => js.putIfAbsent(it.path,
                                                        ScalaToJava.supplier(() => JsNull.NULL)
                                                        ).get(it.path) == it.value
                                   )


          }
          )
  }

  property("put if absent inserts when element doesnt exist")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsValueGen
                 )
          { (path,
             elem
            ) =>
            JsArray.empty().putIfAbsent(path,
                                        ScalaToJava.supplier(() => elem)
                                        ).get(path) == elem
          }
          )
  }


  property("removes existing element and get function returns NOTHING")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsValueGen
                 )
          { (path,
             elem
            ) =>

            JsArray.empty().put(path,
                                elem
                                ).remove(path).get(path) == JsNothing.NOTHING

          }
          )
  }

  property("inserts and removes boolean")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Boolean]
                  )
           { (path,
              value
             ) =>

             val json = JsArray.empty().put(path,
                                            JsBool.of(value)
                                            )
             (json.getOptBool(path).get() == value) &&
             (json.remove(path).get(path) == JsNothing.NOTHING)


           }
           )
  }


}
