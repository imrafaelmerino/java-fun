package jsonvalues;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Patch.OP.*;

public final class Patch
{

    static final String FROM_FIELD = "from";
    static final String OP_FIELD = "op";
    static final String PATH_FIELD = "path";
    static final String VALUE_FIELD = "value";

    enum OP {ADD, REMOVE, MOVE, COPY, REPLACE, TEST}


    public static Builder ops()
    {
        return new Builder();
    }

    public final static class Builder
    {
        private Builder()
        {
        }

        private JsArray ops = JsArray._empty_();


        public Builder add(final String path,
                           final JsElem value
                          )
        {
            ops.append(JsObj.of(PATH_FIELD,
                                JsStr.of(requireNonNull(path)),
                                OP_FIELD,
                                JsStr.of(ADD.name()),
                                VALUE_FIELD,
                                requireNonNull(value)
                               ));
            return this;

        }

        public Builder replace(final String path,
                               final JsElem value
                              )
        {
            ops.append(JsObj.of(PATH_FIELD,
                                JsStr.of(requireNonNull(path)),
                                VALUE_FIELD,
                                requireNonNull(value),
                                OP_FIELD,
                                JsStr.of(REPLACE.name())
                               ));
            return this;

        }

        public Builder remove(final String path)
        {
            ops.append(JsObj.of(PATH_FIELD,
                                JsStr.of(requireNonNull(path)),
                                OP_FIELD,
                                JsStr.of(REMOVE.name())
                               ));
            return this;
        }


        public Builder test(final String path,
                     final JsElem value
                    )
        {
            ops.append(JsObj.of(PATH_FIELD,
                                JsStr.of(requireNonNull(path)),
                                VALUE_FIELD,
                                requireNonNull(value),
                                OP_FIELD,
                                JsStr.of(TEST.name())
                               ));
            return this;

        }


        public Builder move(final String from,
                     final String to
                    )
        {
            ops.append(JsObj.of(PATH_FIELD,
                                JsStr.of(requireNonNull(to)),
                                FROM_FIELD,
                                JsStr.of(requireNonNull(from)),
                                OP_FIELD,
                                JsStr.of(MOVE.name())
                               ));
            return this;
        }

        public Builder copy(final String from,
                     final String to
                    )
        {
            ops.append(JsObj.of(PATH_FIELD,
                                JsStr.of(requireNonNull(to)),
                                FROM_FIELD,
                                JsStr.of(requireNonNull(from)),
                                OP_FIELD,
                                JsStr.of(COPY.name())
                               ));
            return this;
        }

        public JsArray build()
        {
            return ops;
        }
    }

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
            //todo pensar si aplicar json.isImmutable ? json : json.toImmutable or copy... para
            //que si falla una operacion no afecte nada
            TryPatch<T> accPatch = head.apply(json
                                             );
            for (OpPatch<T> op : tail)
                accPatch = accPatch.flatMap(op::apply);
            return accPatch;

        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }

    }

    static <T extends Json<T>> TryPatch<T> of(final T json,
                                              final String patch
                                             )
    {
        try
        {
            return of(json,
                      JsArray._parse_(patch)
                             .orElseThrow()
                     );
        }
        catch (MalformedJson malformedJson)
        {
            return new TryPatch<>(PatchMalformed.patchIsNotAnArray(patch));
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
            final Optional<String> op = opObj.getStr(JsPath.fromKey(OP_FIELD));
            if (!op.isPresent()) throw PatchMalformed.operationRequired(opObj);
            try
            {
                switch (valueOf(op.get()))
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
