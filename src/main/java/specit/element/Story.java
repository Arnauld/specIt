package specit.element;

import specit.util.New;

import java.util.AbstractList;
import java.util.List;
import java.util.Stack;

/**
 *
 *
 */
public class Story extends Element implements FragmentHolder {

    private Stack<ExecutablePart> currentExecutablePart = New.stack();
    //
    private String storyPath;
    private Narrative narrative;
    private List<ExecutablePart> scenarioList = New.arrayList();
    private List<ExecutablePart> backgroundList = New.arrayList();
    private List<Fragment> fragments = New.arrayList();

    @Override
    public void traverse(ElementVisitor visitor) {
        if(visitor.beginStory(this)) {
            if (narrative != null) {
                narrative.traverse(visitor);
            }
            for (ExecutablePart part : backgroundList) {
                part.traverse(visitor);
            }
            for (ExecutablePart part : scenarioList) {
                part.traverse(visitor);
            }
        }
        visitor.endStory(this);
    }

    public void addScenario(Scenario scenario) {
        scenarioList.add(scenario);
        scenario.setParent(this);
        currentExecutablePart.push(scenario);
    }

    /**
     * @param background
     * @see #isBackgroundAccepted()
     */
    public void addBackground(Background background) {
        if (!isBackgroundAccepted()) {
            throw new InvalidElementDefinitionException("Background cannot be declared once scenario started");
        }
        backgroundList.add(background);
        background.setParent(this);
        currentExecutablePart.push(background);
    }

    /**
     * Indicates whether the story can accept a background
     *
     * @return
     */
    public boolean isBackgroundAccepted() {
        if (currentExecutablePart.empty()) {
            return true;
        }
        if (currentExecutablePart.peek() instanceof Background) {
            return true;
        }
        return false;
    }

    protected ExecutablePart executablePart() {
        if (currentExecutablePart.empty()) {
            currentExecutablePart.push(createDefaultExecutablePart());
        }
        return currentExecutablePart.peek();
    }

    private ExecutablePart createDefaultExecutablePart() {
        ExecutablePart defaultExecutablePart = new DefaultExecutablePart();
        scenarioList.add(defaultExecutablePart);
        defaultExecutablePart.setParent(this);
        return defaultExecutablePart;
    }

    /**
     * @param narrative
     * @see #isNarrativeAccepted()
     */
    public void setNarrative(Narrative narrative) {
        if (isNarrativeAccepted()) {
            throw new InvalidElementDefinitionException("Narrative should be declared first!!");
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

    public Narrative getNarrative() {
        return narrative;
    }

    public List<ExecutablePart> getScenarioList() {
        return scenarioList;
    }

    public List<ExecutablePart> getBackgroundList() {
        return backgroundList;
    }

    public void addStep(Step step) {
        executablePart().addStep(step);
    }

    public void addExemple(Example example) {
        executablePart().addExemple(example);
    }

    public void addRequire(Require require) {
        executablePart().addRequire(require);
    }

    public void addFragment(Fragment fragment) {
        if(currentExecutablePart.empty()) {
            fragments.add(fragment);
            fragment.setParent(this);
            currentExecutablePart.push(fragment);
        }
        else {
            currentExecutablePart.peek().addFragment(fragment);
        }
    }

    @Override
    public void fragmentEnds() {
        currentExecutablePart.pop();
    }

    public String getStoryPath() {
        return storyPath;
    }
}
