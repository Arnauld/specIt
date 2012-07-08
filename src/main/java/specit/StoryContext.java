package specit;

import specit.element.Story;

/**
 *
 *
 */
public interface StoryContext {

    Story getCurrentStory();

    <T> T getUserData(Object key);

    void setUserData(Object key, Object value);

    void removeUserData(Object key);
}
