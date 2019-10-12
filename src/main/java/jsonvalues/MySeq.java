package jsonvalues;

/**
 It's an abstract class and not an interface for performance reason. The bytecode instruction invokeinterface
 is slower than invokedinamic, and from the point of view of the design, it's not a public class that
 will be extended by anyone
 */
@SuppressWarnings("squid:S1610")
abstract class MySeq<V extends MySeq<V>> implements Iterable<JsElem>
{
    /**
     returns the first element of the seq. It's called by the library only if the seq is not empty
     @return the first element of the seq
     */
     abstract JsElem head();

    /*
     returns the tail of the seq (all the elements but the head). It's called by the library only if
     the seq is not empty
     @return the tail of the seq
     */
     abstract V tail();

    /*
     returns all the elements of the seq but the last one. It's called by the library only if the seq
     is not empty
     @return all the elements of the seq but the last one
    */
     abstract V init();

    /**
     returns the last element of the seq. It's called by the library only if the seq is not empty
     @return the last element of the seq
     */
     abstract JsElem last();

    /**
     returns the element located at the index. It's called by the library only if the index exists
     @param index the given index
     @return the element located at the index
     */
     abstract JsElem get(final int index);

    /**
     returns the size of the seq
     @return the size of the seq
     */
     abstract int size();

    /**
     returns true if this seq is empty
     @return true if empty, false otherwise
     */
     abstract boolean isEmpty();

    /**
     returns true if the seq contains the element
     @param e the given element
     @return true if the seq contains the element, false otherwise
     */
     abstract boolean contains(final JsElem e);
}
