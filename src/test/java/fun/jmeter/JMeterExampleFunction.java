package fun.jmeter;

import jsonvalues.JsObj;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * JMeter function to generate a payload. To use the `JMeterExampleFunction` function in JMeter, you
 * need to run JMeter in at least Java 21 and follow these steps:
 * <p>
 * 1. Create a JAR of the Test Classes: - Compile the `JMeterExampleFunction` class and other related classes into a JAR
 * file with the command `mvn jar:test-jar`
 * <p>
 * 2. Add the JAR `-X.X.X-tests.jar` to the JMeter lib/ext Folder: - Place the JAR file containing the
 * test classes in the `lib/ext` directory of your JMeter installation.
 * <p>
 * 3. Add Dependencies (json-values and json-fun JARs) to the JMeter lib Folder: - Download the `json-values` and
 * `json-fun` JAR files. - Place these JAR files in the `lib` directory of your JMeter installation.
 * <p>
 * 4. Restart JMeter: Restart JMeter to apply the changes and load the custom function.
 * <p>
 * 5. Use the Custom Function in JMeter: - In your JMeter test plan, you can now use the `JMeterExampleFunction` function by
 * referencing its key: `${__MY_GEN()}`.
 * <p>
 * These steps ensure that JMeter recognizes the custom function and its dependencies. Note that the JARs and
 * dependencies need to be compatible with the JMeter version you are using. Adjust the versions accordingly to avoid
 * compatibility issues.
 */
public final class JMeterExampleFunction extends AbstractFunction {

    Supplier<JsObj> gen = JsObjGen.of("name",
                                      JsStrGen.alphabetic(),
                                      "age",
                                      JsIntGen.arbitrary(0,
                                                         100))
                                  .apply(new Random());

    @Override
    public String execute(final SampleResult sampleResult,
                          final Sampler sampler) {
        return gen.get()
                  .toString();
    }

    @Override
    public void setParameters(final Collection<CompoundVariable> collection) throws InvalidVariableException {

    }

    @Override
    public String getReferenceKey() {
        return "__MY_GEN";
    }

    @Override
    public List<String> getArgumentDesc() {
        return new ArrayList<>();
    }
}
