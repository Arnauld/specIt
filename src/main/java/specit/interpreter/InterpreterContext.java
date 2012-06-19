package specit.interpreter;

import specit.util.New;

import java.util.Map;

/**
 *
 *
 */
public class InterpreterContext {

    private final Map<String, String> variables = New.hashMap();

    public InterpreterContext() {
    }

    public InterpreterContext defineVariables(Map<String, String> newVariableValues) {
        this.variables.putAll(newVariableValues);
        return this;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public InterpreterContext nestedContext(Map<String, String> values) {
        return new InterpreterContext().defineVariables(variables).defineVariables(values);
    }
}
