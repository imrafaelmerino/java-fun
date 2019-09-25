package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

abstract class TestImmutableSeq
{

    final ImmutableSeq seq;

    public TestImmutableSeq(final ImmutableSeq seq)
    {
        this.seq = seq;
    }

    @Test
    public void one_element_appended_at_the_back()
    {
        Assertions.assertEquals(JsInt.of(1),
                                seq.appendBack(JsInt.of(1))
                                   .head()
                               );
    }

    @Test
    public void two_elements_appended_at_the_back()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2));
        Assertions.assertEquals(JsInt.of(1),
                                xs.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                xs.tail()
                                  .head()
                               );
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
    public void two_elements_appended_at_the_front()
    {
        final ImmutableSeq xs = seq.appendFront(JsInt.of(1))
                                   .appendFront(JsInt.of(2));
        Assertions.assertEquals(JsInt.of(2),
                                xs.head()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                xs.tail()
                                  .head()
                               );
    }

    @Test
    public void remove_first_element()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2));

        final ImmutableSeq ys = xs.remove(0);
        Assertions.assertEquals(1,
                                ys.size()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                ys.head()
                               );
    }

    @Test
    public void remove_last_element()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2))
                                   .appendBack(JsInt.of(3));

        final ImmutableSeq ys = xs.remove(2);
        Assertions.assertEquals(2,
                                ys.size()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                ys.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                ys.last()
                               );
    }


    @Test
    public void remove_second_element()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2))
                                   .appendBack(JsInt.of(3));

        final ImmutableSeq ys = xs.remove(1);
        Assertions.assertEquals(2,
                                ys.size()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                ys.head()
                               );
        Assertions.assertEquals(JsInt.of(3),
                                ys.last()
                               );
    }

    @Test
    public void add_element_at_0()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(3));
        final ImmutableSeq ys = xs.add(0,
                                       JsInt.of(2)
                                      );

        Assertions.assertEquals(JsInt.of(2),
                                ys.head()
                               );
    }

    @Test
    public void add_element_at_1()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(3));

        final ImmutableSeq ys = xs.add(1,
                                       JsInt.of(2)
                                      );

        Assertions.assertEquals(3,
                                ys.size()
                               );

        Assertions.assertEquals(JsInt.of(1),
                                ys.head()
                               );

        Assertions.assertEquals(JsInt.of(2),
                                ys.tail()
                                  .head()
                               );

        Assertions.assertEquals(JsInt.of(3),
                                ys.last()
                               );
    }

    @Test
    public void add_element_at_last_position()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2));

        final ImmutableSeq ys = xs.add(2,
                                       JsInt.of(3)
                                      );

        Assertions.assertEquals(3,
                                ys.size()
                               );

        Assertions.assertEquals(JsInt.of(1),
                                ys.head()
                               );

        Assertions.assertEquals(JsInt.of(2),
                                ys.tail()
                                  .head()
                               );

        Assertions.assertEquals(JsInt.of(3),
                                ys.last()
                               );
    }

    @Test
    public void test_update_first_element()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2));

        final ImmutableSeq ys = xs.update(0,
                                          JsInt.of(0)
                                         );

        Assertions.assertEquals(2,
                                ys.size()
                               );
        Assertions.assertEquals(JsInt.of(0),
                                ys.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                ys.tail()
                                  .head()
                               );
    }

    @Test
    public void test_update_last_element()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2))
                                   .appendBack(JsInt.of(3));

        final ImmutableSeq ys = xs.update(2,
                                          JsInt.of(0)
                                         );

        Assertions.assertEquals(3,
                                ys.size()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                ys.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                ys.tail()
                                  .head()
                               );
        Assertions.assertEquals(JsInt.of(0),
                                ys.last()
                               );
    }


    @Test
    public void test_get_by_index()
    {
        final ImmutableSeq xs = seq.appendBack(JsInt.of(1))
                                   .appendBack(JsInt.of(2))
                                   .appendBack(JsInt.of(3));

        Assertions.assertEquals(JsInt.of(1),
                                xs.get(0)
                               );
        Assertions.assertEquals(JsInt.of(2),
                                xs.get(1)
                               );
        Assertions.assertEquals(JsInt.of(3),
                                xs.get(2)
                               );
    }


}
