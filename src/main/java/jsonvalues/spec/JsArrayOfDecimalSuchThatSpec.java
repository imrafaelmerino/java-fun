package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfDecimalSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfDecimalSpec isArrayOfDecimal;

    JsArrayOfDecimalSuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                                 final boolean required,
                                 final boolean nullable
                                ) {
        super(required,
              nullable
             );
        this.isArrayOfDecimal = new JsArrayOfDecimalSpec(required,
                                                         nullable
        );
        this.predicate = predicate;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfDecimalSuchThatSpec(predicate,
                                                required,
                                                true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfDecimalSuchThatSpec(predicate,
                                                false,
                                                nullable
        );

    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfDecimalSuchThat(predicate,
                                                               nullable
                                                              );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> result = isArrayOfDecimal.test(value);
        if (result.isPresent() || value.isNull()) return result;
        return predicate.apply(value.toJsArray());
    }
}
