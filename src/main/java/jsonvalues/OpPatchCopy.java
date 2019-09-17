package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class OpPatchCopy<T extends Json<T>> implements OpPatch<T>
{
    private final JsPath from;
    private final JsPath path;

    OpPatchCopy(final JsObj op
               ) throws PatchMalformed
    {
        Optional<String> from = requireNonNull(op).getStr(JsPath.fromKey("from"));
        if (!from.isPresent()) throw PatchMalformed.fromRequired(op);
        this.from = JsPath.path(from.get());
        Optional<String> opPath = op.getStr(JsPath.fromKey("path"));
        if (!opPath.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path =  JsPath.path(opPath.get());
    }


    @Override
    public TryPatch<T> apply(final T json
                            )
    {
        final JsElem fromElem = Objects.requireNonNull(json)
                                       .get(from);
        if (fromElem.isNothing())
            return new TryPatch<>(PatchOpError.copyingNonExistingValue(from,
                                                                       path,
                                                                       json
                                                                      ));
        return new OpPatchAdd<T>(path,
                                 fromElem
        ).apply(requireNonNull(json));

    }

    @Override
    public String toString()
    {
        return "OpPatchCopy{" +
        "from=" + from +
        ", path=" + path +
        '}';
    }


}
