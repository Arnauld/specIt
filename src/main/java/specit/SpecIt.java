package specit;

import specit.annotation.lifecycle.AfterScenario;
import specit.annotation.lifecycle.AfterStory;
import specit.annotation.lifecycle.BeforeScenario;
import specit.annotation.lifecycle.BeforeStory;
import specit.element.*;
import specit.interpreter.InterpreterContext;
import specit.interpreter.InterpreterConf;
import specit.interpreter.InterpreterListener;
import specit.interpreter.StoryInterpreter;
import specit.invocation.*;
import specit.invocation.converter.IntegerConverter;
import specit.invocation.converter.StringConverter;
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
    private LifecycleRegistry lifecycleRegistry;

    @Override
    public String ignoredCharactersOnPartStart() {
        return "\t ";
    }

    @Override
    public String tableCellSeparator() {
        return "|";
    }

    public SpecIt withAlias(Keyword kw, String value) {
        aliases.put(value, new Alias(kw, value));
        return this;
    }

    public SpecIt withAliases(Keyword kw, String... values) {
        for(String value : values)
            withAlias(kw, value);
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
    public InterpreterContext createInterpreterContext() {
        return new InterpreterContext();
    }

    @Override
    public CommentParser commentParser() {
        return new CommentParser();
    }

    @Override
    public TableParser tableParser() {
        return new TableParser(this);
    }

    @Override
    public RepeatParametersParser repeatParametersParser() {
        return new RepeatParametersParser(this);
    }

    @Override
    public String variablePrefix() {
        return "$";
    }

    public Parser newParser() {
        return new Parser(this);
    }

    public void executeStory(String storyContent) {
        Story story = parseAndBuildStory(storyContent);
        interpretStory(story);
    }

    public void interpretStory(Story story) {
        InvocationContext invocationContext = newInvocationContext(null, story);
        new StoryInterpreter(this).interpretStory(story, interpreterListener(
                invocationContext,
                getCandidateStepRegistry(),
                getLifecycleRegistry(),
                newInvoker()));
    }

    public Story parseAndBuildStory(String storyContent) {
        StoryBuilder builder = new StoryBuilder();
        newParser().scan(storyContent, toParserListener(builder));
        return builder.getStory();
    }

    protected InvocationContext newInvocationContext(InvocationContext parent, Story currentStory) {
        return new InvocationContext(parent, currentStory);
    }

    protected Invoker newInvoker() {
        InstanceProvider instanceProvider = new InstanceProviderBasic();
        return new Invoker(getConverterRegistry(), instanceProvider);
    }

    public LifecycleRegistry getLifecycleRegistry() {
        if (lifecycleRegistry == null) {
            lifecycleRegistry = new LifecycleRegistry();
        }
        return lifecycleRegistry;
    }

    public ConverterRegistry getConverterRegistry() {
        if (converterRegistry == null) {
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

    public void scanAnnotations(Class<?> stepOrLifecycleDefinitions) throws ParameterMappingException {
        getCandidateStepRegistry().scan(stepOrLifecycleDefinitions);
        getLifecycleRegistry().scan(stepOrLifecycleDefinitions);
    }

    protected CandidateStepRegistry getCandidateStepRegistry() {
        if (candidateStepRegistry == null)
            candidateStepRegistry = new CandidateStepRegistry(this);
        return candidateStepRegistry;
    }

    private InterpreterListener interpreterListener(
            final InvocationContext invocationContext,
            final CandidateStepRegistry candidateStepRegistry,
            final LifecycleRegistry lifecycleRegistry,
            final Invoker invoker) {
        return new InterpreterListener() {

            @Override
            public void beginStory(Story story) {
                for (Lifecycle lifecycle : lifecycleRegistry.getLifecycles(BeforeStory.class)) {
                    invoker.invoke(invocationContext, lifecycle);
                }
            }

            @Override
            public void endStory(Story story) {
                for (Lifecycle lifecycle : lifecycleRegistry.getLifecycles(AfterStory.class)) {
                    invoker.invoke(invocationContext, lifecycle);
                }
            }

            @Override
            public void beginScenario(ExecutablePart scenario, InterpreterContext context) {
                for (Lifecycle lifecycle : lifecycleRegistry.getLifecycles(BeforeScenario.class)) {
                    invoker.invoke(invocationContext, lifecycle);
                }
            }

            @Override
            public void endScenario(ExecutablePart scenario, InterpreterContext context) {
                for (Lifecycle lifecycle : lifecycleRegistry.getLifecycles(AfterScenario.class)) {
                    invoker.invoke(invocationContext, lifecycle);
                }
            }

            @Override
            public void invokeStep(Keyword keyword, String resolved, InterpreterContext context) {
                List<CandidateStep> candidateSteps = candidateStepRegistry.find(keyword, resolved);
                if (candidateSteps.isEmpty())
                    throw new IllegalStateException("No step matching <" + resolved + "> with keyword <" + keyword + ">");
                else if (candidateSteps.size() > 1)
                    throw new IllegalStateException("More than one step matching <" + resolved + "> with keyword <" + keyword + "> got: " + candidateSteps);

                CandidateStep candidateStep = candidateSteps.get(0);
                invoker.invoke(invocationContext, resolved, candidateStep);
            }

            @Override
            public void invokeRequire(String resolved, InterpreterContext context) {
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
