package specit.parser;

import specit.element.RawPart;

import java.util.ArrayList;
import java.util.List;

public class ListenerCollector extends Listener {

    private List<RawPart> steps = new ArrayList<RawPart>();

    @Override
    public void on(RawPart step) {
        steps.add(step);
    }

    public List<RawPart> getSteps() {
        return steps;
    }
}
