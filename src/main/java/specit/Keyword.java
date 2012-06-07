package specit;

public enum Keyword {
    Unknown,
    Narrative,
    Scenario,
    Include,
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
