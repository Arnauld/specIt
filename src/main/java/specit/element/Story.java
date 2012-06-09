package specit.element;

import specit.util.New;

import java.util.List;

/**
 *
 *
 */
public class Story extends Element {

    private ExecutablePart executablePart;
    //
    private Narrative narrative;
    private List<ExecutablePart> scenarioList = New.arrayList();
    private List<ExecutablePart> backgroundList = New.arrayList();

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginStory(this);

        if(narrative!=null)
            narrative.traverse(visitor);
        for (ExecutablePart part : backgroundList)
            part.traverse(visitor);
        for (ExecutablePart part : scenarioList)
            part.traverse(visitor);

        visitor.endStory(this);
    }

    public void addScenario(Scenario scenario) {
        scenarioList.add(scenario);
        executablePart = scenario;
    }

    public void addBackground(Background background) {
        if(!isBackgroundAccepted())
            throw new IllegalStateException("Background cannot be declared once scenario started");
        backgroundList.add(background);
        executablePart = background;
    }

    /**
     * Indicates whether the story can accept a background
     *
     * @return
     */
    public boolean isBackgroundAccepted() {
        if (executablePart == null)
            return true;
        if (executablePart instanceof Background)
            return true;
        return false;
    }

    /**
     * @param rawPart If no executable part is defined yet,
     *                the <code>rawPart</code> is used to create a dummy one
     * @return
     */
    public ExecutablePart executablePart(RawPart rawPart) {
        if (executablePart == null) {
            executablePart = createDefaultExecutablePart(rawPart);
        }
        return executablePart;
    }

    private ExecutablePart createDefaultExecutablePart(RawPart rawPart) {
        ExecutablePart defaultExecutablePart = new DefaultExecutablePart();
        scenarioList.add(defaultExecutablePart);
        return defaultExecutablePart;
    }

    public void setNarrative(Narrative narrative) {
        if (!scenarioList.isEmpty() || !backgroundList.isEmpty())
            throw new IllegalStateException("Narrative should be declared first!!");
        this.narrative = narrative;
    }

    public List<ExecutablePart> getScenarioList() {
        return scenarioList;
    }
}
