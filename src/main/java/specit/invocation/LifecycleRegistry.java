package specit.invocation;

import specit.annotation.lifecycle.AfterScenario;
import specit.annotation.lifecycle.AfterStory;
import specit.annotation.lifecycle.BeforeScenario;
import specit.annotation.lifecycle.BeforeStory;
import specit.util.New;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class LifecycleRegistry {

    private Map<Class<? extends Annotation>, List<Lifecycle>> lifecyclePerType;

    public LifecycleRegistry() {
        lifecyclePerType = New.hashMap();
    }

    public void scan(Class<?> klazz) {
        while (klazz != null && !klazz.equals(Object.class)) {
            scanMethods(klazz);
            klazz = klazz.getSuperclass();
        }
    }

    private void scanMethods(Class<?> klazz) {
        for (Method method : klazz.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                Class<?> annotationType = annotation.annotationType();
                if (annotationType.equals(BeforeScenario.class)) {
                    BeforeScenario beforeScenario = (BeforeScenario) annotation;
                    register(new Lifecycle(klazz, method, beforeScenario));
                } else if (annotationType.equals(AfterScenario.class)) {
                    AfterScenario afterScenario = (AfterScenario) annotation;
                    register(new Lifecycle(klazz, method, afterScenario));
                } else if (annotationType.equals(BeforeStory.class)) {
                    BeforeStory beforeStory = (BeforeStory) annotation;
                    register(new Lifecycle(klazz, method, beforeStory));
                } else if (annotationType.equals(AfterStory.class)) {
                    AfterStory afterStory = (AfterStory) annotation;
                    register(new Lifecycle(klazz, method, afterStory));
                }
            }
        }
    }

    private void register(Lifecycle lifecycle) {
        Class<? extends Annotation> lifecycleType = lifecycle.getLifecycleType();
        List<Lifecycle> lifecycles = lifecyclePerType.get(lifecycleType);
        if (lifecycles == null) {
            lifecycles = New.arrayList();
            lifecyclePerType.put(lifecycleType, lifecycles);
        }
        lifecycles.add(lifecycle);
    }

    public List<Lifecycle> getLifecycles(Class<? extends Annotation> lifecycleType) {
        List<Lifecycle> lifecycles = lifecyclePerType.get(lifecycleType);
        if (lifecycles == null)
            return Collections.emptyList();
        else
            return lifecycles;
    }
}
