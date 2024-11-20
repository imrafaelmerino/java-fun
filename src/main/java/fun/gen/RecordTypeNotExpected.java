package fun.gen;

/**
 * A runtime exception indicating that the type of value associated with a key in a Record does not match the expected type.
 * This exception is typically thrown when using the `getXXX` methods of the Record class to retrieve values of specific types.
 */
@SuppressWarnings("serial")
public final class RecordTypeNotExpected extends RuntimeException {

    @SuppressWarnings("InlineFormatString")
    private static final String TEMPLATE =
            "The anticipated data type for the value linked to the key %s is %s, yet the actual type is %s. Please contemplate utilizing an alternative getXXX method, or verify that the generator is producing the anticipated values";

    /**
     * Constructs a new RecordTypeNotExpected instance with the specified error message.
     *
     * @param message The error message indicating the type mismatch.
     */
    public RecordTypeNotExpected(String message) {
        super(message);
    }

    /**
     * Constructs a new RecordTypeNotExpected instance with information about the expected type, actual type, and field key.
     *
     * @param typeExpected The expected type as a string.
     * @param realType     The actual type as a Class object.
     * @param key          The key associated with the field where the type mismatch occurred.
     */
    public RecordTypeNotExpected(String typeExpected,
                                 Class<?> realType,
                                 String key) {
        this(String.format(TEMPLATE,
                           key,
                           typeExpected,
                           realType.getName()));
    }
}
