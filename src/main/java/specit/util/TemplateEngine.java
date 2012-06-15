package specit.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class TemplateEngine {
    private Pattern variablePattern = Pattern.compile("<([^>]+)>");

    public StringBuilder resolve(String text, Map<String, String> variables) {
        StringBuilder resolved = new StringBuilder();

        Matcher matcher = variablePattern.matcher(text);
        int prev = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start > prev) {
                resolved.append(text, prev, start);
            }
            String var = variables.get(text.substring(start + 1, end - 1));
            if (var != null) {
                resolved.append(var);
            } else {
                resolved.append(text, start, end);
            }
            prev = end;
        }
        if (prev < text.length()) {
            resolved.append(text, prev, text.length());
        }

        return resolved;
    }
}
