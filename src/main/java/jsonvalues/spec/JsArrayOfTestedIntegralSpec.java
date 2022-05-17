package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INTEGRAL_EXPECTED;

class JsArrayOfTestedIntegralSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<BigInteger, Optional<JsError>> predicate;

    JsArrayOfTestedIntegralSpec(final Function<BigInteger, Optional<JsError>> predicate,
                                final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedIntegralSpec(predicate,
                                               true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntegralEachSuchThat(predicate,
                                                                    nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isIntegral() ?
                                                        predicate.apply(v.toJsBigInt().value) :
                                                        Optional.of(new JsError(v,
                                                                                INTEGRAL_EXPECTED
                                                                    )
                                                        ),
                                                nullable
                        )
                        .apply(value);
    }
}
