package jsonvalues.specifications.mutable.jsobject

import java.util.Optional
import java.util.function.Predicate

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class FactoryMethodsSpec extends BasePropSpec
{


  property("of factory methods")
  {
    check(forAll(jsPairGen.pairOfValueGen)
          { p =>
            Jsons.mutable.`object`.of("a",
                                      p.elem
                                      ).size() == 1 &&
            Jsons.mutable.`object`.of("a",
                                      p.elem,
                                      "x",
                                      p.elem
                                      ).size() == 2 &&
            Jsons.mutable.`object`.of("a",
                                      p.elem,
                                      "x",
                                      p.elem,
                                      "c",
                                      p.elem
                                      ).size() == 3 &&
            Jsons.mutable.`object`.of("a",
                                      p.elem,
                                      "x",
                                      p.elem,
                                      "c",
                                      p.elem,
                                      "d",
                                      p.elem
                                      ).size() == 4 &&
            Jsons.mutable.`object`.of("a",
                                      p.elem,
                                      "x",
                                      p.elem,
                                      "c",
                                      p.elem,
                                      "d",
                                      p.elem,
                                      "e",
                                      p.elem
                                      ).size() == 5 &&
            Jsons.mutable.`object`.of("a",
                                      p.elem,
                                      "x",
                                      p.elem,
                                      "c",
                                      p.elem,
                                      "d",
                                      p.elem,
                                      "e",
                                      p.elem,
                                      "f",
                                      p.elem
                                      ).size() == 6


          }
          )
  }

  property("json object parser")
  {
    check(forAll(jsGen._jsObjGen_)
          { js =>
            val obj = Jsons.mutable.`object`.parse(js.toString).orElseThrow()
            obj.equals(js)
          }
          )
  }

  property("json object parser: mapping keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = Jsons.mutable.`object`.parse(js.toString,
                                                      ParseBuilder.builder().withKeyMap(it => it + "!")
                                                      )
            val allKeysEndsWithExclamation: Predicate[_ >: JsPair] = p => p.path.stream().filter(pos => pos.isKey).allMatch(pos => pos.asKey().name.endsWith("!"))
            parsed.orElseThrow().stream_().allMatch(allKeysEndsWithExclamation)
          }
          )
  }

  //keyValueFilter doesn't change the structure of the json
  property("json object parser: filtering all x keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = Jsons.mutable.`object`.parse(js.toString,
                                                      ParseBuilder.builder().withElemFilter(_ => false)
                                                      )
            parsed.orElseThrow().stream_().filter(p => p.elem.isNotJson).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: ignoring null")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = Jsons.mutable.`object`.parse(js.toString,
                                                      ParseBuilder.builder().withElemFilter(p => p.elem.isNotNull)
                                                      )

            parsed.orElseThrow().stream_().filter(p => p.elem.isNull).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering string values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = Jsons.mutable.`object`.parse(js.toString,
                                                      ParseBuilder.builder().withElemFilter(p => !p.elem.isStr)
                                                      )

            parsed.orElseThrow().stream_().filter(p => p.elem.isStr).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering number values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val parsed = Jsons.mutable.`object`.parse(js.toString,
                                                      ParseBuilder.builder().withElemFilter(p => p.elem.isNotNumber)
                                                      )

            parsed.orElseThrow().stream_().filter(p => p.elem.isNumber).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: mapping x values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val function: JsPair => JsElem = (pair: JsPair) => pair.mapIfStr(_ => "hi").elem

            val parsed = Jsons.mutable.`object`.parse(js.toString,
                                                      ParseBuilder.builder().withElemMap(ScalaToJava.function(function))
                                                      )

            parsed.orElseThrow().stream_().filter(p => p.elem.isStr).allMatch(p => p.elem.isStr(s => s.equals("hi")))
          }
          )
  }


}
