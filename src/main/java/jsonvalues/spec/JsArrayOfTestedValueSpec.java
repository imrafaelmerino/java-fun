package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfTestedValueSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsValue, Optional<JsError>> predicate;

    JsArrayOfTestedValueSpec(final Function<JsValue, Optional<JsError>> predicate,
                             final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedValueSpec(predicate,
                                            true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValueEachSuchThat(predicate,
                                                                 nullable);
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(predicate,
                                                nullable)
                        .apply(value);
    }
}
