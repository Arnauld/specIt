package specit.element;

/**
 *
 *
 */
public class ElementVisitor {

    protected void beginDefaultFallback(Element element) {
    }

    protected void endDefaultFallback(Element element) {
    }

    public void beginBackground(Background background) {
        beginDefaultFallback(background);
    }

    public void endBackground(Background background) {
        endDefaultFallback(background);
    }

    public void beginExample(Example example) {
        beginDefaultFallback(example);
    }

    public void endExample(Example example) {
        endDefaultFallback(example);
    }

    public void beginNarrative(Narrative narrative) {
        beginDefaultFallback(narrative);
    }

    public void endNarrative(Narrative narrative) {
        endDefaultFallback(narrative);
    }

    public void beginRequire(Require require) {
        beginDefaultFallback(require);
    }

    public void endRequire(Require require) {
        endDefaultFallback(require);
    }

    public void beginScenario(Scenario scenario) {
        beginDefaultFallback(scenario);
    }

    public void endScenario(Scenario scenario) {
        endDefaultFallback(scenario);
    }

    public void beginStep(Step step) {
        beginDefaultFallback(step);
    }

    public void endStep(Step step) {
        endDefaultFallback(step);
    }

    public void beginStory(Story story) {
        beginDefaultFallback(story);
    }

    public void endStory(Story story) {
        endDefaultFallback(story);
    }

    public void beginDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        beginDefaultFallback(defaultExecutablePart);
    }

    public void endDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        endDefaultFallback(defaultExecutablePart);
    }

    public void beginFragment(Fragment fragment) {
        beginDefaultFallback(fragment);
    }
    public void endFragment(Fragment fragment) {
        endDefaultFallback(fragment);
    }

    public void beginRepeat(Repeat repeat) {
        beginDefaultFallback(repeat);
    }
    public void endRepeat(Repeat repeat) {
        endDefaultFallback(repeat);
    }
}
