package jsonvalues;

/**
 Exception that models a programming error made by the user. The user has a bug in their code and something
 has to be fixed. A suggestion in the message of the exception is suggested
 */
public class UserError extends RuntimeException
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

    static UserError lastOfEmptyObj()
    {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "last of empty map",
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


}
