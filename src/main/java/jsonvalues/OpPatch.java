package jsonvalues;

import java.util.Optional;

import static jsonvalues.Patch.*;

interface OpPatch<T extends Json<T>>
{

    TryPatch<T> apply(final T tryPath);


    default JsPath validatePath(final JsObj op) throws PatchMalformed
    {
        Optional<String> path = op.getStr(JsPath.fromKey(PATH_FIELD));
        if (!path.isPresent()) throw PatchMalformed.pathRequired(op);
        return JsPath.of(path.get());
    }

    default JsPath validateFrom(final JsObj op) throws PatchMalformed
    {
        Optional<String> from = op.getStr(JsPath.fromKey(FROM_FIELD));
        if (!from.isPresent()) throw PatchMalformed.fromRequired(op);
        return JsPath.of(from.get());

    }

    default JsElem validateValue(final JsObj op) throws PatchMalformed
    {
        JsElem value = op.get(JsPath.fromKey(VALUE_FIELD));
        if (value.isNothing()) throw PatchMalformed.valueRequired(op);
        return value;
    }

}
