package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.BOOLEAN_EXPECTED;

class JsArrayOfBoolSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    JsArrayOfBoolSpec(final boolean nullable) {
        super(nullable);
    }

    JsArrayOfBoolSpec(final boolean nullable,
                      int min,
                      int max) {
        super(nullable,
              min,
              max);
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfBoolSpec(true,
                                     min,
                                     max);
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfBool(nullable,
                                                    min,
                                                    max);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v -> v.isBool() ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(v,
                                                                            BOOLEAN_EXPECTED)),
                                                nullable,
                                                min,
                                                max)
                        .apply(value);
    }
}
