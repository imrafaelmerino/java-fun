package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsPatchReplace
{

    @Test
    public void test_error_non_existing_element()
    {

        final PatchOpError patchOpError = Assertions.assertThrows(PatchOpError.class,
                                                                  () -> JsObj.of("a",
                                                                                 JsArray.of(1)
                                                                                )
                                                                             .patch(Patch.ops()
                                                                                         .replace("a.b",
                                                                                                    JsInt.of(1)
                                                                                                   )
                                                                                         .build()

                                                                                   )
                                                                             .orElseThrow()

                                                                 );

        Assertions.assertEquals("Trying to remove a non-existing element. REMOVE operation can not be applied in {\"a\":[1]} at a.b",
                                patchOpError.getMessage()
                               );
    }


    @Test
    public void test_replace_existing_element() throws PatchMalformed, PatchOpError
    {

        final JsObj o = JsObj.of("a",
                                 JsObj.of("b",
                                          JsBool.TRUE
                                         )
                                )
                             .patch(Patch.ops()
                                         .replace("a.b",
                                                    JsBool.FALSE
                                                   )
                                         .build()
                                   )
                             .orElseThrow();


        Assertions.assertEquals(JsObj.of("a",
                                         JsObj.of("b",
                                                  JsBool.FALSE
                                                 )
                                        ),
                                o
                               );
    }
}
