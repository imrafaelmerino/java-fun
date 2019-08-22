package jsonvalues;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class OpPatchMove<T extends Json<T>> implements OpPatch<T>
{
    private final JsPath from;
    private final JsPath path;

    OpPatchMove(final JsPath from,
                final JsPath path
               )
    {
        this.from = requireNonNull(from);
        this.path = requireNonNull(path);
    }

    OpPatchMove(final JsObj op) throws PatchMalformed
    {
        this.from = validateFrom(requireNonNull(op));
        this.path = validatePath(op);
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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OpPatchMove<?> that = (OpPatchMove<?>) o;
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
