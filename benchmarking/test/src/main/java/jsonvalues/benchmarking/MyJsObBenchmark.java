/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jsonvalues.benchmarking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalues.JsObj;
import jsonvalues.Jsons;
import jsonvalues.MalformedJson;
import jsonvalues.MutableJsons;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class MyJsObBenchmark
{

    static ObjectMapper mapper = new ObjectMapper();

    static String OBJ_1000;
    static String OBJ_100;
    static String OBJ_10000;
    static String OBJ_100000;
    static String OBJ_1000000;
    static MutableJsons factory = Jsons.mutable.withMap(EclipseCollectionMutableMap.class);


    static
    {
        try
        {
            OBJ_1000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/obj1000.json")));
            OBJ_100 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/obj100.json")));
            OBJ_10000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/obj10000.json")));
            OBJ_100000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/obj100000.json")));
            OBJ_1000000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/obj1000000.json")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsonNode parse_string_jackson_obj_10000() throws IOException
    {

        return mapper.readTree(OBJ_10000);
    }


    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_mutable_obj_10000() throws MalformedJson
    {

        return Jsons.mutable.object.parse(OBJ_10000)
                                   .orElseThrow();
    }

    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_mutable_eclipse_collections_obj_10000() throws MalformedJson
    {

        return factory.object.parse(OBJ_10000)
                             .orElseThrow();
    }

    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_immutable_obj_10000() throws MalformedJson
    {

        return Jsons.immutable.object.parse(OBJ_10000)
                                     .orElseThrow();
    }


    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsonNode parse_string_jackson_obj_100000() throws IOException
    {

        return mapper.readTree(OBJ_100000);
    }


    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_mutable_obj_100000() throws MalformedJson
    {

        return Jsons.mutable.object.parse(OBJ_100000)
                                   .orElseThrow();
    }

    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_mutable_eclipse_collections_obj_100000() throws MalformedJson
    {
        return factory.object.parse(OBJ_100000)
                             .orElseThrow();
    }

    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_immutable_obj_100000() throws MalformedJson
    {
        return Jsons.immutable.object.parse(OBJ_100000).orElseThrow();
    }


    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsonNode parse_string_jackson_obj_1000000() throws IOException
    {
        return mapper.readTree(OBJ_1000000);
    }


    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_mutable_obj_1000000() throws MalformedJson
    {
        return Jsons.mutable.object.parse(OBJ_1000000)
                                   .orElseThrow();
    }

    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_mutable_eclipse_collections_obj_1000000() throws MalformedJson
    {
        return factory.object.parse(OBJ_1000000)
                             .orElseThrow();
    }

    @Benchmark
    @Fork(value = 1, warmups = 10)
    @Warmup(iterations = 10)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Measurement(iterations = 10)
    public JsObj parse_string_json_values_immutable_obj_1000000() throws MalformedJson
    {
        return Jsons.immutable.object.parse(OBJ_1000000)
                                     .orElseThrow();
    }


}
