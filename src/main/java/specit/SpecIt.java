package specit;

import static specit.report.Reporters.asInvocationContextListener;
import static specit.util.ResourceBundles.getOrDefault;

import specit.annotation.UserContext;
import specit.annotation.lifecycle.AfterScenario;
import specit.annotation.lifecycle.AfterStory;
import specit.annotation.lifecycle.BeforeScenario;
import specit.annotation.lifecycle.BeforeStory;
import specit.element.Alias;
import specit.element.Background;
import specit.element.ExecutablePart;
import specit.element.InvocationContext;
import specit.element.InvokableStep;
import specit.element.Keyword;
import specit.element.RawElement;
import specit.element.Scenario;
import specit.element.Story;
import specit.element.StoryBuilder;
import specit.interpreter.InterpreterConf;
import specit.interpreter.InterpreterContext;
import specit.interpreter.InterpreterListener;
import specit.interpreter.StoryInterpreter;
import specit.invocation.AnnotationRegistry;
import specit.invocation.CandidateStep;
import specit.invocation.CandidateStepRegistry;
import specit.invocation.ConverterRegistry;
import specit.invocation.InstanceProvider;
import specit.invocation.InstanceProviderBasic;
import specit.invocation.Invoker;
import specit.invocation.Lifecycle;
import specit.invocation.MappingConf;
import specit.invocation.ParameterMappingException;
import specit.invocation.UserContextFactorySupport;
import specit.invocation.converter.DateConverter;
import specit.invocation.converter.EnumByNameConverter;
import specit.invocation.converter.IntegerConverter;
import specit.invocation.converter.StringConverter;
import specit.parser.CommentParser;
import specit.parser.Listener;
import specit.parser.Parser;
import specit.parser.ParserConf;
import specit.parser.RepeatParametersParser;
import specit.parser.TableParser;
import specit.report.Reporter;
import specit.util.DateFormats;
import specit.util.New;
import specit.util.Proxies;
import specit.util.TemplateEngine;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 */
public class SpecIt implements ParserConf, InterpreterConf, MappingConf {

    public static final String ALIAS_SEPARATOR = "aliasSeparator";
    
    private final Map<String, Alias> aliases = new HashMap<String, Alias>();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private CandidateStepRegistry candidateStepRegistry;
    private ConverterRegistry converterRegistry;
    private AnnotationRegistry annotationRegistry;
    private final List<Reporter> reporters = New.arrayList();
    private StoryLoader storyLoader;
    private String[] storyPaths;

    public SpecIt() {
        initAliasesWithDefault();
    }

    /**
     *
     * @see #initAliasesWithDefault(java.util.Locale)
     * @see java.util.Locale#getDefault()
     */
    public SpecIt initAliasesWithDefault() {
        return initAliasesWithDefault(Locale.getDefault());
    }

    /**
     *
     * @param locale locale for which one will attempt to find localized alias
     * @return
     */
    public SpecIt initAliasesWithDefault(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("specit.i18n.aliases", locale);
        String aliasSeparator = getOrDefault(bundle, ALIAS_SEPARATOR, ",");
        for(String keywordAsString : bundle.keySet()) {
            if(keywordAsString.equals(ALIAS_SEPARATOR)) {
                continue;
            }

            Keyword keyword = Keyword.valueOf(keywordAsString);
            if(keyword==null) {
                continue;
            }

            String inlinedAliases = bundle.getString(keywordAsString);
            for(String alias : inlinedAliases.split(aliasSeparator)) {
                withAlias(keyword, alias.trim());
            }
        }
        return this;
    }

    @Override
    public String ignoredCharactersOnPartStart() {
        return "\t ";
    }

    @Override
    public String tableCellSeparator() {
        return "|";
    }

    /**
     * @see #withAliases(specit.element.Keyword, String...)
     * @see #aliases()
     * @see #initAliasesWithDefault()
     */
    public SpecIt withAlias(Keyword kw, String value) {
        aliases.put(value, new Alias(kw, value));
        return this;
    }

    /**
     * @see #withAlias(specit.element.Keyword, String)
     * @see #aliases()
     * @see #initAliasesWithDefault()
     */
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

