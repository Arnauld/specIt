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
import specit.report.ConsoleColoredReporter;
import specit.report.Reporter;
import specit.report.Reporters;
import specit.util.New;
import specit.util.ProxyDispatch;
import specit.util.TemplateEngine;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static specit.report.Reporters.asInvocationContextListener;

public class SpecIt implements ParserConf, InterpreterConf, MappingConf {

    private final Map<String, Alias> aliases = new HashMap<String, Alias>();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private CandidateStepRegistry candidateStepRegistry;
    private ConverterRegistry converterRegistry;
    private LifecycleRegistry lifecycleRegistry;
    private final List<Reporter> reporters = New.arrayList();

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

    public SpecIt withReporter(Reporter reporter) {
        reporters.add(reporter);
        return this;
    }

    protected Reporter getReporterDispatch() {
        return ProxyDispatch.proxy(reporters, Reporter.class);
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
        Reporter reporterDispatch = getReporterDispatch();
        Story story = parseAndBuildStory(storyContent);
        interpretStory(story, reporterDispatch);
    }

    public void interpretStory(Story story) {
        interpretStory(story, getReporterDispatch());
    }

    protected void interpretStory(Story story, Reporter reporterDispatch) {
        InvocationContext invocationContext = newInvocationContext(null, story, reporterDispatch);
        new StoryInterpreter(this).interpretStory(story, interpreterListener(
                invocationContext,
                getCandidateStepRegistry(),
                getLifecycleRegistry(),
                newInvoker(),
                reporterDispatch));
    }

    public Story parseAndBuildStory(String storyContent) {
        StoryBuilder builder = new StoryBuilder();
        newParser().scan(storyContent, toParserListener(builder));
        return builder.getStory();
    }

    protected InvocationContext newInvocationContext(InvocationContext parent,
                                                     Story currentStory,
                                                     Reporter reporterDispatch) {
        return new InvocationContext(parent, currentStory, asInvocationContextListener(reporterDispatch));
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
            final Invoker invoker,
            final Reporter reporterDispatch) {
        return new InterpreterListener() {

            @Override
            public void beginStory(Story story) {
                reporterDispatch.startStory(story);
                doInvokeLifecycle(BeforeStory.class, invoker, invocationContext);
            }

            @Override
            public void endStory(Story story) {
                doInvokeLifecycle(AfterStory.class, invoker, invocationContext);
                reporterDispatch.endStory(story);
            }

            @Override
            public void beginScenario(ExecutablePart scenarioOrBackground, InterpreterContext context) {
                if(scenarioOrBackground instanceof Scenario)
                    reporterDispatch.startScenario((Scenario)scenarioOrBackground);
                else
                if(scenarioOrBackground instanceof Background)
                    reporterDispatch.startBackground((Background)scenarioOrBackground);
                doInvokeLifecycle(BeforeScenario.class, invoker, invocationContext);
            }

            @Override
            public void endScenario(ExecutablePart scenarioOrBackground, InterpreterContext context) {
                doInvokeLifecycle(AfterScenario.class, invoker, invocationContext);
                if(scenarioOrBackground instanceof Scenario)
                    reporterDispatch.endScenario((Scenario) scenarioOrBackground);
                else
                if(scenarioOrBackground instanceof Background)
                    reporterDispatch.endBackground((Background) scenarioOrBackground);
            }

            @Override
            public void invokeStep(Keyword keyword, String keywordAlias, String resolved, InterpreterContext context) {
                doInvokeStep(keyword, keywordAlias, resolved, candidateStepRegistry, invoker, invocationContext);
            }

            @Override
            public void invokeRequire(String resolved, InterpreterContext context) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void doInvokeLifecycle(Class<? extends Annotation> annotationType, Invoker invoker, InvocationContext invocationContext) {
        for (Lifecycle lifecycle : lifecycleRegistry.getLifecycles(annotationType)) {
            invoker.invoke(invocationContext, lifecycle);
        }
    }

    private void doInvokeStep(Keyword keyword, String keywordAlias, String resolved, CandidateStepRegistry candidateStepRegistry, Invoker invoker, InvocationContext invocationContext) {
        List<CandidateStep> candidateSteps = candidateStepRegistry.find(keyword, resolved);
        if (candidateSteps.isEmpty()) {
            invocationContext.stepInvocationFailed(keywordAlias, resolved, candidateSteps, "No step matching <" + resolved + "> with keyword <" + keyword + ">");
            return;
        }
        else if (candidateSteps.size() > 1) {
            invocationContext.stepInvocationFailed(keywordAlias, resolved, candidateSteps, "More than one step matching <" + resolved + "> with keyword <" + keyword + "> got: " + candidateSteps);
            return;
        }

        CandidateStep candidateStep = candidateSteps.get(0);
        invoker.invoke(invocationContext, keywordAlias, resolved, candidateStep);
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
