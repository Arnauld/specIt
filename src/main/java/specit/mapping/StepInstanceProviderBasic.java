package specit.mapping;

import specit.util.New;

import java.util.Map;

/**
 *
 *
 */
public class StepInstanceProviderBasic implements StepInstanceProvider {

    private Map<Class<?>, Object> instancePerType = New.hashMap();

    public Object getInstance(Class<?> owningType) {
        Object instance = instancePerType.get(owningType);
        if (instance == null) {
            try {
                instance = owningType.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Failed to instanciate instance for step <" + owningType + ">", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to instanciate instance for step <" + owningType + ">", e);
            }
            instancePerType.put(owningType, instance);
        }
        return instance;
    }
}
