package specit.interpreter;

import specit.util.New;

import java.util.Map;

/**
 *
 *
 */
public class ExecutionContext {

    private final Map<String, String> variables = New.hashMap();

    public ExecutionContext() {
    }

    public ExecutionContext defineVariables(Map<String, String> newVariableValues) {
        this.variables.putAll(newVariableValues);
        return this;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public ExecutionContext nestedContext(Map<String, String> values) {
        return new ExecutionContext().defineVariables(variables).defineVariables(values);
    }
}
