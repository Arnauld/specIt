package specit.invocation;

import specit.annotation.UserContextFactory;
import specit.annotation.UserContextScope;
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
public class AnnotationRegistry {

    private Map<Class<? extends Annotation>, List<Lifecycle>> lifecyclePerType;
    private List<UserContextFactorySupport> userContextFactorySupports;

    public AnnotationRegistry() {
        lifecyclePerType = New.hashMap();
        userContextFactorySupports = New.arrayList();
    }

    public void scan(Class<?> klazz) throws ParameterMappingException {
        while (klazz != null && !klazz.equals(Object.class)) {
            scanMethods(klazz);
            klazz = klazz.getSuperclass();
        }
    }

    private void scanMethods(Class<?> klazz) throws ParameterMappingException {
        for (Method method : klazz.getMethods()) {
            scanMethod(klazz, method);
        }
    }

    private void scanMethod(Class<?> klazz, Method method) throws ParameterMappingException {
        for (Annotation annotation : method.getAnnotations()) {
            scanAnnotation(klazz, method, annotation);
        }
    }

    private void scanAnnotation(Class<?> klazz, Method method, Annotation annotation) throws ParameterMappingException {
        Class<?> annotationType = annotation.annotationType();
        if (annotationType.equals(BeforeScenario.class)) {
            BeforeScenario beforeScenario = (BeforeScenario) annotation;
            register(klazz, method, beforeScenario);
        } else if (annotationType.equals(AfterScenario.class)) {
            AfterScenario afterScenario = (AfterScenario) annotation;
            register(klazz, method, afterScenario);
        } else if (annotationType.equals(BeforeStory.class)) {
            BeforeStory beforeStory = (BeforeStory) annotation;
            register(klazz, method, beforeStory);
        } else if (annotationType.equals(AfterStory.class)) {
            AfterStory afterStory = (AfterStory) annotation;
            register(klazz, method, afterStory);
        } else if (annotationType.equals(UserContextFactory.class)) {
            UserContextFactory factory = (UserContextFactory) annotation;
            register(new UserContextFactorySupport(klazz, method, factory));
        }
    }

    private void register(Class<?> klazz, Method method, Annotation annotation) throws ParameterMappingException {
        ParameterMapping[] mappings = new ParameterMappingsBuilder(method).getParameterMappings();
        register(new Lifecycle(klazz, method, annotation, mappings));
    }

    private void register(UserContextFactorySupport userContextFactorySupport) {
        userContextFactorySupports.add(userContextFactorySupport);
    }

    public List<UserContextFactorySupport> getUserContextFactories(UserContextScope scope) {
        List<UserContextFactorySupport> selected = New.arrayList();
        for(UserContextFactorySupport factorySupport : userContextFactorySupports) {
            if(factorySupport.scope() == scope) {
                selected.add(factorySupport);
            }
        }
        return selected;
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
        if (lifecycles == null) {
            return Collections.emptyList();
        } else {
            return lifecycles;
        }
    }
}
