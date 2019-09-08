package jsonvalues;

interface MySeq<V extends MySeq<V, M>, M extends MyMap<M, V>> extends Iterable<JsElem>
{
    /**
     returns the first element of the seq. It's called by the library only if the seq is not empty
     @return the first element of the seq
     */
    JsElem head();

    /*
     returns the tail of the seq (all the elements but the head). It's called by the library only if
     the seq is not empty
     @return the tail of the seq
     */
    V tail();

    /*
     returns all the elements of the seq but the last one. It's called by the library only if the seq
     is not empty
     @return all the elements of the seq but the last one
    */
    V init();

    /**
     returns the last element of the seq. It's called by the library only if the seq is not empty
     @return the last element of the seq
     */
    JsElem last();

    /**
     returns the element located at the index. It's called by the library only if the index exists
     @param index the given index
     @return the element located at the index
     */
    JsElem get(final int index);

    /**
     returns the size of the seq
     @return the size of the seq
     */
    int size();

    /**
     returns true if this seq is empty
     @return true if empty, false otherwise
     */
    boolean isEmpty();

    /**
     returns true if the seq contains the element
     @param e the given element
     @return true if the seq contains the element, false otherwise
     */
    boolean contains(final JsElem e);

    /**
     returns an empty seq of the same type
     @return an empty seq of the same type
     */
    V empty();

    /**
     appends the element to the front of the seq
     @param elem the given element
     @return a seq
     */
    V appendFront(final JsElem elem);

    /**
     appends the element to the back of the seq
     @param elem the given element
     @return a seq
     */
    V appendBack(final JsElem elem);

    /**
     updates the element located at the index with a new element. It will be called by the library only
     if the index exists
     @param index the given index
     @param ele the given element
     @return an updated seq
     */
    V update(final int index,
             final JsElem ele
            );

    /**
     adds an element at the index, shifting elements at greater or equal indexes one position to the right.
     It's called by the library only if the index exists
     @param index the given index
     @param ele the given element
     @return a seq
     */
    V add(final int index,
          final JsElem ele
         );

    /**
     removes the element located at the index. It will be called by the library only
     if the index exists
     @param index the given index
     @return a seq with the element located at the index removed
     */
    V remove(final int index);

    /**
     returns an empty map. Every data structure defined to model a Json array has its dual to model a Json object
     and has to be defined when creating a Json factory.
     @return en empty map of the dual data structure to model JsObj
     */
    M emptyObject();

}
