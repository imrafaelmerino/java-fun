package fun.gen;

/**
 * A runtime exception indicating that the type of a value associated with a key in a Record does not match the expected type.
 * This exception is typically thrown when using the `getXXX` methods of the Record class to retrieve values of specific types.
 */
public final class RecordTypeNotExpected extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private static final String TEMPLATE =
            "The type you expect of the value associated to the key %s is %s, however the " +
                    "real type is %s. Consider using another getXXX method or the generator is not producing the expected values";

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
                           typeExpected,
                           key,
                           realType.getName()));
    }
}
