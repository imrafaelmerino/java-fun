package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsArrayOfTestedNumberSpec extends AbstractSizableArrSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsArrayOfTestedNumberSpec(final Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                              final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedNumberSpec(final Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                              final boolean nullable,
                              int min,
                              int max
    ) {
        super(nullable,
              min,
              max);
        this.predicate = predicate;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedNumberSpec(predicate,
                                             true,
                                             min,
                                             max
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumberEachSuchThat(predicate,
                                                                  nullable,
                                                                  min,
                                                                  max
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isNumber() ?
                                                        predicate.apply(v.toJsNumber()) :
                                                        Optional.of(Pair.of(v,
                                                                               NUMBER_EXPECTED
                                                                    )
                                                        ),
                                                nullable,
                                                min,
                                                max
                        )
                        .apply(value);
    }
}
