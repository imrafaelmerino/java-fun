package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsStrSpec extends AbstractPredicateSpec implements JsValuePredicate {
    JsStrSpec(final boolean required,
              final boolean nullable
    ) {
        super(required,
              nullable
        );
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsStrSpec(required,
                             true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsStrSpec(false,
                             nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStr(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testElem(JsValue::isStr,
                                  STRING_EXPECTED,
                                  required,
                                  nullable
                        )
                        .apply(value);
    }
}
