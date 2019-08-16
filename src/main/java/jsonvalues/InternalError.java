package jsonvalues;

/**
 Exception that models an internal error made by a developer. An issue in GitHub should be open reporting
 the exception message.
 */
public class InternalError extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private static final String GENERAL_MESSAGE = "Create an issue in https://github.com/imrafaelmerino/values: %s.";

    private InternalError(final String message)
    {
        super(String.format(GENERAL_MESSAGE,
                            message
                           ));
    }

    static InternalError arrayOptionNotImplemented(final String option)
    {
        return new InternalError(String.format("New option %s in enum JsArray.TYPE not implemented.",
                                               option
                                              ));
    }

    static InternalError keyNotFound(final String key)
    {
        return new InternalError(String.format("key %s not found in map. Use contains(key) before of the method getOptional(key).",
                                               key
                                              ));
    }

    static InternalError tokenNotExpected(String token)
    {
        return new InternalError(String.format("token %s not expected during parsing",
                                               token
                                              ));
    }
}
