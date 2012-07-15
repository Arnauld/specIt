package specit.junit;

import specit.SpecIt;
import specit.SpecItRuntimeException;
import specit.util.New;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class SpecItRunner extends Runner {

    private final SpecIt specIt;

    /**
     * Annotation for a method which provides parameters to be injected into the
     * test class constructor by <code>Parameterized</code>
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Parameters {
    }

    private RootSuite rootSuite;


    public SpecItRunner(Class<?> testClass) throws Throwable {
        specIt = getSpecIt(testClass);
        this.rootSuite = new RootSuite(this, testClass);
    }

    public SpecIt getSpecIt() {
        return specIt;
    }

    private Set<String> generated = New.linkedHashSet();

    public String ensureUniqueness(String key) {
        while (generated.contains(key)) {
            key = key + '\u200B'; // zero-width-space
        }
        generated.add(key);
        return key;
    }

    private SpecIt getSpecIt(Class<?> testClass) throws Throwable {
        return (SpecIt) getParametersMethod(new TestClass(testClass)).invokeExplosively(null);
    }

    @Override
    public Description getDescription() {
        try {
            return rootSuite.getSuiteDescription();
        }
        catch (SpecItRuntimeException rte) {
            throw rte;
        }
        catch (Throwable throwable) {
            throw new SpecItRuntimeException(throwable);
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        rootSuite.run(notifier);
    }

    @Override
    public int testCount() {
        return rootSuite.testCount();
    }

    private static FrameworkMethod getParametersMethod(TestClass testClass) throws Exception {
        List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Parameters.class);
        for (FrameworkMethod each : methods) {
            int modifiers = each.getMethod().getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                return each;
            }
        }

        throw new Exception("No public static parameters method on class "
                + testClass.getName());
    }
}
