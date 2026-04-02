package fun.gen;

/**
 * Thrown when a generator cannot produce a required number of unique values within a bounded number of tries.
 */
public final class GenerationExhaustedException extends GenerationException {

    public GenerationExhaustedException(final String message) {
        super(message);
    }
}
