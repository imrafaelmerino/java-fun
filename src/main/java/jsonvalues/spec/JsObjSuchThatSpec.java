package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

class JsObjSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {

    final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsObjSuchThatSpec(final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                      final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsObjSuchThatSpec(predicate,
                                     true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofObjSuchThat(predicate,
                                                    nullable
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        Optional<Pair<JsValue, ERROR_CODE>> error = Functions.testElem(JsValue::isObj,
                                                                       OBJ_EXPECTED,
                                                                       nullable
                                                             )
                                                             .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsObj());
    }
}
