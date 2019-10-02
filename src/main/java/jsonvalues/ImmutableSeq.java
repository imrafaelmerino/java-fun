package jsonvalues;

/**
 Represents an immutable data structure where elements of a JsArray are stored.
 */
abstract class ImmutableSeq extends MySeq<ImmutableSeq>
{


    /**
     appends the element to the front of the seq
     @param elem the given element
     @return a seq
     */
     abstract ImmutableSeq appendFront(final JsElem elem);

    /**
     appends the element to the back of the seq
     @param elem the given element
     @return a seq
     */
     abstract ImmutableSeq appendBack(final JsElem elem);

    /**
     updates the element located at the index with a new element. It will be called by the library only
     if the index exists
     @param index the given index
     @param ele the given element
     @return an updated seq
     */
     abstract ImmutableSeq update(final int index,
                                        final JsElem ele
                                       );

    /**
     adds an element at the index, shifting elements at greater or equal indexes one position to the right.
     It's called by the library only if the index exists
     @param index the given index
     @param ele the given element
     @return a seq
     */
     abstract ImmutableSeq add(final int index,
                                     final JsElem ele
                                    );

    /**
     removes the element located at the index. It will be called by the library only
     if the index exists
     @param index the given index
     @return a seq with the element located at the index removed
     */
     abstract ImmutableSeq remove(final int index);





}
