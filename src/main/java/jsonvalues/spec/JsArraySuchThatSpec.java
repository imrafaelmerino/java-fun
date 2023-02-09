package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


class JsArraySuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {

    final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfValueSpec isArray;

    JsArraySuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                        final boolean nullable
    ) {
        super(nullable);
        this.isArray = new JsArrayOfValueSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArraySuchThatSpec(predicate,
                                       true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValueSuchThat(predicate,
                                                             nullable
        );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> result = isArray.testValue(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
