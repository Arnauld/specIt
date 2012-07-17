package specit.element;

import java.lang.annotation.Annotation;

/**
 * List of all keywords managed by <code>specIt</code>.
 * <p/>
 * Keywords are used in several places such as for parsing (they provide the starting word
 * identifying the type of parsed part), step matching or reporting.
 */
public enum Keyword {
    Unknown,
    Narrative,
    Scenario,
    Background,
    Require,
    Given,
    When,
    Then,
    /**
     *
     */
    And,
    Example,
    Fragment,
    Repeat;

    public static Keyword findByNameIgnoringCase(String name) {
        for(Keyword keyword : Keyword.values()) {
            if(name.equalsIgnoreCase(keyword.name()))
                return keyword;
        }
        return null;
    }

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
