package specit;

import specit.element.*;
import specit.interpreter.ExecutionContext;
import specit.interpreter.InterpreterConf;
import specit.interpreter.InterpreterListener;
import specit.interpreter.StoryInterpreter;
import specit.mapping.*;
import specit.mapping.converter.IntegerConverter;
import specit.mapping.converter.StringConverter;
import specit.parser.*;
import specit.util.TemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecIt implements ParserConf, InterpreterConf, MappingConf {

    private final Map<String, Alias> aliases = new HashMap<String, Alias>();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private CandidateStepRegistry candidateStepRegistry;
    private ConverterRegistry converterRegistry;

    @Override
    public String ignoredCharactersOnPartStart() {
        return "\t ";
    }

    @Override
    public String cellSeparator() {
        return "|";
    }

    public SpecIt withAlias(Keyword kw, String value) {
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

    @Override
    public String variablePrefix() {
        return "$";
    }

    public Parser newParser () {
        return new Parser(this);
    }

    public void executeStory (String storyContent) {
        StoryBuilder builder = new StoryBuilder();
        newParser().scan(storyContent, toParserListener(builder));

        Story story = builder.getStory();

        CandidateStepRegistry candidateStepRegistry = getCandidateStepRegistry();

        StepInstanceProvider stepInstanceProvider = new StepInstanceProviderBasic();
        Invoker invoker = new Invoker(getConverterRegistry(), stepInstanceProvider);

        new StoryInterpreter(this).interpretStory(story, interpreterListener(candidateStepRegistry, invoker));
    }

    public ConverterRegistry getConverterRegistry() {
        if(converterRegistry==null) {
            converterRegistry = new ConverterRegistry();
            registerDefaultConverters(converterRegistry);
        }
        return converterRegistry;
    }

    protected void registerDefaultConverters(ConverterRegistry registry) {
        registry.registerConverter(String.class, new StringConverter());
        registry.registerConverter(Integer.class, new IntegerConverter());
        registry.registerConverter(int.class, new IntegerConverter());
    }

    public void registerStepDefinitions(Class<?> stepDefinitions) {
        getCandidateStepRegistry().scan(stepDefinitions);
    }

    protected CandidateStepRegistry getCandidateStepRegistry() {
        if(candidateStepRegistry==null)
            candidateStepRegistry = new CandidateStepRegistry(this);
        return candidateStepRegistry;
    }

    private InterpreterListener interpreterListener(final CandidateStepRegistry candidateStepRegistry, final Invoker invoker) {
        return new InterpreterListener() {
            @Override
            public void invokeStep(Keyword keyword, String resolved, ExecutionContext context) {
                List<CandidateStep> candidateSteps = candidateStepRegistry.find(keyword, resolved);
                if(candidateSteps.isEmpty())
                    throw new IllegalStateException("No step matching <" + resolved +"> with keyword <" + keyword + ">");
                else if(candidateSteps.size()>1)
                    throw new IllegalStateException("More than one step matching <" + resolved +"> with keyword <" + keyword + "> got: " + candidateSteps);

                CandidateStep candidateStep = candidateSteps.get(0);
                invoker.invoke(resolved, candidateStep);
            }

            @Override
            public void invokeRequire(String resolved, ExecutionContext context) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Listener toParserListener(final StoryBuilder builder) {
        return new Listener() {
            @Override
            public void on(RawPart rawPart) {
                builder.append(rawPart);
            }
        };
    }
}
