package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsLongSpec extends AbstractNullableSpec implements JsValuePredicate {
    JsLongSpec(final boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsLongSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofLong(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isLong,
                                  LONG_EXPECTED,
                                  nullable
                        )
                        .apply(value);
    }
}
