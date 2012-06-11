package specit.element;

import specit.util.New;

import java.util.List;

/**
 *
 *
 */
public class Story extends Element {

    private ExecutablePart currentExecutablePart;
    //
    private Narrative narrative;
    private List<ExecutablePart> scenarioList = New.arrayList();
    private List<ExecutablePart> backgroundList = New.arrayList();

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginStory(this);

        if(narrative!=null) {
            narrative.traverse(visitor);
        }
        for (ExecutablePart part : backgroundList) {
            part.traverse(visitor);
        }
        for (ExecutablePart part : scenarioList) {
            part.traverse(visitor);
        }

        visitor.endStory(this);
    }

    public void addScenario(Scenario scenario) {
        scenarioList.add(scenario);
        currentExecutablePart = scenario;
    }

    /**
     *
     * @param background
     * @see #isBackgroundAccepted()
     */
    public void addBackground(Background background) {
        if(!isBackgroundAccepted()) {
            throw new IllegalStateException("Background cannot be declared once scenario started");
        }
        backgroundList.add(background);
        currentExecutablePart = background;
    }

    /**
     * Indicates whether the story can accept a background
     *
     * @return
     */
    public boolean isBackgroundAccepted() {
        if (currentExecutablePart == null) {
            return true;
        }
        if (currentExecutablePart instanceof Background) {
            return true;
        }
        return false;
    }

    /**
     * @param rawPart If no executable part is defined yet,
     *                the <code>rawPart</code> is used to create a dummy one
     * @return
     */
    public ExecutablePart executablePart(RawPart rawPart) {
        if (currentExecutablePart == null) {
            currentExecutablePart = createDefaultExecutablePart();
        }
        return currentExecutablePart;
    }

    private ExecutablePart createDefaultExecutablePart() {
        ExecutablePart defaultExecutablePart = new DefaultExecutablePart();
        scenarioList.add(defaultExecutablePart);
        return defaultExecutablePart;
    }

    /**
     *
     * @param narrative
     * @see #isNarrativeAccepted()
     */
    public void setNarrative(Narrative narrative) {
        if (isNarrativeAccepted()) {
            throw new IllegalStateException("Narrative should be declared first!!");
        }
        this.narrative = narrative;
    }

    /**
     * Indicates whether the story can accept a narrative
     *
     * @return
     */
    public boolean isNarrativeAccepted() {
        return !scenarioList.isEmpty() || !backgroundList.isEmpty();
    }

    public List<ExecutablePart> getScenarioList() {
        return scenarioList;
    }

    public List<ExecutablePart> getBackgroundList() {
        return backgroundList;
    }
}
