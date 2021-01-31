package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import jsonvalues.JsObj;

import java.io.InputStream;

import static com.dslplatform.json.MyDslJson.INSTANCE;
import static java.util.Objects.requireNonNull;

public class JsObjParser {


    private final JsSpecParser parser;

    /**
     @param spec the Json spec what defines the schema the json has to conform
     */
    public JsObjParser(final JsObjSpec spec) {
        parser = spec.parser();
    }


    /**
     parses an array of bytes into a Json object that must conform the spec of the parser. If the
     array of bytes doesn't represent a well-formed Json or is a well-formed Json that doesn't
     conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
     returned.

     @param bytes a Json object serialized in an array of bytes
     @return a try computation with the result
     */
    public JsObj parse(byte[] bytes){
        return INSTANCE.parseToJsObj(requireNonNull(bytes),
                                     parser
                                    );

    }

    /**
     parses a string into a Json object that must conform the spec of the parser. If the
     string doesn't represent a well-formed Json or is a well-formed Json that doesn't
     conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
     returned.

     @param str a Json object serialized in a string
     @return a try computation with the result
     */
    public JsObj parse(String str){

        return INSTANCE.parseToJsObj(requireNonNull(str).getBytes(),
                                     parser
                                    );
    }

    /**
     parses an input stream of bytes into a Json object that must conform the spec of the parser. If the
     the input stream of bytes doesn't represent a well-formed Json object or is a well-formed Json that doesn't
     conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
     returned. Any I/O exception processing the input stream is wrapped in a Try computation as well

     @param inputstream the input stream of bytes
     @return a try computation with the result
     */
    public JsObj parse(InputStream inputstream){
        return INSTANCE.parseToJsObj(requireNonNull(inputstream),
                                     parser
                                    );
    }


}


