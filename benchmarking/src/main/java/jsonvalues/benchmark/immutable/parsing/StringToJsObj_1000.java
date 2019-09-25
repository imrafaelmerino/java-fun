package jsonvalues.benchmark.immutable.parsing;

import jsonvalues.*;
import org.openjdk.jmh.annotations.Benchmark;

public class StringToJsObj_1000
{

    private static final String object = jsonvalues.benchmark.Data.OBJ_1000.get();

    @Benchmark
    public JsObj scala_hash_map() throws MalformedJson
    {

        return ScalaFactory.hmv.object.parse(object)
                                      .orElseThrow();
    }

    @Benchmark
    public JsObj scala_tree_map() throws MalformedJson
    {

        return ScalaFactory.tmv.object.parse(object)
                                      .orElseThrow();
    }

    @Benchmark
    public JsObj clojure_hash_map() throws MalformedJson
    {

        return ClojureFactory.hmv.object.parse(object)
                                        .orElseThrow();
    }

    @Benchmark
    public JsObj clojure_tree_map() throws MalformedJson
    {

        return ClojureFactory.tmv.object.parse(object)
                                        .orElseThrow();
    }

    @Benchmark
    public JsObj vavr_hash_map() throws MalformedJson
    {

        return VavrFactory.hmv.object.parse(object)
                                     .orElseThrow();
    }

    @Benchmark
    public JsObj vavr_tree_map() throws MalformedJson
    {

        return VavrFactory.tmv.object.parse(object)
                                     .orElseThrow();
    }

    @Benchmark
    public JsObj pcollections_hash_map() throws MalformedJson
    {

        return PCollectionsFactory.hmv.object.parse(object)
                                             .orElseThrow();
    }

}
