package jsonvalues;

/**
 * Exception that models an internal error made by a developer of json-values. An issue in GitHub should be open reporting
 * the exception message.
 */
final class JsValuesInternalError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private JsValuesInternalError(final String message) {
        super(String.format("Create an issue in https://github.com/imrafaelmerino/values: %s.",
                            message
<<<<<<< HEAD
                           ));
=======
        ));
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    static JsValuesInternalError arrayOptionNotImplemented(final String option) {
        return new JsValuesInternalError(String.format("New option %s in enum JsArray.TYPE not implemented.",
                                                       option
        ));
    }

    static JsValuesInternalError tokenNotExpected(String token) {
        return new JsValuesInternalError(String.format("token %s not expected during parsing",
                                                       token
        ));
    }
}
