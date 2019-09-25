package jsonvalues.benchmark.immutable.parsing;

import jsonvalues.*;
import org.openjdk.jmh.annotations.Benchmark;

public class StringToJsArray_100000
{
    private static final String array = jsonvalues.benchmark.Data.ARR_100000.get();

    @Benchmark
    public JsArray scala_vector() throws MalformedJson
    {

        return ScalaFactory.hmv.array.parse(array)
                                     .orElseThrow();
    }

    @Benchmark
    public JsArray scala_list() throws MalformedJson
    {

        return ScalaFactory.hml.array.parse(array)
                                     .orElseThrow();
    }

    @Benchmark
    public JsArray clojure_vector() throws MalformedJson
    {

        return ClojureFactory.hmv.array.parse(array)
                                       .orElseThrow();
    }

    @Benchmark
    public JsArray clojure_set() throws MalformedJson
    {

        return ClojureFactory.hms.array.parse(array)
                                       .orElseThrow();
    }

    @Benchmark
    public JsArray vavr_vector() throws MalformedJson
    {

        return VavrFactory.hmv.array.parse(array)
                                    .orElseThrow();
    }

    @Benchmark
    public JsArray vavr_list() throws MalformedJson
    {

        return VavrFactory.hml.array.parse(array)
                                    .orElseThrow();
    }

    @Benchmark
    public JsArray vavr_set() throws MalformedJson
    {

        return VavrFactory.hms.array.parse(array)
                                    .orElseThrow();
    }

    @Benchmark
    public JsArray pcollections_vector() throws MalformedJson
    {

        return PCollectionsFactory.hmv.array.parse(array)
                                            .orElseThrow();
    }

}
