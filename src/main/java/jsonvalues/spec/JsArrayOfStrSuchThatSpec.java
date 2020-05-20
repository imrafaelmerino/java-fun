package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

class JsArrayOfStrSuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {
    private final Function<JsArray, Optional<Error>> predicate;
    private JsArrayOfStrSpec isArrayOfString;

    JsArrayOfStrSuchThatSpec(final Function<JsArray, Optional<Error>> predicate,
                             final boolean required,
                             final boolean nullable
                            ) {
        super(required,
              nullable
             );
        this.isArrayOfString = new JsArrayOfStrSpec(required,
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
        return new JsArrayOfStrSuchThatSpec(predicate,
                                            required,
                                            true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfStrSuchThatSpec(predicate,
                                            false,
                                            nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfStrSuchThat(predicate,
                                                           nullable
                                                          );
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> result = isArrayOfString.test(value);
        if (result.isPresent() || value.isNull()) return result;
        return predicate.apply(value.toJsArray());
    }
}
