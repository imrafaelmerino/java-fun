package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfNumberSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfNumberSpec isArrayOfNumber;

    JsArrayOfNumberSuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                                final boolean nullable
    ) {
        super(nullable);
        this.isArrayOfNumber = new JsArrayOfNumberSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfNumberSuchThatSpec(predicate,
                                               true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumberSuchThat(predicate,
                                                              nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        Optional<JsError> result = isArrayOfNumber.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
