package jsonvalues.spec;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import java.util.Set;

public interface JsArraySpec extends JsSpec {

    default Set<JsErrorPair> test(final JsArray array) {
        return test(JsPath.empty(),
                    array);
    }





}
