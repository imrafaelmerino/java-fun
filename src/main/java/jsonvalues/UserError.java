package jsonvalues;

/**
 Exception that models a programming error made by the user. The user has a bug in their code and something
 has to be fixed. Part of the exception message is a suggestion to fix the bug.
 */
public final class UserError extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private static final String GUARD_ARR_CONDITION_SUGGESTION = "use the guard condition arr.isEmpty() before";
    private static final String GUARD_OBJ_CONDITION_SUGGESTION = "use the guard condition obj.isEmpty() before";
    private static final String GENERAL_MESSAGE = "%s. Suggestion: %s.";

    private UserError(final String message)
    {
        super(message);
    }

    static <T extends JsElem> UserError immutableArgExpected(T arg)
    {

        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("Mutable object found: %s",
                                                         arg
                                                        ),
                                           "create an immutable object instead. Don't use _xxx_ methods"
                                          ));
    }

    static UserError isNotAJsBool(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBool expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBool() before invoking asJsBool()"
                                          ));
    }

    static UserError isNotAJsInt(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsInt expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isInt() before invoking asJsInt()"
                                          ));
    }

    static UserError isNotAJsLong(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsLong expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isLong() or isInt() before invoking asJsLong()"
                                          ));
    }

    static UserError isNotAJsDouble(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsDouble expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isDouble() or isDecimal() before invoking asJsDouble()"
                                          ));
    }

    static UserError isNotAJsBigInt(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBigInt expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBigInt() or isIntegral() before invoking asJsBigInt()"
                                          ));
    }

    static UserError isNotAJsBigDec(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBigDec expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBigDec() or isDecimal() before invoking asJsBigDec()"
                                          ));
    }

    static UserError isNotAJsArray(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsArray expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isArray() before invoking asJsArray()"
                                          ));
    }

    static UserError isNotAJsObj(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsObj expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isObj() before invoking asJsObj()"
                                          ));
    }

    static UserError isNotAJson(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("Json expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isJson() or isArray() or isObj() before invoking asJson()"
                                          ));
    }

    static UserError isNotAJsString(final JsElem elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsStr expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isStr() before invoking asJsStr()"
                                          ));
    }

    static UserError lastOfEmptyPath()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "last() of empty path",
                                           "call the guard condition isEmpty() before invoking last()"
                                          ));
    }

    static UserError headOfEmptyPath()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "head() of empty path",
                                           "call the guard condition isEmpty() before invoking head()"
                                          ));
    }

    static UserError tailOfEmptyPath()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "tail() of empty path",
                                           "call the guard condition isEmpty() before invoking tail()"
                                          ));
    }

    static UserError initOfEmptyPath()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "init() of empty path",
                                           "call the guard condition isEmpty() before invoking init()"
                                          ));
    }

    static <T extends JsElem> UserError mutableArgExpected(T arg)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("Immutable object found: %s",
                                                         arg
                                                        ),
                                           "create a mutable object instead using _xxx_ methods"
                                          ));
    }

    static UserError headOfEmptyObj()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "head of empty map",
                                           GUARD_OBJ_CONDITION_SUGGESTION
                                          ));
    }

    static UserError tailOfEmptyObj()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "tail of empty map",
                                           GUARD_OBJ_CONDITION_SUGGESTION
                                          ));
    }

    static UserError headOfEmptyArr()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "head of empty vector",
                                           GUARD_ARR_CONDITION_SUGGESTION
                                          ));
    }


    static UserError tailOfEmptyArr()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "tail of empty vector",
                                           GUARD_ARR_CONDITION_SUGGESTION
                                          ));
    }

    static UserError lastOfEmptyArr()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "last of empty vector",
                                           GUARD_ARR_CONDITION_SUGGESTION
                                          ));
    }

    static UserError initOfEmptyObj()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "init of empty obj",
                                           GUARD_OBJ_CONDITION_SUGGESTION
                                          ));
    }

    static UserError initOfEmptyArr()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "init of empty vector",
                                           GUARD_ARR_CONDITION_SUGGESTION
                                          ));
    }

    static UserError asKeyOfIndex()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "asKey() of index",
                                           "use the guard condition position.isKey() before"
                                          ));
    }

    static UserError asIndexOfKey()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "asIndex() of key",
                                           "use the guard condition position.isIndex() before"
                                          ));
    }

    static UserError incOfKey()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "inc() of key",
                                           "use the guard condition last().isIndex() before invoking inc()"
                                          ));
    }

    static UserError decOfKey()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "dec() of key",
                                           "use the guard condition last().isIndex() before invoking dec()"
                                          ));
    }

    static UserError trampolineNotCompleted()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "trampoline not completed",
                                           "Before calling the method get() on a trampoline, make sure a Trampoline.done() status is returned"
                                          ));
    }

     static UserError unsupportedOperationOnlist(final Class<?> listClass)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "Unsupported operation supported by the list from which the JsArray was created.",
                                           String.format("Is the list %s unmodifiable?",listClass)
                                          ));
    }
}
