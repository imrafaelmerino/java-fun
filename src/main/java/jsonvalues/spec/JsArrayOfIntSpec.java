package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsArrayOfIntSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {


    JsArrayOfIntSpec(final boolean required,
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
        return new JsArrayOfIntSpec(required,
                                    true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfIntSpec(false,
                                    nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfInt(nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> {
                                                    if (v.isInt()) return Optional.empty();
                                                    else return Optional.of(new JsError(v,
                                                                                        INT_EXPECTED));
                                                },
                                                required,
                                                nullable
                        )
                        .apply(value);

    }
}
