package jsonvalues;

/**
 Represents a mutable data structure where elements of a JsArray are stored.
 */
abstract class MutableSeq extends MySeq<MutableSeq>
{


    /**
     creates and returns a copy of this sequence
     @return a new instance
     */
     abstract MutableSeq copy();

    /**
     appends the element to the front of the seq
     @param elem the given element
     */
     abstract void appendFront(final JsElem elem);

    /**
     appends the element to the back of the seq
     @param elem the given element
     */
     abstract void appendBack(final JsElem elem);

    /**
     updates the element located at the index with a new element. It will be called by the library only
     if the index exists
     @param index the given index
     @param ele the given element
     */
     abstract void update(final int index,
                                final JsElem ele
                               );

    /**
     adds an element at the index, shifting elements at greater or equal indexes one position to the right.
     It's called by the library only if the index exists
     @param index the given index
     @param ele the given element
     */
     abstract void add(final int index,
                             final JsElem ele
                            );

    /**
     removes the element located at the index. It will be called by the library only
     if the index exists
     @param index the given index
     */
     abstract void remove(final int index);


}
