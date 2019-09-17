package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class OpPatchMove<T extends Json<T>> implements OpPatch<T>
{
    private final JsPath from;
    private final JsPath path;

    OpPatchMove(final JsObj op) throws PatchMalformed
    {
        Optional<String> from = requireNonNull(op).getStr(JsPath.fromKey("from"));
        if (!from.isPresent()) throw PatchMalformed.fromRequired(op);
        this.from = JsPath.path(from.get());
        Optional<String> opPath = op.getStr(JsPath.fromKey("path"));
        if (!opPath.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path =  JsPath.path(opPath.get());
    }

    @Override
    public TryPatch<T> apply(final T json)
    {
        return new OpPatchRemove<T>(from).apply(requireNonNull(json)
                                               )
                                         .flatMap(it -> new OpPatchAdd<T>(path,
                                                                          json.get(from)
                                         ).apply(it
                                                ));

    }

    @Override
    public String toString()
    {
        return "OpPatchMove{" +
        "from=" + from +
        ", path=" + path +
        '}';
    }

}
