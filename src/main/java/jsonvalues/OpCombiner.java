package jsonvalues;

 abstract class OpCombiner<T>
{
    final T a;
    final T b;

     OpCombiner(final T a,
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
        if (c.isObj() && d.isObj()) return new OpCombinerObjs(c.asJsObj(),
                                                              d.asJsObj()
        ).combine();
        return new OpCombinerArrs(c.asJsArray(),
                                  d.asJsArray()
        ).combine();
    }

     abstract Trampoline<T> combine();
}
