package jsonvalues;

import java.util.stream.Stream;

interface MySeq<V extends MySeq<V, M>, M extends MyMap<M, V>> extends Iterable<JsElem>
{
    JsElem head();

    V tail();

    V init();

    JsElem last();

    JsElem get(int index);

    int size();

    boolean isEmpty();

    boolean contains(JsElem e);

    Stream<JsElem> stream();

    V empty();

    V add(java.util.Collection<? extends JsElem> list);

    V appendFront(JsElem elem);

    V appendBack(JsElem elem);

    V update(int index,
             JsElem ele
            );

    V add(int index,
          JsElem ele
         );

    V remove(int index);

    M emptyObject();

}
