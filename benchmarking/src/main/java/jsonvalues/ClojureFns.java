package jsonvalues;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import java.util.function.Function;

public class ClojureFns
{

    private ClojureFns()
    {
    }

    private static final Function<String, IFn> cljFn = fn -> Clojure.var("clojure.core",
                                                                         fn
                                                                        );

    public static final IFn rest = cljFn.apply("rest");
    public static final IFn vec = cljFn.apply("vec");
    public static final IFn isEmpty = cljFn.apply("empty?");
    public static final IFn butlast = cljFn.apply("butlast");
    public static final IFn assoc = cljFn.apply("assoc");
    public static final IFn concat = cljFn.apply("concat");
    public static final IFn subvec = cljFn.apply("subvec");
    public static final IFn conj = cljFn.apply("conj");
    public static final IFn cons = cljFn.apply("cons");
    public static final IFn last = cljFn.apply("last");
    public static final IFn nth = cljFn.apply("nth");
    public static final IFn first = cljFn.apply("first");
    public static final IFn toSet = cljFn.apply("set");

}
