package jsonvalues.benchmark.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalues.Jsons;
import jsonvalues.MutableJsons;
import jsonvalues.mymaps.mutable.EclipseCollectionMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyJsArrayBenchmark
{

    static ObjectMapper mapper = new ObjectMapper();
    static MutableJsons factory= Jsons.mutable.withMap(EclipseCollectionMap.class);
    static String ARR_1000;
    static String ARR_10000;
    static String ARR_100000;
    static String ARR_1000000;
    static
    {
        try
        {
            ARR_1000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/arr1000.json")));
            ARR_10000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/arr10000.json")));
            ARR_100000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/arr100000.json")));
            ARR_1000000 = new String(Files.readAllBytes(Paths.get("/Users/rmerino/Projects/jsonvalues/data/arr1000000.json")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
