package specit;

import specit.element.Alias;
import specit.element.Keyword;
import specit.interpreter.ExecutionContext;
import specit.interpreter.InterpreterConf;
import specit.parser.CommentParser;
import specit.parser.TableParser;
import specit.parser.ParserConf;
import specit.util.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class Conf implements ParserConf, InterpreterConf {

    private final Map<String, Alias> aliases = new HashMap<String, Alias>();
    private final TemplateEngine templateEngine = new TemplateEngine();

    @Override
    public String ignoredCharactersOnPartStart() {
        return "\t ";
    }

    @Override
    public String cellSeparator() {
        return "|";
    }

    public Conf withAlias(Keyword kw, String value) {
        aliases.put(value, new Alias(kw, value));
        return this;
    }

    @Override
    public Iterable<Alias> aliases() {
        return aliases.values();
    }

    @Override
    public TemplateEngine templateEngine() {
        return templateEngine;
    }

    @Override
    public ExecutionContext createExecutionContext() {
        return new ExecutionContext();
    }

    @Override
    public CommentParser commentParser() {
        return new CommentParser();
    }

    @Override
    public TableParser tabularVariablesParser() {
        return new TableParser(this);
    }
}
