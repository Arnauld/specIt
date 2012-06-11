package specit.interpreter;

import specit.element.*;
import specit.util.New;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class StoryInterpreter {

    private final InterpreterConf conf;

    public StoryInterpreter(InterpreterConf conf) {
        this.conf = conf;
    }

    public void interpretStory(final Story story, final InterpreterListener listener) {
        listener.beginStory(story);
        try {
            List<ExecutablePart> scenarioList = story.getScenarioList();
            for (ExecutablePart scenario : scenarioList) {
                final ExecutablePart scenarioRef = scenario;

                List<ExecutablePart> backgroundList = story.getBackgroundList();
                List<ExecutablePart> chainedParts = New.arrayList(backgroundList);
                chainedParts.add(scenario);

                Chain chain = new Chain(story, listener, chainedParts);
                chain.invokeNextWithPreProcess(new PreProcess() {
                                                   @Override
                                                   public void preExecute(ExecutionContext context) {
                                                       listener.beginScenario(scenarioRef, context);
                                                   }
                                               }, new PostProcess() {
                                                   @Override
                                                   public void postExecute(ExecutionContext context) {
                                                       listener.endScenario(scenarioRef, context);
                                                   }
                                               }
                );
            }
        } finally {
            listener.endStory(story);
        }
    }

    private class Chain implements PreProcess, PostProcess {
        private final Story story;
        private final InterpreterListener listener;
        private final List<ExecutablePart> chainedParts;
        private final int chainIndex;
        private final Chain previous;
        //
        private PreProcess preProcess;
        private PostProcess postProcess;

        private Chain(Story story, InterpreterListener listener, List<ExecutablePart> chainedParts) {
            this(story, listener, chainedParts, 0, null);
        }

        private Chain(Story story, InterpreterListener listener, List<ExecutablePart> chainedParts, int chainIndex, Chain previous) {
            this.story = story;
            this.listener = listener;
            this.chainedParts = chainedParts;
            this.chainIndex = chainIndex;
            this.previous = previous;
        }

        public void invokeNextWithPreProcess(PreProcess preProcess, PostProcess postProcess) {
            this.preProcess = preProcess;
            this.postProcess = postProcess;

            if(chainedParts.size()==chainIndex) {
                ExecutionContext context = conf.createExecutionContext();
                execute(context);
                return;
            }
            else {
                Chain next = new Chain(story, listener, chainedParts, chainIndex+1, this);
                interpretScenarioOrBackground(chainedParts.get(chainIndex), story, listener, next);
            }
        }

        @Override
        public void preExecute(ExecutionContext context) {
            if(previous!=null)
                previous.preExecute(context);
            if(preProcess!=null)
                preProcess.preExecute(context);
        }

        public void execute(ExecutionContext context) {
            preExecute(context);
            selfExecute(context);
            postExecute(context);
        }

        @Override
        public void postExecute(ExecutionContext context) {
            if(postProcess!=null)
                postProcess.postExecute(context);
            if(previous!=null)
                previous.postExecute(context);
        }

        protected void selfExecute(ExecutionContext context) {
        }
    }

    public interface PreProcess {
        void preExecute(ExecutionContext context);
    }

    public interface PostProcess {
        void postExecute(ExecutionContext context);
    }

    private void interpretScenarioOrBackground(ExecutablePart scenario,
                                               Story story,
                                               InterpreterListener listener,
                                               Chain chain) {
        HashMap<String, String> variables = New.hashMap();

        List<Example> examples = scenario.getExamples();
        if (examples.isEmpty()) {
            interpretScenarioWithVariables(variables, scenario, story, listener, chain);
        } else {
            recursivelyTraverseExamplesThroughInterpret(0, examples, variables, scenario, story, listener, chain);
        }
    }

    private void recursivelyTraverseExamplesThroughInterpret(int exampleIndex,
                                                             List<Example> examples,
                                                             Map<String, String> cumulatedVariables,
                                                             ExecutablePart scenario,
                                                             Story story,
                                                             InterpreterListener listener,
                                                             Chain chain) {
        if (exampleIndex == examples.size()) {
            interpretScenarioWithVariables(cumulatedVariables, scenario, story, listener, chain);
            return;
        }
        Example example = examples.get(exampleIndex);
        List<Map<String, String>> variablesRows = example.getVariablesRows();
        if (variablesRows.isEmpty()) {
            recursivelyTraverseExamplesThroughInterpret(exampleIndex + 1, examples, cumulatedVariables, scenario, story, listener, chain);
            return;
        }

        for (Map<String, String> variablesRow : variablesRows) {
            Map<String, String> nextVariables = New.hashMap(cumulatedVariables);
            nextVariables.putAll(variablesRow);
            recursivelyTraverseExamplesThroughInterpret(exampleIndex + 1, examples, nextVariables, scenario, story, listener, chain);
        }
    }

    private void interpretScenarioWithVariables(final Map<String, String> variables,
                                                final ExecutablePart scenario,
                                                final Story story,
                                                final InterpreterListener listener,
                                                Chain chain) {
        chain.invokeNextWithPreProcess(new PreProcess() {
            @Override
            public void preExecute(ExecutionContext context) {
                context.defineVariables(variables);
                scenario.traverseExecutablePart(new ExecutionVisitor(context, listener));
            }
        }, null);
    }

    /**
     * Resolves any variables and invoke the <code>Step</code> directive.
     * @param context
     * @param rawPart
     * @param listener
     */
    private void invokeStep(ExecutionContext context, RawPart rawPart, InterpreterListener listener) {
        String rawContent = rawPart.contentAfterAlias().trim();
        Map<String, String> variables = context.getVariables();
        String resolved = conf.templateEngine().resolve(rawContent, variables).toString();

        listener.invokeStep(rawPart.getKeyword(), resolved, context);
    }

    /**
     * Resolves any variables and invoke the <code>Require</code> directive.
     * @param context
     * @param rawPart
     * @param listener
     */
    private void invokeRequire(ExecutionContext context, RawPart rawPart, InterpreterListener listener) {
        String rawContent = rawPart.contentAfterAlias().trim();
        Map<String, String> variables = context.getVariables();
        String resolved = conf.templateEngine().resolve(rawContent, variables).toString();

        listener.invokeRequire(resolved, context);
    }

    private class ExecutionVisitor extends ElementVisitor {
        private final ExecutionContext context;
        private final InterpreterListener listener;

        public ExecutionVisitor(ExecutionContext context, InterpreterListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        public void beginDefaultFallback(Element element) {
            throw new IllegalStateException();
        }

        @Override
        public void beginStep(Step step) {
            if (!step.hasRawPart())
                return;

            invokeStep(context, step.getRawPart(), listener);
        }

        @Override
        public void endStep(Step step) {
        }

        @Override
        public void beginRequire(Require require) {
            if (!require.hasRawPart())
                return;

            invokeRequire(context, require.getRawPart(), listener);
        }

        @Override
        public void endRequire(Require require) {
        }

        @Override
        public void beginExample(Example example) {
            // examples are already handled in #recursivelyTraverseExamplesThroughInterpret
        }

        @Override
        public void endExample(Example example) {
            // examples are already handled in #recursivelyTraverseExamplesThroughInterpret
        }
    }
}
