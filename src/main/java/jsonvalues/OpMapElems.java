package jsonvalues;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class OpMapElems<T>
{
    T json;

    OpMapElems(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> map(final Function<? super JsPair, ? extends JsElem> fn,
                               final Predicate<? super JsPair> predicate,
                               final JsPath path
                              );

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    abstract Trampoline<T> map_(final Function<? super JsPair, ? extends JsElem> fn,
                                final Predicate<? super JsPair> predicate,
                                final JsPath path
                               );


}
