package specit.mapping;

import specit.annotation.Given;
import specit.annotation.Then;
import specit.annotation.When;
import specit.element.Keyword;
import specit.util.New;
import specit.util.ParametrizedString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 *
 */
public class CandidateStepRegistry {

    private final MappingConf conf;
    private List<CandidateStep> candidateSteps;

    public CandidateStepRegistry(MappingConf conf) {
        this.conf = conf;
        this.candidateSteps = New.arrayList();
    }

    public List<CandidateStep> find(Keyword keyword, String pattern) {
        List<CandidateStep> found = New.arrayList();
        for (CandidateStep candidateStep : candidateSteps) {
            if (candidateStep.matches(keyword, pattern))
                found.add(candidateStep);
        }
        return found;
    }


    public void scan(Class<?> klazz) {
        while (klazz != null && !klazz.equals(Object.class)) {
            scanMethods(klazz);
            klazz = klazz.getSuperclass();
        }
    }

    /**
     *
     */
    protected void scanMethods(Class<?> klazz) {
        for (Method method : klazz.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                Class<?> annotationType = annotation.annotationType();
                if (annotationType.equals(Given.class)) {
                    Given given = (Given) annotation;
                    register(klazz, method, Keyword.Given, given.value());
                }
                if (annotationType.equals(When.class)) {
                    When when = (When) annotation;
                    register(klazz, method, Keyword.When, when.value());
                }
                if (annotationType.equals(Then.class)) {
                    Then then = (Then) annotation;
                    register(klazz, method, Keyword.Then, then.value());
                }
            }
        }
    }

    protected void register(Class<?> klazz, Method method, Keyword keyword, String[] patterns) {
        for (String pattern : patterns) {
            register(klazz, method, keyword, pattern);
        }
    }

    protected void register(Class<?> klazz, Method method, Keyword keyword, String pattern) {
        ParametrizedString pString = new ParametrizedString(pattern, conf.variablePrefix());

        ParameterMappingsBuilder builder = new ParameterMappingsBuilder(method, pString);
        ParameterMapping[] parameterMappings = builder.getParameterMappings();

        CandidateStep candidateStep = new CandidateStep(klazz, method, keyword, pString, parameterMappings);
        candidateSteps.add(candidateStep);
    }

}
