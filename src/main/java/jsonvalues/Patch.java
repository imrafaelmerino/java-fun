package jsonvalues;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Patch<T extends Json<?>>
{

    enum OP
    {ADD, REMOVE, MOVE, COPY, REPLACE, TEST}

    enum PATCH_OPTIONS
    {ALWAYS_ADD}

    static <T extends Json<?>> TryPatch<T> of(final String str,
                                              final PATCH_OPTIONS... options
                                             )
    {
        try
        {
            final JsArray array = JsArray._parse_(str)
                                         .orElseThrow();
            final List<OpPatch<Json<?>>> ops = parseOps(array);

            return null;

        }
        catch (MalformedJson malformedJson)
        {
            return new TryPatch<>(PatchMalformed.patchIsNotAnArray(str));
        }
        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
    }

    private static <T extends Json<?>> List<OpPatch<T>> parseOps(final JsArray array
                                                                ) throws PatchMalformed
    {
        List<OpPatch<T>> ops = new ArrayList<>();
        for (JsElem elem : array)
        {
            if (!elem.isObj()) throw PatchMalformed.operationIsNotAnObj(elem);
            final JsObj opObj = elem.asJsObj();
            final Optional<String> op = opObj.getStr("op");
            if (!op.isPresent()) throw PatchMalformed.operationRequired(opObj);
            try
            {
                switch (OP.valueOf(op.get()))
                {
                    case ADD:

                        break;
                    case REMOVE:
                        break;
                    case MOVE:
                        break;
                    case COPY:
                        break;
                    case REPLACE:
                        break;
                    case TEST:
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

    Patch(final JsArray operations,
          PATCH_OPTIONS... options
         ) throws PatchMalformed
    {
        parseOps(operations);

    }
}
