package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

abstract class TestImmutableSet
{

    final ImmutableSeq seq;

    public TestImmutableSet(final ImmutableSeq seq)
    {
        this.seq = seq;
    }

    @Test
    public void one_element_appended()
    {
        Assertions.assertEquals(JsInt.of(1),
                                seq.appendBack(JsInt.of(1))
                                   .head()
                               );

        Assertions.assertEquals(JsInt.of(1),
                                seq.appendFront(JsInt.of(1))
                                   .head()
                               );
    }

    //appendback and appendfront instert at any arbitrary position, there's no notion of order in a set
    @Test
    public void two_elements_appended_are_contained()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2));
        Assertions.assertTrue(xs.contains(JsInt.of(1)));
        Assertions.assertTrue(xs.contains(JsInt.of(2)));
    }

    @Test
    public void one_element_appended_at_the_front()
    {
        Assertions.assertEquals(JsInt.of(1),
                                seq.appendFront(JsInt.of(1))
                                   .head()
                               );
    }

    @Test
    public void head_is_not_cotained_in_tail()
    {

        final ImmutableSeq seq = this.seq.appendBack(JsStr.of("a"))
                                         .
                                         appendBack(JsStr.of("b"))
                                         .
                                         appendBack(JsStr.of("c"));

        Assertions.assertFalse(seq.tail().contains(seq.head()));
    }

    @Test
    public void remove_element_by_index_throws_error()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2));

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> xs.remove(0)
                               );
    }


    @Test
    public void add_element_by_index_throws_error()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(3));

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> xs.add(0,
                                             JsInt.of(2)
                                            )
                               );
    }

    @Test
    public void test_update_an_element_by_position_throws_error()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2));

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> xs.update(0,
                                                JsInt.of(0)
                                               )
                               );


    }

    @Test
    public void test_get_by_index_throws_error()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2))
                                   .appendBack(JsInt.of(3));

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> xs.get(0)
                               );
    }


}
