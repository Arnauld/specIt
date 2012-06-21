package specit.element;

import java.lang.annotation.Annotation;

public enum Keyword {
    Unknown,
    Narrative,
    Scenario,
    Background,
    Require,
    Given,
    When,
    Then,
    And,
    Example,
    Fragment,
    Repeat;

    public boolean isStep() {
        return this == Given || this == When || this == Then || this == And;
    }

    public Class<? extends Annotation> annotationType() {
        switch (this) {
            case Given:
                return specit.annotation.Given.class;
            case When:
                return specit.annotation.When.class;
            case Then:
                return specit.annotation.Then.class;
            default:
                return null;
        }
    }
}
