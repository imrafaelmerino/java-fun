package jsonvalues.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalues.JsObj;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public class JsSerializers {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsObj json;
    private static final Map<?,?> map;

    static {
        try {
            json = JsObj.parse(Conf.PERSON_JSON);
            map = objectMapper.readValue(Conf.PERSON_JSON,
                                         Map.class
                                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Benchmark
    public void jackson(Blackhole bh) throws JsonProcessingException {
        byte[] bytes = objectMapper.writeValueAsBytes(map);
        bh.consume(bytes);
    }

    @Benchmark
    public void json_values(Blackhole bh) {
        byte[] str = json.serialize();
        bh.consume(str);
    }


}

