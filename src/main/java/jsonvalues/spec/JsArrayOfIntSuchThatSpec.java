package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfIntSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> predicate;
    private final JsArrayOfIntSpec isArrayOfInt;

    JsArrayOfIntSuchThatSpec(final Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                             final boolean nullable
    ) {
        super(nullable);
        this.isArrayOfInt = new JsArrayOfIntSpec(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfIntSuchThatSpec(predicate,
                                            true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntSuchThat(predicate,
                                                           nullable
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        final Optional<Pair<JsValue, ERROR_CODE>> result = isArrayOfInt.test(value);
        return result.isPresent() || value.isNull() ?
               result :
               predicate.apply(value.toJsArray());
    }
}
