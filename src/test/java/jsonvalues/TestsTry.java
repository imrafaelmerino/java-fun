package jsonvalues;

import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static jsonvalues.JsNull.NULL;

public class TestsTry
{

    @Test
    public void test_try_obj() throws MalformedJson
    {

        TryObj tryFailure = Jsons.immutable.object.parse("{");
        TryObj trySuccess = Jsons.immutable.object.parse("{\"a\": null}");
        Assertions.assertTrue(tryFailure.isFailure());
        Assertions.assertFalse(tryFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryFailure::orElseThrow
                               );
        Assertions.assertEquals(Jsons.immutable.object.empty(),
                                tryFailure.orElse(Jsons.immutable.object::empty)
                               );
        Assertions.assertEquals(Jsons.immutable.object.of("a",
                                                          NULL
                                                         ),
                                trySuccess.orElseThrow()
                               );
        Assertions.assertEquals(Optional.empty(),
                                tryFailure.toOptional()
                               );
        Assertions.assertEquals(Optional.of(Jsons.immutable.object.of("a",
                                                                      NULL
                                                                     )),
                                trySuccess.toOptional()
                               );
    }

    @Test
    public void test_try_arr() throws MalformedJson
    {
        TryArr tryObjFailure = Jsons.immutable.array.parse("[");
        TryArr tryArrSuccess = Jsons.immutable.array.parse("[null]");
        Assertions.assertTrue(tryObjFailure.isFailure());
        Assertions.assertFalse(tryObjFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryObjFailure::orElseThrow
                               );
        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                tryObjFailure.orElse(Jsons.immutable.array::empty)
                               );
        Assertions.assertEquals(Jsons.immutable.array.of(NULL),
                                tryArrSuccess.orElseThrow()
                               );
        Assertions.assertEquals(Optional.empty(),
                                tryObjFailure.toOptional()
                               );
        Assertions.assertEquals(Optional.of(Jsons.immutable.array.of(NULL)),
                                tryArrSuccess.toOptional()
                               );
    }

    @Test
    public void test_try() throws MalformedJson
    {
        Try tryFailure = Jsons.immutable.parse("{");
        Try trySuccess = Jsons.immutable.parse("{\"a\": null}");
        Assertions.assertTrue(tryFailure.isFailure());
        Assertions.assertFalse(tryFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryFailure::orElseThrow
                               );
        Assertions.assertThrows(MalformedJson.class,
                                tryFailure::objOrElseThrow
                               );
        Assertions.assertEquals(Jsons.immutable.object.empty(),
                                tryFailure.objOrElse(Jsons.immutable.object::empty)
                               );
        Assertions.assertEquals(Jsons.immutable.object.of("a",
                                                          NULL
                                                         ),
                                trySuccess.objOrElseThrow()
                               );
        Assertions.assertEquals(Jsons.immutable.object.of("a",
                                                          NULL
                                                         ),
                                trySuccess.orElseThrow()
                               );
//        Assertions.assertEquals(Optional.empty(),tryFailure.toOptional());
//        Assertions.assertEquals(Optional.of(Jsons.immutable.object.of("a",NULL)),trySuccess.toOp
//
        Try tryArrFailure = Jsons.immutable.parse("[");
        Try tryArrSuccess = Jsons.immutable.parse("[null]");
        Assertions.assertTrue(tryArrFailure.isFailure());
        Assertions.assertFalse(tryArrFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryArrFailure::orElseThrow
                               );
        Assertions.assertThrows(MalformedJson.class,
                                tryArrFailure::arrOrElseThrow
                               );
        Assertions.assertEquals(Jsons.immutable.array.empty(),
                                tryArrFailure.arrOrElse(Jsons.immutable.array::empty)
                               );
        Assertions.assertEquals(Jsons.immutable.array.of(NULL),
                                tryArrSuccess.arrOrElseThrow()
                               );
        Assertions.assertEquals(Jsons.immutable.array.of(NULL),
                                tryArrSuccess.orElseThrow()
                               );
//        Assertions.assertEquals(Optional.empty(),tryArrFailure.toOptional());
//        Assertions.assertEquals(Optional.of(Jsons.immutable.array.of(NULL)),tryArrSuccess.toOptional());
    }

    @Test
    public void test_parsing_errors() throws MalformedJson
    {

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.parse("{}")
                                                     .arrOrElseThrow()
                               );

        Assertions.assertThrows(MalformedJson.class,
                                () -> Jsons.immutable.parse("[]")
                                                     .objOrElseThrow()
                               );
    }
}
