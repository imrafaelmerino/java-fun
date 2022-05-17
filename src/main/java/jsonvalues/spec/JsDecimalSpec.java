package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.DECIMAL_EXPECTED;

class JsDecimalSpec extends AbstractPredicateSpec implements JsValuePredicate {

    JsDecimalSpec(final boolean nullable) {
        super(nullable);
    }


    @Override
    public JsSpec nullable() {
        return new JsDecimalSpec(true);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofDecimal(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isDecimal,
                                  DECIMAL_EXPECTED,
                                  nullable
                        )
                        .apply(value);

    }
}
