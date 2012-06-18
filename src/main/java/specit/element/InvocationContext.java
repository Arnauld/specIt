package specit.element;

import specit.util.New;

import java.util.Map;

/**
 *
 *
 */
public class InvocationContext {
    private final Map<Object,Object> values = New.hashMap();

    public <T> T get(Object key) {
        return (T)values.get(key);
    }

    public InvocationContext set(Object key, Object value) {
        values.put(key, value);
        return this;
    }
}
