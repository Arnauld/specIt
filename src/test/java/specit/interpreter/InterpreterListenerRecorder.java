package specit.interpreter;

import specit.element.ExecutablePart;
import specit.element.InvokableStep;
import specit.element.Keyword;
import specit.element.Story;
import specit.util.New;

import java.util.List;

/**
 *
 *
 */
public class InterpreterListenerRecorder extends InterpreterListener {

    private List<Event> events = New.arrayList();

    @Override
    public void beginStory(Story story) {
        events.add(new BeginStory(story));
    }

    @Override
    public void endStory(Story story) {
        events.add(new EndStory(story));
    }

    @Override
    public void beginScenario(ExecutablePart scenario, InterpreterContext context) {
        events.add(new BeginScenario(scenario, context));
    }

    @Override
    public void endScenario(ExecutablePart scenario, InterpreterContext context) {
        events.add(new EndScenario(scenario, context));
    }

    @Override
    public void invokeStep(InvokableStep invokableStep, InterpreterContext context) {
        events.add(new InvokeStep(invokableStep, context));
    }

    @Override
    public void invokeRequire(String resolved, InterpreterContext context) {
        events.add(new InvokeRequire(resolved, context));
    }

    public List<Event> getEvents() {
        return events;
    }

    public interface Event {
    }

    public class BeginStory implements InterpreterListenerRecorder.Event {
        private final Story story;

        public BeginStory(Story story) {
            this.story = story;
        }

        public Story getStory() {
            return story;
        }
    }

    public class EndStory implements InterpreterListenerRecorder.Event {
        private final Story story;

        public EndStory(Story story) {
            this.story = story;
        }

        public Story getStory() {
            return story;
        }
    }

    public class BeginScenario implements InterpreterListenerRecorder.Event {
        private final ExecutablePart scenario;
        private final InterpreterContext context;

        public BeginScenario(ExecutablePart scenario, InterpreterContext context) {
            this.scenario = scenario;
            this.context = context;
        }

        public ExecutablePart getScenario() {
            return scenario;
        }

        public InterpreterContext getContext() {
            return context;
        }
    }

    public class EndScenario implements InterpreterListenerRecorder.Event {
        private final ExecutablePart scenario;
        private final InterpreterContext context;

        public EndScenario(ExecutablePart scenario, InterpreterContext context) {
            this.scenario = scenario;
            this.context = context;
        }

        public ExecutablePart getScenario() {
            return scenario;
        }

        public InterpreterContext getContext() {
            return context;
        }
    }

    public class InvokeStep implements InterpreterListenerRecorder.Event {

        private final InvokableStep invokableStep;
        private final InterpreterContext context;

        public InvokeStep(InvokableStep invokableStep, InterpreterContext context) {
            this.invokableStep = invokableStep;
            this.context = context;
        }

        public Keyword getKeyword() {
            return invokableStep.getKeyword();
        }

        public String getResolved() {
            return invokableStep.getAdjustedInput();
        }

        public InterpreterContext getContext() {
            return context;
        }
    }

    public class InvokeRequire implements InterpreterListenerRecorder.Event {

        private final String resolved;
        private final InterpreterContext context;

        public InvokeRequire(String resolved, InterpreterContext context) {
            this.resolved = resolved;
            this.context = context;
        }

        public String getResolved() {
            return resolved;
        }

        public InterpreterContext getContext() {
            return context;
        }
    }
}
