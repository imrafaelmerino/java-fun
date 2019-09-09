package jsonvalues.specifications.mutable.array

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class AppendToArraySpec extends BasePropSpec
{

  property("appends one element")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>

            Jsons.mutable.array.empty().append(path,
                                               elem
                                               ).getArray(path).get().size() == 1
          }
          )
  }

  property("appends three element")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen
                 )
          { (path,
             a,
             b,
             c
            ) =>

            val array = Jsons.mutable.array.empty().append(path,
                                                           a,
                                                           b,
                                                           c
                                                           )
            array.getArray(path).get().size() == 3 &&
            array.get(path.index(0)).equals(a) &&
            array.get(path.index(1)).equals(b) &&
            array.get(path.index(2)).equals(c)
          }
          )
  }

  property("prepends three element")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen
                 )
          { (path,
             a,
             b,
             c
            ) =>

            val array = Jsons.mutable.array.empty().prepend(path,
                                                            a,
                                                            b,
                                                            c
                                                            )
            array.getArray(path).get().size() == 3 &&
            array.get(path.index(0)).equals(a) &&
            array.get(path.index(1)).equals(b) &&
            array.get(path.index(2)).equals(c)
          }
          )
  }

  property("appends a json array")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsArrGen
                 )
          { (path,
             a
            ) =>

            val array = Jsons.mutable.array.empty().appendAll(path,
                                                              a
                                                              )
            array.getArray(path).get().size() == a.size() &&
            a.get(JsPath.fromIndex(0)).equals(array.get(path.index(0))) &&
            a.get(JsPath.fromIndex(-1)).equals(array.get(path.index(-1)))
          }
          )
  }

  property("appends a json array if exists the target")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsArrGen
                 )
          { (path,
             a
            ) =>

            val empty = Jsons.mutable.array.empty().appendAllIfPresent(path,
                                                                       () => a
                                                                       )

            val notEmpty = Jsons.mutable.array.empty().put(path,
                                                           Jsons.mutable.array.empty()
                                                           ).appendAllIfPresent(path,
                                                                                () => a
                                                                                )
            !empty.getArray(path).isPresent && notEmpty.getArray(path).get().equals(a)
          }
          )
  }

  property("prepends a json array if exists the target")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsArrGen
                 )
          { (path,
             a
            ) =>

            val empty = Jsons.mutable.array.empty().prependAllIfPresent(path,
                                                                        () => a
                                                                        )

            val notEmpty = Jsons.mutable.array.empty().put(path,
                                                           Jsons.mutable.array.of(1)
                                                           ).prependAllIfPresent(path,
                                                                                 () => a
                                                                                 )
            !empty.getArray(path).isPresent && notEmpty.getArray(path).get().equals(Jsons.mutable.array.of(1).prependAll(a))
          }
          )
  }

  property("appendIfPresent elements doesn't append anything when the array doesn't exist")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            !Jsons.mutable.array.empty().appendIfPresent(path,
                                                         ScalaToJava.supplier(() => elem)
                                                         ).getArray(path).isPresent
          }
          )
  }
  //
  property("appends elements to the front after creating a new array in an empty object")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            Jsons.mutable.array.empty().prepend(path,
                                                elem,
                                                elem
                                                ).getArray(path).get().size() == 2
          }
          )
  }

  property("prependIfPresent elements doesn't append anything when the array doesn't exist")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            !Jsons.mutable.array.empty().appendIfPresent(path,
                                                         ScalaToJava.supplier(() => elem)
                                                         ).getArray(path).isPresent
          }
          )
  }

  property("appendsFrontIfPresent appends elements to the front when the array containsPath")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            Jsons.mutable.array.empty().prepend(path,
                                                elem
                                                ).prependIfPresent(path,
                                                                   ScalaToJava.supplier(() => elem)
                                                                   ).getArray(path).get().size() == 2


          }
          )
  }

  property("appendsBackIfPresent appends elements to the back when the array containsPath")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            Jsons.mutable.array.empty().append(path,
                                               elem
                                               ).appendIfPresent(path,
                                                                 ScalaToJava.supplier(() => elem)
                                                                 ).getArray(path).get().size() == 2


          }
          )
  }
}
