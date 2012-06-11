package specit.element;

/**
 *
 */
public class StoryBuilder {
    private Story story = new Story();

    public StoryBuilder append(RawPart rawPart) {
        switch(rawPart.getKeyword()) {
            case Scenario:
                story.addScenario(new Scenario(rawPart));
                break;
            case Background:
                story.addBackground(new Background(rawPart));
                break;
            case And:
            case Given:
            case When:
            case Then:
                story.executablePart(rawPart).addStep(new Step(rawPart));
                break;
            case Example:
                story.executablePart(rawPart).addExemple(new Example(rawPart));
                break;
            case Narrative:
                story.setNarrative(new Narrative(rawPart));
                break;
            case Require:
                Require require = new Require(rawPart);
                ExecutablePart executablePart = story.executablePart(rawPart);
                executablePart.addRequire(require);
                break;
            case Unknown:
                // nothing to do :)
                break;
            default:
                // make sure switch evolves with the enum values
                throw new IllegalStateException("Unsupported rawPart type <" + rawPart.getKeyword() + "> using alias <" + rawPart.getKeywordAlias() + ">");
        }
        return this;
    }

    public Story getStory() {
        return story;
    }
}
