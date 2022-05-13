package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsArrayOfLongSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfLongSpec(final boolean required,
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
        return new JsArrayOfLongSpec(required,
                                     true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfLongSpec(false,
                                     nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLong(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> {
                                                    if (v.isInt() || v.isLong()) return Optional.empty();
                                                    else return Optional.of(new JsError(v,
                                                                                        LONG_EXPECTED));
                                                },
                                                required,
                                                nullable
                        )
                        .apply(value);
    }
}
