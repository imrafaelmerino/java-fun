package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

abstract class TestMutableMap
{

    private final Supplier<MutableMap> empty;

    public TestMutableMap(final Supplier<MutableMap> empty)
    {
        this.empty = empty;
    }

    @Test
    public void test_add_key()
    {

        final MutableMap map = empty.get();

        map.update("color",
                   JsStr.of("red")
                  );

        Assertions.assertEquals(JsStr.of("red"),
                                map.get("color")
                               );


    }

    @Test
    public void test_remove_one_key_map_is_empty()
    {

        final MutableMap map = empty.get();
        map.update("color",
                   JsStr.of("red")
                  );

        map.remove("color");

        Assertions.assertTrue(map.isEmpty());


    }

    @Test
    public void tail_returns_all_elements_but_head()
    {
        final MutableMap map = empty.get();

        map.update("color",
                   JsStr.of("red")
                  );
        map.update("size",
                   JsInt.of(10)
                  );
        map.update("height",
                   JsStr.of("tall")
                  );

        final Map.Entry<String, JsElem> head = map.head();

        final MutableMap tail = map.tail(head.getKey());

        Assertions.assertFalse(tail.contains(head.getKey()));

    }

    @Test
    public void keys_of_inserted_pairs_are_returned()
    {

        Assertions.assertTrue(empty.get()
                                   .isEmpty());

        final MutableMap map = empty.get();
        map.update("color",
                   JsStr.of("red")
                  );
        map.update("size",
                   JsInt.of(10)
                  );
        map.update("height",
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

        final MutableMap map = empty.get();
        map.update("color",
                   JsStr.of("red")
                  );
        map.update("size",
                   JsInt.of(10)
                  );
        map.update("height",
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

        final MutableMap map = empty.get();
        map.update("color",
                   JsStr.of("red")
                  );
        map.update("size",
                   JsInt.of(10)
                  );
        map.update("height",
                   JsStr.of("tall")
                  );

        final Iterator<Map.Entry<String, JsElem>> iterator = map.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> next = iterator.next();
            Assertions.assertEquals(next.getValue(),
                                    map.get(next.getKey())
                                   );
        }
    }

    @Test
    public void test_empty_has_size_zero()
    {
        Assertions.assertEquals(0,
                                empty.get()
                                     .size()
                               );
    }

    @Test
    public void test_empty_has_size_one_after_adding()
    {
        final MutableMap map = empty.get();
        map.update("color",
                   JsStr.of("red")
                  );
        Assertions.assertEquals(1,
                                map.size()
                               );
    }

    @Test
    public void test_tail_has_same_reference(){
        final MutableMap map = empty.get();
        map.update("color",
                   JsStr.of("red")
                  );
        map.update("size",
                   JsInt.of(10)
                  );
        map.update("height",
                   JsStr.of("tall")
                  );

        final MutableMap tail = map.tail(map.head()
                                            .getKey());
        final Iterator<Map.Entry<String, JsElem>> iterator = tail.iterator();
        while(iterator.hasNext()){
            final Map.Entry<String, JsElem> next = iterator.next();

            Assertions.assertSame(map.get(next.getKey()),next.getValue());

        }
    }

    @Test
    public void test_copy(){
        final MutableMap map = empty.get();
        map.update("color",
                   JsStr.of("red")
                  );
        map.update("size",
                   JsInt.of(10)
                  );
        map.update("height",
                   JsStr.of("tall")
                  );

        Assertions.assertNotSame(map,map.copy());
        Assertions.assertNotSame(map,map.copy());
    }

}
