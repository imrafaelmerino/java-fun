package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfLongSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfLongSpec isArrayOfLong;

    JsArrayOfLongSuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                              final boolean nullable
    ) {
        super(nullable);
        this.isArrayOfLong = new JsArrayOfLongSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfLongSuchThatSpec(predicate,
                                             true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfLongSuchThat(predicate,
                                                            nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> result = isArrayOfLong.test(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
