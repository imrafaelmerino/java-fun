package jsonvalues;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class OpPatchCopy<T extends Json<T>> implements OpPatch<T>
{
    private final JsPath from;
    private final JsPath path;

    public OpPatchCopy(final JsPath from,
                       final JsPath path
                      )
    {
        this.from = requireNonNull(from);
        this.path = requireNonNull(path);
    }

    OpPatchCopy(final JsObj op
               ) throws PatchMalformed
    {
        this.from = validateFrom(requireNonNull(op));
        this.path = validatePath(op);
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
    public boolean equals(final Object o)
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
