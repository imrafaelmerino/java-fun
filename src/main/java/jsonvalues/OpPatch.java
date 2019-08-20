package jsonvalues;

import java.util.Optional;

interface OpPatch<T extends Json<?>> extends java.util.function.Function<T, TryPatch<T>>
{

    TryPatch<T> map(TryPatch<T> tryPath);

    default JsPath validatePath(JsObj op) throws PatchMalformed
    {
        Optional<String> path = op.getStr("path");
        if (!path.isPresent()) throw PatchMalformed.pathRequired(op);
        return JsPath.of(path.get());
    }

    default JsPath validateFrom(JsObj op) throws PatchMalformed
    {
        Optional<String> from = op.getStr("from");
        if (!from.isPresent()) throw PatchMalformed.fromRequired(op);
        return JsPath.of(from.get());

    }

    default JsElem validateValue(JsObj op) throws PatchMalformed
    {
        JsElem value = op.get("value");
        if (value.isNothing()) throw PatchMalformed.valueRequired(op);
        return value;
    }

}
