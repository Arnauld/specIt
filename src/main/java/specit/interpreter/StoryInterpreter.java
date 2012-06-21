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
                                                   public void preExecute(InterpreterContext context) {
                                                       listener.beginScenario(scenarioRef, context);
                                                   }
                                               }, new PostProcess() {
                                                   @Override
                                                   public void postExecute(InterpreterContext context) {
                                                       listener.endScenario(scenarioRef, context);
                                                   }
                                               }
                );
            }
        } finally {
            listener.endStory(story);
        }
    }

    private void interpretScenarioOrBackground(ExecutablePart scenario,
                                               InterpreterListener listener,
                                               Chain chain) {
        HashMap<String, String> variables = New.hashMap();

        List<Example> examples = scenario.getExamples();
        if (examples.isEmpty()) {
            interpretScenarioWithVariables(variables, scenario, listener, chain);
        } else {
            recursivelyTraverseExamplesThroughInterpret(0, examples, variables, scenario, listener, chain);
        }
    }

    private void recursivelyTraverseExamplesThroughInterpret(int exampleIndex,
                                                             List<Example> examples,
                                                             Map<String, String> cumulatedVariables,
                                                             ExecutablePart scenario,
                                                             InterpreterListener listener,
                                                             Chain chain) {
        if (exampleIndex == examples.size()) {
            interpretScenarioWithVariables(cumulatedVariables, scenario, listener, chain);
            return;
        }
        Example example = examples.get(exampleIndex);
        Table exampleTable = example.getExampleTable();
        if (exampleTable.isEmpty()) {
            recursivelyTraverseExamplesThroughInterpret(exampleIndex + 1, examples, cumulatedVariables, scenario, listener, chain);
            return;
        }

        for (Table.Row row : exampleTable) {
            Map<String, String> nextVariables = New.hashMap(cumulatedVariables);
            nextVariables.putAll(row.asMap());
            recursivelyTraverseExamplesThroughInterpret(exampleIndex + 1, examples, nextVariables, scenario, listener, chain);
        }
    }

    private void interpretScenarioWithVariables(final Map<String, String> variables,
                                                final ExecutablePart scenario,
                                                final InterpreterListener listener,
                                                Chain chain) {
        chain.invokeNextWithPreProcess(new PreProcess() {
            @Override
            public void preExecute(InterpreterContext context) {
                context.defineVariables(variables);
                scenario.traverseExecutablePart(new ExecutionVisitor(context, listener));
            }
        }, null);
    }

    /**
     * Resolves any variables and invoke the <code>Step</code> directive.
     */
    private void invokeStep(InterpreterContext context, Step step, InterpreterListener listener) {
        String rawContent = step.getRawPart().contentAfterAlias().trim();
        Map<String, String> variables = context.getVariables();
        String resolved = conf.templateEngine().resolve(rawContent, variables).toString();

        listener.invokeStep(new InvokableStep(step, resolved), context);
    }

    /**
     * Resolves any variables and invoke the <code>Require</code> directive.
     */
    private void invokeRequire(InterpreterContext context, RawPart rawPart, InterpreterListener listener) {
        String rawContent = rawPart.contentAfterAlias().trim();
        Map<String, String> variables = context.getVariables();
        String resolved = conf.templateEngine().resolve(rawContent, variables).toString();

        listener.invokeRequire(resolved, context);
    }

    private class ExecutionVisitor extends ElementVisitor {
        private final InterpreterContext context;
        private final InterpreterListener listener;

        public ExecutionVisitor(InterpreterContext context, InterpreterListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        public boolean beginDefaultFallback(Element element) {
            throw new IllegalStateException();
        }

        @Override
        public boolean beginRepeat(Repeat repeat) {
            if (!repeat.hasRawPart()) {
                return false;
            }

            RepeatParameters parameters = repeat.getRepeatParameters();
            if(!parameters.hasReference())
                return false;

            Fragment fragment = repeat.findParentInStoryTree();
            if(fragment==null) {
                throw new IllegalStateException("No fragment found for Repeat directive [" + parameters.getReference() + "]");
            }

            if(parameters.hasWithTable()) {
                Table table = parameters.getWithTable();
                for(Table.Row row : table) {
                    InterpreterContext subContext = context.nestedContext(row.asMap());
                    fragment.traverseExecutablePart(new ExecutionVisitor(subContext, listener));
                }
            }
            else {
                for(int i=0; i<parameters.getLoopCount(); i++) {
                    fragment.traverseExecutablePart(this);
                }
            }
            return true;
        }

        @Override
        public void endRepeat(Repeat repeat) {
        }

        @Override
        public boolean beginStep(Step step) {
            if (!step.hasRawPart()) {
                return false;
            }

            invokeStep(context, step, listener);
            return true;
        }

        @Override
        public void endStep(Step step) {
        }

        @Override
        public boolean beginRequire(Require require) {
            if (!require.hasRawPart()) {
                return false;
            }

            invokeRequire(context, require.getRawPart(), listener);
            return true;
        }

        @Override
        public void endRequire(Require require) {
        }

        @Override
        public boolean beginExample(Example example) {
            // examples are already handled in #recursivelyTraverseExamplesThroughInterpret
            return false;
        }

        @Override
        public void endExample(Example example) {
            // examples are already handled in #recursivelyTraverseExamplesThroughInterpret
        }

        @Override
        public boolean beginFragment(Fragment fragment) {
            return false;
        }

        @Override
        public void endFragment(Fragment fragment) {
        }
    }

    private final class Chain implements PreProcess, PostProcess {
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

            if (chainedParts.size() == chainIndex) {
                InterpreterContext context = conf.createInterpreterContext();
                execute(context);
            } else {
                Chain next = new Chain(story, listener, chainedParts, chainIndex + 1, this);
                interpretScenarioOrBackground(chainedParts.get(chainIndex), listener, next);
            }
        }

        @Override
        public void preExecute(InterpreterContext context) {
            if (previous != null) {
                previous.preExecute(context);
            }

            if (preProcess != null) {
                preProcess.preExecute(context);
            }
        }

        public void execute(InterpreterContext context) {
            preExecute(context);
            postExecute(context);
        }

        @Override
        public void postExecute(InterpreterContext context) {
            if (postProcess != null) {
                postProcess.postExecute(context);
            }

            if (previous != null) {
                previous.postExecute(context);
            }
        }
    }

    public interface PreProcess {
        void preExecute(InterpreterContext context);
    }

    public interface PostProcess {
        void postExecute(InterpreterContext context);
    }
}
