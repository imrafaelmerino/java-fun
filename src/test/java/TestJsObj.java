import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsPath.fromKey;
import static jsonvalues.JsPath.path;

public class TestJsObj
{
    @Test
    public void test_creates_mutable_empty_object()
    {
        JsObj obj = Jsons.mutable.object.empty();
        Assertions.assertTrue(obj.isEmpty());

        final JsObj obj1 = obj.put(JsPath.fromKey("a"),
                                   1
                                  );// obj is mutable
        Assertions.assertEquals(1,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );
    }

    @Test
    public void test_creates_immutable_empty_object()
    {
        JsObj obj = Jsons.immutable.object.empty();
        Assertions.assertTrue(obj.isEmpty());

        final JsObj obj1 = obj.put(JsPath.fromKey("a"),
                                   1
                                  );// obj is mutable
        Assertions.assertTrue(obj.isEmpty());
        Assertions.assertEquals(OptionalInt.of(1),
                                obj1.getInt(JsPath.fromKey("a"))
                               );
    }

    @Test
    public void test_creates_mutable_one_element_object()
    {
        JsObj obj = Jsons.mutable.object.of("a",
                                            JsInt.of(1)
                                           );
        Assertions.assertEquals(1,
                                obj.size()
                               );

        final JsObj obj1 = obj.put(JsPath.fromKey("b"),
                                   2
                                  );// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );
    }

    @Test
    public void test_creates_mutable_two_elements_object()
    {
        JsObj obj = Jsons.mutable.object.of("a",
                                            JsInt.of(1),
                                            "b",
                                            JsStr.of("a")
                                           );
        Assertions.assertEquals(2,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove(JsPath.fromKey("b"));// obj is mutable
        Assertions.assertEquals(1,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );

    }

    @Test
    public void test_creates_immutable_two_elements_object()
    {
        JsObj obj = Jsons.immutable.object.of("a",
                                              JsInt.of(1),
                                              "b",
                                              JsStr.of("a")
                                             );
        Assertions.assertEquals(2,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove(JsPath.fromKey("b"));// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
                               );
        Assertions.assertEquals(1,
                                obj1.size()
                               );

    }

    @Test
    public void test_creates_mutable_three_elements_object()
    {
        JsObj obj = Jsons.mutable.object.of("a",
                                            JsLong.of(10),
                                            "b",
                                            JsStr.of("b"),
                                            "c",
                                            JsInt.of(10)
                                           );
        Assertions.assertEquals(3,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove(JsPath.fromKey("a"));// obj is mutable
        Assertions.assertEquals(2,
                                obj.size()
                               );
        Assertions.assertEquals(obj,
                                obj1
                               );

    }

    @Test
    public void test_creates_immutable_three_elements_object()
    {
        JsObj obj = Jsons.immutable.object.of("a",
                                              JsLong.of(10),
                                              "b",
                                              JsStr.of("b"),
                                              "c",
                                              JsInt.of(10)
                                             );
        Assertions.assertEquals(3,
                                obj.size()
                               );

        final JsObj obj1 = obj.remove(JsPath.fromKey("a"));// obj is mutable
        Assertions.assertEquals(3,
                                obj.size()
                               );
        Assertions.assertEquals(2,
                                obj1.size()
                               );

    }

    @Test
    public void test_creates_mutable_four_elements_object()
    {
        JsObj obj = Jsons.mutable.object.of("a",
                                            JsStr.of("A"),
                                            "b",
                                            JsStr.of("B"),
                                            "c",
                                            JsStr.of("C"),
                                            "d",
                                            JsStr.of("D"),
                                            "e",
                                            Jsons.mutable.object.empty()
                                           );
        Assertions.assertEquals(5,
                                obj.size()
                               );

        obj.mapKeys(pair -> pair.path.last()
                                     .asKey().name + pair.elem.asJsStr().x,
                    p -> p.elem.isStr()
                   ); // obj is mutable

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("aA",
                                 "bB",
                                 "cC",
                                 "dD",
                                 "e"
                                ));
        Assertions.assertEquals(obj.fields(),
                                set
                               );

    }

    @Test
    public void test_creates_immutable_four_elements_object()
    {
        JsObj obj = Jsons.immutable.object.of("a",
                                              JsStr.of("A"),
                                              "b",
                                              JsStr.of("B"),
                                              "c",
                                              JsStr.of("C"),
                                              "h",
                                              NULL,
                                              "d",
                                              JsStr.of("D")
                                             );
        Assertions.assertEquals(5,
                                obj.size()
                               );

        final JsObj obj1 = obj.mapKeys(pair -> pair.path.last()
                                                        .asKey().name + pair.elem.asJsStr().x,
                                       p -> p.elem.isStr()
                                      );// obj is mutable

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("aA",
                                 "bB",
                                 "cC",
                                 "h",
                                 "dD"
                                ));
        Assertions.assertEquals(set,
                                obj1.fields()

                               );

        Set<String> set1 = new HashSet<>();
        set1.addAll(Arrays.asList("a",
                                  "b",
                                  "c",
                                  "d",
                                  "h"
                                 ));
        Assertions.assertEquals(set1,
                                obj.fields()

                               );

    }

    @Test
    public void test_creates_mutable_five_elements_object()
    {

        JsObj obj = Jsons.mutable.object.of("a",
                                            JsStr.of("A"),
                                            "b",
                                            JsStr.of("B"),
                                            "c",
                                            JsStr.of("C"),
                                            "d",
                                            JsStr.of("D"),
                                            "e",
                                            JsStr.of("E")
                                           );

        Assertions.assertEquals(5,
                                obj.size()
                               );
        obj.remove(JsPath.fromKey("b"));
        Assertions.assertEquals(4,
                                obj.size()
                               );
    }

    @Test
    public void test_creates_immutable_five_elements_object()
    {

        JsObj obj = Jsons.immutable.object.of("a",
                                              JsStr.of("A"),
                                              "b",
                                              JsStr.of("B"),
                                              "c",
                                              JsStr.of("C"),
                                              "d",
                                              JsStr.of("D"),
                                              "e",
                                              JsStr.of("E")
                                             );

        Assertions.assertEquals(5,
                                obj.size()
                               );
        final JsObj obj1 = obj.mapKeys(it -> it.path.last()
                                                    .asKey().name.toUpperCase());

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("A",
                                 "B",
                                 "C",
                                 "D",
                                 "E"
                                ));

