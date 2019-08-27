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
        Optional<String> path = op.getStr(JsPath.fromKey("path"));
        if (!path.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path =  JsPath.path(path.get());
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

    @Override
    public boolean equals(final @Nullable  Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OpPatchCopy<?> that = (OpPatchCopy<?>) o;
        return from.equals(that.from) &&
        path.equals(that.path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(from,
                            path
                           );
    }



}
