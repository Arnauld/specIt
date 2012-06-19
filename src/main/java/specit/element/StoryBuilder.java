package specit.element;

/**
 *
 */
public class StoryBuilder {
    private final Story story = new Story();

    public StoryBuilder append(RawPart rawPart) {
        switch (rawPart.getKeyword()) {
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
                story.addStep(new Step(rawPart));
                break;
            case Repeat:
                story.addStep(new Repeat(rawPart));
                break;
            case Example: // Foreach :)
                story.addExemple(new Example(rawPart));
                break;
            case Narrative:
                story.setNarrative(new Narrative(rawPart));
                break;
            case Require:
                story.addRequire(new Require(rawPart));
                break;
            case Fragment:
                story.addFragment(new Fragment(rawPart));
                break;
            case Unknown:
                // nothing to do :)
                break;
            default:
                // make sure switch evolves with the enum values
                throw new InvalidElementDefinitionException("Unsupported rawPart type <" + rawPart.getKeyword() + "> using alias <" + rawPart.getKeywordAlias() + ">");
        }
        return this;
    }

    public Story getStory() {
        return story;
    }
}