        Assertions.assertEquals(set,
                                obj1.fields()
                               );
        Set<String> set1 = new HashSet<>();
        set1.addAll(Arrays.asList("a",
                                  "b",
                                  "c",
                                  "d",
                                  "e"
                                 ));
        Assertions.assertEquals(set1,
                                obj.fields()
                               );
    }

    @Test
    public void test_creates_mutable_object_from_pairs()
    {

        JsObj obj = Jsons.mutable.object.of(JsPair.of(fromKey("a"),
                                                      JsInt.of(1)
                                                     ),
                                            JsPair.of(fromKey("b"),
                                                      JsInt.of(2)
                                                     ),
                                            JsPair.of(fromKey("c"),
                                                      JsInt.of(3)
                                                     ),
                                            JsPair.of(path("/d/0/0"),
                                                      JsInt.of(5)
                                                     ),
                                            JsPair.of(path("/d/0/1"),
                                                      JsInt.of(6)
                                                     )
                                           );

        Assertions.assertEquals(5,
                                obj.size_()
                               );

        Assertions.assertEquals(4,
                                obj.size()
                               );

        Assertions.assertEquals(OptionalInt.of(6),
                                obj.getInt(path("/d/0/1"))
                               );


    }

    @Test
    public void test_creates_immutable_object_from_pairs()
    {

        JsObj obj = Jsons.immutable.object.of(JsPair.of(fromKey("a"),
                                                        JsInt.of(1)
                                                       ),
                                              JsPair.of(fromKey("b"),
                                                        JsInt.of(2)
                                                       ),
                                              JsPair.of(fromKey("c"),
                                                        JsInt.of(3)
                                                       ),
                                              JsPair.of(path("/d/0/0"),
                                                        JsInt.of(5)
                                                       ),
                                              JsPair.of(path("/d/0/1"),
                                                        JsInt.of(6)
                                                       )
                                             );

        Assertions.assertEquals(5,
                                obj.size_()
                               );

        Assertions.assertEquals(4,
                                obj.size()
                               );

        Assertions.assertEquals(OptionalInt.of(6),
                                obj.getInt(path("/d/0/1"))
                               );


    }


    @Test
    public void test_head_and_tail_of_empty_obj_returns_exception()
    {

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.object.empty()
                                                            .head()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.object.empty()
                                                          .head()
                               );
        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.object.empty()
                                                            .tail("a")
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.object.empty()
                                                          .tail("a")
                               );
    }

    @Test
    public void test_equals_and_hashcode()
    {
        final JsObj obj = Jsons.immutable.object.of("a",
                                                    JsInt.of(1),
                                                    "b",
                                                    JsBigInt.of(BigInteger.ONE),
                                                    "c",
                                                    JsLong.of(1),
                                                    "d",
                                                    JsBigDec.of(BigDecimal.ONE)
                                                   );
        final JsObj obj1 = Jsons.immutable.object.of("a",
                                                     JsBigDec.of(BigDecimal.ONE),
                                                     "b",
                                                     JsLong.of(1),
                                                     "c",
                                                     JsInt.of(1),
                                                     "d",
                                                     JsBigInt.of(BigInteger.ONE)
                                                    );

        Assertions.assertEquals(obj,
                                obj1
                               );
        Assertions.assertEquals(obj.hashCode(),
                                obj1.hashCode()
                               );

    }

    @Test
    public void test_map_strings_to_lowercase_and_reduce()
    {

        final JsObj immutable = Jsons.immutable.object.of("a",
                                                          Jsons.immutable.object.of("a1",
                                                                                    JsStr.of("A"),
                                                                                    "b1",
                                                                                    JsStr.of("B")
                                                                                   ),
                                                          "n",
                                                          JsInt.of(1),
                                                          "c",
                                                          JsStr.of("C"),
                                                          "d",
                                                          Jsons.immutable.object.of("d1",
                                                                                    JsStr.of("D"),
                                                                                    "e1",
                                                                                    JsStr.of("E"),
                                                                                    "g",
                                                                                    JsInt.of(2)
                                                                                   ),
                                                          "f",
                                                          JsStr.of("F")
                                                         );

        final JsObj mutable = Jsons.mutable.object.of("a",
                                                      Jsons.mutable.object.of("a1",
                                                                              JsStr.of("A"),
                                                                              "b1",
                                                                              JsStr.of("B")
                                                                             ),
                                                      "n",
                                                      JsInt.of(1),
                                                      "c",
                                                      JsStr.of("C"),
                                                      "d",
                                                      Jsons.mutable.object.of("d1",
                                                                              JsStr.of("D"),
                                                                              "e1",
                                                                              JsStr.of("E")
                                                                             ),
                                                      "f",
                                                      JsStr.of("F")
                                                     );

        for (JsObj obj : Arrays.asList(immutable,
                                       mutable
                                      ))
        {

            final JsObj obj1 = obj.mapElems_(pair ->
                                             {
                                                 Assertions.assertEquals(pair.elem,
                                                                         obj.get(pair.path)
                                                                        );
                                                 return pair.mapIfStr(String::toLowerCase).elem;
                                             });

            final Optional<String> reduced_ = obj1.reduce_(String::concat,
                                                           p ->
                                                           {
                                                               Assertions.assertEquals(p.elem,
                                                                                       obj1.get(p.path)
                                                                                      );
                                                               return p.elem.asJsStr().x;
                                                           },
                                                           p -> p.elem.isStr()
                                                          );

            final char[] chars_ = reduced_.get()
                                          .toCharArray();
            Arrays.sort(chars_);
            Assertions.assertEquals("abcdef",
                                    new String(chars_)
                                   );

            final Optional<String> reduced = obj1.reduce(String::concat,
                                                         p ->
                                                         {
                                                             Assertions.assertEquals(p.elem,
                                                                                     obj1.get(p.path)
                                                                                    );
                                                             return p.elem.asJsStr().x;
                                                         },
                                                         p -> p.elem.isStr()
                                                        );

            final char[] chars = reduced.get()
                                        .toCharArray();
            Arrays.sort(chars);
            Assertions.assertEquals("cf",
                                    new String(chars)
                                   );
        }


    }

    @Test
    public void test_map_keys_to_uppercase_removing_trailing_white_spaces_mutable()
    {

        final Supplier<JsObj> supplier = () -> Jsons.mutable.object.of(" a ",
                                                                       Jsons.mutable.object.of(" a1 ",
                                                                                               JsStr.of("A"),
                                                                                               " b1 ",
                                                                                               JsStr.of("B")
                                                                                              ),
                                                                       " c ",
                                                                       JsStr.of("C"),
                                                                       " d ",
                                                                       Jsons.mutable.object.of(" d1 ",
                                                                                               JsStr.of("D"),
                                                                                               " e1 ",
                                                                                               Jsons.mutable.array.of(Jsons.mutable.object.of(" f ",
                                                                                                                                              JsStr.of("F1")
                                                                                                                                             ),
                                                                                                                      Jsons.mutable.object.of(" g ",
                                                                                                                                              JsStr.of("G1")
                                                                                                                                             )
                                                                                                                     )
                                                                                              ),
                                                                       " f ",
                                                                       JsStr.of("F")
                                                                      );
        final JsObj obj = supplier.get();
        final JsObj mapped = obj.mapKeys_(p ->
                                          {

                                              Assertions.assertEquals(p.elem,
                                                                      supplier.get()
                                                                              .get(p.path)
                                                                     );
                                              return p.path.last()
                                                           .asKey().name.trim()
                                                                        .toUpperCase();


                                          });


        Assertions.assertFalse(mapped.stream_()
                                     .map(it ->
                                          {
                                              final String name = it.path.last()
                                                                         .asKey().name;
                                              return name.contains(" ") || name.chars()
                                                                               .mapToObj(Character::isLowerCase)
                                                                               .findAny()
                                                                               .orElse(false);
                                          })
                                     .findFirst()
                                     .orElse(false));

    }


    @Test
    public void test_map_keys_to_uppercase_removing_trailing_white_spaces_immutable()
    {
        final JsObj obj = Jsons.immutable.object.of(" a ",
                                                    Jsons.immutable.object.of(" a1 ",
                                                                              JsStr.of("A"),
                                                                              " b1 ",
                                                                              JsStr.of("B")
                                                                             ),
                                                    " c ",
                                                    JsStr.of("C"),
                                                    " d ",
                                                    Jsons.immutable.object.of(" d1 ",
                                                                              JsStr.of("D"),
                                                                              " e1 ",
                                                                              Jsons.immutable.array.of(Jsons.immutable.object.of(" f ",
                                                                                                                                 JsStr.of("F1")
                                                                                                                                ),
                                                                                                       Jsons.immutable.object.of(" g ",
                                                                                                                                 JsStr.of("G1")
                                                                                                                                )
                                                                                                      )
                                                                             ),
                                                    " f ",
                                                    JsStr.of("F")
                                                   );


        final JsObj mapped = obj.mapKeys_(p ->
                                          {
                                              Assertions.assertEquals(p.elem,
                                                                      obj.get(p.path)
                                                                     );
                                              return p.path.last()
                                                           .asKey().name.trim()
                                                                        .toUpperCase();
                                          });

        Assertions.assertFalse(mapped.stream_()
                                     .map(it ->
                                          {
                                              final String name = it.path.last()
                                                                         .asKey().name;
                                              return name.contains(" ") || name.chars()
                                                                               .mapToObj(Character::isLowerCase)
                                                                               .findAny()
                                                                               .orElse(false);
                                          })
                                     .findFirst()
                                     .orElse(false));

    }

    @Test
    public void test_map_keys_to_uppercase_removing_trailing_white_spaces_if_condition()
    {
        final JsObj immutable = Jsons.immutable.object.of(" a ",
                                                          Jsons.immutable.object.of(" a1 ",
                                                                                    JsStr.of("A"),
                                                                                    " b1 ",
                                                                                    JsStr.of("B")
                                                                                   ),
                                                          " c ",
                                                          JsStr.of("C"),
                                                          " d ",
                                                          Jsons.immutable.object.of(" d1 ",
                                                                                    JsStr.of("D"),
                                                                                    " e1 ",
                                                                                    Jsons.immutable.array.of(Jsons.immutable.object.of(" f ",
                                                                                                                                       JsStr.of("F1")
                                                                                                                                      ),
                                                                                                             Jsons.immutable.object.of(" g ",
                                                                                                                                       JsStr.of("G1")
                                                                                                                                      )
                                                                                                            )
                                                                                   ),
                                                          " f ",
                                                          JsStr.of("F")
                                                         );

        final JsObj mutable = Jsons.mutable.object.of(" a ",
                                                      Jsons.mutable.object.of(" a1 ",
                                                                              JsStr.of("A"),
                                                                              " b1 ",
                                                                              JsStr.of("B")
                                                                             ),
                                                      " c ",
                                                      JsStr.of("C"),
                                                      " d ",
                                                      Jsons.mutable.object.of(" d1 ",
                                                                              JsStr.of("D"),
                                                                              " e1 ",
                                                                              Jsons.mutable.array.of(Jsons.mutable.object.of(" f ",
                                                                                                                             JsStr.of("F1")
                                                                                                                            ),
                                                                                                     Jsons.mutable.object.of(" g ",
                                                                                                                             JsStr.of("G1")
                                                                                                                            )
                                                                                                    )
                                                                             ),
                                                      " f ",
                                                      JsStr.of("F")
                                                     );


        for (JsObj obj : Arrays.asList(mutable,
                                       immutable
                                      ))
        {
            final Predicate<JsPair> containsLetterD = p -> p.path.last()
                                                                 .isKey(name -> name.contains("d"));
            final JsObj mapped = obj.mapKeys_(p ->
                                              {
                                                  Assertions.assertEquals(p.elem,
                                                                          obj.get(p.path)
                                                                         );
                                                  return p.path.last()
                                                               .asKey().name.trim()
                                                                            .toUpperCase();
                                              },
                                              containsLetterD.negate()
                                             );

            Assertions.assertFalse(mapped.stream_()
                                         .filter(containsLetterD.negate())
                                         .anyMatch(it ->
                                                   {
                                                       final String name = it.path.last()
                                                                                  .asKey().name;
                                                       return name.contains(" ") || name.chars()
                                                                                        .mapToObj(Character::isLowerCase)
                                                                                        .findAny()
                                                                                        .orElse(false);
                                                   }));

        }
    }


    @Test
    public void test_filter_keys_immutable()
    {
        final Supplier<JsObj> obj = () -> Jsons.immutable.object.of("a",
                                                                    Jsons.immutable.object.of("a1",
                                                                                              JsStr.of("A"),
                                                                                              "b1",
                                                                                              JsStr.of("B")
                                                                                             ),
                                                                    "b2",
                                                                    JsStr.of("C"),
                                                                    "d",
                                                                    Jsons.immutable.object.of("d1",
                                                                                              JsStr.of("D"),
                                                                                              "e1",
                                                                                              Jsons.immutable.array.of(Jsons.immutable.object.of("f",
                                                                                                                                                 JsStr.of("F1")
                                                                                                                                                ),
                                                                                                                       TRUE,
                                                                                                                       Jsons.immutable.object.of("b3",
                                                                                                                                                 JsStr.of("G1"),
                                                                                                                                                 "c",
                                                                                                                                                 JsStr.of("G2")
                                                                                                                                                ),
                                                                                                                       FALSE
                                                                                                                      )
                                                                                             ),
                                                                    "b4",
                                                                    JsStr.of("F")
                                                                   );


        final JsObj obj1 = obj.get()
                              .filterKeys_(p ->
                                           {
                                               Assertions.assertEquals(p.elem,
                                                                       obj.get()
                                                                          .get(p.path)
                                                                      );
                                               return !p.path.last()
                                                             .isKey(name -> name.startsWith("b"));
                                           }
                                          );
        Assertions.assertFalse(obj1.stream_()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))));

        final JsObj obj2 = obj.get()
                              .filterKeys(p ->
                                          {
                                              Assertions.assertEquals(p.elem,
                                                                      obj.get()
                                                                         .get(p.path)
                                                                     );
                                              return !p.path.last()
                                                            .isKey(name -> name.startsWith("b"));
                                          }
                                         );
        Assertions.assertFalse(obj2.stream()
                                   .anyMatch(p -> p.path.last()
                                                        .isKey(name -> name.startsWith("b"))
                                            ));


    }

    @Test
    public void test_filter_keys_mutable()
    {


        final Supplier<JsObj> obj = () -> Jsons.mutable.object.of("a",
                                                                  Jsons.mutable.object.of("a1",
                                                                                          JsStr.of("A"),
                                                                                          "b1",
                                                                                          JsStr.of("B")
                                                                                         ),
                                                                  "b2",
                                                                  JsStr.of("C"),
                                                                  "d",
                                                                  Jsons.mutable.object.of("d1",
                                                                                          JsStr.of("D"),
                                                                                          "e1",
                                                                                          Jsons.mutable.array.of(Jsons.mutable.object.of("f",
                                                                                                                                         JsStr.of("F1")
                                                                                                                                        ),
                                                                                                                 Jsons.mutable.object.of("b3",
                                                                                                                                         JsStr.of("G1"),
                                                                                                                                         "c",
                                                                                                                                         JsStr.of("G2")
                                                                                                                                        )
                                                                                                                )
                                                                                         ),
                                                                  "b4",
                                                                  JsStr.of("F")
                                                                 );


        final JsObj copy = obj.get();
        final JsObj obj1 = copy.filterKeys_(p ->
                                            {
                                                Assertions.assertEquals(p.elem,
                                                                        copy.get(p.path)
                                                                       );
                                                return !p.path.last()
                                                              .isKey(key -> key.startsWith("b"));
                                            });
        Assertions.assertFalse(obj1.stream_()
                                   .map(p -> p.path.last()
                                                   .asKey().name.startsWith("b"))
                                   .findFirst()
                                   .orElse(false)
                              );
        Assertions.assertEquals(obj1,
                                copy
                               );

        final JsObj copy1 = obj.get();
        final JsObj obj2 = copy1.filterKeys(p ->
                                            {
                                                Assertions.assertEquals(p.elem,
                                                                        copy1.get(p.path)
                                                                       );
                                                return !p.path.last()
                                                              .asKey().name.startsWith("b");
                                            }
                                           );
        Assertions.assertFalse(obj2.stream()
                                   .map(p -> p.path.last()
                                                   .asKey().name.startsWith("b"))
                                   .findFirst()
                                   .orElse(false)
                              );
        Assertions.assertEquals(obj2,
                                copy1
                               );


    }

    @Test
    public void test_parse_string() throws MalformedJson
    {
        final JsObj mutable = Jsons.mutable.object.of("a",
                                                      Jsons.mutable.object.of("a1",
                                                                              JsStr.of("A"),
                                                                              "b1",
                                                                              JsStr.of("B")
                                                                             ),
                                                      "b2",
                                                      JsStr.of("C"),
                                                      "d",
                                                      Jsons.mutable.object.of("d1",
                                                                              JsStr.of("D"),
                                                                              "e1",
                                                                              Jsons.mutable.array.of(Jsons.mutable.object.of("f",
                                                                                                                             JsStr.of("F1")
                                                                                                                            ),
                                                                                                     Jsons.mutable.object.of("b3",
                                                                                                                             JsStr.of("G1"),
                                                                                                                             "c",
                                                                                                                             JsStr.of("G2")
                                                                                                                            )
                                                                                                    )
                                                                             ),
                                                      "b4",
                                                      JsStr.of("F")
                                                     );

        final String str = mutable.toString();

        Assertions.assertEquals(Jsons.immutable.object.toImmutable(mutable),
                                Jsons.immutable.object.parse(str)
                                                      .orElseThrow()
                               );
        Assertions.assertEquals(mutable,
                                Jsons.mutable.object.parse(str)
                                                    .orElseThrow()
                               );

        final TryObj tryObjImmutable = Jsons.immutable.object.parse(str,
                                                                    ParseBuilder.builder()
                                                                                .withKeyFilter(p -> !p.last()
                                                                                                      .isKey(it -> it.startsWith("b")))
                                                                   );

        final TryObj tryObjMutable = Jsons.mutable.object.parse(str,
                                                                ParseBuilder.builder()
                                                                            .withKeyFilter(p -> !p.last()
                                                                                                  .isKey(it -> it.startsWith("b")))
                                                               );

        for (JsObj obj : Arrays.asList(tryObjImmutable.orElseThrow(),
                                       tryObjMutable.orElseThrow()
                                      ))
        {

            Assertions.assertFalse(obj
                                   .stream_()
                                   .map(p -> p.path.last()
                                                   .asKey().name.startsWith("b"))
                                   .findFirst()
                                   .orElse(false)
                                  );

            Assertions.assertEquals(4,
                                    obj
                                    .size_()
                                   );
        }


    }


    @Test
    public void test_filter_jsons_from_immutable() throws MalformedJson
    {
        final JsObj obj = Jsons.immutable.object.of("a",
                                                    Jsons.immutable.object.of("R",
                                                                              JsInt.of(1)
                                                                             ),
                                                    "b",
                                                    Jsons.immutable.object.of("R",
                                                                              JsInt.of(1)
                                                                             ),
                                                    "c",
                                                    Jsons.immutable.array.of(Jsons.immutable.object.of("R",
                                                                                                       JsInt.of(1)
                                                                                                      ),
                                                                             Jsons.immutable.object.of("S",
                                                                                                       JsInt.of(1),
                                                                                                       "T",
                                                                                                       Jsons.immutable.object.of("R",
                                                                                                                                 JsInt.of(1)
                                                                                                                                )
                                                                                                      )
                                                                            ),
                                                    "d",
                                                    Jsons.immutable.object.of("d",
                                                                              JsInt.of(3)
                                                                             ),
                                                    "e",
                                                    NULL
                                                   );
        final JsObj result = obj.filterObjs((p, o) ->
                                            {
                                                Assertions.assertEquals(o,
                                                                        obj.get(p)
                                                                       );
                                                return !o.containsPath(JsPath.fromKey("R"));
                                            });
        final JsObj result1 = obj.filterObjs_((p, o) ->
                                              {
                                                  Assertions.assertEquals(o,
                                                                          obj.get(p)
                                                                         );
                                                  return !o.containsPath(JsPath.fromKey("R"));
                                              });


        Assertions.assertEquals(Jsons.immutable.object.parse("{\"c\":[{\"R\":1},{\"T\":{\"R\":1},\"S\":1}],\"d\":{\"d\":3},\"e\": null}")
                                                      .orElseThrow(),
                                result
                               );
        Assertions.assertEquals(Jsons.immutable.object.parse("{\"c\":[{\"S\":1}],\"d\":{\"d\":3},\"e\": null}")
                                                      .orElseThrow(),
                                result1
                               );
    }


    @Test
    public void test_filter_jsons_from_mutable() throws MalformedJson
    {
        final Supplier<JsObj> supplier = () -> Jsons.mutable.object.of("a",
                                                                       Jsons.mutable.object.of("R",
                                                                                               JsInt.of(1)
                                                                                              ),
                                                                       "b",
                                                                       Jsons.mutable.object.of("R",
                                                                                               JsInt.of(1)
                                                                                              ),
                                                                       "c",
                                                                       Jsons.mutable.array.of(Jsons.mutable.object.of("R",
                                                                                                                      JsInt.of(1)
                                                                                                                     ),
                                                                                              Jsons.mutable.object.of("S",
                                                                                                                      JsInt.of(1),
                                                                                                                      "T",
                                                                                                                      Jsons.mutable.object.of("R",
                                                                                                                                              JsInt.of(1)
                                                                                                                                             )
                                                                                                                     )
                                                                                             ),
                                                                       "d",
                                                                       Jsons.mutable.object.of("d",
                                                                                               JsInt.of(3)
                                                                                              )
                                                                      );
        final JsObj obj = Jsons.mutable.object.parse(supplier.get()
                                                             .toString())
                                              .orElseThrow();


        final JsObj result = obj.filterObjs((p, o) ->
                                            {
                                                Assertions.assertEquals(o,
                                                                        supplier.get()
                                                                                .get(p)
                                                                       );
                                                return !o.containsPath(JsPath.fromKey("R"));
                                            });
        final JsObj obj1 = Jsons.mutable.object.parse(supplier.get()
                                                              .toString())
                                               .orElseThrow();

        final JsObj result_ = obj1.filterObjs_((p, o) ->
                                               {
                                                   Assertions.assertEquals(o,
                                                                           supplier.get()
                                                                                   .get(p)
                                                                          );
                                                   return !o.containsPath(JsPath.fromKey("R"));
                                               });


        Assertions.assertEquals(Jsons.mutable.object.parse("{\"c\":[{\"R\":1},{\"T\":{\"R\":1},\"S\":1}],\"d\":{\"d\":3}}")
                                                    .orElseThrow(),
                                result
                               );
        Assertions.assertEquals(result,
                                obj
                               );
        Assertions.assertEquals(Jsons.mutable.object.parse("{\"c\":[{\"S\":1}],\"d\":{\"d\":3}}")
                                                    .orElseThrow(),
                                result_
                               );
        Assertions.assertEquals(result_,
                                obj1
                               );


    }

    @Test
    public void test_filter_values_from_mutable_object() throws MalformedJson
    {

        Supplier<JsObj> supplier = () -> Jsons.mutable.object.of("a",
                                                                 JsInt.of(1),
                                                                 "b",
                                                                 NULL,
                                                                 "c",
                                                                 Jsons.mutable.array.of(JsInt.of(1),
                                                                                        NULL,
                                                                                        JsInt.of(2),
                                                                                        NULL,
                                                                                        JsInt.of(3),
                                                                                        Jsons.mutable.array.of(JsInt.of(1),
                                                                                                               NULL,
                                                                                                               JsInt.of(2)
                                                                                                              ),
                                                                                        Jsons.mutable.object.of("a",
                                                                                                                NULL,
                                                                                                                "b",
                                                                                                                JsInt.of(1)
                                                                                                               )
                                                                                       )
                                                                );
        final JsObj obj = supplier.get();
        obj.filterElems_(p ->
                         {
                             Assertions.assertEquals(p.elem,
                                                     supplier.get()
                                                             .get(p.path)
                                                    );
                             return p.elem.isNotNull();
                         });

        Assertions.assertEquals(Jsons.mutable.object.parse("{\"a\":1,\"c\":[1,2,3,[1,2],{\"b\":1}]}\n")
                                                    .orElseThrow(),
                                obj
                               );

        final JsObj obj1 = supplier.get();
        obj1.filterElems(p ->
                         {
                             Assertions.assertEquals(p.elem,
                                                     supplier.get()
                                                             .get(p.path)
                                                    );
                             return p.elem.isNotNull();
                         });

        Assertions.assertEquals(Jsons.mutable.object.parse("{\"a\":1,\"c\":[1,null,2,null,3,[1,null,2],{\"a\":null,\"b\":1}]}")
                                                    .orElseThrow(),
                                obj1
                               );

    }

    @Test
    public void test_filter_values_from_immutable_object() throws MalformedJson
    {

        JsObj obj = Jsons.immutable.object.of("a",
                                              JsInt.of(1),
                                              "b",
                                              NULL,
                                              "c",
                                              Jsons.immutable.array.of(JsInt.of(1),
                                                                       NULL,
                                                                       JsInt.of(2),
                                                                       NULL,
                                                                       JsInt.of(3),
                                                                       Jsons.immutable.array.of(JsInt.of(1),
                                                                                                NULL,
                                                                                                JsInt.of(2)
                                                                                               ),
                                                                       Jsons.immutable.object.of("a",
                                                                                                 NULL,
                                                                                                 "b",
                                                                                                 JsInt.of(1)
                                                                                                )
                                                                      )
                                             );

        final JsObj obj1 = obj.filterElems_(p -> p.elem.isNotNull());

        Assertions.assertEquals(Jsons.immutable.object.parse("{\"a\":1,\"c\":[1,2,3,[1,2],{\"b\":1}]}\n")
                                                      .orElseThrow(),
                                obj1
                               );

    }

    @Test
    public void test_put_and_get()
    {
        final JsObj empty = Jsons.immutable.object.empty();
        final JsObj a = empty.put(JsPath.fromKey("a"),
                                  JsBigDec.of(BigDecimal.valueOf(0.1d))
                                 );

        Assertions.assertEquals(OptionalDouble.of(0.1d),
                                a.getDouble(fromKey("a"))
                               );
        Assertions.assertEquals(Optional.of(BigDecimal.valueOf(0.1d)),
                                a.getBigDecimal(fromKey("a"))
                               );
    }

    @Test
    public void test_map_json_obj_mutable()
    {
        Supplier<JsObj> supp = () -> Jsons.mutable.object.of("a",
                                                             Jsons.mutable.object.empty(),
                                                             "b",
                                                             Jsons.mutable.object.empty(),
                                                             "c",
                                                             Jsons.mutable.object.empty(),
                                                             "d",
                                                             Jsons.mutable.object.of("a",
                                                                                     Jsons.mutable.object.empty(),
                                                                                     "b",
                                                                                     Jsons.mutable.array.of(Jsons.mutable.object.empty(),
                                                                                                            Jsons.mutable.object.empty(),
                                                                                                            Jsons.mutable.array.of(Jsons.mutable.object.empty(),
                                                                                                                                   NULL,
                                                                                                                                   Jsons.mutable.object.empty()
                                                                                                                                  )
                                                                                                           )
                                                             ,
                                                                                     "c",
                                                                                     Jsons.mutable.object.of("a",
                                                                                                             TRUE
                                                                                                            )
                                                                                    )

                                                            );

        final JsObj obj = supp.get();

        obj.mapObjs_((p, o) -> o.put(JsPath.fromKey("path"),
                                     p.toString()
                                    ),
                     (p, o) -> o.isEmpty()
                    );
        supp.get()
            .stream_()
            .filter(p -> p.elem.isJson())
            .forEach(it ->
                     {
                         final JsPath path = it.path;
                         final JsObj o = ((JsObj) it.elem);
                         if (o.isEmpty()) Assertions.assertEquals(Optional.of(path.toString()),
                                                                  obj.getStr(path.append(fromKey("path")))

                                                                 );


                     });
    }

    @Test
    public void test_parse_with_options() throws MalformedJson
    {
        Supplier<JsObj> supplier = () -> Jsons.mutable.object.of("a",
                                                                 JsStr.of("1"),
                                                                 "b",
                                                                 JsInt.of(1),
                                                                 "c",
                                                                 Jsons.mutable.object.of("a",
                                                                                         TRUE,
                                                                                         "b",
                                                                                         Jsons.mutable.array.of(NULL,
                                                                                                                FALSE,
                                                                                                                JsInt.of(1),
                                                                                                                JsStr.of("A"),
                                                                                                                JsBigDec.of(BigDecimal.ONE),
                                                                                                                JsDouble.of(1.5d)
                                                                                                               )
                                                                                        ),
                                                                 "d",
                                                                 NULL,
                                                                 "e",
                                                                 TRUE,
                                                                 "f",
                                                                 FALSE
                                                                );
        Assertions.assertEquals(supplier.get()
                                        .filterElems_(p -> p.elem.isNotNull()),
                                Jsons.mutable.object.parse(supplier.get()
                                                                   .toString(),
                                                           ParseBuilder.builder()
                                                                       .withElemFilter(p -> p.elem.isNotNull())
                                                          )
                                                    .orElseThrow()
                               );
    }

    @Test
    public void test_operations()
    {

        JsObj obj = Jsons.immutable.object.of(JsPair.of(path("/a/b/c"),
                                                        JsInt.of(1)
                                                       ),
                                              JsPair.of(path("/a/b/d"),
                                                        JsInt.of(2)
                                                       )
                                             );

        Assertions.assertEquals(obj.appendAll(JsPath.fromIndex(0),
                                              Jsons.immutable.array.of(1,
                                                                       2
                                                                      )
                                             ),
                                obj
                               );

        Assertions.assertEquals(obj.prependAll(JsPath.fromIndex(0),
                                               Jsons.immutable.array.of(1,
                                                                        2
                                                                       )
                                              ),
                                obj
                               );
        Assertions.assertEquals(NOTHING,
                                obj.get(JsPath.fromIndex(0))
                               );
        Assertions.assertEquals(obj,
                                obj.put(JsPath.fromIndex(0),
                                        a -> NULL
                                       )
                               );

        Assertions.assertEquals(obj,
                                obj.remove(path("/a/0"))
                               );

        Assertions.assertEquals(obj,
                                obj.remove(JsPath.fromIndex(0))
                               );

        Assertions.assertEquals(obj,
                                obj.remove(path("/a/b/c/d"))
                               );

        Assertions.assertEquals(obj,
                                obj.remove(path("/a/b/c/0"))
                               );
    }


    @Test
    public void test_error_when_mixing_implementations()
    {

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.object.of("a",
                                                              Jsons.immutable.object.of("a",
                                                                                        NULL
                                                                                       )
                                                             )
                               );
        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.array.of(NULL,
                                                             TRUE,
                                                             FALSE,
                                                             Jsons.immutable.object.of("a",
                                                                                       NULL
                                                                                      )
                                                            )
                               );
        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.object.of("a",
                                                                Jsons.mutable.object.of("a",
                                                                                        NULL
                                                                                       )
                                                               )
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.of(NULL,
                                                               TRUE,
                                                               FALSE,
                                                               Jsons.mutable.object.of("a",
                                                                                       NULL
                                                                                      )
                                                              )
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.array.ofIterable(Arrays.asList(Jsons.immutable.array.of(1,
                                                                                                            2
                                                                                                           ))
                                                                    )
                               );
    }


    @Test
    public void test_parse_into_immutable() throws MalformedJson
    {
        JsObj obj = Jsons.immutable.object.of(JsPair.of(path("/a/b/0"),
                                                        NULL
                                                       ),
                                              JsPair.of(path("/a/b/1"),
                                                        TRUE
                                                       ),
                                              JsPair.of(path("/a/b/c"),
                                                        FALSE
                                                       ),
                                              JsPair.of(path("/a/b/c/d"),
                                                        JsInt.of(1)
                                                       ),
                                              JsPair.of(path("/a/a/a/"),
                                                        JsStr.of("a")
                                                       ),
                                              JsPair.of(path("/a/b/0"),
                                                        JsBigDec.of(BigDecimal.ONE)
                                                       )
                                             );

        Assertions.assertEquals(obj,
                                Jsons.immutable.object.parse(obj.toString())
                                                      .orElseThrow()
                               );

        Assertions.assertEquals(obj,
                                Jsons.immutable.parse(obj.toString())
                                               .objOrElseThrow()
                               );

    }

    @Test
    public void test_parse_into_mutable() throws MalformedJson
    {
        JsObj obj = Jsons.mutable.object.of(JsPair.of(path("/a/b/0"),
                                                      NULL
                                                     ),
                                            JsPair.of(path("/a/b/1"),
                                                      TRUE
                                                     ),
                                            JsPair.of(path("/a/b/c"),
                                                      FALSE
                                                     ),
                                            JsPair.of(path("/a/b/c/d"),
                                                      JsInt.of(1)
                                                     ),
                                            JsPair.of(path("/a/a/a"),
                                                      JsStr.of("a")
                                                     ),
                                            JsPair.of(path("/a/b/0"),
                                                      JsBigDec.of(BigDecimal.ONE)
                                                     )
                                           );

        Assertions.assertEquals(obj,
                                Jsons.mutable.object.parse(obj.toString())
                                                    .orElseThrow()
                               );

        Assertions.assertEquals(obj,
                                Jsons.mutable.parse(obj.toString())
                                             .objOrElseThrow()
                               );

    }

    @Test
    public void test_map_json_mutable_obj()
    {

        Supplier<JsObj> supplier = () -> Jsons.mutable.object.of("a",
                                                                 Jsons.mutable.object.empty(),
                                                                 "b",
                                                                 JsStr.of("B"),
                                                                 "c",
                                                                 Jsons.mutable.object.of("a",
                                                                                         JsStr.of("a"),
                                                                                         "b",
                                                                                         JsInt.of(1),
                                                                                         "e",
                                                                                         Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                                                        JsStr.of("a"),
                                                                                                                                        "b",
                                                                                                                                        JsInt.of(1)
                                                                                                                                       ),
                                                                                                                NULL,
                                                                                                                TRUE
                                                                                                               ),
                                                                                         "h",
                                                                                         Jsons.mutable.array.of(Jsons.mutable.object.of("c",
                                                                                                                                        JsStr.of("C"),
                                                                                                                                        "d",
                                                                                                                                        JsStr.of("D")
                                                                                                                                       ),
                                                                                                                Jsons.mutable.object.of("d",
                                                                                                                                        JsStr.of("D"),
                                                                                                                                        "e",
                                                                                                                                        JsStr.of("E"),
                                                                                                                                        "f",
                                                                                                                                        Jsons.mutable.object.of("g",
                                                                                                                                                                JsStr.of("G"),
                                                                                                                                                                "h",
                                                                                                                                                                JsStr.of("H")
                                                                                                                                                               )
                                                                                                                                       )
                                                                                                               )
                                                                                        )
                                                                );


        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put(JsPath.fromKey("size"),
                                                                                    json.size()
                                                                                   );
        final JsObj obj = supplier.get();
        final JsObj newObj = obj.mapObjs_((p, o) ->
                                          {
                                              Assertions.assertEquals(o,
                                                                      supplier.get()
                                                                              .get(p)
                                                                     );
                                              return addSizeFn.apply(p,
                                                                     o
                                                                    );
                                          },
                                          (p, o) -> o.isNotEmpty()
                                         );

        Assertions.assertEquals(obj,
                                newObj
                               );

        final JsObj obj1 = supplier.get();
        final JsObj newObj1 = obj1.mapObjs((p, o) ->
                                           {
                                               Assertions.assertEquals(o,
                                                                       supplier.get()
                                                                               .get(p)
                                                                      );
                                               return addSizeFn.apply(p,
                                                                      o
                                                                     );
                                           },
                                           (p, o) -> o.isNotEmpty()
                                          );
        Assertions.assertEquals(obj1,
                                newObj1
                               );


    }


    @Test
    public void test_map_values_mutable() throws MalformedJson
    {

        Supplier<JsObj> supplier = () -> Jsons.mutable.object.of("a",
                                                                 JsInt.of(1),
                                                                 "b",
                                                                 NULL,
                                                                 "c",
                                                                 JsInt.of(3),
                                                                 "d",
                                                                 FALSE,
                                                                 "e",
                                                                 Jsons.mutable.array.of(NULL,
                                                                                        Jsons.mutable.array.of(1,
                                                                                                               2,
                                                                                                               3
                                                                                                              ),
                                                                                        JsInt.of(1),
                                                                                        Jsons.mutable.object.of("a",
                                                                                                                JsInt.of(1),
                                                                                                                "b",
                                                                                                                JsInt.of(2)
                                                                                                               ),
                                                                                        NULL,
                                                                                        Jsons.mutable.array.of(JsInt.of(1),
                                                                                                               JsStr.of("a"),
                                                                                                               TRUE
                                                                                                              )
                                                                                       )
                                                                );

        final JsObj obj = supplier.get();
        obj.mapElems(pair ->
                     {
                         Assertions.assertEquals(pair.elem,
                                                 supplier.get()
                                                         .get(pair.path)
                                                );
                         return pair.mapIfInt(i -> i + 10).elem;
                     },
                     p -> p.elem.isInt()
                    );


        Assertions.assertEquals(Jsons.mutable.object.parse("{\"a\":11,\"b\":null,\"c\":13,\"d\":false,\"e\":[null,[1,2,3],1,{\"a\":1,\"b\":2},null,[1,\"a\",true]]}\n")
                                                    .orElseThrow(),
                                obj
                               );

        final JsObj obj1 = supplier.get();
        obj1.mapElems_(pair ->
                       {
                           Assertions.assertEquals(pair.elem,
                                                   supplier.get()
                                                           .get(pair.path)
                                                  );
                           return pair.mapIfInt(i -> i + 10).elem;

                       },
                       p -> p.elem.isInt()
                      );

        Assertions.assertEquals(Jsons.mutable.object.parse("{\"a\":11,\"b\":null,\"c\":13,\"d\":false,\"e\":[null,[11,12,13],11,{\"a\":11,\"b\":12},null,[11,\"a\",true]]}\n")
                                                    .orElseThrow(),
                                obj1
                               );
    }

    @Test
    public void test_equals()
    {

        JsObj obj = Jsons.immutable.object.of("a",
                                              Jsons.immutable.object.of("b",
                                                                        Jsons.immutable.array.of(1,
                                                                                                 2,
                                                                                                 3
                                                                                                )
                                                                       ),
                                              "b",
                                              Jsons.immutable.array.of("a",
                                                                       "b",
                                                                       "c"
                                                                      )
                                             );

        JsObj obj2 = Jsons.immutable.object.of("a",
                                               Jsons.immutable.object.of("b",
                                                                         Jsons.immutable.array.of(1,
                                                                                                  2,
                                                                                                  3,
                                                                                                  1,
                                                                                                  2,
                                                                                                  3
                                                                                                 )
                                                                        ),
                                               "b",
                                               Jsons.immutable.array.of("a",
                                                                        "b",
                                                                        "c",
                                                                        "a",
                                                                        "b",
                                                                        "c"
                                                                       )
                                              );
        JsObj obj3 = Jsons.immutable.object.of("a",
                                               Jsons.immutable.object.of("b",
                                                                         Jsons.immutable.array.of(3,
                                                                                                  2,
                                                                                                  1,
                                                                                                  1,
                                                                                                  2,
                                                                                                  3
                                                                                                 )
                                                                        ),
                                               "b",
                                               Jsons.immutable.array.of("c",
                                                                        "b",
                                                                        "a",
                                                                        "a",
                                                                        "b",
                                                                        "c"
                                                                       )
                                              );
        Assertions.assertTrue(obj.equals(obj,
                                         LIST
                                        )
                             );

        Assertions.assertFalse(obj.equals(obj2,
                                          LIST
                                         )
                              );
        Assertions.assertTrue(obj2.equals(obj3,
                                          SET
                                         )
                             );
        Assertions.assertTrue(obj2.equals(obj3,
                                          MULTISET
                                         )
                             );
        Assertions.assertTrue(obj.equals(obj2,
                                         SET
                                        )
                             );
        Assertions.assertTrue(obj2.equals(obj2,
                                          LIST
                                         )
                             );
        Assertions.assertTrue(obj3.equals(obj3,
                                          LIST
                                         )
                             );
    }

    @Test
    public void test_map_json_immutable_obj() throws MalformedJson
    {
        JsObj obj = Jsons.immutable.object.of("a",
                                              JsStr.of("A"),
                                              "b",
                                              Jsons.immutable.object.empty(),
                                              "c",
                                              Jsons.immutable.array.empty(),
                                              "h",
                                              Jsons.immutable.array.of(Jsons.immutable.object.of("c",
                                                                                                 JsStr.of("C"),
                                                                                                 "d",
                                                                                                 JsStr.of("D")
                                                                                                ),
                                                                       NULL,
                                                                       Jsons.immutable.object.of("d",
                                                                                                 JsStr.of("D"),
                                                                                                 "e",
                                                                                                 JsStr.of("E"),
                                                                                                 "f",
                                                                                                 Jsons.immutable.object.of("g",
                                                                                                                           JsStr.of("G"),
                                                                                                                           "h",
                                                                                                                           JsStr.of("H")
                                                                                                                          )
                                                                                                )
                                                                      )
                                             );


        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put(JsPath.fromKey("size"),
                                                                                    json.size()
                                                                                   );
        final JsObj newObj = obj.mapObjs_((p, o) ->
                                          {
                                              Assertions.assertEquals(o,
                                                                      obj.get(p)
                                                                     );
                                              return addSizeFn.apply(p,
                                                                     o
                                                                    );
                                          },
                                          (p, o) -> o.isNotEmpty()
                                         );

        Assertions.assertNotEquals(obj,
                                   newObj
                                  );

        Assertions.assertEquals(Jsons.immutable.parse("{\"a\":\"A\",\"b\":{},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}\n")
                                               .objOrElseThrow(),
                                newObj
                               );

        Assertions.assertEquals(Jsons.immutable.object.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"size\":2,\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"size\":3,\"f\":{\"size\":2,\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                                                      .orElseThrow(),
                                obj.mapObjs_((p, o) ->
                                             {

                                                 Assertions.assertEquals(o,
                                                                         obj.get(p)
                                                                        );
                                                 return addSizeFn.apply(p,
                                                                        o
                                                                       );
                                             }
                                            )
                               );

        Assertions.assertEquals(Jsons.immutable.object.parse("{\"a\":\"A\",\"b\":{\"size\":0},\"c\":[],\"h\":[{\"c\":\"C\",\"d\":\"D\"},null,{\"e\":\"E\",\"f\":{\"g\":\"G\",\"h\":\"H\"},\"d\":\"D\"}]}")
                                                      .orElseThrow(),
                                obj.mapObjs((p, o) ->
                                            {

                                                Assertions.assertEquals(o,
                                                                        obj.get(p)
                                                                       );
                                                return addSizeFn.apply(p,
                                                                       o
                                                                      );
                                            }
                                           )
                               );

        JsObj obj1 = Jsons.immutable.object.of("a",
                                               Jsons.immutable.object.of("b",
                                                                         JsStr.of("B"),
                                                                         "c",
                                                                         JsStr.of("C")
                                                                        ),
                                               "b",
                                               Jsons.immutable.object.empty(),
                                               "c",
                                               Jsons.immutable.array.empty(),
                                               "d",
                                               Jsons.immutable.object.of("e",
                                                                         JsStr.of("E"),
                                                                         "f",
                                                                         JsStr.of("F"),
                                                                         "g",
                                                                         Jsons.immutable.object.of("h",
                                                                                                   JsStr.of("H"),
                                                                                                   "i",
                                                                                                   JsStr.of("I")
                                                                                                  )
                                                                        )
                                              );

        final JsObj newObj1 = obj1.mapObjs((p, o) ->
                                           {
                                               Assertions.assertEquals(o,
                                                                       obj1.get(p)
                                                                      );
                                               return addSizeFn.apply(p,
                                                                      o
                                                                     );
                                           },
                                           (p, o) -> o.isNotEmpty()
                                          );


        Assertions.assertEquals(Jsons.immutable.parse("{\"a\":{\"size\":2,\"b\":\"B\",\"c\":\"C\"},\"b\":{},\"c\":[],\"d\":{\"e\":\"E\",\"size\":3,\"f\":\"F\",\"g\":{\"i\":\"I\",\"h\":\"H\"}}}\n")
                                               .objOrElseThrow(),
                                newObj1
                               );

    }

    @Test
    public void test_throws_malformed_json_error()
    {
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": 1,[]}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": 1,\"b\"{}}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": 1,\"b\": {}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": 1,\"b\": [1,2]")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": 1,\"b\": [1,2,{2: 1}]}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": tv}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": tra}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": truf}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": trued}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": f1}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": fae}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": falf}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": falsi}")
                                                            .orElseThrow()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": falsea}")
                                                            .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": \"\\u-0026\"}")
                                                            .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": -12a}")
                                                            .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": \"\\x\"}")
                                                            .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": nall}")
                                                            .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": nuxx}")
                                                            .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": nulx}")
                                                            .orElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.object.parse("{\"a\": nullx}")
                                                            .orElseThrow()
                               );


    }

    @Test
    public void test_contains_element_in_object()
    {
        final JsObj obj = Jsons.immutable.object.of("a",
                                                    JsInt.of(1),
                                                    "b",
                                                    Jsons.immutable.array.of(JsInt.of(2),
                                                                             Jsons.immutable.object.of("a",
                                                                                                       NULL
                                                                                                      )
                                                                            )
                                                   );
        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, JsElem> next = iterator.next();
            Assertions.assertTrue(obj.containsElem(next.getValue()));
        }
        final JsObj _obj_ = Jsons.mutable.object.of("a",
                                                    JsInt.of(1),
                                                    "b",
                                                    Jsons.mutable.array.of(JsInt.of(2),
                                                                           Jsons.mutable.object.of("a",
                                                                                                   NULL
                                                                                                  )
                                                                          )
                                                   );

        final Iterator<Map.Entry<String, JsElem>> iterator1 = _obj_.iterator();
        while (iterator1.hasNext())
        {
            Map.Entry<String, JsElem> next = iterator1.next();
            Assertions.assertTrue(_obj_.containsElem(next.getValue()));

        }
        Assertions.assertTrue(obj.containsElem(JsInt.of(1)));
        Assertions.assertTrue(_obj_.containsElem(JsInt.of(1)));
        Assertions.assertFalse(obj.containsElem(JsInt.of(2)));
        Assertions.assertFalse(_obj_.containsElem(JsInt.of(2)));
        Assertions.assertTrue(obj.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(_obj_.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(obj.containsElem_(NULL));
        Assertions.assertTrue(_obj_.containsElem_(NULL));
        Assertions.assertTrue(obj.containsPath(path("/b/0")));
        Assertions.assertTrue(_obj_.containsPath(path("/b/1/a")));
        Assertions.assertTrue(obj.containsPath(JsPath.fromKey("a")));
        Assertions.assertTrue(_obj_.containsPath(JsPath.fromKey("b")));
        Assertions.assertFalse(obj.containsPath(JsPath.fromKey("3")));
        Assertions.assertFalse(_obj_.containsPath(JsPath.fromKey("3")));
        Assertions.assertFalse(obj.containsPath(path("/1/b")));
        Assertions.assertFalse(_obj_.containsPath(path("/1/b")));

    }


    @Test
    public void test_add_element_into_immutable_object_with_errors()
    {


        final UserError error1 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.object.empty()
                                                                                     .add(JsPath.path("/a/0"),
                                                                                          JsStr.of("hi")
                                                                                         )
                                                        );

        Assertions.assertEquals("Parent not found at /a while applying add in {}. Suggestion: either check if the parent exists or call the put method, which always does the insertion.",
                                error1.getMessage()
                               );


        final UserError error2 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.object.of("a",
                                                                                         Jsons.immutable.array.of(1)
                                                                                        )
                                                                                     .add(JsPath.path("/a/b"),
                                                                                          JsStr.of("hi")
                                                                                         )
                                                        );

        Assertions.assertEquals("Trying to add the key 'b' in an array. add operation can not be applied in {\"a\":[1]} at /a/b. Suggestion: call get(path).isObj() before.",
                                error2.getMessage()
                               );

        final UserError error3 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.object.of("a",
                                                                                         Jsons.immutable.object.of("a",
                                                                                                                   JsInt.of(1)
                                                                                                                  )
                                                                                        )
                                                                                     .add(JsPath.path("/a/0"),
                                                                                          JsStr.of("hi")
                                                                                         )
                                                        );

        Assertions.assertEquals("Trying to add at the index '0' in an object. add operation can not be applied in {\"a\":{\"a\":1}} at /a/0. Suggestion: call get(path).isArray() before.",
                                error3.getMessage()
                               );

        final UserError error4 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.object.of("a",
                                                                                         JsStr.of("a")
                                                                                        )
                                                                                     .add(JsPath.path("/a/b"),
                                                                                          JsStr.of("hi")
                                                                                         )
                                                        );

        Assertions.assertEquals("Element located at '/a' is not a Json. add operation can not be applied in {\"a\":\"a\"} at /a/b. Suggestion: call get(path).isJson() before.",
                                error4.getMessage()
                               );


    }

    @Test
    public void test_add_element_into_mutable_object_with_errors()
    {


        final UserError error1 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.object.empty()
                                                                                   .add(JsPath.path("/a/0"),
                                                                                        JsStr.of("hi")
                                                                                       )
                                                        );

        Assertions.assertEquals("Parent not found at /a while applying add in {}. Suggestion: either check if the parent exists or call the put method, which always does the insertion.",
                                error1.getMessage()
                               );


        final UserError error2 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.object.of("a",
                                                                                       Jsons.mutable.array.of(1)
                                                                                      )
                                                                                   .add(JsPath.path("/a/b"),
                                                                                        JsStr.of("hi")
                                                                                       )
                                                        );

        Assertions.assertEquals("Trying to add the key 'b' in an array. add operation can not be applied in {\"a\":[1]} at /a/b. Suggestion: call get(path).isObj() before.",
                                error2.getMessage()
                               );

        final UserError error3 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.object.of("a",
                                                                                       Jsons.mutable.object.of("a",
                                                                                                               JsInt.of(1)
                                                                                                              )
                                                                                      )
                                                                                   .add(JsPath.path("/a/0"),
                                                                                        JsStr.of("hi")
                                                                                       )
                                                        );

        Assertions.assertEquals("Trying to add at the index '0' in an object. add operation can not be applied in {\"a\":{\"a\":1}} at /a/0. Suggestion: call get(path).isArray() before.",
                                error3.getMessage()
                               );

        final UserError error4 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.object.of("a",
                                                                                       JsStr.of("a")
                                                                                      )
                                                                                   .add(JsPath.path("/a/b"),
                                                                                        JsStr.of("hi")
                                                                                       )
                                                        );

        Assertions.assertEquals("Element located at '/a' is not a Json. add operation can not be applied in {\"a\":\"a\"} at /a/b. Suggestion: call get(path).isJson() before.",
                                error4.getMessage()
                               );


    }

    @Test
    public void test_add_element_into_immutable_json_recursively()
    {

        JsObj obj = Jsons.immutable.object.of("a",
                                              Jsons.immutable.object.of("b",
                                                                        Jsons.immutable.array.of(1,
                                                                                                 2,
                                                                                                 3
                                                                                                ),
                                                                        "c",
                                                                        Jsons.immutable.object.of("d",
                                                                                                  TRUE
                                                                                                 )
                                                                       )
                                             );

        Assertions.assertEquals(Jsons.immutable.array.of(1,
                                                         2,
                                                         3,
                                                         4
                                                        ),
                                obj.add(path("/a/b/-1"),
                                        JsInt.of(4)
                                       )
                                   .get(JsPath.path("/a/b"))
                               );

        Assertions.assertEquals(Jsons.immutable.array.of(1,
                                                         2,
                                                         3,
                                                         4
                                                        ),
                                obj.add(path("/a/b/3"),
                                        JsInt.of(4)
                                       )
                                   .get(JsPath.path("/a/b"))
                               );

        Assertions.assertEquals(Jsons.immutable.array.of(4,
                                                         1,
                                                         2,
                                                         3
                                                        ),
                                obj.add(path("/a/b/0"),
                                        JsInt.of(4)
                                       )
                                   .get(JsPath.path("/a/b"))
                               );


        Assertions.assertEquals(FALSE,
                                obj.add(path("/a/c/d"),
                                        e -> e.asJsBool()
                                              .negate()
                                       )
                                   .get(JsPath.path("/a/c/d"))
                               );

        Assertions.assertEquals(JsStr.of("bye!"),
                                obj.add(path("/a/c/e"),
                                        JsStr.of("bye!")
                                       )
                                   .get(JsPath.path("/a/c/e"))
                               );

    }


    @Test
    public void test_malformed_json() throws MalformedJson
    {
        final MalformedJson malformedJson = Assertions.assertThrows(MalformedJson.class,
                                                                    () -> Jsons.immutable.object.parse("")
                                                                                                .orElseThrow()
                                                                   );

        Assertions.assertEquals("Invalid token=EOF at (line no=1, column no=0, offset=-1). Expected tokens are: [CURLYOPEN, SQUAREOPEN]",
                                malformedJson.getMessage()
                               );


        final MalformedJson malformedJson1 = Assertions.assertThrows(MalformedJson.class,
                                                                     () -> Jsons.immutable.object.parse("{]")
                                                                                                 .orElseThrow()
                                                                    );

        Assertions.assertEquals("Unexpected char 93 at (line no=1, column no=2, offset=1), expecting '\"' or '}'",
                                malformedJson1.getMessage()
                               );


    }

}
