package jsonvalues;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.lang.String.format;

class Errors
{

    static UnaryOperator<JsElem> errorIfMutableArg = e -> errorIfMutable(() -> format("All the args have to be immutable. Mutable arg found: %s ",
                                                                                      e
                                                                                     )).apply(e);
    static UnaryOperator<JsElem> errorIfImmutableArg = e -> errorIfImmutable(() -> format("All the args have to be mutable. Immutable arg found: %s",
                                                                                          e
                                                                                         )).apply(e);

    static <T extends Collection<? extends JsElem>> UnaryOperator<T> errorIfAnyMutableInOf()

    {
        return l -> Errors.<T>errorIfAnyMutable(e -> format("The list contains a mutable element: %s",
                                                            e
                                                           )).apply(l);
    }


    static <T extends Collection<? extends JsElem>> UnaryOperator<T> errorIfAnyImmutableInOf()
    {
        return l -> Errors.<T>errorIfAnyImmutable(e -> format("The list contains an immutable element: %s",
                                                              e
                                                             )).apply(l);
    }

    private static UnaryOperator<JsElem> errorIfMutable(Supplier<String> message)
    {
        return errorIf(e -> e.isJson() && e.asJson()
                                           .isMutable(),
                       message
                      );
    }

    private static UnaryOperator<JsElem> errorIfImmutable(Supplier<String> message)
    {
        return errorIf(e -> e.isJson() && e.asJson()
                                           .isImmutable(),
                       message
                      );

    }

    private static UnaryOperator<JsElem> errorIf(Predicate<JsElem> predicate,
                                                 Supplier<String> message
                                                )
    {
        return e ->
        {
            if (predicate.test(e)) throw new UnsupportedOperationException(message.get());
            return e;
        };
    }

    private static <T extends Collection<? extends JsElem>> UnaryOperator<T> errorIfAnyMutable(Function<JsElem, String> message)
    {
        return l ->
        {
            for (JsElem e : l)
            {
                errorIfMutable(() -> message.apply(e)).apply(e);
            }
            return l;
        };
    }

    private static <T extends Collection<? extends JsElem>> UnaryOperator<T> errorIfAnyImmutable(Function<JsElem, String> message)
    {
        return l ->
        {
            for (JsElem e : l)
            {
                errorIfImmutable(() -> message.apply(e)).apply(e);
            }
            return l;
        };
    }
}
