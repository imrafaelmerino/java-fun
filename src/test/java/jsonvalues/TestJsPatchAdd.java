package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jsonvalues.Patch.ops;


public class TestJsPatchAdd
{


    @Test
    public void test_add_operation_path_is_missing()
    {
        final PatchMalformed patchMalformed = Assertions.assertThrows(PatchMalformed.class,
                                                                      () -> JsObj.empty()
                                                                                 .patch(ops().add("a.b",
                                                                                                  JsInt.of(1)
                                                                                                 )
                                                                                             .toArray()
                                                                                             .remove(JsPath.of("/0/path"))
                                                                                       )
                                                                                 .orElseThrow()

                                                                     );

        Assertions.assertEquals("path is missing in {\"value\":1,\"op\":\"ADD\"}",
                                patchMalformed.getMessage()
                               );
    }

    @Test
    public void test_add_operation_value_is_missing()
    {
        final PatchMalformed patchMalformed = Assertions.assertThrows(PatchMalformed.class,
                                                                      () -> JsObj.empty()
                                                                                 .patch(ops().add("a.b",
                                                                                                  JsInt.of(1)
                                                                                                 )
                                                                                             .toArray()
                                                                                             .remove(JsPath.of("/0/value"))
                                                                                       )
                                                                                 .orElseThrow()

                                                                     );

        Assertions.assertEquals("value is missing in {\"path\":\"a.b\",\"op\":\"ADD\"}",
                                patchMalformed.getMessage()
                               );
    }

    @Test
    public void test_operation_is_not_a_json_object()
    {
        final PatchMalformed patchMalformed = Assertions.assertThrows(PatchMalformed.class,
                                                                      () -> JsObj.empty()
                                                                                 .patch(JsArray.of("should be a json object")
                                                                                       )
                                                                                 .orElseThrow()

                                                                     );

        Assertions.assertEquals("the operation \"should be a json object\" is not a Json object.",
                                patchMalformed.getMessage()
                               );
    }

    @Test
    public void test_operation_not_supported()
    {
        final PatchMalformed patchMalformed = Assertions.assertThrows(PatchMalformed.class,
                                                                      () -> JsObj.empty()
                                                                                 .patch(JsArray.of(JsObj.of("op",
                                                                                                            JsStr.of("'not supported'")
                                                                                                           )
                                                                                                  )
                                                                                       )
                                                                                 .orElseThrow()

                                                                     );

        Assertions.assertEquals("op specified in {\"op\":\"'not supported'\"} not supported",
                                patchMalformed.getMessage()
                               );
    }

    @Test
    public void test_missing_operation()
    {
        final PatchMalformed patchMalformed = Assertions.assertThrows(PatchMalformed.class,
                                                                      () -> JsObj.empty()
                                                                                 .patch(JsArray.of(JsObj.empty())
                                                                                       )
                                                                                 .orElseThrow()

                                                                     );

        Assertions.assertEquals("op field is missing in {}",
                                patchMalformed.getMessage()
                               );
    }

    @Test
    public void test_add_key_to_array_error()
    {
        final PatchOpError patchOpError = Assertions.assertThrows(PatchOpError.class,
                                                                  () -> JsArray.empty()
                                                                               .patch(ops().add("/a",
                                                                                                JsInt.of(1)
                                                                                               )
                                                                                           .toArray()
                                                                                     )
                                                                               .orElseThrow()
                                                                 );

        Assertions.assertEquals("Trying to add the key 'a' in an array. add operation can not be applied in [] at /a. Suggestion: call get(path).isObj() before.",
                                patchOpError.getMessage()
                               );

        final PatchOpError patchOpError1 = Assertions.assertThrows(PatchOpError.class,
                                                                   () -> JsObj.of("a",
                                                                                  JsArray.of(1)
                                                                                 )
                                                                              .patch(ops().add("/a/b",
                                                                                               JsInt.of(1)
                                                                                              )
                                                                                          .toArray()
                                                                                    )
                                                                              .orElseThrow()

                                                                  );

        Assertions.assertEquals("Trying to add the key 'b' in an array. add operation can not be applied in {\"a\":[1]} at /a/b. Suggestion: call get(path).isObj() before.",
                                patchOpError1.getMessage()
                               );
    }

    @Test
    public void test_add_index_to_object_error()
    {
        final PatchOpError patchOpError = Assertions.assertThrows(PatchOpError.class,
                                                                  () -> JsObj.empty()
                                                                             .patch(ops().add("/0",
                                                                                              JsInt.of(1)
                                                                                             )
                                                                                         .toArray()
                                                                                   )
                                                                             .orElseThrow()

                                                                 );

        Assertions.assertEquals("Trying to add at the index '0' in an object. add operation can not be applied in {} at /0. Suggestion: call get(path).isArray() before.",
                                patchOpError.getMessage()
                               );

        final PatchOpError patchOpError1 = Assertions.assertThrows(PatchOpError.class,
                                                                   () -> JsObj.of("a",
                                                                                  JsObj.empty()
                                                                                 )
                                                                              .patch(ops().add("/a/0",
                                                                                               JsInt.of(1)
                                                                                              )
                                                                                          .toArray()
                                                                                    )
                                                                              .orElseThrow()

                                                                  );

        Assertions.assertEquals("Trying to add at the index '0' in an object. add operation can not be applied in {\"a\":{}} at /a/0. Suggestion: call get(path).isArray() before.",
                                patchOpError1.getMessage()
                               );
    }

    @Test
    public void test_add_parent_doesnt_exist_error()
    {
        final PatchOpError patchOpError = Assertions.assertThrows(PatchOpError.class,
                                                                  () -> JsObj.empty()
                                                                             .patch(ops().add("/a/b",
                                                                                              JsInt.of(1)
                                                                                             )
                                                                                         .toArray()
                                                                                   )
                                                                             .orElseThrow()

                                                                 );
        Assertions.assertEquals("Parent not found at /a while applying add in {}. Suggestion: either check if the parent exists or call the put method, which always does the insertion.",
                                patchOpError.getMessage()
                               );
    }


    @Test
    public void test_add_several_keys_to_object_successfully() throws PatchMalformed, PatchOpError
    {
        final JsObj o = JsObj.empty()
                             .patch(ops().add("/a",
                                              JsInt.of(1)
                                             )
                                         .add("/b",
                                              JsBool.TRUE
                                             )
                                         .toArray()
                                   )
                             .orElseThrow();

        Assertions.assertEquals(JsObj.of("a",
                                         JsInt.of(1),
                                         "b",
                                         JsBool.TRUE
                                        ),
                                o
                               );

    }


}
