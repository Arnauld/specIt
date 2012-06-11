package specit.interpreter;

import specit.element.ExecutablePart;
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
    public void beginScenario(ExecutablePart scenario, ExecutionContext context) {
        events.add(new BeginScenario(scenario, context));
    }

    @Override
    public void endScenario(ExecutablePart scenario, ExecutionContext context) {
        events.add(new EndScenario(scenario, context));
    }

    @Override
    public void invokeStep(Keyword keyword, String resolved, ExecutionContext context) {
        events.add(new InvokeStep(keyword, resolved, context));
    }

    @Override
    public void invokeRequire(String resolved, ExecutionContext context) {
        events.add(new InvokeRequire(resolved, context));
    }

    public List<Event> getEvents() {
        return events;
    }

    public interface Event {}

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
        private final ExecutionContext context;

        public BeginScenario(ExecutablePart scenario, ExecutionContext context) {
            this.scenario = scenario;
            this.context = context;
        }

        public ExecutablePart getScenario() {
            return scenario;
        }

        public ExecutionContext getContext() {
            return context;
        }
    }
    public class EndScenario implements InterpreterListenerRecorder.Event {
        private final ExecutablePart scenario;
        private final ExecutionContext context;

        public EndScenario(ExecutablePart scenario, ExecutionContext context) {
            this.scenario = scenario;
            this.context = context;
        }

        public ExecutablePart getScenario() {
            return scenario;
        }

        public ExecutionContext getContext() {
            return context;
        }
    }

    public class InvokeStep implements InterpreterListenerRecorder.Event {

        private final Keyword keyword;
        private final String resolved;
        private final ExecutionContext context;

        public InvokeStep(Keyword keyword, String resolved, ExecutionContext context) {
            this.keyword = keyword;
            this.resolved = resolved;
            this.context = context;
        }

        public Keyword getKeyword() {
            return keyword;
        }

        public String getResolved() {
            return resolved;
        }

        public ExecutionContext getContext() {
            return context;
        }
    }

    public class InvokeRequire implements InterpreterListenerRecorder.Event {

        private final String resolved;
        private final ExecutionContext context;

        public InvokeRequire(String resolved, ExecutionContext context) {
            this.resolved = resolved;
            this.context = context;
        }

        public String getResolved() {
            return resolved;
        }

        public ExecutionContext getContext() {
            return context;
        }
    }
}
