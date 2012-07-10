package specit.element;

/**
 *
 */
public class StoryBuilder {
    private final Story story = new Story();

    public StoryBuilder append(RawElement rawElement) {
        switch (rawElement.getKeyword()) {
            case Scenario:
                story.addScenario(new Scenario(rawElement));
                break;
            case Background:
                story.addBackground(new Background(rawElement));
                break;
            case And:
            case Given:
            case When:
            case Then:
                story.addStep(new Step(rawElement));
                break;
            case Repeat:
                story.addStep(new Repeat(rawElement));
                break;
            case Example: // Foreach :)
                story.addExemple(new Example(rawElement));
                break;
            case Narrative:
                story.setNarrative(new Narrative(rawElement));
                break;
            case Require:
                story.addRequire(new Require(rawElement));
                break;
            case Fragment:
                story.addFragment(new Fragment(rawElement));
                break;
            case Unknown:
                // nothing to do :)
                break;
            default:
                // make sure switch evolves with the enum values
                throw new InvalidElementDefinitionException(
                        "Unsupported rawElement"
                                + " type <" + rawElement.getKeyword() + ">"
                                + " using alias <" + rawElement.getKeywordAlias() + ">");
        }
        return this;
    }

    public Story getStory() {
        return story;
    }
}
