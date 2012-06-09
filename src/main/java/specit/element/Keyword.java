package specit.element;

public enum Keyword {
    Unknown,
    Narrative,
    Scenario,
    Require,
    Background,
    Given,
    When,
    Then,
    And,
    Example;

    public boolean isStep() {
        return this==Given || this==When || this==Then || this==And;
    }
}
