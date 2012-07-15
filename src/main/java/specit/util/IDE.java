package specit.util;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class IDE {

    public static boolean isExecutedWithinIDE() {
        List<String> idePackages = Arrays.asList("org.eclipse", "com.intellij");
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
        for (int i = traceElements.length - 1; i >= 0; i--) {
            StackTraceElement traceElement = traceElements[i];
            if (startsWithOneOf(traceElement.getClassName(), idePackages)) {
                return true;
            }
        }
        return false;
    }

    private static boolean startsWithOneOf(String value, List<String> prefixes) {
        for (String prefix : prefixes) {
            if (value.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
