package jsonvalues;

/**
 Exception that models a programming error made by the user. The user has a bug in their code and something
 has to be fixed. Part of the exception message is a suggestion to fix the bug.
 */
public final class UserError extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private static final String ERROR_DEFAULT_CONSTRUCTOR = "Define a default constructor in your implementation that creates an empty seq data structure";
    private static final String ERROR_IMMUTABLE_IMPL = "Define a default constructor in your implementation that creates an empty map data structure";
    private static final String GUARD_ARR_CONDITION_SUGGESTION = "use the guard condition arr.isEmpty() before";
    private static final String GUARD_OBJ_CONDITION_SUGGESTION = "use the guard condition obj.isEmpty() before";
    private static final String GENERAL_MESSAGE = "%s. Suggestion: %s.";

    private UserError(final String message)
    {
        super(message);
    }


    static UserError indexOutOfBounds(int size,
                                      int index,
                                      final String op
                                     )
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("Index out of bounds applying '%s'. Index: %s. Size of the array: %s",
                                                         op,
                                                         index,
                                                         size
                                                        ),
                                           "call the size method to know the length of the array before doing anything"
                                          ));
    }

    public static UserError indexWithLeadingZeros(final String token)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("index %s with leading zeros",
                                                         token
                                                        ),
                                           "removes the leading zeros"
                                          ));
    }

    static UserError isNotAJsBool(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBool expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBool() before invoking asJsBool()"
                                          ));
    }

    static UserError isNotAJsInt(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsInt expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isInt() before invoking asJsInt()"
                                          ));
    }

    static UserError isNotAJsLong(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsLong expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isLong() or isInt() before invoking asJsLong()"
                                          ));
    }

    static UserError isNotAJsDouble(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsDouble expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isDouble() or isDecimal() before invoking asJsDouble()"
                                          ));
    }

    static UserError isNotAJsBigInt(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBigInt expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBigInt() or isIntegral() before invoking asJsBigInt()"
                                          ));
    }

    static UserError isNotAJsBigDec(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBigDec expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBigDec() or isDecimal() before invoking asJsBigDec()"
                                          ));
    }

    static UserError isNotAJsArray(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsArray expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isArray() before invoking asJsArray()"
                                          ));
    }

  public static UserError isNotAJsNumber(final JsValue elem)
  {
    return new UserError(String.format(GENERAL_MESSAGE,
                                       String.format("JsNumber expected, but %s was found",
                                                     elem.getClass()
                                                    ),
                                       "call the guard condition isNumber() before invoking asJsArray()"
                                      ));
  }

    static UserError isNotAJsObj(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsObj expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isObj() before invoking asJsObj()"
                                          ));
    }

    static UserError isNotAJson(final JsValue elem)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("Json expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isJson() or isArray() or isObj() before invoking asJson()"
                                          ));
    }

    static UserError isNotAJsString(final JsValue elem)
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



    static UserError pathEmpty(final String op)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("Empty path calling %s method",
                                                         op
                                                        ),
                                           "check that the path is not empty calling path.isEmpty()"
                                          ));
    }

    static UserError pathMalformed(final String path)
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("malformed path: %s",
                                                         path
                                                        ),
                                           "Go to https://imrafaelmerino.github.io/json-values/#jspath"
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

    static <T extends JsValue> UserError mutableArgExpected(T arg)
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
                                           "head of empty seq",
                                           GUARD_ARR_CONDITION_SUGGESTION
                                          ));
    }


    static UserError tailOfEmptyArr()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "tail of empty seq",
                                           GUARD_ARR_CONDITION_SUGGESTION
                                          ));
    }

    static UserError lastOfEmptyArr()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "last of empty seq",
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
                                           "init of empty seq",
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







}
