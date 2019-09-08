import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static jsonvalues.JsArray.TYPE.*;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

public class TestJsArray
{

    @Test
    public void test_creation_mutable_one_element_json_array()
    {
        JsArray arr = Jsons.mutable.array.of(NULL);
        arr.prepend(JsStr.of("a"));

        Assertions.assertEquals(JsStr.of("a"),
                                arr.head()

                               );

        Assertions.assertEquals(NULL,
                                arr.tail()
                                   .head()
                               );
    }

    @Test
    public void test_creation_mutable_two_element_json_array()
    {
        Supplier<JsArray> supplier = () -> Jsons.mutable.array.of(JsInt.of(1),
                                                                  TRUE,
                                                                  JsInt.of(2),
                                                                  JsStr.of("a"),
                                                                  NULL,
                                                                  Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                                 JsInt.of(1),
                                                                                                                 "b",
                                                                                                                 NULL,
                                                                                                                 "c",
                                                                                                                 JsInt.of(3)
                                                                                                                ))
                                                                 );


        final int result = supplier.get()
                                   .mapElems(p -> p.mapIfInt(i -> i + 100)
                                             .elem
                                            )
                                   .reduce(Integer::sum,
                                           pair -> pair.elem.asJsInt().x,
                                           pair -> pair.elem.isInt()
                                          )
                                   .orElse(-1);


        Assertions.assertEquals(203,
                                result
                               );

        final int result1 = supplier.get()
                                    .mapElems(p -> p.mapIfInt(i -> i + 100).elem,
                                              p -> p.elem.isInt(i -> i > 1)
                                             )
                                    .reduce(Integer::sum,
                                            pair -> pair.elem.asJsInt().x,
                                            p -> p.elem.isInt()
                                           )
                                    .orElse(-1);

        Assertions.assertEquals(103,
                                result1

                               );

        final int result3 = supplier.get()
                                    .mapElems_(p -> p.elem.asJsInt()
                                                          .map(i -> i + 100),
                                               p -> p.elem.isInt()
                                              )
                                    .reduce_(Integer::sum,
                                             pair -> pair.elem.asJsInt().x,
                                             pair -> pair.elem.isInt()
                                            )
                                    .orElse(-1);


        Assertions.assertEquals(407,
                                result3
                               );

    }

    @Test
    public void test_creation_immutable_two_element_json_array()
    {
        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsInt.of(2),
                                               JsStr.of("a"),
                                               NULL,
                                               JsStr.of("h"),
                                               Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         NULL
                                                                        )
                                              );


        final int result = arr.mapElems(p -> p.mapIfInt(i -> i + 100).elem
                                       )
                              .reduce(Integer::sum,
                                      pair -> pair.elem.asJsInt().x,
                                      p -> p.elem.isInt()
                                     )
                              .orElse(-1);

        final int result1 = arr.mapElems(p -> p.elem.asJsInt()
                                                    .map(i -> i + 100),
                                         p -> p.elem.isInt()
                                        )
                               .reduce(Integer::sum,
                                       pair -> pair.elem.asJsInt().x,
                                       p -> p.elem.isInt()
                                      )
                               .orElse(-1);


        Assertions.assertEquals(203,
                                result
                               );

        Assertions.assertEquals(203,
                                result1
                               );


        final int result2 = arr.mapElems_(p -> p.mapIfInt(i -> i + 100).elem
                                         )
                               .reduce_(Integer::sum,
                                        pair -> pair.elem.asJsInt().x,
                                        p -> p.elem.isInt()
                                       )
                               .orElse(-1);

        final int result3 = arr.mapElems_(p -> p.elem.asJsInt()
                                                     .map(i -> i + 100),
                                          p -> p.elem.isInt()
                                         )
                               .reduce_(Integer::sum,
                                        pair -> pair.elem.asJsInt().x,
                                        p -> p.elem.isInt()
                                       )
                               .orElse(-1);
        Assertions.assertEquals(406,
                                result2

                               );
        Assertions.assertEquals(406,
                                result3
                               );
    }

    @Test
    public void test_creation_mutable_three_element_json_array()
    {
        JsArray arr = Jsons.mutable.array.of(JsStr.of("a"),
                                             JsStr.of("b"),
                                             JsStr.of("c")
                                            );
        Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            JsElem elem = iterator.next();
            if (elem.isStr(s -> s.equals("b"))) iterator.remove();
        }

        Assertions.assertEquals(arr,
                                Jsons.mutable.array.of("a",
                                                       "c"
                                                      )
                               );

    }

    @Test
    public void test_creation_mutable_four_element_json_array()
    {
        JsArray arr = Jsons.mutable.array.of(JsLong.of(10),
                                             JsStr.of("b"),
                                             JsStr.of("c"),
                                             JsInt.of(10)
                                            );

        arr.filterElems(p -> p.elem.isIntegral());


        Assertions.assertTrue(arr.stream_()
                                 .allMatch(p -> p.elem.isIntegral())
                             );
    }

    @Test
    public void test_creation_mutable_five_element_json_array()
    {
        JsArray arr = Jsons.mutable.array.of(Jsons.mutable.array.of(NULL,
                                                                    TRUE
                                                                   ),
                                             JsStr.of("A"),
                                             JsStr.of("B"),
                                             JsInt.of(1),
                                             JsStr.of("C"),
                                             JsStr.of("D"),
                                             JsStr.of("E"),
                                             Jsons.mutable.object.of("a",
                                                                     NULL
                                                                    )
                                            );
        arr.mapElems(p -> p.mapIfStr(String::toLowerCase).elem); // arr is mutable

        // ["a","b","c","d","e"]
        Assertions.assertEquals(arr,
                                Jsons.mutable.array.of(Jsons.mutable.array.of(NULL,
                                                                              TRUE
                                                                             ),
                                                       JsStr.of("a"),
                                                       JsStr.of("b"),
                                                       JsInt.of(1),
                                                       JsStr.of("c"),
                                                       JsStr.of("d"),
                                                       JsStr.of("e"),
                                                       Jsons.mutable.object.of("a",
                                                                               NULL
                                                                              )
                                                      )
                               );
    }

    @Test
    public void test_creation_mutable_json_array_containing_arbitrary_number_of_elements()
    {
        JsArray arr = Jsons.mutable.array.of(JsStr.of("A"),
                                             JsStr.of("B"),
                                             JsStr.of("C"),
                                             JsStr.of("D"),
                                             JsStr.of("E"),
                                             JsStr.of("F"),
                                             JsStr.of("G")
                                            );

        arr.mapElems(p -> p.mapIfStr(String::toLowerCase).elem)
           .filterElems(p -> p.elem.isStr(letter -> Comparator.<String>naturalOrder()
           .compare(letter,
                    "d"
                   ) < 0));
        Assertions.assertEquals(Jsons.mutable.array.of("a",
                                                       "b",
                                                       "c"
                                                      ),
                                arr
                               );

    }

    @Test
    public void test_parse_string_into_mutable_json_array() throws MalformedJson
    {

        Assertions.assertEquals(Jsons.mutable.array.parse("[1,2]")
                                                   .orElseThrow(),
                                Jsons.mutable.array.of(1,
                                                       2
                                                      )
                               );

        final Optional<JsArray> opt = Jsons.mutable.array.parse("[1,2]")
                                                         .toOptional();

        Assertions.assertTrue(opt.isPresent());


        final Optional<JsArray> optEmpty = Jsons.mutable.array.parse("[1,2")
                                                              .toOptional();

        Assertions.assertFalse(optEmpty.isPresent());

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.mutable.array.parse("[1,2")
                                                         .orElseThrow()
                               );


        Assertions.assertEquals(Jsons.mutable.array.parse("[1,2")
                                                   .orElse(Jsons.mutable.array::empty),
                                Jsons.mutable.array.empty()
                               );


    }

    @Test
    public void test_parse_string_into_mutable_json_array_mapping_and_filtering_elements_while_the_parsing() throws MalformedJson
    {

        String str = "[1,2,3,true,false]";
        final JsArray array = Jsons.mutable.array.parse(str,
                                                        ParseBuilder.builder()
                                                                    .withElemFilter(p -> p.elem.isInt())
                                                                    .withElemMap(p -> p.mapIfInt(i -> i + 1).elem)
                                                       )
                                                 .orElseThrow();

        Assertions.assertEquals(array,
                                Jsons.mutable.array.of(2,
                                                       3,
                                                       4
                                                      )
                               );
    }

    @Test
    public void test_creation_of_mutable_empty_json_array()
    {

        JsArray arr = Jsons.mutable.array.empty();
        Assertions.assertEquals(0,
                                arr.size()
                               );

        arr.append(JsInt.of(1));  // arr is mutable
        Assertions.assertEquals(1,
                                arr.size()
                               );

    }


    @Test
    public void test_creation_array_from_iterable()
    {

        final ArrayList<JsElem> list = new ArrayList<>();
        list.add(JsStr.of("a"));
        list.add(JsStr.of("b"));

        JsArray arr = Jsons.mutable.array.ofIterable(list);

        Assertions.assertEquals(arr.size(),
                                list.size()
                               );


    }

    @Test
    public void test_appending_a_json_array_to_the_back_of_another_json_array()
    {
        JsArray arr = Jsons.immutable.array.of("a",
                                               "b"
                                              );
        JsArray arr1 = arr.appendAll(Jsons.immutable.array.of(NULL,
                                                              TRUE,
                                                              FALSE
                                                             ));

        Assertions.assertTrue(arr.size() == 2);
        Assertions.assertTrue(arr1.size() == 5);
        Assertions.assertEquals(arr,
                                Jsons.immutable.array.of("a",
                                                         "b"
                                                        )
                               );
        Assertions.assertEquals(arr1,
                                Jsons.immutable.array.of(JsStr.of("a"),
                                                         JsStr.of("b"),
                                                         NULL,
                                                         TRUE,
                                                         FALSE
                                                        )
                               );

        JsArray _arr_ = Jsons.mutable.array.of("a",
                                               "b"
                                              );

        _arr_.appendAll(Jsons.immutable.array.of(NULL,
                                                 TRUE,
                                                 FALSE
                                                ));

        Assertions.assertTrue(_arr_.size() == 5);

        Assertions.assertEquals(_arr_,
                                Jsons.mutable.array.of(JsStr.of("a"),
                                                       JsStr.of("b"),
                                                       NULL,
                                                       TRUE,
                                                       FALSE
                                                      )
                               );
    }

    @Test
    public void test_prepending_a_json_array_to_the_front_of_another_json_array()
    {
        JsArray arr = Jsons.immutable.array.of("a",
                                               "b"
                                              );
        JsArray arr1 = arr.prependAll(Jsons.immutable.array.of(NULL,
                                                               TRUE,
                                                               FALSE
                                                              ));

        Assertions.assertEquals(2,
                                arr.size()
                               );
        Assertions.assertTrue(arr1.size() == 5);
        Assertions.assertEquals(arr,
                                Jsons.immutable.array.of("a",
                                                         "b"
                                                        )
                               );
        Assertions.assertEquals(arr1,
                                Jsons.immutable.array.of(NULL,
                                                         TRUE,
                                                         FALSE,
                                                         JsStr.of("a"),
                                                         JsStr.of("b")
                                                        )
                               );

        JsArray _arr_ = Jsons.mutable.array.of("a",
                                               "b"
                                              );

        _arr_.prependAll(Jsons.immutable.array.of(NULL,
                                                  TRUE,
                                                  FALSE
                                                 ));

        Assertions.assertTrue(_arr_.size() == 5);

        Assertions.assertEquals(_arr_,
                                Jsons.mutable.array.of(NULL,
                                                       TRUE,
                                                       FALSE,
                                                       JsStr.of("a"),
                                                       JsStr.of("b")
                                                      )
                               );


    }

    @Test
    public void test_appends_one_or_more_elements_to_the_back_of_a_json_array()
    {

        JsArray arr = Jsons.immutable.array.of("a",
                                               "b"
                                              );
        JsArray arr1 = arr.append(JsStr.of("c"),
                                  JsStr.of("d")
                                 ); // ["a","b","c","d"]

        Assertions.assertEquals(Jsons.immutable.array.of("a",
                                                         "b",
                                                         "c",
                                                         "d"
                                                        ),
                                arr1
                               );

        JsArray _arr_ = Jsons.mutable.array.of("a",
                                               "b"
                                              );
        _arr_.append(JsStr.of("c"),
                     JsStr.of("d")
                    );

        Assertions.assertEquals(Jsons.mutable.array.of("a",
                                                       "b",
                                                       "c",
                                                       "d"
                                                      ),
                                _arr_
                               );
    }

    @Test
    public void test_prepend()
    {

        JsArray arr = Jsons.immutable.array.of("a",
                                               "b"
                                              );

        Assertions.assertEquals(Jsons.immutable.array.of("c",
                                                         "d",
                                                         "a",
                                                         "b"
                                                        ),
                                arr.prepend(JsStr.of("c"),
                                            JsStr.of("d")
                                           )
                               );

        Assertions.assertTrue(arr.size() == 2);


        JsArray _arr_ = Jsons.mutable.array.of("a",
                                               "b"
                                              );

        _arr_.prepend(JsStr.of("c"),
                      JsStr.of("d")
                     );

        Assertions.assertTrue(arr.size() == 2);
        Assertions.assertEquals(Jsons.mutable.array.of("c",
                                                       "d",
                                                       "a",
                                                       "b"
                                                      ),
                                _arr_

                               );


    }

    @Test
    public void test_collecting_an_array_from_a_collector() throws MalformedJson
    {

        final JsArray arr = Jsons.immutable.array.of(JsStr.of("a"),
                                                     JsStr.of("b"),
                                                     Jsons.immutable.array.of(Jsons.immutable.object.empty(),
                                                                              TRUE,
                                                                              FALSE,
                                                                              NULL,
                                                                              Jsons.immutable.array.empty()
                                                                             ),
                                                     JsStr.of("a")
                                                    );
        final JsArray a = arr.stream_()
                             .parallel()
                             .collect(Jsons.mutable.array.collector());
        System.out.println(arr);
        System.out.println(a);
        Assertions.assertTrue(arr.same(a)
                             );


        Assertions.assertEquals(Jsons.mutable.array.parse(arr.toString())
                                                   .orElseThrow(),
                                arr.stream_()
                                   .parallel()
                                   .collect(Jsons.mutable.array.collector())
                               );

        Assertions.assertEquals(arr,
                                Jsons.immutable.array.toImmutable(Jsons.mutable.array.toMutable(arr))

                               );
        final JsArray expected = Jsons.mutable.array.toMutable(arr);
        Assertions.assertEquals(expected,
                                arr.stream_()
                                   .parallel()
                                   .collect(Jsons.mutable.array.collector())
                               );
        Assertions.assertTrue(arr.same(Jsons.mutable.array.toMutable(arr)));

    }

    @Test
    public void test_contains_element_in_js_array()
    {
        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );
        Assertions.assertTrue(arr.containsElem(JsInt.of(1)));   //true
        Assertions.assertTrue(arr.containsElem(TRUE));   //true
        Assertions.assertTrue(arr.containsElem(JsStr.of("a"))); //true
        Assertions.assertTrue(arr.containsElem(NULL));
        Assertions.assertFalse(arr.containsElem(JsInt.of(10)));


        JsArray _arr_ = Jsons.immutable.array.of(JsInt.of(1),
                                                 TRUE,
                                                 JsStr.of("a"),
                                                 NULL
                                                );
        Assertions.assertTrue(_arr_.containsElem(JsInt.of(1)));   //true
        Assertions.assertTrue(_arr_.containsElem(TRUE));   //true
        Assertions.assertTrue(_arr_.containsElem(JsStr.of("a"))); //true
        Assertions.assertTrue(_arr_.containsElem(NULL));
        Assertions.assertFalse(arr.containsElem(JsInt.of(10)));
    }


    @Test
    public void test_empty_immutable_js_array_returns_the_same_instance()
    {

        Assertions.assertSame(Jsons.immutable.array.empty(),
                              Jsons.immutable.array.empty()
                             );
    }

    @Test
    public void test_head_of_json_array_returns_the_firt_element_or_an_exception()
    {

        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE
                                              );
        Assertions.assertEquals(JsInt.of(1),
                                arr.head()

                               );

        Assertions.assertSame(TRUE,
                              arr.tail()
                                 .head()

                             );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.empty()
                                                           .head()
                               );

        JsArray _arr_ = Jsons.immutable.array.of(JsInt.of(1),
                                                 TRUE
                                                );
        Assertions.assertEquals(JsInt.of(1),
                                _arr_.head()

                               );

        Assertions.assertSame(TRUE,
                              _arr_.tail()
                                   .head()

                             );


        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.array.empty()
                                                         .head()
                               );

    }

    @Test
    public void test_init_of_json_array_returns_all_the_elements_except_the_last_or_an_exception()
    {

        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );

        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(1),
                                                         TRUE,
                                                         JsStr.of("a")
                                                        ),
                                arr.init()
                               );

        Assertions.assertEquals(Jsons.immutable.array.of(TRUE,
                                                         JsStr.of("a")
                                                        ),
                                arr.tail()
                                   .init()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.empty()
                                                           .init()
                               );

        JsArray _arr_ = Jsons.mutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );

        Assertions.assertEquals(Jsons.mutable.array.of(JsInt.of(1),
                                                       TRUE,
                                                       JsStr.of("a")
                                                      ),
                                _arr_.init()
                               );

        Assertions.assertEquals(Jsons.mutable.array.of(TRUE,
                                                       JsStr.of("a")
                                                      ),
                                _arr_.tail()
                                     .init()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.array.empty()
                                                         .init()
                               );


    }


    @Test
    public void test_last_returns_the_last_element_or_throws_exception_if_emtpy()
    {

        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );

        Assertions.assertEquals(NULL,
                                arr.last()
                               );

        Assertions.assertEquals(JsStr.of("a"),
                                arr.init()
                                   .last()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.empty()
                                                           .last()
                               );

        JsArray _arr_ = Jsons.mutable.array.of(JsInt.of(1),
                                               TRUE,
                                               JsStr.of("a"),
                                               NULL
                                              );

        Assertions.assertEquals(NULL,
                                _arr_.last()
                               );

        Assertions.assertEquals(JsStr.of("a"),
                                _arr_.init()
                                     .last()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.array.empty()
                                                         .last()
                               );

    }


    @Test
    public void test_create_immutable_json_array_from_list_of_elements()
    {

        JsArray arr = Jsons.immutable.array.ofIterable(Arrays.asList(JsStr.of("a"),
                                                                     JsInt.of(1)
                                                                    )
                                                      );
        JsArray newArr = arr.remove(JsPath.fromIndex(-1));

        Assertions.assertEquals(2,
                                arr.size()
                               );

        Assertions.assertEquals(1,
                                newArr.size()
                               );

    }

    @Test
    public void test_create_one_element_immutable_json_array()
    {
        JsArray arr = Jsons.immutable.array.of(NULL);
        JsArray arr1 = arr.prepend(JsStr.of("a"));
        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        Assertions.assertEquals(JsStr.of("a"),
                                arr1.head()
                               );

    }

    @Test
    public void test_create_two_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                               NULL,
                                               JsInt.of(2),
                                               Jsons.immutable.object.empty()
                                              );
        JsArray newArr = arr.mapElems(p -> p.mapIfInt(i -> i + 10).elem,
                                      p -> p.elem.isInt()
                                     );

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );
        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(11),
                                                         NULL,
                                                         JsInt.of(12),
                                                         Jsons.immutable.object.empty()

                                                        ),
                                newArr
                               );

    }

    @Test
    public void test_create_three_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsStr.of("a"),
                                               JsStr.of("b"),
                                               JsStr.of("c")
                                              );

        final JsArray newArr = arr.mapElems(p -> p.mapIfStr(String::toUpperCase).elem);

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );
        Assertions.assertEquals(Jsons.immutable.array.of("A",
                                                         "B",
                                                         "C"
                                                        ),
                                newArr
                               );


    }

    @Test
    public void test_create_four_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsLong.of(10),
                                               JsStr.of("b"),
                                               JsStr.of("c"),
                                               JsInt.of(10)
                                              );
        JsArray arr1 = arr.filterElems(p -> p.elem.isIntegral());

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );
        Assertions.assertEquals(Jsons.immutable.array.of(JsLong.of(10),
                                                         JsInt.of(10)
                                                        ),
                                arr1
                               );
    }

    @Test
    public void test_create_five_elements_immutable_json_array()
    {

        JsArray arr = Jsons.immutable.array.of(JsStr.of("A"),
                                               JsStr.of("B"),
                                               JsStr.of("C"),
                                               JsStr.of("D"),
                                               JsStr.of("E")
                                              );
        JsArray arr1 = arr.put(JsPath.fromIndex(-1),
                               "F"
                              );

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );

        Assertions.assertEquals(JsStr.of("F"),
                                arr1.get(JsPath.fromIndex(-1))
                               );

    }

    @Test
    public void test_create_six_elements_imutable_json_array()
    {
        JsArray arr = Jsons.immutable.array.of(JsStr.of("A"),
                                               JsStr.of("B"),
                                               JsStr.of("C"),
                                               JsStr.of("D"),
                                               JsStr.of("E"),
                                               JsStr.of("F"),
                                               JsStr.of("G")
                                              );

        JsArray arr1 = arr.mapElems(pair -> pair.mapIfStr(s ->
                                                          {
                                                              final int index = pair.path.last()
                                                                                         .asIndex().n;
                                                              return s.concat(String.valueOf(index));
                                                          })
                                    .elem
                                   );

        Assertions.assertNotEquals(arr,
                                   arr1
                                  );

        Assertions.assertEquals(Jsons.immutable.array.of(JsStr.of("A0"),
                                                         JsStr.of("B1"),
                                                         JsStr.of("C2"),
                                                         JsStr.of("D3"),
                                                         JsStr.of("E4"),
                                                         JsStr.of("F5"),
                                                         JsStr.of("G6")
                                                        ),
                                arr1
                               );
    }

    @Test
    public void test_parse_string_into_immutable_json_array() throws MalformedJson
    {

        Assertions.assertEquals(Jsons.immutable.array.of(1,
                                                         2
                                                        ),
                                Jsons.immutable.array.parse("[1,2]")
                                                     .orElseThrow()
                               );
        Assertions.assertEquals(Optional.of(Jsons.immutable.array.of(1,
                                                                     2
                                                                    )),
                                Jsons.immutable.array.parse("[1,2]")
                                                     .toOptional()
                               );
        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.array.parse("[1,2")
                                                           .orElseThrow()
                               );

        Assertions.assertTrue(Jsons.mutable.array.empty()
                                                 .same(
                                                 Jsons.immutable.array.parse("[1,2")
                                                                      .orElse(Jsons.immutable.array::empty))
                             );


        Assertions.assertEquals(Optional.empty(),
                                Jsons.immutable.array.parse("[1,2")
                                                     .toOptional()
                               ); // Optional.empty


    }

    @Test
    public void test_tail_of_json_array_returns_all_elements_except_first_one()
    {

        JsArray arr = Jsons.immutable.array.of("a",
                                               "b",
                                               "c"
                                              );
        Assertions.assertEquals(Jsons.immutable.array.of("b",
                                                         "c"
                                                        ),
                                arr.tail()
                               ); //  ["b","c"]

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.immutable.array.empty()
                                                           .tail()
                               );


        JsArray _arr_ = Jsons.mutable.array.of("a",
                                               "b",
                                               "c"
                                              );
        Assertions.assertEquals(Jsons.mutable.array.of("b",
                                                       "c"
                                                      ),
                                _arr_.tail()
                               ); //  ["b","c"]

        Assertions.assertThrows(UserError.class,
                                () -> Jsons.mutable.array.empty()
                                                         .tail()
                               );

    }

    @Test
    public void test_parse_string_into_immutable_json_array_with_options() throws MalformedJson
    {

        final JsArray arr = Jsons.immutable.array.parse("[1,2,3,true,false,null,[null,true,4]]",
                                                        ParseBuilder.builder()
                                                                    .withElemFilter(p -> p.elem.isInt())
                                                                    .withElemMap(p -> JsElems.mapIfInt(i -> i + 10)
                                                                                             .apply(p.elem))
                                                       )
                                                 .orElseThrow();

        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(11),
                                                         JsInt.of(12),
                                                         JsInt.of(13),
                                                         Jsons.immutable.array.of(14)
                                                        ),
                                arr
                               );

    }


    @Test
    public void test_static_factory_methods_from_primitives()
    {

        JsArray arr = Jsons.immutable.array.of(1,
                                               2,
                                               3
                                              );
        Assertions.assertNotEquals(arr,
                                   arr.append(JsInt.of(4))
                                  );

        JsArray _arr_ = Jsons.mutable.array.of(1,
                                               2,
                                               3
                                              );
        Assertions.assertEquals(_arr_,
                                _arr_.append(JsInt.of(4))
                               );

        JsArray arr1 = Jsons.immutable.array.of("a",
                                                "b",
                                                "c"
                                               );
        Assertions.assertNotEquals(arr1,
                                   arr1.append(JsStr.of("d"))
                                  );

        JsArray _arr1_ = Jsons.mutable.array.of("a",
                                                "b",
                                                "c"
                                               );
        Assertions.assertEquals(_arr1_,
                                _arr1_.append(JsStr.of("d"))
                               );

        JsArray arr2 = Jsons.immutable.array.of(true,
                                                false
                                               );
        Assertions.assertNotEquals(arr2,
                                   arr2.append(JsBool.of(false))
                                  );

        JsArray _arr2_ = Jsons.mutable.array.of(true,
                                                false
                                               );
        Assertions.assertEquals(_arr2_,
                                _arr2_.append(JsBool.of(false))
                               );

        JsArray arr3 = Jsons.immutable.array.of(1l,
                                                2l,
                                                3l
                                               );
        Assertions.assertNotEquals(arr3,
                                   arr3.append(JsLong.of(4))
                                  );

        JsArray _arr3_ = Jsons.mutable.array.of(1l,
                                                2l,
                                                3l
                                               );
        Assertions.assertEquals(_arr3_,
                                _arr3_.append(JsLong.of(4))
                               );

        JsArray arr4 = Jsons.immutable.array.of(1.1d,
                                                2.2d,
                                                3.3d
                                               );
        Assertions.assertNotEquals(arr4,
                                   arr4.append(JsDouble.of(4.4d))
                                  );

        JsArray _arr4_ = Jsons.mutable.array.of(1.1d,
                                                2.2d,
                                                3.3d
                                               );
        Assertions.assertEquals(_arr4_,
                                _arr4_.append(JsDouble.of(4.4d))
                               );

    }

    @Test
    public void test_traversing_mutable_json_array_by_an_iterator_and_removing_some_elements()
    {

        final JsArray arr = Jsons.mutable.array.of(1,
                                                   2,
                                                   3,
                                                   4
                                                  );
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            final JsElem next = iterator.next();
            if (next.isInt(i -> i == 3)) iterator.remove();
        }
        Assertions.assertEquals(arr,
                                Jsons.mutable.array.of(1,
                                                       2,
                                                       4
                                                      )
                               );
    }

    @Test
    public void test_create_immutable_json_array_from_one_or_more_pairs() throws MalformedJson
    {

        final JsArray arr = Jsons.immutable.array.of(JsPair.of(JsPath.fromIndex(0),
                                                               JsInt.of(1)
                                                              ),
                                                     JsPair.of(JsPath.fromIndex(2),
                                                               JsInt.of(3)
                                                              )
                                                    );
        Assertions.assertEquals(Jsons.immutable.array.of(JsInt.of(1),
                                                         NULL,
                                                         JsInt.of(3)
                                                        ),
                                arr
                               );

        final JsArray arr1 = Jsons.immutable.array.of(JsPair.of(JsPath.path("/0/a"),
                                                                JsInt.of(1)
                                                               ),
                                                      JsPair.of(JsPath.path("/2/b"),
                                                                JsInt.of(3)
                                                               )
                                                     );

        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"a\": 1},null,{\"b\": 3}]")
                                                     .orElseThrow(),
                                arr1
                               );


    }

    @Test
    public void test_create_mutable_json_array_from_one_or_more_pairs() throws MalformedJson
    {

        final JsArray arr = Jsons.mutable.array.of(JsPair.of(JsPath.fromIndex(0),
                                                             JsInt.of(1)
                                                            ),
                                                   JsPair.of(JsPath.fromIndex(2),
                                                             JsInt.of(3)
                                                            )
                                                  );
        Assertions.assertEquals(Jsons.mutable.array.of(JsInt.of(1),
                                                       NULL,
                                                       JsInt.of(3)
                                                      ),
                                arr
                               );

        final JsArray arr1 = Jsons.mutable.array.of(JsPair.of(JsPath.path("/0/a"),
                                                              JsInt.of(1)
                                                             ),
                                                    JsPair.of(JsPath.path("/2/b"),
                                                              JsInt.of(3)
                                                             )
                                                   );

        Assertions.assertEquals(Jsons.mutable.array.parse("[{\"a\": 1},null,{\"b\": 3}]")
                                                   .orElseThrow(),
                                arr1
                               );


    }

    @Test
    public void test_intersection() throws MalformedJson
    {

        final JsArray arr1 = Jsons.immutable.array.parse("[{\"a\": 1, \"b\": [1,2,2]}]")
                                                  .orElseThrow();
        final JsArray arr2 = Jsons.immutable.array.parse("[{\"a\": 1, \"b\": [1,2]}]")
                                                  .orElseThrow();

        Assertions.assertEquals(arr1,
                                arr1.intersection(arr1,
                                                  LIST
                                                 )
                               );
        Assertions.assertEquals(arr2,
                                arr2.intersection(arr2,
                                                  LIST
                                                 )
                               );
        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  LIST
                                                 )
                               );

        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  LIST
                                                 )
                               );

        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  SET
                                                 )
                               );

        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                arr1.intersection(arr2,
                                                  MULTISET
                                                 )
                               );

        Assertions.assertEquals(arr2,
                                arr1.intersection_(arr2)
                               );


        Assertions.assertEquals(arr2,
                                arr1.intersection_(arr2
                                                  )
                               );


    }

    @Test
    public void test_equals_and_hashcode()
    {
        final JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                                     JsBigInt.of(BigInteger.ONE),
                                                     JsLong.of(1),
                                                     JsBigDec.of(BigDecimal.ONE),
                                                     JsDouble.of(1d)
                                                    );
        final JsArray arr1 = Jsons.immutable.array.of(JsBigDec.of(BigDecimal.ONE),
                                                      JsLong.of(1),
                                                      JsInt.of(1),
                                                      JsBigInt.of(BigInteger.ONE),
                                                      JsDouble.of(1d)
                                                     );

        Assertions.assertEquals(arr,
                                arr1
                               );
        Assertions.assertEquals(arr.hashCode(),
                                arr1.hashCode()
                               );

    }


    @Test
    public void test_reduce_strings()
    {
        final JsArray array = Jsons.immutable.array.of(JsStr.of("a"),
                                                       JsStr.of("b"),
                                                       JsInt.of(1),
                                                       JsInt.of(2),
                                                       Jsons.immutable.object.of("key",
                                                                                 JsStr.of("c"),
                                                                                 "key1",
                                                                                 JsStr.of("d"),
                                                                                 "key3",
                                                                                 Jsons.immutable.array.of("e",
                                                                                                          "f",
                                                                                                          "g"
                                                                                                         )
                                                                                )
                                                      );

        final Optional<String> result = array.reduce_(String::concat,
                                                      p -> p.elem.asJsStr().x,
                                                      p -> p.elem.isStr()
                                                     );

        final char[] chars = result.get()
                                   .toCharArray();

        Arrays.sort(chars);
        Assertions.assertEquals("abcdefg",
                                new String(chars)
                               );

        final Optional<String> result1 = array.reduce(String::concat,
                                                      p -> p.elem.asJsStr().x,
                                                      p -> p.elem.isStr()
                                                     );

        final char[] chars1 = result1.get()
                                     .toCharArray();

        Arrays.sort(chars1);
        Assertions.assertEquals("ab",
                                new String(chars1)
                               );
    }

    @Test
    public void test_filter_mutable_jsons() throws MalformedJson
    {
        Supplier<JsArray> supplier = () -> Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                          NULL
                                                                                         ),
                                                                  JsInt.of(1),
                                                                  Jsons.mutable.object.of("a",
                                                                                          NULL
                                                                                         ),
                                                                  Jsons.mutable.object.of("a",
                                                                                          JsInt.of(1)
                                                                                         ),
                                                                  Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                                 NULL
                                                                                                                ),
                                                                                         Jsons.mutable.object.of("a",
                                                                                                                 JsInt.of(1)
                                                                                                                ),
                                                                                         Jsons.mutable.object.of("b",
                                                                                                                 NULL
                                                                                                                ),
                                                                                         Jsons.mutable.object.of("a",
                                                                                                                 NULL
                                                                                                                )
                                                                                        )
                                                                 );

        final JsArray arr = Jsons.mutable.array.parse(supplier.get()
                                                              .toString())
                                               .orElseThrow();
        arr.filterObjs_((p, o) ->
                        {
                            Assertions.assertEquals(o,
                                                    supplier.get()
                                                            .get(p)
                                                   );
                            return o.get(JsPath.fromKey("a"))
                                    .isNotNull();
                        });
        Assertions.assertEquals(Jsons.mutable.array.parse("[1,{\"a\":1},[{\"a\":1},{\"b\":null}]]\n")
                                                   .orElseThrow(),
                                arr
                               );

        final JsArray arr1 = Jsons.mutable.array.parse(supplier.get()
                                                               .toString())
                                                .orElseThrow();

        arr1.filterObjs((p, o) ->
                        {
                            Assertions.assertEquals(o,
                                                    supplier.get()
                                                            .get(p)
                                                   );
                            return o.get(JsPath.fromKey("a"))
                                    .isNotNull();
                        });

        Assertions.assertEquals(Jsons.mutable.array.parse("[1,{\"a\":1},[{\"a\":null},{\"a\":1},{\"b\":null},{\"a\":null}]]\n")
                                                   .orElseThrow(),
                                arr1
                               );
    }

    @Test
    public void test_filter_immutable_jsons() throws MalformedJson
    {
        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         NULL
                                                                        ),
                                               JsInt.of(1),
                                               Jsons.immutable.object.of("a",
                                                                         NULL
                                                                        ),
                                               Jsons.immutable.object.of("a",
                                                                         JsInt.of(1)
                                                                        ),
                                               Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                                                  NULL
                                                                                                 ),
                                                                        Jsons.immutable.object.of("a",
                                                                                                  JsInt.of(1)
                                                                                                 ),
                                                                        Jsons.immutable.object.of("b",
                                                                                                  NULL
                                                                                                 ),
                                                                        Jsons.immutable.object.of("a",
                                                                                                  NULL
                                                                                                 )
                                                                       )
                                              );


        final JsArray arr1 = arr.filterObjs_((p, o) ->
                                             {
                                                 Assertions.assertEquals(o,
                                                                         arr.get(p)
                                                                        );
                                                 return o.get(JsPath.fromKey("a"))
                                                         .isNotNull();
                                             });
        Assertions.assertEquals(Jsons.immutable.array.parse("[1,{\"a\":1},[{\"a\":1},{\"b\":null}]]\n")
                                                     .orElseThrow(),
                                arr1
                               );


        final JsArray arr2 = arr.filterObjs((p, o) ->
                                            {
                                                Assertions.assertEquals(o,
                                                                        arr.get(p)
                                                                       );
                                                return o.get(JsPath.fromKey("a"))
                                                        .isNotNull();
                                            });

        Assertions.assertEquals(Jsons.immutable.array.parse("[1,{\"a\":1},[{\"a\":null},{\"a\":1},{\"b\":null},{\"a\":null}]]\n")
                                                     .orElseThrow(),
                                arr2
                               );
    }

    @Test
    public void test_filter_keys_mutable() throws MalformedJson
    {
        JsArray array = Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                       NULL,
                                                                       "b",
                                                                       Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                                      NULL,
                                                                                                                      "b",
                                                                                                                      JsInt.of(1)
                                                                                                                     ),
                                                                                              Jsons.mutable.object.of("a",
                                                                                                                      NULL,
                                                                                                                      "b",
                                                                                                                      JsInt.of(1)
                                                                                                                     ),
                                                                                              Jsons.mutable.object.of("a",
                                                                                                                      NULL,
                                                                                                                      "b",
                                                                                                                      JsInt.of(1)
                                                                                                                     )
                                                                                             )
                                                                      ),
                                               Jsons.mutable.object.of("a",
                                                                       NULL,
                                                                       "b",
                                                                       Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                                      NULL,
                                                                                                                      "b",
                                                                                                                      JsInt.of(1)
                                                                                                                     ),
                                                                                              Jsons.mutable.object.of("a",
                                                                                                                      NULL,
                                                                                                                      "b",
                                                                                                                      JsInt.of(1)
                                                                                                                     ),
                                                                                              Jsons.mutable.object.of("a",
                                                                                                                      NULL,
                                                                                                                      "b",
                                                                                                                      JsInt.of(1)
                                                                                                                     )
                                                                                             )
                                                                      )
                                              );

        array.filterKeys_(p -> p.elem.isNotNull());

        Assertions.assertEquals(Jsons.mutable.array.parse("[{\"b\":[{\"b\":1},{\"b\":1},{\"b\":1}]},{\"b\":[{\"b\":1},{\"b\":1},{\"b\":1}]}]\n")
                                                   .orElseThrow(),
                                array
                               );
    }

    @Test
    public void test_parse_with_options_mutable() throws MalformedJson
    {
        final Supplier<JsArray> supplier = () -> Jsons.mutable.array.of(NULL,
                                                                        Jsons.mutable.array.of(1,
                                                                                               2
                                                                                              ),
                                                                        NULL,
                                                                        Jsons.mutable.array.of("a",
                                                                                               "b"
                                                                                              ),
                                                                        NULL
                                                                       );
        Assertions.assertEquals(supplier.get()
                                        .filterElems_(p ->
                                                      {
                                                          Assertions.assertEquals(p.elem,
                                                                                  supplier.get()
                                                                                          .get(p.path)
                                                                                 );
                                                          return p.elem.isNotNull();
                                                      }),
                                Jsons.mutable.array.parse(supplier.get()
                                                                  .toString(),
                                                          ParseBuilder.builder()
                                                                      .withElemFilter(p -> p.elem.isNotNull())
                                                         )
                                                   .orElseThrow()
                               );
    }

    @Test
    public void test_parse_with_options_immutable() throws MalformedJson
    {
        final JsArray array = Jsons.immutable.array.of(NULL,
                                                       Jsons.immutable.array.of(1,
                                                                                2
                                                                               ),
                                                       NULL,
                                                       Jsons.immutable.array.of("a",
                                                                                "b"
                                                                               ),
                                                       NULL
                                                      );
        Assertions.assertEquals(array.filterElems_(p ->
                                                   {
                                                       Assertions.assertEquals(p.elem,
                                                                               array.get(p.path)
                                                                              );
                                                       return p.elem.isNotNull();
                                                   }),
                                Jsons.immutable.array.parse(array.toString(),
                                                            ParseBuilder.builder()
                                                                        .withElemFilter(p -> p.elem.isNotNull())
                                                           )
                                                     .orElseThrow()
                               );
    }


    @Test
    public void test_map_json_immutable() throws MalformedJson
    {

        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2)
                                                                        ),
                                               JsStr.of("c"),
                                               TRUE,
                                               FALSE,
                                               Jsons.immutable.object.of("a",
                                                                         Jsons.immutable.object.of("d",
                                                                                                   JsInt.of(1),
                                                                                                   "e",
                                                                                                   JsInt.of(2)
                                                                                                  ),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         JsInt.of(3)
                                                                        )
                                              );

        final JsArray a_ = arr.mapObjs_((path, obj) ->
                                        {
                                            Assertions.assertEquals(obj,
                                                                    arr.get(path)
                                                                   );
                                            return obj.put(JsPath.fromKey("size"),
                                                           obj.size()
                                                          );
                                        });


        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"size\":2,\"d\":1},\"b\":2,\"c\":3}]\n")
                                                     .orElseThrow(),
                                a_
                               );

        final JsArray a = arr.mapObjs((path, obj) ->
                                      {
                                          Assertions.assertEquals(obj,
                                                                  arr.get(path)
                                                                 );
                                          return obj.put(JsPath.fromKey("size"),
                                                         obj.size()
                                                        );
                                      });

        Assertions.assertEquals(Jsons.immutable.array.parse("[{\"size\":2,\"a\":1,\"b\":2},\"c\",true,false,{\"size\":3,\"a\":{\"e\":2,\"d\":1},\"b\":2,\"c\":3}]\n")
                                                     .orElseThrow(),
                                a
                               );
    }

    @Test
    public void test_map_json_immutable_with_predicate() throws MalformedJson
    {

        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         Jsons.immutable.object.empty()
                                                                        ),
                                               Jsons.immutable.object.empty(),
                                               JsStr.of("c"),
                                               TRUE,
                                               FALSE,
                                               Jsons.immutable.object.of("a",
                                                                         JsInt.of(1),
                                                                         "b",
                                                                         JsInt.of(2),
                                                                         "c",
                                                                         JsInt.of(3)
                                                                        )
                                              );

        final JsArray a = arr.mapObjs_((path, obj) ->
                                       {
                                           Assertions.assertEquals(obj,
                                                                   arr.get(path)
                                                                  );
                                           return obj.put(JsPath.fromKey("size"),
                                                          obj.size()
                                                         );
                                       },
                                       (p, obj) -> obj.isNotEmpty()
                                      );


        Assertions.assertEquals(Jsons.immutable.array.parse("[\n"
                                                            + "  {\n"
                                                            + "    \"size\": 3,\n"
                                                            + "    \"a\": 1,\n"
                                                            + "    \"b\": 2,\n"
                                                            + "    \"c\": {}\n"
                                                            + "  },\n"
                                                            + "  {},\n"
                                                            + "  \"c\",\n"
                                                            + "  true,\n"
                                                            + "  false,\n"
                                                            + "  {\n"
                                                            + "    \"size\": 3,\n"
                                                            + "    \"a\": 1,\n"
                                                            + "    \"b\": 2,\n"
                                                            + "    \"c\": 3\n"
                                                            + "  }\n"
                                                            + "]\n")
                                                     .orElseThrow(),
                                a
                               );
    }

    @Test
    public void test_map_json_mutable() throws MalformedJson
    {

        Supplier<JsArray> supplier = () -> Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                          JsInt.of(1),
                                                                                          "b",
                                                                                          JsInt.of(2)
                                                                                         ),
                                                                  JsStr.of("c"),
                                                                  TRUE,
                                                                  FALSE,
                                                                  Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                                 JsInt.of(1)
                                                                                                                ),
                                                                                         NULL,
                                                                                         Jsons.mutable.object.of("a",
                                                                                                                 JsInt.of(1),
                                                                                                                 "b",
                                                                                                                 JsInt.of(2)
                                                                                                                ),
                                                                                         TRUE,
                                                                                         FALSE
                                                                                        ),
                                                                  Jsons.mutable.object.of("a",
                                                                                          JsInt.of(1),
                                                                                          "b",
                                                                                          JsInt.of(2),
                                                                                          "c",
                                                                                          JsInt.of(3)
                                                                                         )
                                                                 );

        JsArray arr = supplier.get();
        final JsArray a_ = arr.mapObjs_((path, obj) ->
                                        {
                                            Assertions.assertEquals(obj,
                                                                    supplier.get()
                                                                            .get(path)
                                                                   );
                                            return obj.put(JsPath.fromKey("size"),
                                                           obj.size()
                                                          );
                                        });

        Assertions.assertEquals(arr,
                                a_
                               );
        Assertions.assertEquals(Jsons.mutable.array.parse("[{\"a\":1,\"b\":2,\"size\":2},\"c\",true,false,[{\"a\":1,\"size\":1},null,{\"a\":1,\"b\":2,\"size\":2},true,false],{\"a\":1,\"b\":2,\"c\":3,\"size\":3}]\n")
                                                   .orElseThrow(),
                                a_
                               );
        final JsArray a = supplier.get()
                                  .mapObjs((path, obj) ->
                                           {
                                               Assertions.assertEquals(obj,
                                                                       supplier.get()
                                                                               .get(path)
                                                                      );
                                               return obj.put(JsPath.fromKey("size"),
                                                              obj.size()
                                                             );
                                           });
        Assertions.assertEquals(Jsons.mutable.array.parse("[{\"a\":1,\"b\":2,\"size\":2},\"c\",true,false,[{\"a\":1},null,{\"a\":1,\"b\":2},true,false],{\"a\":1,\"b\":2,\"c\":3,\"size\":3}]\n")
                                                   .orElseThrow(),
                                a
                               );
    }

    @Test
    public void test_map_json_mutable_array()
    {

        final Supplier<JsArray> supplier = () -> Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                JsStr.of("A"),
                                                                                                "b",
                                                                                                JsStr.of("B")
                                                                                               ),
                                                                        Jsons.mutable.object.empty(),
                                                                        Jsons.mutable.array.empty(),
                                                                        NULL,
                                                                        Jsons.mutable.object.of("c",
                                                                                                JsStr.of("C"),
                                                                                                "d",
                                                                                                JsStr.of("D"),
                                                                                                "e",
                                                                                                Jsons.mutable.object.of("f",
                                                                                                                        JsStr.of("F")
                                                                                                                       )
                                                                                               )
                                                                       );

        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put(JsPath.fromKey("size"),
                                                                                    json.size()
                                                                                   );

        JsArray arr = supplier.get();
        final JsArray newArr = arr.mapObjs((p, o) ->
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


        Assertions.assertEquals(arr,
                                newArr
                               );

        final Supplier<JsArray> supplier1 = () -> Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                 JsStr.of("A"),
                                                                                                 "b",
                                                                                                 JsStr.of("B")
                                                                                                ),
                                                                         NULL,
                                                                         Jsons.mutable.object.empty(),
                                                                         Jsons.mutable.array.empty(),
                                                                         Jsons.mutable.object.of("c",
                                                                                                 JsStr.of("C"),
                                                                                                 "d",
                                                                                                 JsStr.of("D"),
                                                                                                 "e",
                                                                                                 Jsons.mutable.object.of("f",
                                                                                                                         JsStr.of("F"),
                                                                                                                         "g",
                                                                                                                         Jsons.mutable.object.of("h",
                                                                                                                                                 JsStr.of("H")
                                                                                                                                                )
                                                                                                                        )
                                                                                                )
                                                                        );

        final JsArray arr1 = supplier1.get();
        final JsArray newArr1 = arr1.mapObjs_((p, o) ->
                                              {
                                                  Assertions.assertEquals(o,
                                                                          supplier1.get()
                                                                                   .get(p)
                                                                         );
                                                  return addSizeFn.apply(p,
                                                                         o
                                                                        );
                                              },
                                              (p, o) -> o.isNotEmpty()
                                             );

        Assertions.assertEquals(arr1,
                                newArr1
                               );

    }

    @Test
    public void test_map_json_immutable_array() throws MalformedJson
    {
        final JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                               Jsons.immutable.object.empty(),
                                                                               "b",
                                                                               JsStr.of("B")
                                                                              ),
                                                     NULL,
                                                     Jsons.immutable.object.empty(),
                                                     Jsons.immutable.array.empty(),
                                                     Jsons.immutable.object.of("c",
                                                                               JsStr.of("C"),
                                                                               "d",
                                                                               JsStr.of("D"),
                                                                               "e",
                                                                               Jsons.immutable.object.of("f",
                                                                                                         JsStr.of("F")
                                                                                                        )
                                                                              )
                                                    );

        final BiFunction<JsPath, JsObj, JsObj> addSizeFn = (path, json) -> json.put(JsPath.fromKey("size"),
                                                                                    json.size()
                                                                                   );

        final JsArray newArr = arr.mapObjs((p, o) ->
                                           {
                                               Assertions.assertEquals(o,
                                                                       arr.get(p)
                                                                      );
                                               return addSizeFn.apply(p,
                                                                      o
                                                                     );
                                           },
                                           (p, o) -> o.isNotEmpty()
                                          );

        Assertions.assertNotEquals(arr,
                                   newArr
                                  );

        Assertions.assertEquals(Jsons.immutable.parse("[{\"size\":2,\"a\":{},\"b\":\"B\"},null,{},[],{\"e\":{\"f\":\"F\"},\"size\":3,\"c\":\"C\",\"d\":\"D\"}]\n")
                                               .arrOrElseThrow(),
                                newArr
                               );

        final JsArray arr1 = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                                Jsons.immutable.object.empty(),
                                                                                "b",
                                                                                JsStr.of("B")
                                                                               ),
                                                      NULL,
                                                      Jsons.immutable.object.empty(),
                                                      Jsons.immutable.array.empty(),
                                                      Jsons.immutable.object.of("c",
                                                                                Jsons.immutable.array.empty(),
                                                                                "d",
                                                                                JsStr.of("D"),
                                                                                "e",
                                                                                Jsons.immutable.object.of("f",
                                                                                                          JsStr.of("F"),
                                                                                                          "g",
                                                                                                          Jsons.immutable.object.of("h",
                                                                                                                                    JsStr.of("H")
                                                                                                                                   )
                                                                                                         )
                                                                               )
                                                     );

        final JsArray newArr1 = arr1.mapObjs_((p, o) ->
                                              {
                                                  Assertions.assertEquals(o,
                                                                          arr1.get(p)
                                                                         );
                                                  return addSizeFn.apply(p,
                                                                         o
                                                                        );
                                              },
                                              (p, o) -> o.isNotEmpty()
                                             );


        Assertions.assertNotEquals(arr1,
                                   newArr1
                                  );

        Assertions.assertEquals(Jsons.immutable.parse("[{\"size\":2,\"a\":{},\"b\":\"B\"},null,{},[],{\"e\":{\"size\":2,\"f\":\"F\",\"g\":{\"size\":1,\"h\":\"H\"}},\"size\":3,\"c\":[],\"d\":\"D\"}]\n")
                                               .arrOrElseThrow(),
                                newArr1
                               );
    }


    @Test
    public void test_operations_immutable()
    {

        JsArray array = Jsons.immutable.array.of(JsPair.of(JsPath.path("/0/b/0"),
                                                           JsInt.of(1)
                                                          )
                                                );

        Assertions.assertEquals(array,
                                array.remove(JsPath.path("/0/b/c"))
                               );

        Assertions.assertEquals(array,
                                array.remove(JsPath.path("/0/0/c"))
                               );

        Assertions.assertEquals(array,
                                array.remove(JsPath.path("/0/b/0/a"))
                               );
    }


    @Test
    public void test_operations_mutable()
    {

        JsArray array = Jsons.mutable.array.of(JsPair.of(JsPath.path("/0/b/0"),
                                                         JsInt.of(1)
                                                        )
                                              );

        Assertions.assertEquals(array,
                                array.remove(JsPath.path("/0/b/c"))
                               );

        Assertions.assertEquals(array,
                                array.remove(JsPath.path("/0/0/c"))
                               );

        Assertions.assertEquals(array,
                                array.remove(JsPath.path("/0/b/0/a"))
                               );
    }

    @Test
    public void test_append_front_immutable()
    {

        final JsArray array = Jsons.immutable.array.empty()
                                                   .prepend(JsInt.of(1))
                                                   .prepend(JsStr.of("a"));

        Assertions.assertEquals(array.get(JsPath.fromIndex(0)),
                                JsStr.of("a")
                               );

        Assertions.assertEquals(array.get(JsPath.fromIndex(1)),
                                JsInt.of(1)
                               );
    }

    @Test
    public void test_append_front_mutable()
    {

        final JsArray array = Jsons.mutable.array.empty()
                                                 .prepend(JsInt.of(1))
                                                 .prepend(JsStr.of("a"));

        Assertions.assertEquals(array.get(JsPath.fromIndex(0)),
                                JsStr.of("a")
                               );

        Assertions.assertEquals(array.get(JsPath.fromIndex(1)),
                                JsInt.of(1)
                               );
    }

    @Test
    public void test_parse_into_immutable() throws MalformedJson
    {
        JsArray arr = Jsons.immutable.array.of(JsPair.of(JsPath.path("/0/b/0"),
                                                         NULL
                                                        ),
                                               JsPair.of(JsPath.path("/0/b/1"),
                                                         TRUE
                                                        ),
                                               JsPair.of(JsPath.path("/0/b/c"),
                                                         FALSE
                                                        ),
                                               JsPair.of(JsPath.path("/1/b/c/d"),
                                                         JsInt.of(1)
                                                        ),
                                               JsPair.of(JsPath.path("/1/a/a"),
                                                         JsStr.of("a")
                                                        ),
                                               JsPair.of(JsPath.path("/1/b/0"),
                                                         JsBigDec.of(BigDecimal.ONE)
                                                        ),
                                               JsPair.of(JsPath.path("/1/b/1"),
                                                         NULL
                                                        )
                                              );

        Assertions.assertEquals(arr,
                                Jsons.immutable.array.parse(arr.toString())
                                                     .orElseThrow()
                               );

        Assertions.assertEquals(arr,
                                Jsons.immutable.parse(arr.toString())
                                               .arrOrElseThrow()
                               );

    }

    @Test
    public void test_parse_into_mutable() throws MalformedJson
    {
        JsArray arr = Jsons.mutable.array.of(JsPair.of(JsPath.path("/0/b/0"),
                                                       NULL
                                                      ),
                                             JsPair.of(JsPath.path("/0/b/1"),
                                                       TRUE
                                                      ),
                                             JsPair.of(JsPath.path("/0/b/c"),
                                                       FALSE
                                                      ),
                                             JsPair.of(JsPath.path("/1/b/c/d"),
                                                       JsInt.of(1)
                                                      ),
                                             JsPair.of(JsPath.path("/1/a/a"),
                                                       JsStr.of("a")
                                                      ),
                                             JsPair.of(JsPath.path("/1/b/0"),
                                                       JsBigDec.of(BigDecimal.ONE)
                                                      ),
                                             JsPair.of(JsPath.path("/1/b/1"),
                                                       NULL
                                                      )
                                            );

        Assertions.assertEquals(arr,
                                Jsons.mutable.array.parse(arr.toString())
                                                   .orElseThrow()
                               );

        Assertions.assertEquals(arr,
                                Jsons.mutable.parse(arr.toString())
                                             .arrOrElseThrow()
                               );

    }

    @Test
    public void test_parse_malformed_string_into_mutable_fails()
    {

        Assertions.assertTrue(Jsons.mutable.parse("[")
                                           .isFailure());
    }


    @Test
    public void test_equals_arr_of_str()
    {

        final JsArray arr = Jsons.immutable.array.of("a",
                                                     "b",
                                                     "c",
                                                     "a",
                                                     "c"
                                                    );

        final JsArray arr2 = Jsons.immutable.array.of("a",
                                                      "b",
                                                      "c"
                                                     );

        final JsArray arr3 = Jsons.immutable.array.of("a",
                                                      "a",
                                                      "b",
                                                      "c",
                                                      "c"
                                                     );

        Assertions.assertTrue(arr.equals(arr,
                                         LIST
                                        ));
        Assertions.assertTrue(arr.equals(arr2,
                                         SET
                                        ));
        Assertions.assertTrue(arr.equals(arr3,
                                         MULTISET
                                        ));


    }

    @Test
    public void test_equals_arr_of_obj()
    {

        final JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                               NULL
                                                                              ),
                                                     Jsons.immutable.object.of("b",
                                                                               NULL
                                                                              ),
                                                     Jsons.immutable.object.of("c",
                                                                               NULL
                                                                              ),
                                                     Jsons.immutable.object.of("a",
                                                                               NULL
                                                                              ),
                                                     Jsons.immutable.object.of("c",
                                                                               NULL
                                                                              )
                                                    );

        final JsArray arr2 = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                                NULL
                                                                               ),
                                                      Jsons.immutable.object.of("b",
                                                                                NULL
                                                                               ),
                                                      Jsons.immutable.object.of("c",
                                                                                NULL
                                                                               )
                                                     );

        final JsArray arr3 = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                                NULL
                                                                               ),
                                                      Jsons.immutable.object.of("a",
                                                                                NULL
                                                                               ),
                                                      Jsons.immutable.object.of("b",
                                                                                NULL
                                                                               ),
                                                      Jsons.immutable.object.of("c",
                                                                                NULL
                                                                               ),
                                                      Jsons.immutable.object.of("c",
                                                                                NULL
                                                                               )
                                                     );

        Assertions.assertTrue(arr.equals(arr,
                                         LIST
                                        ));
        Assertions.assertTrue(arr.equals(arr2,
                                         SET
                                        ));
        Assertions.assertTrue(arr.equals(arr3,
                                         MULTISET
                                        ));


    }

    @Test
    public void test_filter_keys_immutable() throws MalformedJson
    {
        final JsArray arr = Jsons.immutable.array.of(NULL,
                                                     TRUE,
                                                     FALSE,
                                                     Jsons.immutable.object.of("a",
                                                                               NULL,
                                                                               "b",
                                                                               NULL,
                                                                               "c",
                                                                               TRUE
                                                                              ),
                                                     Jsons.immutable.object.of("a",
                                                                               NULL,
                                                                               "b",
                                                                               NULL,
                                                                               "c",
                                                                               TRUE,
                                                                               "d",
                                                                               Jsons.immutable.array.of(NULL,
                                                                                                        Jsons.immutable.object.of("a",
                                                                                                                                  NULL,
                                                                                                                                  "b",
                                                                                                                                  TRUE
                                                                                                                                 )
                                                                                                       )
                                                                              )
                                                    );

        final JsArray arr1 = arr.filterKeys_(p -> p.elem.isNotNull());

        Assertions.assertEquals(Jsons.immutable.array.parse("[null,true,false,{\"c\":true},{\"c\":true,\"d\":[null,{\"b\":true}]}]\n")
                                                     .orElseThrow(),
                                arr1
                               );
    }

    @Test
    public void test_contains_element_in_array()
    {
        final JsArray arr = Jsons.immutable.array.of(JsInt.of(1),
                                                     Jsons.immutable.array.of(JsInt.of(2),
                                                                              Jsons.immutable.object.of("a",
                                                                                                        NULL
                                                                                                       )
                                                                             )
                                                    );
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            JsElem next = iterator.next();
            Assertions.assertTrue(arr.containsElem(next));
        }
        final JsArray _arr_ = Jsons.mutable.array.of(JsInt.of(1),
                                                     Jsons.mutable.array.of(JsInt.of(2),
                                                                            Jsons.mutable.object.of("a",
                                                                                                    NULL
                                                                                                   )
                                                                           )
                                                    );

        final Iterator<JsElem> iterator1 = _arr_.iterator();
        while (iterator1.hasNext())
        {
            JsElem next = iterator1.next();
            Assertions.assertTrue(_arr_.containsElem(next));

        }
        Assertions.assertTrue(arr.containsElem(JsInt.of(1)));
        Assertions.assertTrue(_arr_.containsElem(JsInt.of(1)));
        Assertions.assertFalse(arr.containsElem(JsInt.of(2)));
        Assertions.assertFalse(_arr_.containsElem(JsInt.of(2)));
        Assertions.assertTrue(arr.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(_arr_.containsElem_(JsInt.of(2)));
        Assertions.assertTrue(arr.containsElem_(NULL));
        Assertions.assertTrue(_arr_.containsElem_(NULL));
        Assertions.assertTrue(arr.containsPath(JsPath.path("/1/1/a")));
        Assertions.assertTrue(_arr_.containsPath(JsPath.path("/1/1/a")));
        Assertions.assertTrue(arr.containsPath(JsPath.fromIndex(1)));
        Assertions.assertTrue(_arr_.containsPath(JsPath.fromIndex(1)));
        Assertions.assertTrue(arr.containsPath(JsPath.fromIndex(0)));
        Assertions.assertTrue(_arr_.containsPath(JsPath.fromIndex(0)));
        Assertions.assertFalse(arr.containsPath(JsPath.fromIndex(3)));
        Assertions.assertFalse(_arr_.containsPath(JsPath.fromIndex(3)));
        Assertions.assertFalse(arr.containsPath(JsPath.path("/1/b")));
        Assertions.assertFalse(_arr_.containsPath(JsPath.path("/1/b")));
    }

    @Test
    public void test_add_element_into_non_empty_immutable_array()
    {
        final JsArray arr = Jsons.immutable.array.of(1);

        final JsArray newArr = arr.add(JsPath.fromIndex(0),
                                       JsInt.of(2)
                                      );

        Assertions.assertEquals(OptionalInt.of(2),
                                newArr.getInt(JsPath.fromIndex(0))
                               );

        Assertions.assertEquals(2,
                                newArr.size()
                               );

        Assertions.assertEquals(1,
                                arr.size()
                               );

        JsArray arr2 = Jsons.immutable.array.of(Jsons.immutable.array.of(Jsons.immutable.array.of("a",
                                                                                                  "c",
                                                                                                  "d"
                                                                                                 )));

        Assertions.assertEquals(Jsons.immutable.array.of("a",
                                                         "b",
                                                         "c",
                                                         "d"
                                                        ),
                                arr2.add(JsPath.path("/0/0/1"),
                                         JsStr.of("b")
                                        )
                                    .getArray(JsPath.path("/0/0"))
                                    .get()
                               );
    }

    @Test
    public void test_add_element_into_non_empty_mutable_array()
    {
        final JsArray arr = Jsons.mutable.array.of(1);

        arr.add(JsPath.fromIndex(0),
                JsInt.of(2)
               );

        Assertions.assertEquals(OptionalInt.of(2),
                                arr.getInt(JsPath.fromIndex(0))
                               );

        Assertions.assertEquals(2,
                                arr.size()
                               );
        JsArray arr2 = Jsons.mutable.array.of(Jsons.mutable.array.of(Jsons.mutable.array.of("a",
                                                                                            "c",
                                                                                            "d"
                                                                                           )));
        Assertions.assertEquals(Jsons.mutable.array.of("a",
                                                       "b",
                                                       "c",
                                                       "d"
                                                      ),
                                arr2.add(JsPath.path("/0/0/1"),
                                         JsStr.of("b")
                                        )
                                    .getArray(JsPath.path("/0/0"))
                                    .get()
                               );
    }

    @Test
    public void test_add_element_into_empty_immutable_array()
    {
        final JsArray arr = Jsons.immutable.array.empty();

        final JsArray newArr = arr.add(JsPath.fromIndex(0),
                                       JsInt.of(2)
                                      );

        Assertions.assertEquals(OptionalInt.of(2),
                                newArr.getInt(JsPath.fromIndex(0))
                               );


        final JsArray newArr1 = newArr.add(JsPath.fromIndex(-1),
                                           JsInt.of(3)
                                          );

        Assertions.assertEquals(OptionalInt.of(3),
                                newArr1.getInt(JsPath.fromIndex(1))
                               );

        Assertions.assertEquals(1,
                                newArr.size()
                               );
    }

    @Test
    public void test_add_element_into_empty_mutable_array()
    {
        final JsArray arr = Jsons.mutable.array.empty();

        arr.add(JsPath.fromIndex(0),
                JsInt.of(2)
               );

        Assertions.assertEquals(OptionalInt.of(2),
                                arr.getInt(JsPath.fromIndex(0))
                               );


        arr.add(JsPath.fromIndex(-1),
                JsInt.of(3)
               );

        Assertions.assertEquals(OptionalInt.of(2),
                                arr.getInt(JsPath.fromIndex(0))
                               );
        Assertions.assertEquals(OptionalInt.of(3),
                                arr.getInt(JsPath.fromIndex(1))
                               );

    }

    @Test
    public void test_add_element_into_immutable_array_with_errors()
    {

        final UserError error = Assertions.assertThrows(UserError.class,
                                                        () -> Jsons.immutable.array.empty()
                                                                                   .add(1,
                                                                                        JsStr.of("hi")
                                                                                       )
                                                       );

        Assertions.assertEquals("Index out of bounds applying 'add'. Index: 1. Size of the array: 0. Suggestion: call the size method to know the length of the array before doing anything.",
                                error.getMessage()
                               );

        final UserError error1 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.array.empty()
                                                                                    .add(JsPath.path("/0/a"),
                                                                                         JsStr.of("hi")
                                                                                        )
                                                        );

        Assertions.assertEquals("Parent not found at /0 while applying add in []. Suggestion: either check if the parent exists or call the put method, which always does the insertion.",
                                error1.getMessage()
                               );


        final UserError error2 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.array.of(Jsons.immutable.array.of(Jsons.immutable.array.of(1)))
                                                                                    .add(JsPath.path("/0/a"),
                                                                                         JsStr.of("hi")
                                                                                        )
                                                        );

        Assertions.assertEquals("Trying to add the key 'a' in an array. add operation can not be applied in [[[1]]] at /0/a. Suggestion: call get(path).isObj() before.",
                                error2.getMessage()
                               );

        final UserError error3 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                                                                  JsInt.of(1)
                                                                                                                 ))
                                                                                    .add(JsPath.path("/0/0"),
                                                                                         JsStr.of("hi")
                                                                                        )
                                                        );

        Assertions.assertEquals("Trying to add at the index '0' in an object. add operation can not be applied in [{\"a\":1}] at /0/0. Suggestion: call get(path).isArray() before.",
                                error3.getMessage()
                               );


        final UserError error4 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.immutable.array.of(JsStr.of("a"))
                                                                                    .add(JsPath.path("/0/0"),
                                                                                         JsStr.of("hi")
                                                                                        )
                                                        );

        Assertions.assertEquals("Element located at '/0' is not a Json. add operation can not be applied in [\"a\"] at /0/0. Suggestion: call get(path).isJson() before.",
                                error4.getMessage()
                               );


    }

    @Test
    public void test_add_element_into_mutable_array_with_errors()
    {

        final UserError error = Assertions.assertThrows(UserError.class,
                                                        () -> Jsons.mutable.array.empty()
                                                                                 .add(1,
                                                                                      JsStr.of("hi")
                                                                                     )
                                                       );

        Assertions.assertEquals("Index out of bounds applying 'add'. Index: 1. Size of the array: 0. Suggestion: call the size method to know the length of the array before doing anything.",
                                error.getMessage()
                               );

        final UserError error1 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.array.empty()
                                                                                  .add(JsPath.path("/0/a"),
                                                                                       JsStr.of("hi")
                                                                                      )
                                                        );

        Assertions.assertEquals("Parent not found at /0 while applying add in []. Suggestion: either check if the parent exists or call the put method, which always does the insertion.",
                                error1.getMessage()
                               );


        final UserError error2 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.array.of(Jsons.mutable.array.of(Jsons.mutable.array.of(1)))
                                                                                  .add(JsPath.path("/0/a"),
                                                                                       JsStr.of("hi")
                                                                                      )
                                                        );

        Assertions.assertEquals("Trying to add the key 'a' in an array. add operation can not be applied in [[[1]]] at /0/a. Suggestion: call get(path).isObj() before.",
                                error2.getMessage()
                               );

        final UserError error3 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.array.of(Jsons.mutable.object.of("a",
                                                                                                              JsInt.of(1)
                                                                                                             ))
                                                                                  .add(JsPath.path("/0/0"),
                                                                                       JsStr.of("hi")
                                                                                      )
                                                        );

        Assertions.assertEquals("Trying to add at the index '0' in an object. add operation can not be applied in [{\"a\":1}] at /0/0. Suggestion: call get(path).isArray() before.",
                                error3.getMessage()
                               );


        final UserError error4 = Assertions.assertThrows(UserError.class,
                                                         () -> Jsons.mutable.array.of(JsStr.of("a"))
                                                                                  .add(JsPath.path("/0/0"),
                                                                                       JsStr.of("hi")
                                                                                      )
                                                        );

        Assertions.assertEquals("Element located at '/0' is not a Json. add operation can not be applied in [\"a\"] at /0/0. Suggestion: call get(path).isJson() before.",
                                error4.getMessage()
                               );


    }

    @Test
    public void test_passing_empty_path()
    {
        JsArray arr = Jsons.immutable.array.of(Jsons.immutable.object.of("a",
                                                                         JsStr.of("a")
                                                                        ),
                                               JsStr.of("a")
                                              );


        final UserError error = Assertions.assertThrows(UserError.class,
                                                        () -> arr.add(JsPath.empty(),
                                                                      JsStr.of("a")
                                                                     )
                                                       );
        Assertions.assertEquals("Empty path calling add method. Suggestion: check that the path is not empty calling path.isEmpty().",
                                error.getMessage()
                               );

        System.out.println(arr.prepend(JsPath.empty(),
                                       JsInt.of(1)
                                      ));

    }

}
