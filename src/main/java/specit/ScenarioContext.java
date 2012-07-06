package specit;

import specit.element.Scenario;

/**
 *
 *
 */
public interface ScenarioContext {

    public Scenario getCurrentScenario();

    public StoryContext getStoryContext();

    public <T> T getUserData(Object key);

    public void setUserData(Object key, Object value);

    public void removeUserData(Object key);
}
