package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class TestSuiteConventionsTest {

    private static final Path TEST_SOURCES = Path.of("src/test/java");
    private static final Pattern TEST_ANNOTATED_METHOD = Pattern.compile(
            "@(?:Test|ParameterizedTest)\\s*(?:\\R\\s*@[^\r\n]+\\s*)*\\R\\s*(?:public\\s+)?void\\s+([A-Za-z0-9_]+)\\s*\\("
    );
    private static final Pattern PUBLIC_TEST_CLASS = Pattern.compile("(?m)^\\s*public\\s+class\\s+\\w+Test\\b");
    private static final Pattern PUBLIC_TEST_METHOD = Pattern.compile(
            "@(?:Test|ParameterizedTest)\\s*(?:\\R\\s*@[^\r\n]+\\s*)*\\R\\s*public\\s+void\\s+"
    );

    @Test
    void shouldUseTestSuffixForFilesWhenContainingJUnitTests() throws IOException {
        List<String> violations = new ArrayList<>();
        for (Path path : findJavaTestFiles()) {
            String source = read(path);
            if (containsTestAnnotation(source) && !path.getFileName().toString().endsWith("Test.java")) {
                violations.add(path.toString());
            }
        }
        Assertions.assertTrue(violations.isEmpty(),
                              "Test classes should use *Test.java naming: " + violations);
    }

    @Test
    void shouldNameMethodsUsingShouldWhenPatternWhenContainingJUnitTests() throws IOException {
        List<String> violations = new ArrayList<>();
        for (Path path : findJavaTestFiles()) {
            String source = read(path);
            if (!containsTestAnnotation(source)) continue;

            Matcher matcher = TEST_ANNOTATED_METHOD.matcher(source);
            while (matcher.find()) {
                String methodName = matcher.group(1);
                if (!(methodName.startsWith("should") && methodName.contains("When"))) {
                    violations.add(path + "#" + methodName);
                }
            }
        }
        Assertions.assertTrue(violations.isEmpty(),
                              "Test method names should follow should...When...: " + violations);
    }

    @Test
    void shouldAvoidPublicModifiersWhenUsingJUnit5TestClassesAndMethods() throws IOException {
        List<String> violations = new ArrayList<>();
        for (Path path : findJavaTestFiles()) {
            String source = read(path);
            if (!containsTestAnnotation(source)) continue;

            if (PUBLIC_TEST_CLASS.matcher(source).find()) {
                violations.add(path + " contains public test class");
            }
            if (PUBLIC_TEST_METHOD.matcher(source).find()) {
                violations.add(path + " contains public @Test method");
            }
        }
        Assertions.assertTrue(violations.isEmpty(),
                              "JUnit 5 tests should avoid public class/method modifiers: " + violations);
    }

    private static List<Path> findJavaTestFiles() throws IOException {
        try (Stream<Path> files = Files.walk(TEST_SOURCES)) {
            return files.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".java"))
                        .toList();
        }
    }

    private static boolean containsTestAnnotation(String source) {
        return source.contains("@Test") || source.contains("@ParameterizedTest");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path);
    }
}
