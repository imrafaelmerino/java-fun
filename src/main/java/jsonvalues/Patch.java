package jsonvalues;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 Encapsulates a RFC 6902 implementation. Json patch operations can be applied to Jsons using the method
 {@link Json#patch(JsArray)}.
 */
public final class Patch
{
    /**
     List of supported patch-operations
     */
    private enum OP
    {ADD, REMOVE, MOVE, COPY, REPLACE, TEST}


    private Patch()
    {
    }

    /**
     return a new patch-operation builder
     @return a new patch-operation builder
     */
    public static Builder ops()
    {
        return new Builder();
    }

    /**
     represents a builder to create json-patch operations according to the RFC 6902 specification.
     */
    public static final class Builder
    {
        private Builder()
        {
        }

        private JsArray ops = JsArray._empty_();

        /**
         ADD operation.
         @param path target location of the operation
         @param value element to be added
         @return this builder with an ADD operation appended
         */
        public Builder add(final String path,
                           final JsElem value
                          )
        {
            ops.append(JsObj.of("path",
                                JsStr.of(requireNonNull(path)),
                                "op",
                                JsStr.of(OP.ADD.name()),
                                "value",
                                requireNonNull(value)
                               ));
            return this;
        }

        /**
         REPLACE operation.
         @param path target location of the operation
         @param value element to be replaced with
         @return this builder with a REPLACE operation appended
         */
        public Builder replace(final String path,
                               final JsElem value
                              )
        {
            ops.append(JsObj.of("path",
                                JsStr.of(requireNonNull(path)),
                                "value",
                                requireNonNull(value),
                                "op",
                                JsStr.of(OP.REPLACE.name())
                               ));
            return this;

        }

        /**
         REMOVE operation.
         @param path target location of the operation
         @return this builder with a REMOVE operation appended
         */
        public Builder remove(final String path)
        {
            ops.append(JsObj.of("path",
                                JsStr.of(requireNonNull(path)),
                                "op",
                                JsStr.of(OP.REMOVE.name())
                               ));
            return this;
        }

        /**
         TEST operation.
         @param path target location of the operation
         @param value element to be tested
         @return this builder with a TEST operation appended
         */
        public Builder test(final String path,
                            final JsElem value
                           )
        {
            ops.append(JsObj.of("path",
                                JsStr.of(requireNonNull(path)),
                                "value",
                                requireNonNull(value),
                                "op",
                                JsStr.of(OP.TEST.name())
                               ));
            return this;

        }

        /**
         MOVE operation.
         @param from target location of the operation
         @param to source location of the operation
         @return this builder with a MOVE operation appended
         */
        public Builder move(final String from,
                            final String to
                           )
        {
            ops.append(JsObj.of("path",
                                JsStr.of(requireNonNull(to)),
                                "from",
                                JsStr.of(requireNonNull(from)),
                                "op",
                                JsStr.of(OP.MOVE.name())
                               ));
            return this;
        }

        /**
         COPY operation.
         @param from target location of the operation
         @param to source location of the operation
         @return this builder with a COPY operation appended
         */
        public Builder copy(final String from,
                            final String to
                           )
        {
            ops.append(JsObj.of("path",
                                JsStr.of(requireNonNull(to)),
                                "from",
                                JsStr.of(requireNonNull(from)),
                                "op",
                                JsStr.of(OP.COPY.name())
                               )
                      );
            return this;
        }

        /**
         returns the array of operations
         @return a JsArray
         */
        public JsArray toArray()
        {
            return ops;
        }

        /**
         returns the array of operations as a string
         @return a String
         */
        public String toString()
        {
            return ops.toString();
        }
    }

    /**
     returns the result of a patch wrapped into a Try computation. If the operation doesn't fail,
     a new json object is returned, so the given json never is mutated.
     @param json the json that will be changed with an array of operations
     @param array the array of operations
     @param <T> the type of the object returned
     @return a new json object or a PatchOpError wrapped into a TryPatch computation
     */
    static <T extends Json<T>> TryPatch<T> of(final T json,
                                              final JsArray array
                                             )
    {
        try
        {
            final List<OpPatch<T>> ops = parseOps(array);
            if (ops.isEmpty()) return new TryPatch<>(json);
            OpPatch<T> head = ops.get(0);
            List<OpPatch<T>> tail = ops.subList(1,
                                                ops.size()
                                               );

            T immutable = json.isMutable() ? json.toImmutable() : json;
            TryPatch<T> accPatch = head.apply(immutable);
            for (OpPatch<T> op : tail) accPatch = accPatch.flatMap(op::apply);
            return json.isMutable() ? accPatch.map(Json<T>::toMutable) : accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }

    }

    private static <T extends Json<T>> List<OpPatch<T>> parseOps(final JsArray array
                                                                ) throws PatchMalformed
    {
        List<OpPatch<T>> ops = new ArrayList<>();
        for (JsElem elem : array)
        {
            if (!elem.isObj()) throw PatchMalformed.operationIsNotAnObj(elem);
            final JsObj opObj = elem.asJsObj();
            final Optional<String> op = opObj.getStr(JsPath.fromKey("op"));
            if (!op.isPresent()) throw PatchMalformed.operationRequired(opObj);
            try
            {
                switch (OP.valueOf(op.get()
                                     .toUpperCase()))
                {
                    case ADD:
                        ops.add(new OpPatchAdd<>(opObj));
                        break;
                    case REMOVE:
                        ops.add(new OpPatchRemove<>(opObj));
                        break;
                    case MOVE:
                        ops.add(new OpPatchMove<>(opObj));
                        break;
                    case COPY:
                        ops.add(new OpPatchCopy<>(opObj));
                        break;
                    case REPLACE:
                        ops.add(new OpPatchReplace<>(opObj));
                        break;
                    case TEST:
                        ops.add(new OpPatchTest<>(opObj));
                        break;
                    default:
                        throw InternalError.patchOperationNotSupported(op.get());
                }
            }
            catch (IllegalArgumentException e)
            {
                throw PatchMalformed.operationNotSupported(opObj);
            }

        }
        return ops;
    }
}
