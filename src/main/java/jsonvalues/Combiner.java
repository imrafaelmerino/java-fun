package jsonvalues;

 abstract class Combiner<T>
{
    final T a;
    final T b;

     Combiner(final T a,
             final T b
            )
    {
        this.a = a;
        this.b = b;
    }

    Trampoline<? extends Json<?>> combine(Json<?> c,
                                          Json<?> d
                                         )
    {
        if (c.isObj() && d.isObj()) return new ObjCombiner(c.asJsObj(),
                                                           d.asJsObj()
        ).combine();
        return new ArrCombiner(c.asJsArray(),
                               d.asJsArray()
        ).combine();
    }

     abstract Trampoline<T> combine();
}
