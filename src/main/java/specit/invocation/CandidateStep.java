package specit.invocation;

import specit.element.Keyword;
import specit.util.ParametrizedString;

import java.lang.reflect.Method;

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

    public CandidateStep(Class<?> owningType,
                         Method method,
                         Keyword keyword,
                         ParametrizedString pattern,
                         ParameterMapping[] parameterMappings)
    {
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

    public boolean matches(Keyword otherKeyword, String otherPattern) {
        if (getKeyword() != otherKeyword) {
            return false;
        }
        return getPattern().matches(otherPattern);
    }

    @Override
    public String toString() {
        return "CandidateStep{"
                + "pattern=" + pattern
                + ", keyword=" + keyword
                + ", owner=" + owningType + "#" + method.getName()
                + '}';
    }
}
