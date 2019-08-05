package jsonvalues;

import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.ifJsonElse;
import static jsonvalues.Trampoline.more;

abstract class AbstractJson
{

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsObj> put_(final JsPath path,
                                  final Trampoline<Trampoline<? extends Json<?>>> head,
                                  final Trampoline<Trampoline<JsObj>> tail

                                 )

    {
        return more(tail).flatMap(json -> head.get()
                                              .map(it ->
                                                   json.put(path,
                                                            it
                                                           )
                                                  )
                                 );
    }

    static <T> Optional<T> reduceElem(final JsPair p,
                                      final BinaryOperator<T> op,
                                      final Function<? super JsPair, T> fn,
                                      final Optional<T> result
                                     )
    {
        final T mapped = fn.apply(p);

        final Optional<T> t = result.map(it -> op.apply(it,
                                                        mapped
                                                       ));
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);

    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static <T> Trampoline<Optional<T>> reduce_(final Json<?> json,
                                               final BinaryOperator<T> op,
                                               final Function<? super JsPair, T> fn,
                                               final Predicate<? super JsPair> predicate,
                                               final JsPath headPath,
                                               final Optional<T> result

                                              )
    {
        if (json.isObj()) return AbstractJsObj.reduce_(json.asJsObj(),
                                                       op,
                                                       fn,
                                                       predicate,
                                                       headPath,
                                                       result
                                                      );
        return AbstractJsArray.reduce_(json.asJsArray(),
                                       op,
                                       fn,
                                       predicate,
                                       headPath.index(-1),
                                       result
                                      );
    }


    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsObj> mapHead_(final Function<? super JsPair, ? extends JsElem> fn,
                                      final Predicate<? super JsPair> predicate,
                                      final Map.Entry<String, JsElem> head,
                                      final JsPath headPath,
                                      final Trampoline<JsObj> tailCall,
                                      final Function<Json<?>, Trampoline<Trampoline<? extends Json<?>>>> headTrampoline
                                     )
    {
        return ifJsonElse(json -> put_(JsPath.of(head.getKey()),
                                       headTrampoline.apply(json),
                                       () -> tailCall
                                      ),
                          elem -> JsPair.of(headPath,
                                            elem
                                           )
                                        .ifElse(predicate,
                                                p -> AbstractJsObj.put(head.getKey(),
                                                                       fn.apply(p),
                                                                       () -> tailCall
                                                                      ),
                                                p -> AbstractJsObj.put(head.getKey(),
                                                                       elem,
                                                                       () -> tailCall
                                                                      )
                                               )
                         ).apply(head.getValue());
    }

    @SuppressWarnings("squid:S00100")
    //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsArray> appendFront_(final Trampoline<Trampoline<? extends Json<?>>> head,
                                            final Trampoline<Trampoline<JsArray>> tail
                                           )

    {
        return more(tail).flatMap(json -> head.get()
                                              .map(json::prepend));
    }

    // squid:S1452: Json<?> has only two possible types: JsObj or JsArr,
    // squid:S00100: naming convention: xx_ traverses the whole json
    // squid:S00117: ARRAY_AS  should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S1452", "squid:S00117"})
    static Trampoline<? extends Json<?>> union_(final Json<?> a,
                                                final Json<?> b,
                                                final JsArray.TYPE ARRAY_AS
                                               )
    {

        if (a.isObj() && b.isObj()) return AbstractJsObj.union_(a.asJsObj(),
                                                                b.asJsObj(),
                                                                ARRAY_AS
                                                               );
        if (ARRAY_AS == JsArray.TYPE.LIST) return AbstractJsArray.union_(a.asJsArray(),
                                                                         b.asJsArray()
                                                                        );

        return AbstractJsArray.union(a.asJsArray(),
                                     b.asJsArray(),
                                     ARRAY_AS
                                    );


    }

    //squid:S1452 -> private method not exposed to the user. the wildcard allows to refactor some code, and Json<?> has only two possible types: JsObj or JsArr
    //squid:S00100 ->  naming convention: xx_ traverses the whole json
    //squid:S00117 -> ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S1452", "squid:S00117"})
    static Trampoline<? extends Json<?>> intersection_(final Json<?> a,
                                                       final Json<?> b,
                                                       final JsArray.TYPE ARRAY_AS
                                                      )
    {

        if (a.isObj() && b.isObj()) return AbstractJsObj.intersection_(a.asJsObj(),
                                                                       b.asJsObj(),
                                                                       ARRAY_AS
                                                                      );
        if (ARRAY_AS == JsArray.TYPE.LIST) return AbstractJsArray.intersection_(a.asJsArray(),
                                                                                b.asJsArray()
                                                                               );
        return AbstractJsArray.intersection(a.asJsArray(),
                                            b.asJsArray(),
                                            ARRAY_AS
                                           );


    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<? extends Json<?>> combiner_(final Json<?> a,
                                                   final Json<?> b
                                                  )
    {

        if (a.isObj() && b.isObj()) return combiner_(a.asJsObj(),
                                                     b.asJsObj()
                                                    );
        return combiner_(a.asJsArray(),
                         b.asJsArray()
                        );


    }

    /**
     Declarative way of consuming an element based on its type
     @param ifValue the consumer to be invoked if this JsElem is a JsValue
     @param ifObj the consumer to be invoked if this JsElem is a JsObj
     @param ifArray the consumer to be invoked if this JsElem is a JsArray
     */
    Consumer<JsElem> accept(final Consumer<JsElem> ifValue,
                            final Consumer<JsObj> ifObj,
                            final Consumer<JsArray> ifArray
                           )
    {
        requireNonNull(ifValue);
        requireNonNull(ifObj);
        requireNonNull(ifArray);
        return e ->
        {
            if (e.isNotJson()) ifValue.accept(e);
            if (e.isObj()) ifObj.accept(e.asJsObj());
            if (e.isArray()) ifArray.accept(e.asJsArray());
        };

    }

}
