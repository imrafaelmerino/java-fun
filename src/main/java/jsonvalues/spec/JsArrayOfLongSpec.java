package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsArrayOfLongSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfLongSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfLongSpec(final boolean nullable,
                      int min,
                      int max) {
        super(nullable,
              min,
              max);
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfLongSpec(true,
                                     min,
                                     max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLong(nullable,
                                                    min,
                                                    max);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isInt() || v.isLong() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                            LONG_EXPECTED)),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
