package specit.interpreter;

import specit.element.ExecutablePart;
import specit.element.Keyword;
import specit.element.Story;

/**
 *
 *
 */
public class InterpreterListener {
    public void beginStory(Story story) {
    }

    public void endStory(Story story) {
    }

    public void beginScenario(ExecutablePart scenario, ExecutionContext context) {
    }

    public void endScenario(ExecutablePart scenario, ExecutionContext context) {
    }

    public void invokeStep(Keyword keyword, String resolved, ExecutionContext context) {
    }

    public void invokeRequire(String resolved, ExecutionContext context) {
    }
}
