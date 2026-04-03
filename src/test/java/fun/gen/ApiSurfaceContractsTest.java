package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

class ApiSurfaceContractsTest {

    @Test
    void shouldNotExposeLegacyAliasMethodsWhenInspectingGenApi() {
        List<String> publicMethodNames = Arrays.stream(Gen.class.getMethods())
                                               .map(Method::getName)
                                               .toList();

        Assertions.assertFalse(publicMethodNames.contains("cons"));
        Assertions.assertFalse(publicMethodNames.contains("then"));
        Assertions.assertFalse(publicMethodNames.contains("suchThat"));

        Assertions.assertTrue(publicMethodNames.contains("constant"));
        Assertions.assertTrue(publicMethodNames.contains("flatMap"));
        Assertions.assertTrue(publicMethodNames.contains("filter"));
    }

    @Test
    void shouldExposeOnlyExplicitCollectionReadSemanticsWhenInspectingMyRecordApi() {
        Assertions.assertThrows(NoSuchMethodException.class,
                                () -> MyRecord.class.getMethod("getOptionalList",
                                                               String.class));
        Assertions.assertThrows(NoSuchMethodException.class,
                                () -> MyRecord.class.getMethod("getList",
                                                               String.class));
        Assertions.assertThrows(NoSuchMethodException.class,
                                () -> MyRecord.class.getMethod("getOptionalSet",
                                                               String.class));
        Assertions.assertThrows(NoSuchMethodException.class,
                                () -> MyRecord.class.getMethod("getSet",
                                                               String.class));
        Assertions.assertThrows(NoSuchMethodException.class,
                                () -> MyRecord.class.getMethod("getOptionalMap",
                                                               String.class));
        Assertions.assertThrows(NoSuchMethodException.class,
                                () -> MyRecord.class.getMethod("getMap",
                                                               String.class));

        Assertions.assertDoesNotThrow(() -> MyRecord.class.getMethod("getOptionalListView",
                                                                     String.class));
        Assertions.assertDoesNotThrow(() -> MyRecord.class.getMethod("getListCopy",
                                                                     String.class));
        Assertions.assertDoesNotThrow(() -> MyRecord.class.getMethod("getOptionalSetView",
                                                                     String.class));
        Assertions.assertDoesNotThrow(() -> MyRecord.class.getMethod("getSetCopy",
                                                                     String.class));
        Assertions.assertDoesNotThrow(() -> MyRecord.class.getMethod("getOptionalMapView",
                                                                     String.class));
        Assertions.assertDoesNotThrow(() -> MyRecord.class.getMethod("getMapCopy",
                                                                     String.class));
    }

    @Test
    void shouldExposeOnlyCompactOfFactoriesWhenInspectingMyRecordGenApi() {
        List<Method> staticOfMethods = Arrays.stream(MyRecordGen.class.getDeclaredMethods())
                                             .filter(it -> Modifier.isStatic(it.getModifiers()))
                                             .filter(it -> it.getName().equals("of"))
                                             .toList();

        Assertions.assertEquals(2,
                                staticOfMethods.size(),
                                "Expected only compact of() factories");

        int maxOfArity = staticOfMethods.stream()
                                        .mapToInt(Method::getParameterCount)
                                        .max()
                                        .orElse(0);

        Assertions.assertTrue(maxOfArity <= 3,
                              "of(...) should be compact and not overload-heavy");
    }
}
