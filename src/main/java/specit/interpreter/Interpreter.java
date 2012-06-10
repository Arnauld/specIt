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
public class Interpreter {

    private final InterpreterConf conf;

    public Interpreter(InterpreterConf conf) {
        this.conf = conf;
    }

    public void interpretStory(Story story, InterpreterListener listener) {
        listener.beginStory(story);
        try {
            List<ExecutablePart> scenarioList = story.getScenarioList();
            for (ExecutablePart scenario : scenarioList) {
                ExecutionContext context = conf.createExecutionContext();
                listener.beginScenario(scenario, context);
                try {
                    for (ExecutablePart background : story.getBackgroundList()) {
                        interpretScenarioOrBackground(context, background, story, listener);
                    }
                    interpretScenarioOrBackground(context, scenario, story, listener);
                } finally {
                    listener.endScenario(scenario, context);
                }
            }
        } finally {
            listener.endStory(story);
        }

    }

    private void interpretScenarioOrBackground(ExecutionContext context,
                                               ExecutablePart scenario,
                                               Story story,
                                               InterpreterListener listener) {
        HashMap<String, String> variables = New.hashMap();

        List<Example> examples = scenario.getExamples();
        if (examples.isEmpty()) {
            interpretScenarioWithVariables(context, variables, scenario, story, listener);
        } else {
            recursivelyTraverseExamplesThroughInterpret(context, 0, examples, variables, scenario, story, listener);
        }
    }

    private void recursivelyTraverseExamplesThroughInterpret(ExecutionContext context,
                                                             int exampleIndex,
                                                             List<Example> examples,
                                                             Map<String, String> cumulatedVariables,
                                                             ExecutablePart scenario,
                                                             Story story,
                                                             InterpreterListener listener) {
        if (exampleIndex == examples.size()) {
            interpretScenarioWithVariables(context, cumulatedVariables, scenario, story, listener);
            return;
        }
        Example example = examples.get(exampleIndex);
        List<Map<String, String>> variablesRows = example.getVariablesRows();
        if (variablesRows.isEmpty()) {
            recursivelyTraverseExamplesThroughInterpret(context, exampleIndex + 1, examples, cumulatedVariables, scenario, story, listener);
            return;
        }

        for (Map<String, String> variablesRow : variablesRows) {
            Map<String, String> nextVariables = New.hashMap(cumulatedVariables);
            nextVariables.putAll(variablesRow);
            recursivelyTraverseExamplesThroughInterpret(context, exampleIndex + 1, examples, nextVariables, scenario, story, listener);
        }
    }

    private void interpretScenarioWithVariables(ExecutionContext context,
                                                Map<String, String> variables,
                                                ExecutablePart scenario,
                                                Story story,
                                                InterpreterListener listener) {
        context.defineVariables(variables);
        scenario.traverseExecutablePart(new ExecutionVisitor(context, listener));
    }

    private void invokeStep(ExecutionContext context,
                            Keyword keyword,
                            String stepContent,
                            InterpreterListener listener) {

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

            RawPart rawPart = step.getRawPart();
            String rawContent = rawPart.getRawContent();
            Map<String, String> variables = context.getVariables();
            rawContent = conf.templateEngine().resolve(rawContent, variables).toString();
            invokeStep(context, rawPart.getKeyword(), rawContent, listener);
        }

        @Override
        public void endStep(Step step) {
        }

        @Override
        public void beginRequire(Require require) {
            // throw new UnsupportedOperationException("Not implemented yet");
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
