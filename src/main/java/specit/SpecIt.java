package specit;

import specit.annotation.UserContextScope;
import specit.annotation.lifecycle.AfterScenario;
import specit.annotation.lifecycle.AfterStory;
import specit.annotation.lifecycle.BeforeScenario;
import specit.annotation.lifecycle.BeforeStory;
import specit.element.*;
import specit.interpreter.InterpreterConf;
import specit.interpreter.InterpreterContext;
import specit.interpreter.InterpreterListener;
import specit.interpreter.StoryInterpreter;
import specit.invocation.*;
import specit.invocation.converter.IntegerConverter;
import specit.invocation.converter.StringConverter;
import specit.parser.*;
import specit.report.Reporter;
import specit.util.New;
import specit.util.Proxies;
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
    private AnnotationRegistry annotationRegistry;
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
        for (String value : values) {
            withAlias(kw, value);
        }
        return this;
    }

    public SpecIt withReporter(Reporter reporter) {
        reporters.add(reporter);
        return this;
    }

    protected Reporter getReporterDispatch() {
        return Proxies.proxyDispatch(reporters, Reporter.class);
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

    protected void interpretStory(Story story, Reporter reporterDispatch) {
        InvocationContext invocationContext = newInvocationContext(null, story, reporterDispatch);
        InstanceProvider instanceProvider = new InstanceProviderBasic();

        new StoryInterpreter(this).interpretStory(story, interpreterListener(
                invocationContext,
                getCandidateStepRegistry(),
                newInvoker(instanceProvider),
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

    protected Invoker newInvoker(InstanceProvider instanceProvider) {
        return new Invoker(getConverterRegistry(), instanceProvider);
    }

    public AnnotationRegistry getAnnotationRegistry() {
        if (annotationRegistry == null) {
            annotationRegistry = new AnnotationRegistry();
        }
        return annotationRegistry;
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

    public SpecIt scanAnnotations(Class<?> annotationDefinitions) throws ParameterMappingException {
        getCandidateStepRegistry().scan(annotationDefinitions);
        getAnnotationRegistry().scan(annotationDefinitions);
        return this;
    }

    protected CandidateStepRegistry getCandidateStepRegistry() {
        if (candidateStepRegistry == null)
            candidateStepRegistry = new CandidateStepRegistry(this);
        return candidateStepRegistry;
    }

    private InterpreterListener interpreterListener(
            final InvocationContext invocationContext,
            final CandidateStepRegistry candidateStepRegistry,
            final Invoker invoker,
            final Reporter reporterDispatch) {
        return new InterpreterListenerAdapter(
                invocationContext,
                candidateStepRegistry,
                annotationRegistry,
                invoker,
                reporterDispatch);
    }

    private Listener toParserListener(final StoryBuilder builder) {
        return new Listener() {
            @Override
            public void on(RawPart rawPart) {
                builder.append(rawPart);
            }
        };
    }

    private static class InterpreterListenerAdapter extends InterpreterListener {

        private final InvocationContext invocationContext;
        private final CandidateStepRegistry candidateStepRegistry;
        private final AnnotationRegistry annotationRegistry;
        private final Invoker invoker;
        private final Reporter reporterDispatch;

        public InterpreterListenerAdapter(InvocationContext invocationContext,
                                          CandidateStepRegistry candidateStepRegistry,
                                          AnnotationRegistry annotationRegistry,
                                          Invoker invoker,
                                          Reporter reporterDispatch) {
            this.invocationContext = invocationContext;
            this.candidateStepRegistry = candidateStepRegistry;
            this.annotationRegistry = annotationRegistry;
            this.invoker = invoker;
            this.reporterDispatch = reporterDispatch;
        }

        @Override
        public void beginStory(Story story) {
            reporterDispatch.startStory(story);
            doCreateUserContexts(UserContextScope.Story, invoker, invocationContext);
            doInvokeLifecycles(BeforeStory.class, invoker, invocationContext);
        }

        @Override
        public void endStory(Story story) {
            doInvokeLifecycles(AfterStory.class, invoker, invocationContext);
            doDiscardUserContexts(UserContextScope.Story, invoker, invocationContext);
            reporterDispatch.endStory(story);
        }

        @Override
        public void beginScenario(ExecutablePart scenarioOrBackground, InterpreterContext context) {
            if (scenarioOrBackground instanceof Scenario) {
                reporterDispatch.startScenario((Scenario) scenarioOrBackground);
            }
            else if (scenarioOrBackground instanceof Background) {
                reporterDispatch.startBackground((Background) scenarioOrBackground);
            }
            doCreateUserContexts(UserContextScope.Scenario, invoker, invocationContext);
            doInvokeLifecycles(BeforeScenario.class, invoker, invocationContext);
        }

        @Override
        public void endScenario(ExecutablePart scenarioOrBackground, InterpreterContext context) {
            doInvokeLifecycles(AfterScenario.class, invoker, invocationContext);
            doDiscardUserContexts(UserContextScope.Scenario, invoker, invocationContext);
            if (scenarioOrBackground instanceof Scenario) {
                reporterDispatch.endScenario((Scenario) scenarioOrBackground);
            }
            else if (scenarioOrBackground instanceof Background) {
                reporterDispatch.endBackground((Background) scenarioOrBackground);
            }
        }

        @Override
        public void invokeStep(InvokableStep invokableStep, InterpreterContext context) {
            doInvokeStep(invokableStep, candidateStepRegistry, invoker, invocationContext);
        }

        @Override
        public void invokeRequire(String resolved, InterpreterContext context) {
            throw new UnsupportedOperationException();
        }

        private void doInvokeLifecycles(Class<? extends Annotation> annotationType, Invoker invoker, InvocationContext invocationContext) {
            for (Lifecycle lifecycle : annotationRegistry.getLifecycles(annotationType)) {
                invoker.invoke(invocationContext, lifecycle);
            }
        }

        private void doCreateUserContexts(UserContextScope scope, Invoker invoker, InvocationContext invocationContext) {
            for(UserContextFactorySupport factory : annotationRegistry.getUserContextFactories(scope)) {
                invoker.invoke(invocationContext, factory);
            }
        }

        private void doDiscardUserContexts(UserContextScope scope, Invoker invoker, InvocationContext invocationContext) {
            invocationContext.discardUserContexts(scope);
        }

        private void doInvokeStep(InvokableStep invokableStep, CandidateStepRegistry candidateStepRegistry, Invoker invoker, InvocationContext invocationContext) {
            String resolved = invokableStep.getAdjustedInput();
            Keyword keyword = invokableStep.getKeyword();

            List<CandidateStep> candidateSteps = candidateStepRegistry.find(keyword, resolved);
            if (candidateSteps.isEmpty()) {
                invocationContext.stepInvocationFailed(invokableStep, candidateSteps, "No step matching <" + resolved + "> with keyword <" + keyword + ">");
                return;
            } else if (candidateSteps.size() > 1) {
                invocationContext.stepInvocationFailed(invokableStep, candidateSteps, "More than one step matching <" + resolved + "> with keyword <" + keyword + "> got: " + candidateSteps);
                return;
            }

            CandidateStep candidateStep = candidateSteps.get(0);
            invoker.invoke(invocationContext, invokableStep, candidateStep);
        }
    }

}
