package jsonvalues.specifications.immutable.array

import jsonvalues.JsArray.TYPE
import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class SetTheoryOpsSpec extends BasePropSpec
{


  property("union of an array with itself returns the same array (TYPE.SET)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union(js,
                     TYPE.SET
                     ).equals(js) && js.union(js,
                                              TYPE.SET
                                              ).hashCode() == js.hashCode()
          }

          )
  }

  property("union of an array with itself returns the same array (TYPE.LIST)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            println(js)
            js.union(js,
                     TYPE.LIST
                     ).equals(js) && js.union(js,
                                              TYPE.LIST
                                              ).hashCode() == js.hashCode()
          }
          )
  }

  property("union of an array with itself returns the same array duplicated (TYPE.MULTISET)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union(js,
                     TYPE.MULTISET
                     ).size() == 2 * js.size()
          }
          )
  }


  property("unionAll of an array with itself returns the same array ")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.unionAll(js).equals(js) && js.unionAll(js).hashCode() == js.hashCode()
          }
          )
  }


  property("intersection of an array with itself returns the same array (MULTISET AND LIST)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.intersection(js,
                            TYPE.LIST
                            ).equals(js) &&
            js.intersection(js,
                            TYPE.MULTISET
                            ).equals(js)
          }
          )
  }
  //
  property("union of an array A with an empty array, returns A")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union(JsArray.empty(),
                     TYPE.LIST
                     ).equals(js) && js.unionAll(JsArray.empty()).equals(js)
          }
          )
  }
  //
  property("array union is commutative (MULTISET AND LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                 )
          { (a,
             b
            ) =>
            a.union(b,
                    TYPE.MULTISET
                    ).size() == b.union(a,
                                        TYPE.MULTISET
                                        ).size() &&
            a.union(b,
                    TYPE.LIST
                    ).size() == b.union(a,
                                        TYPE.LIST
                                        ).size()
          }
          )
  }
  //
  property("array unionAll is commutative (MULTISET AND LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                 )
          { (a,
             b
            ) =>
            a.unionAll(b).size() == b.unionAll(a).size()


          }
          )
  }
  property("arrays intersection is commutative (ORDERED LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                 )
          { (a,
             b
            ) =>
            a.intersection(b,
                           TYPE.LIST
                           ).equals(b.intersection(a,
                                                   TYPE.LIST
                                                   )
                                    )
          }
          )
  }

  property("arrays intersectionAll is commutative (ORDERED LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                 )
          { (a,
             b
            ) =>
            a.intersectionAll(b).equals(b.intersectionAll(a))
          }
          )
  }


  property("intersection of an array with an empty array returns an empty array")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.intersection(JsArray.empty(),
                            TYPE.LIST
                            ).equals(JsArray.empty()) &&
            js.intersection(JsArray.empty(),
                            TYPE.SET
                            ).equals(JsArray.empty()) &&
            js.intersection(JsArray.empty(),
                            TYPE.LIST
                            ).equals(JsArray.empty())
          }
          )
  }

  property("intersectionAll of an array with an empty array returns an empty array")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.intersectionAll(JsArray.empty()).equals(JsArray.empty())
          }
          )
  }


}
