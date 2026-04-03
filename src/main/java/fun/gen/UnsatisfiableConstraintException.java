package fun.gen;

/**
 * Thrown when a filtered or constrained generator cannot satisfy its predicate within the configured budget.
 */
public final class UnsatisfiableConstraintException extends GenerationException {

    public UnsatisfiableConstraintException(final String message) {
        super(message);
    }
}
