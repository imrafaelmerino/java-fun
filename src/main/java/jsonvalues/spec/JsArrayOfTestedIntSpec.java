package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsArrayOfTestedIntSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    final IntFunction<Optional<JsError>> predicate;

    JsArrayOfTestedIntSpec(final IntFunction<Optional<JsError>> predicate,
                           final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedIntSpec(predicate,
                                          true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfIntEachSuchThat(predicate,
                                                               nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {

        return Functions.testArrayOfTestedValue(v ->
                                                        v.isInt() ?
                                                        predicate.apply(v.toJsInt().value) :
                                                        Optional.of(new JsError(v,
                                                                                INT_EXPECTED
                                                                    )
                                                        )
                                ,
                                                nullable
                        )
                        .apply(value);
    }
}
