package specit.element;

import specit.util.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class Conf {

    private final Map<String, Alias> aliases = new HashMap<String, Alias>();
    private final TemplateEngine templateEngine = new TemplateEngine();

    public String ignoredCharactersOnPartStart() {
        return "\t ";
    }

    public Conf withAlias(Keyword kw, String value) {
        aliases.put(value, new Alias(kw, value));
        return this;
    }

    public Iterable<Alias> aliases() {
        return aliases.values();
    }

    public TemplateEngine templateEngine() {
        return templateEngine;
    }
}
