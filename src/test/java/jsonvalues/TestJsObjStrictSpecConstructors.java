package jsonvalues;

import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TestJsObjStrictSpecConstructors {
    @Test
    public void test_20_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number,
                                          "q",
                                          number,
                                          "r",
                                          number,
                                          "s",
                                          number,
                                          "t",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE,
                             "r",
                             ONE,
                             "s",
                             ONE,
                             "t",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }
    @Test
    public void test_19_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number,
                                          "q",
                                          number,
                                          "r",
                                          number,
                                          "s",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE,
                             "r",
                             ONE,
                             "s",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }
    @Test
    public void test_18_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number,
                                          "q",
                                          number,
                                          "r",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE,
                             "r",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }
    @Test
    public void test_17_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number,
                                          "q",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());



    }
    @Test
    public void test_16_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());

    }

    @Test
    public void test_15_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());



    }

    @Test
    public void test_14_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());



    }

    @Test
    public void test_13_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());



    }


    @Test
    public void test_12_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());




    }

    @Test
    public void test_11_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_10_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_9_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());

    }

    @Test
    public void test_8_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());



    }

    @Test
    public void test_6_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_5_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_4_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_3_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());


        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());

    }

    @Test
    public void test_2_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number,
                                          "b",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());

    }

    @Test
    public void test_1_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.strict("a",
                                          number
        );

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE
        );

        Set<JsErrorPair> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertFalse(spec.test(obj.set("z",ONE)).isEmpty());


    }
}
