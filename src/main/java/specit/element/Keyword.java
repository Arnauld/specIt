package specit.element;

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
}
