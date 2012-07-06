package specit;

import specit.element.Story;

/**
 *
 *
 */
public interface StoryContext {

    public Story getCurrentStory();

    public <T> T getUserData(Object key);

    public void setUserData(Object key, Object value);

    public void removeUserData(Object key);
}
