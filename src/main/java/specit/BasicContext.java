package specit;

import specit.util.New;

import java.util.Map;

/**
 *
 */
public class BasicContext {

    private Map<Object, Object> data = New.concurrentHashMap();

    public <T> T getUserData(Object key) {
        return (T) data.get(key);
    }

    public void setUserData(Object key, Object value) {
        data.put(key, value);
    }

    public void removeUserData(Object key) {
        data.remove(key);
    }
}
