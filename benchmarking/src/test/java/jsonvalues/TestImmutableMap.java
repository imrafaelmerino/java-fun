package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;

abstract class TestImmutableMap
{

    private final ImmutableMap empty;

    public TestImmutableMap(final ImmutableMap empty)
    {
        this.empty = empty;
    }

    @Test
    public void test_add_key()
    {

        final ImmutableMap map = empty.update("color",
                                              JsStr.of("red")
                                             );

        Assertions.assertEquals(JsStr.of("red"),
                                map.get("color")
                               );


    }

    @Test
    public void test_remove_one_key_map_is_empty()
    {

        final ImmutableMap map = empty.update("color",
                                              JsStr.of("red")
                                             );

        final ImmutableMap empty = map.remove("color");


        Assertions.assertTrue(empty.isEmpty());


    }

    @Test
    public void tail_returns_all_elements_but_head()
    {

        final ImmutableMap map = empty.update("color",
                                              JsStr.of("red")
                                             )
                                      .update("size",
                                              JsInt.of(10)
                                             )
                                      .update("height",
                                              JsStr.of("tall")
                                             );

        final Map.Entry<String, JsElem> head = map.head();

        final ImmutableMap tail = map.tail(head.getKey());

        Assertions.assertFalse(tail.contains(head.getKey()));

    }

    @Test
    public void keys_of_inserted_pairs_are_returned()
    {

        Assertions.assertTrue(empty.isEmpty());

        final ImmutableMap map = empty.update("color",
                                              JsStr.of("red")
                                             )
                                      .update("size",
                                              JsInt.of(10)
                                             )
                                      .update("height",
                                              JsStr.of("tall")
                                             );

        Assertions.assertTrue(map.keys()
                                 .contains("color"));
        Assertions.assertTrue(map.keys()
                                 .contains("size"));
        Assertions.assertTrue(map.keys()
                                 .contains("height"));
        Assertions.assertEquals(3,
                                map.keys()
                                   .size()
                               );
    }

    @Test
    public void test_get_key_inserted()
    {

        final ImmutableMap map = empty.update("color",
                                              JsStr.of("red")
                                             )
                                      .update("size",
                                              JsInt.of(10)
                                             )
                                      .update("height",
                                              JsStr.of("tall")
                                             );

        Assertions.assertEquals(JsStr.of("red"),
                                map.get("color")
                               );
        Assertions.assertEquals(JsInt.of(10),
                                map.get("size")
                               );
        Assertions.assertEquals(JsStr.of("tall"),
                                map.get("height")
                               );
    }

    @Test
    public void test_iterator()
    {

        final ImmutableMap map = empty.update("color",
                                              JsStr.of("red")
                                             )
                                      .update("size",
                                              JsInt.of(10)
                                             )
                                      .update("height",
                                              JsStr.of("tall")
                                             );

        final Iterator<Map.Entry<String, JsElem>> iterator = map.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> next = iterator.next();
            Assertions.assertEquals(next.getValue(),
                                    map.get(next.getKey()));
        }
    }

    @Test
    public void test_empty_has_size_zero()
    {
        Assertions.assertEquals(0,
                                empty.size());
    }

    @Test
    public void test_empty_has_size_one_after_adding()
    {
        final ImmutableMap map = empty.update("color",
                                              JsStr.of("red")
                                             );
        Assertions.assertEquals(1,
                                map.size());
    }

}
