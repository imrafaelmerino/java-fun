package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsPatchRemove
{

    @Test
    public void test_removes_non_existing_value_error()
    {

        final PatchOpError patchOpError = Assertions.assertThrows(PatchOpError.class,
                                                                  () -> JsObj.empty()
                                                                             .patch(Patch.ops()
                                                                                         .remove("/a")
                                                                                         .build()
                                                                                   )
                                                                             .orElseThrow()
                                                                 );
        Assertions.assertEquals("Trying to remove a non-existing element. REMOVE operation can not be applied in {} at /a",
                                patchOpError.getMessage()
                               );
        final PatchOpError patchOpError1 = Assertions.assertThrows(PatchOpError.class,
                                                                   () -> JsObj.of("a",
                                                                                  JsStr.of("hi")
                                                                                 )
                                                                              .patch(Patch.ops()
                                                                                          .remove("/a")
                                                                                          .remove("/b")
                                                                                          .build()

                                                                                    )
                                                                              .orElseThrow()
                                                                  );
        Assertions.assertEquals("Trying to remove a non-existing element. REMOVE operation can not be applied in {} at /b",
                                patchOpError1.getMessage()
                               );
    }


}