    public SpecIt withReporters(Reporter...reporters) {
        this.reporters.addAll(Arrays.asList(reporters));
        return this;
    }

    protected Reporter getReporterDispatch() {
        return Proxies.proxyDispatch(reporters, Reporter.class);
    }

    /**
     * @see #withAlias(specit.element.Keyword, String)
     * @see #withAliases(specit.element.Keyword, String...)
     * @see #initAliasesWithDefault()
     */
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

    /**
     * @see #withStoryPaths(String...)
     * @see #withStoryLoader(StoryLoader)
     */
    public String[] storyPaths() {
        return storyPaths;
    }

    /**
     * @see #storyPaths()
     * @see #withStoryLoader(StoryLoader)
     */
    public SpecIt withStoryPaths(String... storyPaths) {
        this.storyPaths = storyPaths;
        return this;
    }

    /**
     * @see #storyPaths()
     * @see #withStoryPaths(String...)
     */
    public SpecIt withStoryLoader(StoryLoader storyLoader) {
        this.storyLoader = storyLoader;
        return this;
    }

    public Story loadStory(String storyPath) {
        String storyContent = storyLoader.loadStoryAsText(storyPath);
        return parseAndBuildStory(storyPath, storyContent);
    }

    public void executeStoryContent(String storyContent) {
        Story story = parseAndBuildStory("", storyContent);
        interpretStory(story);
    }

    public void interpretStory(Story story) {
        interpretStory(story, invokerInterpreterListener(story));
    }

    public void interpretStory(Story story, InterpreterListener interpreterListener) {
        new StoryInterpreter(this).interpretStory(story, interpreterListener);
    }

    public InterpreterListener invokerInterpreterListener(Story story) {
        Reporter reporterDispatch = getReporterDispatch();
        InvocationContext invocationContext = newInvocationContext(null, story, reporterDispatch);
        InstanceProvider instanceProvider = new InstanceProviderBasic();

        return interpreterListener(
                invocationContext,
                newInvoker(instanceProvider),
                reporterDispatch);
    }

    public Story parseAndBuildStory(String storyPath, String storyContent) {
        StoryBuilder builder = new StoryBuilder(storyPath);
        newParser().scan(storyContent, toParserListener(builder));
        return builder.getStory();
    }

    protected InvocationContext newInvocationContext(InvocationContext parent,
                                                     Story currentStory,
                                                     Reporter reporterDispatch)
    {
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
        registry.registerConverter(Date.class, new DateConverter(defaultDateFormats()));
        registry.registerConverter(int.class, new IntegerConverter());
        registry.registerConverter(new EnumByNameConverter());
    }

    /**
     * <pre>
     *     "yyyy/MM/dd",
     *     "yyyy-MM-dd",
     *     "yyyy/MM/dd'T'HH:mm:ss",
     *     "yyyy/MM/dd' 'HH:mm:ss",
     *     "yyyy-MM-dd'T'HH:mm:ssz",
     *     "yyyy-MM-dd' 'HH:mm:ssz",
     *     "dd/MM/yyyy",
     *     "dd-MM-yyyy",
     *     "dd/MM/yyyy'T'HH:mm:ss",
     *     "dd/MM/yyyy' 'HH:mm:ss",
     *     "dd-MM-yyyy'T'HH:mm:ssz",
     *     "dd-MM-yyyy' 'HH:mm:ssz"
     * </pre>
     */
    protected List<DateFormat> defaultDateFormats() {
        return DateFormats.toDateFormats(
                "yyyy/MM/dd",
                "yyyy-MM-dd",
                "yyyy/MM/dd'T'HH:mm:ss",
                "yyyy/MM/dd' 'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ssz",
                "yyyy-MM-dd' 'HH:mm:ssz",
                "dd/MM/yyyy",
                "dd-MM-yyyy",
                "dd/MM/yyyy'T'HH:mm:ss",
                "dd/MM/yyyy' 'HH:mm:ss",
                "dd-MM-yyyy'T'HH:mm:ssz",
                "dd-MM-yyyy' 'HH:mm:ssz"
        );
    }

