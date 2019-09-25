package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

abstract class TestMutableSeq
{

    final Supplier<MutableSeq> seq;

    public TestMutableSeq(final Supplier<MutableSeq> seq)
    {
        this.seq = seq;
    }

    @Test
    public void one_element_appended_at_the_back()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );
    }

    @Test
    public void two_elements_appended_at_the_back()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));
        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                seq.tail()
                                   .head()
                               );
    }

    @Test
    public void one_element_appended_at_the_front()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendFront(JsInt.of(1));
        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );
    }

    @Test
    public void two_elements_appended_at_the_front()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendFront(JsInt.of(1));
        seq.appendFront(JsInt.of(2));
        Assertions.assertEquals(JsInt.of(2),
                                seq.head()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                seq.tail()
                                   .head()
                               );
    }

    @Test
    public void remove_first_element()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));
        seq.remove(0);
        Assertions.assertEquals(1,
                                seq.size()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                seq.head()
                               );
    }

    @Test
    public void remove_last_element()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));
        seq.appendBack(JsInt.of(3));

        seq.remove(2);
        Assertions.assertEquals(2,
                                seq.size()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                seq.last()
                               );
    }


    @Test
    public void remove_second_element()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));
        seq.appendBack(JsInt.of(3));

        seq.remove(1);
        Assertions.assertEquals(2,
                                seq.size()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );
        Assertions.assertEquals(JsInt.of(3),
                                seq.last()
                               );
    }

    @Test
    public void add_element_at_0()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(3));
        seq.add(0,
                JsInt.of(2)
               );

        Assertions.assertEquals(JsInt.of(2),
                                seq.head()
                               );
    }

    @Test
    public void add_element_at_1()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(3));

        seq.add(1,
                JsInt.of(2)
               );

        Assertions.assertEquals(3,
                                seq.size()
                               );

        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );

        Assertions.assertEquals(JsInt.of(2),
                                seq.tail()
                                   .head()
                               );

        Assertions.assertEquals(JsInt.of(3),
                                seq.last()
                               );
    }

    @Test
    public void add_element_at_last_position()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));

        seq.add(2,
                JsInt.of(3)
               );

        Assertions.assertEquals(3,
                                seq.size()
                               );

        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );

        Assertions.assertEquals(JsInt.of(2),
                                seq.tail()
                                   .head()
                               );

        Assertions.assertEquals(JsInt.of(3),
                                seq.last()
                               );
    }

    @Test
    public void test_update_first_element()
    {

        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));

        seq.update(0,
                   JsInt.of(0)
                  );

        Assertions.assertEquals(2,
                                seq.size()
                               );
        Assertions.assertEquals(JsInt.of(0),
                                seq.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                seq.tail()
                                   .head()
                               );
    }

    @Test
    public void test_update_last_element()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));
        seq.appendBack(JsInt.of(3));

        seq.update(2,
                   JsInt.of(0)
                  );

        Assertions.assertEquals(3,
                                seq.size()
                               );
        Assertions.assertEquals(JsInt.of(1),
                                seq.head()
                               );
        Assertions.assertEquals(JsInt.of(2),
                                seq.tail()
                                   .head()
                               );
        Assertions.assertEquals(JsInt.of(0),
                                seq.last()
                               );
    }


    @Test
    public void test_get_by_index()
    {
        final MutableSeq seq = this.seq.get();
        seq.appendBack(JsInt.of(1));
        seq.appendBack(JsInt.of(2));
        seq.appendBack(JsInt.of(3));

        Assertions.assertEquals(JsInt.of(1),
                                seq.get(0)
                               );
        Assertions.assertEquals(JsInt.of(2),
                                seq.get(1)
                               );
        Assertions.assertEquals(JsInt.of(3),
                                seq.get(2)
                               );
    }


}
