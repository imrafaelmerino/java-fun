package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsPatchReplace
{

    @Test
    public void test_error_non_existing_element()
    {

        final PatchOpError patchOpError = Assertions.assertThrows(PatchOpError.class,
                                                                  () -> Jsons.immutable.object.of("a",
                                                                                                  Jsons.immutable.array.of(1)
                                                                                                 )
                                                                                              .patch(Patch.create()
                                                                                                          .replace("/a/b",
                                                                                                                   JsInt.of(1)
                                                                                                                  )
                                                                                                          .toArray()

                                                                                                    )
                                                                                              .orElseThrow()

                                                                 );

        Assertions.assertEquals("Trying to remove a non-existing element. REMOVE operation can not be applied in {\"a\":[1]} at /a/b",
                                patchOpError.getMessage()
                               );
    }


    @Test
    public void test_replace_existing_element() throws PatchMalformed, PatchOpError
    {

        final JsObj o = Jsons.immutable.object.of("a",
                                                  Jsons.immutable.object.of("b",
                                                                            JsBool.TRUE
                                                                           )
                                                 )
                                              .patch(Patch.create()
                                                          .replace("/a/b",
                                                                   JsBool.FALSE
                                                                  )
                                                          .toArray()
                                                    )
                                              .orElseThrow();


        Assertions.assertEquals(Jsons.immutable.object.of("a",
                                                          Jsons.immutable.object.of("b",
                                                                                    JsBool.FALSE
                                                                                   )
                                                         ),
                                o
                               );
    }
}
