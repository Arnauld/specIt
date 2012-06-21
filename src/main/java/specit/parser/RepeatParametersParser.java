package specit.parser;

import specit.element.RepeatParameters;
import specit.element.Table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 */
public class RepeatParametersParser {

    public static final String FRAGMENT_REF = "(?:[^\\[]*)\\[([^\\]]+)\\]\\s+";

    private final ParserConf conf;
    private Pattern withPattern;
    private Pattern timesPattern;

    public RepeatParametersParser(ParserConf conf) {
        this.conf = conf;
        // (?s) <=> DOTALL <=> '.' matches also line terminators
        this.withPattern = Pattern.compile(FRAGMENT_REF + "with:(?s)(.*)");
        this.timesPattern = Pattern.compile(FRAGMENT_REF + "(\\d+)\\s+time[s]?");
    }

    public RepeatParameters parse(String content) {
        String trimmed = content.trim();
        Matcher matcher = withPattern.matcher(trimmed);
        if (matcher.matches()) {
            String reference = matcher.group(1);
            String tableAsString = matcher.group(2);

            Table table = conf.tableParser().parse(tableAsString);
            return new RepeatParameters(reference, table);
        }

        matcher = timesPattern.matcher(trimmed);
        if (matcher.matches()) {
            String reference = matcher.group(1);
            String loopAsString = matcher.group(2);

            int loop = Integer.parseInt(loopAsString);
            return new RepeatParameters(reference, loop);
        }

        throw new IllegalArgumentException("Repeat content does not match any case! got: <" + content + ">");
    }
}
