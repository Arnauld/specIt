package specit.mapping;

import specit.element.Keyword;
import specit.util.ParametrizedString;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 *
 */
public class CandidateStep {
    private final Class<?> owningType;
    private final Method method;
    private final Keyword keyword;
    private final ParametrizedString pattern;
    private final ParameterMapping[] parameterMappings;

    public CandidateStep(Class<?> owningType, Method method, Keyword keyword, ParametrizedString pattern, ParameterMapping[] parameterMappings) {
        this.owningType = owningType;
        this.method = method;
        this.keyword = keyword;
        this.pattern = pattern;
        this.parameterMappings = parameterMappings;
    }

    public Class<?> getOwningType() {
        return owningType;
    }

    public Method getMethod() {
        return method;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public ParametrizedString getPattern() {
        return pattern;
    }

    public ParameterMapping[] getParameterMappings() {
        return parameterMappings;
    }
}
