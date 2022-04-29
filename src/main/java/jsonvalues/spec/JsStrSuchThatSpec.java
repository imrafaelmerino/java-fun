package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsStrSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {


    final Function<String, Optional<JsError>> predicate;

    JsStrSuchThatSpec(final boolean required,
                      final boolean nullable,
                      final Function<String, Optional<JsError>> predicate
                     ) {
        super(required,
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
        return new JsStrSuchThatSpec(required,
                                     true,
                                     predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsStrSuchThatSpec(false,
                                     nullable,
                                     predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStrSuchThat(predicate,
                                                    nullable
                                                   );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> error = jsonvalues.spec.Functions.testElem(JsValue::isStr,
                                                                           STRING_EXPECTED,
                                                                           required,
                                                                           nullable
                                                                        )
                                                                 .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsStr().value);
    }
}
