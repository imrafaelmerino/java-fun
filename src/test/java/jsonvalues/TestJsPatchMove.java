package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jsonvalues.JsNothing.NOTHING;

public class TestJsPatchMove
{

    @Test
    public void test_move_from_obj_array() throws PatchMalformed, PatchOpError
    {
        JsObj obj = Jsons.immutable.object.of("a",
                                              Jsons.immutable.object.of("b",
                                                                        JsInt.of(2)
                                                                       ),
                                              "b",
                                              Jsons.immutable.array.of(1,
                                                                       3
                                                                      )
                                             );
        final TryPatch<JsObj> result = obj.patch(Patch.create()
                                                      .move("/a/b",
                                                            "/b/1"
                                                           )
                                                      .toArray());
        Assertions.assertEquals(NOTHING,
                                result.orElseThrow()
                                      .get(JsPath.path("/a/b"))
                               );
        Assertions.assertEquals(Jsons.immutable.array.of(1,
                                                         2,
                                                         3
                                                        ),
                                result.orElseThrow()
                                      .get(JsPath.path("/b"))
                               );
        final TryPatch<JsObj> result2 = obj.patch(Patch.create()
                                                       .test("/a/b",
                                                             JsInt.of(2)
                                                            )
                                                       .move("/a/b",
                                                             "/a"
                                                            )
                                                       .remove("/b/0")
                                                       .toArray()
                                                 );
        Assertions.assertEquals(JsInt.of(2),
                                result2.orElseThrow()
                                       .get(JsPath.path("/a"))
                               );
        Assertions.assertEquals(JsInt.of(3),
                                result2.orElseThrow()
                                       .get(JsPath.path("/b/0"))
                               );
        final TryPatch<JsObj> result3 = obj.patch(Patch.create()
                                                       .test("/a/b",
                                                             JsNull.NULL
                                                            )
                                                       .move("/a/b",
                                                             "/a"
                                                            )
                                                       .toArray()
                                                 );
        Assertions.assertTrue(result3.isFailure());
    }
}
