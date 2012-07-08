package specit;

import specit.element.Scenario;

/**
 *
 *
 */
public interface ScenarioContext {

    Scenario getCurrentScenario();

    StoryContext getStoryContext();

    <T> T getUserData(Object key);

    void setUserData(Object key, Object value);

    void removeUserData(Object key);
}
