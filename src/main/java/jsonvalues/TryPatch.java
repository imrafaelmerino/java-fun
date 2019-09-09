package jsonvalues;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Computation that applies a group of operations to a Json according to the RFC 6902 and may either
 result in a PatchMalformed or PatchOpError exceptions, or a successfully transformed json.
 @param <T> the type of the successful result of the computation
 */
public final class TryPatch<T extends Json<T>>
{

    private @Nullable PatchMalformed patchMalformed;

    private @Nullable PatchOpError patchOpError;

    private @Nullable T result;

    /**
     return true if the patch didn't fail
     @return true if the patch didn't fail
     */
    public boolean isSuccess()
    {
        return result != null;
    }

    /**
     return true if the patch failed
     @return true if the patch failed
     */
    public boolean isFailure()
    {
        return patchMalformed != null || patchOpError != null;
    }

    /**
     return true if the patch couldn't be applied because an operation was malformed: a field was missing or it
     didn't have the expected schema
     @return true if the patch couldn't be applied because an operation was malformed
     */
    public boolean isMalformed()
    {
        return patchMalformed != null;
    }

    /**
     return true if the patch failed because an operation returned an error. Go to the
     <a href="http://tools.ietf.org/html/rfc6901">RFC 6902</a> to see all the details.
     @return true if the patch failed because some operation couldn't be applied
     */
    public boolean isOpError()
    {
        return patchOpError != null;
    }

    /**
     the result of the patch wrapped into an optional. If the patch failed, an Optional.empty() is returned
     @return an Optional
     */
    public Optional<T> toOptional()
    {
        if (result != null) return Optional.of(result);
        return Optional.empty();
    }

    /**
     @return the result of the patch it it didn't fail or throws the error
     @throws PatchMalformed if the patch couldn't be applied because the operations had a malformed schema
     @throws PatchOpError if the patch was applied but failed
     */
    public T orElseThrow() throws PatchMalformed, PatchOpError
    {
        if (patchOpError != null) throw patchOpError;
        if (patchMalformed != null) throw patchMalformed;
        if (result != null) return result;
        throw InternalError.tryPatchComputationWithNoResult();
    }

    @EnsuresNonNull("#1")
    public TryPatch(final PatchMalformed malformed)
    {
        this.patchMalformed = requireNonNull(malformed);
    }

    @EnsuresNonNull("#1")
    public TryPatch(final PatchOpError opError)
    {
        this.patchOpError = requireNonNull(opError);
    }

    @EnsuresNonNull("#1")
    public TryPatch(final @NonNull T result)
    {
        this.result = requireNonNull(result);
    }

    /**
     if this TryPatch failed, it returns this instance, whereas if it didn't fail, it returns the TryPatch
     result of applying the given function to the result of this TryPatch.
     @param fn the given function that will be applied only if this patch didn't fail. The result of the
     flatMap operation will be the result of this function, if this TryPatch didn't fail
     @return a TryPatch
     */
    public TryPatch<T> flatMap(Function<T, TryPatch<T>> fn)
    {
        if (result != null) return requireNonNull(fn).apply(result);
        return this;
    }

    /**
     if this TryPatch failed, it returns this instance, whereas if it didn't fail, it returns the result
     of this TryPatch mapped with the given function and wrapped in a TryPatch.
     @param fn the given function that will be applied only if this patch didn't fail. The result
     of the map operation will be the result of this function wrapped in a TryPatch
     @return a TryPatch
     */
    public TryPatch<T> map(UnaryOperator<T> fn)
    {
        if (result != null) return new TryPatch<>(requireNonNull(fn)
                                                  .apply(result));
        return this;
    }
}
