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

    //squid:S1452: method not exposed to the user of the api. Avoid some duplicate code
    @SuppressWarnings("squid:S1452")
    Trampoline<? extends Json<?>> combine(Json<?> c,
                                          Json<?> d
                                         )
    {
        if (c.isObj() && d.isObj()) return new OpCombinerObjs(c.asJsObj(),
                                                              d.asJsObj()).combine();
        return new OpCombinerArrs(c.asJsArray(),
                                  d.asJsArray()
        ).combine();
    }

     abstract Trampoline<T> combine();
}
