package jsonvalues;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

public class TryPatch<T extends Json<?>>
{

    private @Nullable PatchMalformed patchMalformed;

    private @Nullable PatchOpError patchOpError;

    private @Nullable T result;

    boolean isSuccess(){
        return result!=null;
    }
    boolean isFailure(){
        return patchMalformed!=null || patchOpError!=null;
    }
    boolean isMalformed(){
        return patchMalformed !=null;
    }
    boolean isOpError(){
        return patchOpError !=null;
    }
    Optional<T> toOptional(){
        if(result!=null)return Optional.of(result);
        return Optional.empty();
    }
    T orElseThrow() throws PatchMalformed,PatchOpError{
        if(patchOpError!=null) throw patchOpError;
        if(patchMalformed!=null) throw patchMalformed;
        if(result!=null) return result;
        throw InternalError.tryPatchComputationWithNoResult();
    }

    @EnsuresNonNull("#1")
    TryPatch(final PatchMalformed malformed){
        this.patchMalformed = malformed;
    }

    @EnsuresNonNull("#1")
    TryPatch(final PatchOpError opError){
        this.patchOpError = opError;
    }
    @EnsuresNonNull("#1")
    TryPatch(final T result){
        this.result = result;
    }


}
