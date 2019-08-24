package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jsonvalues.JsNothing.NOTHING;

public class TestJsPatchMove
{

    @Test
    public void test_move_from_obj_array() throws PatchMalformed, PatchOpError
    {
        JsObj obj = JsObj.of("a",
                             JsObj.of("b",
                                      JsInt.of(2)
                                     ),
                             "b",
                             JsArray.of(1,
                                        3
                                       )
                            );
        final TryPatch<JsObj> result = obj.patch(Patch.ops()
                                                      .move("/a/b",
                                                            "/b/1"
                                                           )
                                                      .build());
        Assertions.assertEquals(NOTHING,
                                result.orElseThrow()
                                      .get(JsPath.of("/a/b"))
                               );
        Assertions.assertEquals(JsArray.of(1,
                                           2,
                                           3
                                          ),
                                result.orElseThrow()
                                      .get(JsPath.of("/b"))
                               );
        final TryPatch<JsObj> result2 = obj.patch(Patch.ops()
                                                       .test("/a/b",
                                                             JsInt.of(2)
                                                            )
                                                       .move("/a/b",
                                                             "/a"
                                                            )
                                                       .remove("/b/0")
                                                       .build()
                                                 );
        Assertions.assertEquals(JsInt.of(2),
                                result2.orElseThrow()
                                       .get(JsPath.of("/a"))
                               );
        Assertions.assertEquals(JsInt.of(3),
                                result2.orElseThrow()
                                       .get(JsPath.of("/b/0"))
                               );
        final TryPatch<JsObj> result3 = obj.patch(Patch.ops()
                                                       .test("/a/b",
                                                             JsNull.NULL
                                                            )
                                                       .move("/a/b",
                                                             "/a"
                                                            )
                                                       .build()
                                                 );
        Assertions.assertTrue(result3.isFailure());
    }
}
