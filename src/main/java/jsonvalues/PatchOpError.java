package jsonvalues;

public final class PatchOpError extends Exception
{
    private PatchOpError(final String message)
    {
        super(message);
    }
}