    public SpecIt scanAnnotations(Class<?> annotationDefinitions) throws ParameterMappingException {
        getCandidateStepRegistry().scan(annotationDefinitions);
        getAnnotationRegistry().scan(annotationDefinitions);
        return this;
    }

    protected CandidateStepRegistry getCandidateStepRegistry() {
        if (candidateStepRegistry == null) {
            candidateStepRegistry = new CandidateStepRegistry(this);
        }
        return candidateStepRegistry;
    }

    private InterpreterListener interpreterListener(
            final InvocationContext invocationContext,
            final Invoker invoker,
            final Reporter reporterDispatch)
    {
        return new InterpreterListenerAdapter(
                invocationContext,
                getCandidateStepRegistry(),
                getAnnotationRegistry(),
                invoker,
                reporterDispatch);
    }

    private Listener toParserListener(final StoryBuilder builder) {
        return new Listener() {
            @Override
            public void on(RawElement rawElement) {
                builder.append(rawElement);
            }
        };
    }

    /**
     *
     */
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
                                          Reporter reporterDispatch)
        {
            this.invocationContext = invocationContext;
            this.candidateStepRegistry = candidateStepRegistry;
            this.annotationRegistry = annotationRegistry;
            this.invoker = invoker;
            this.reporterDispatch = reporterDispatch;
        }

        @Override
        public void beginStory(Story story) {
            reporterDispatch.startStory(story);
            doCreateUserContexts(UserContext.Scope.Story);
            doInvokeLifecycles(BeforeStory.class);
        }

        @Override
        public void endStory(Story story) {
            doInvokeLifecycles(AfterStory.class);
            doDiscardUserContexts(UserContext.Scope.Story);
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
            invocationContext.beginScenarioOrBackground(scenarioOrBackground);
            doCreateUserContexts(UserContext.Scope.Scenario);
            doInvokeLifecycles(BeforeScenario.class);
        }

        @Override
        public void endScenario(ExecutablePart scenarioOrBackground, InterpreterContext context) {
            doInvokeLifecycles(AfterScenario.class);
            doDiscardUserContexts(UserContext.Scope.Scenario);
            invocationContext.endScenarioOrBackground(scenarioOrBackground);
            if (scenarioOrBackground instanceof Scenario) {
                reporterDispatch.endScenario((Scenario) scenarioOrBackground);
            }
            else if (scenarioOrBackground instanceof Background) {
                reporterDispatch.endBackground((Background) scenarioOrBackground);
            }
        }

        @Override
        public void invokeStep(InvokableStep invokableStep, InterpreterContext context) {
            doInvokeStep(invokableStep);
        }

        @Override
        public void invokeRequire(String resolved, InterpreterContext context) {
            throw new UnsupportedOperationException();
        }

        private void doInvokeLifecycles(Class<? extends Annotation> annotationType) {
            for (Lifecycle lifecycle : annotationRegistry.getLifecycles(annotationType)) {
                invoker.invoke(invocationContext, lifecycle);
            }
        }

        private void doCreateUserContexts(UserContext.Scope scope) {
            for (UserContextFactorySupport factory : annotationRegistry.getUserContextFactories(scope)) {
                invoker.invoke(invocationContext, factory);
            }
        }

        private void doDiscardUserContexts(UserContext.Scope scope) {
            invocationContext.discardUserContexts(scope);
        }

        private void doInvokeStep(InvokableStep invokableStep) {
            String resolved = invokableStep.getAdjustedInput();
            Keyword keyword = invokableStep.getKeyword();

            List<CandidateStep> candidateSteps = candidateStepRegistry.find(keyword, resolved);
            if (candidateSteps.isEmpty()) {
                invocationContext.stepInvocationFailed(invokableStep, candidateSteps,
                        "No step matching <" + resolved + "> with keyword <" + keyword + ">");
                return;
            }
            else if (candidateSteps.size() > 1) {
                invocationContext.stepInvocationFailed(invokableStep, candidateSteps,
                        "More than one step matching <" + resolved + "> with keyword <" + keyword + "> got: "
                                + candidateSteps);
                return;
            }

            CandidateStep candidateStep = candidateSteps.get(0);
            invoker.invoke(invocationContext, invokableStep, candidateStep);
        }
    }
}
