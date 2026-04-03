package fun.gen;

/**
 * Base exception type for generation-time failures in {@code java-fun}.
 */
public class GenerationException extends RuntimeException {

    public GenerationException(final String message) {
        super(message);
    }

    public GenerationException(final String message,
                               final Throwable cause) {
        super(message,
              cause);
    }
}
