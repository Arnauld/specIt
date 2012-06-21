package specit.element;

/**
 *
 *
 */
public class ElementVisitor {

    protected boolean beginDefaultFallback(Element element) {
        return true;
    }

    protected void endDefaultFallback(Element element) {
    }

    public boolean beginBackground(Background background) {
        return beginDefaultFallback(background);
    }

    public void endBackground(Background background) {
        endDefaultFallback(background);
    }

    public boolean beginExample(Example example) {
        return beginDefaultFallback(example);
    }

    public void endExample(Example example) {
        endDefaultFallback(example);
    }

    public boolean beginNarrative(Narrative narrative) {
        return beginDefaultFallback(narrative);
    }

    public void endNarrative(Narrative narrative) {
        endDefaultFallback(narrative);
    }

    public boolean beginRequire(Require require) {
        return beginDefaultFallback(require);
    }

    public void endRequire(Require require) {
        endDefaultFallback(require);
    }

    public boolean beginScenario(Scenario scenario) {
        return beginDefaultFallback(scenario);
    }

    public void endScenario(Scenario scenario) {
        endDefaultFallback(scenario);
    }

    public boolean beginStep(Step step) {
        return beginDefaultFallback(step);
    }

    public void endStep(Step step) {
        endDefaultFallback(step);
    }

    public boolean beginStory(Story story) {
        return beginDefaultFallback(story);
    }

    public void endStory(Story story) {
        endDefaultFallback(story);
    }

    public boolean beginDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        return beginDefaultFallback(defaultExecutablePart);
    }

    public void endDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        endDefaultFallback(defaultExecutablePart);
    }

    public boolean beginFragment(Fragment fragment) {
        return beginDefaultFallback(fragment);
    }

    public void endFragment(Fragment fragment) {
        endDefaultFallback(fragment);
    }

    public boolean beginRepeat(Repeat repeat) {
        return beginDefaultFallback(repeat);
    }

    public void endRepeat(Repeat repeat) {
        endDefaultFallback(repeat);
    }
}
