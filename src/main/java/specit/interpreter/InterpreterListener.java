package specit.interpreter;

import specit.element.ExecutablePart;
import specit.element.InvokableStep;
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

    public void beginScenario(ExecutablePart scenario, InterpreterContext context) {
    }

    public void endScenario(ExecutablePart scenario, InterpreterContext context) {
    }

    public void invokeStep(InvokableStep invokableStep, InterpreterContext context) {
    }

    public void invokeRequire(String resolved, InterpreterContext context) {
    }

}
