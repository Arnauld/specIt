package specit.parser;

import specit.element.RawElement;

import java.util.ArrayList;
import java.util.List;

public class ListenerCollector extends Listener {

    private List<RawElement> steps = new ArrayList<RawElement>();

    @Override
    public void on(RawElement step) {
        steps.add(step);
    }

    public List<RawElement> getSteps() {
        return steps;
    }
}
