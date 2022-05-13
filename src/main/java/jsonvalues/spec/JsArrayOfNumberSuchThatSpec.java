package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfNumberSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {

    private final Function<JsArray, Optional<JsError>> predicate;
    private final JsArrayOfNumberSpec isArrayOfNumber;

    JsArrayOfNumberSuchThatSpec(final Function<JsArray, Optional<JsError>> predicate,
                                final boolean required,
                                final boolean nullable
    ) {
        super(required,
              nullable
        );
        this.isArrayOfNumber = new JsArrayOfNumberSpec(required,
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
        return new JsArrayOfNumberSuchThatSpec(predicate,
                                               required,
                                               true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfNumberSuchThatSpec(predicate,
                                               false,
                                               nullable
        );

    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfNumberSuchThat(predicate,
                                                              nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> result = isArrayOfNumber.test(value);
        if (result.isPresent() || value.isNull()) return result;
        return predicate.apply(value.toJsArray());
    }
}
