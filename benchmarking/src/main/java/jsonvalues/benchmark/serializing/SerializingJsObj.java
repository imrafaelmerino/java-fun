package jsonvalues.benchmark.serializing;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.dslplatform.json.derializers.DeserializerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalues.JsObj;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public class SerializingJsObj
{

  private final static String objectUT = "{\n"
    + "  \"a\": \"a\",\n"
    + "  \"b\": 2,\n"
    + "  \"c\": true,\n"
    + "  \"d\": 10,\n"
    + "  \"e\": \"b\",\n"
    + "  \"f\": false,\n"
    + "  \"integers\": [\n"
    + "    1,\n"
    + "    2,\n"
    + "    3,\n"
    + "    4,\n"
    + "    5,\n"
    + "    6,\n"
    + "    7,\n"
    + "    8,\n"
    + "    9,\n"
    + "    10,\n"
    + "    11,\n"
    + "    12,\n"
    + "    13,\n"
    + "    14,\n"
    + "    15,\n"
    + "    16\n"
    + "  ],\n"
    + "  "
    + "\"strings\": [\n"
    + "    \"1\",\n"
    + "    \"2\",\n"
    + "    \"3\",\n"
    + "    \"4\",\n"
    + "    \"5\",\n"
    + "    \"6\",\n"
    + "    \"7\",\n"
    + "    " + "\"8\",\n"
    + "    \"9\",\n"
    + "    \"10\",\n"
    + "    \"11\",\n"
    + "    \"12\",\n"
    + "    \"13\",\n"
    + "    \"14\",\n"
    + "    \"15\",\n"
    + "    \"16\"\n"
    + "  ],\n"
    + "  \"objects\": [\n"
    + "    {\n"
    + "      \"a\": \"hi\",\n"
    + "      \"b\": 10,\n"
    + "     "
    + " \"c\": true,\n"
    + "      \"d\": [\n"
    + "        \"a\",\n"
    + "        \"b\",\n"
    + "        \"c\",\n"
    + "        \"d\",\n"
    + "        \"e\",\n"
    + "        \"f\",\n"
    + "        \"g\"\n"
    + "      ],\n"
    + "      \"e\": 1.10\n"
    + "    },\n"
    + "    {\n"
    + "      \"a\": \"hi\",\n"
    + "      \"b\": 10,\n"
    + "      \"c\": true,\n"
    + "      \"d\": [\n"
    + "        \"a\",\n"
    + "       "
    + " \"b\",\n"
    + "        \"c\",\n"
    + "        \"d\",\n"
    + "        \"e\",\n"
    + "        \"f\",\n"
    + "        \"g\"\n"
    + "      ],\n"
    + "      \"e\": 1.10\n"
    + "    }\n"
    + "  ]\n"
    + "} ";

  private static JsObj json;

  static
  {
    try
    {
      json = JsObj.parse(objectUT);
    }
    catch (DeserializerException e)
    {
      throw new RuntimeException(e);
    }
  }

  final static ObjectMapper objectMapper = new ObjectMapper();

  private static Map map;

  static
  {
    try
    {
      map = objectMapper.readValue(objectUT,
                                   Map.class
                                  );
    }
    catch (JsonProcessingException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Benchmark
  public void jackson(Blackhole bh) throws JsonProcessingException
  {
    byte[] bytes = objectMapper.writeValueAsBytes(map);
    bh.consume(bytes);
  }

  @Benchmark
  public void json_values(Blackhole bh)
  {
    byte[] str = json.serialize();
    bh.consume(str);
  }


}

