package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsPatchRemove
{

    @Test
    public void test_removes_non_existing_value_error()
    {

        final PatchOpError patchOpError = Assertions.assertThrows(PatchOpError.class,
                                                                  () -> Jsons.immutable.object.empty()
                                                                                              .patch(Patch.create()
                                                                                                          .remove("/a")
                                                                                                          .toArray()
                                                                                                    )
                                                                                              .orElseThrow()
                                                                 );
        Assertions.assertEquals("Trying to remove a non-existing element. REMOVE operation can not be applied in {} at /a",
                                patchOpError.getMessage()
                               );
        final PatchOpError patchOpError1 = Assertions.assertThrows(PatchOpError.class,
                                                                   () -> Jsons.immutable.object.of("a",
                                                                                                   JsStr.of("hi")
                                                                                                  )
                                                                                               .patch(Patch.create()
                                                                                                           .remove("/a")
                                                                                                           .remove("/b")
                                                                                                           .toArray()

                                                                                                     )
                                                                                               .orElseThrow()
                                                                  );
        Assertions.assertEquals("Trying to remove a non-existing element. REMOVE operation can not be applied in {} at /b",
                                patchOpError1.getMessage()
                               );
    }

    @Test
    public void test_from_array() throws PatchMalformed, PatchOpError
    {
        Assertions.assertEquals(Jsons.immutable.array.of(1,
                                                         3
                                                        ),
                                Jsons.immutable.array.of(1,
                                                         2,
                                                         3,
                                                         4
                                                        )
                                                     .patch(Patch.create()
                                                                 .remove("/1")
                                                                 .remove("/2")
                                                                 .toArray()

                                                           )
                                                     .orElseThrow()
                               );
    }


}
