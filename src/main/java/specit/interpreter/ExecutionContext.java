package specit.interpreter;

import specit.element.*;
import specit.util.New;

import java.util.Map;

/**
 *
 *
 */
public class ExecutionContext {

    private final InterpreterConf conf;
    private final Map<String,String> variables = New.hashMap();

    public ExecutionContext(InterpreterConf conf) {
        this.conf = conf;
    }

    public void defineVariables(Map<String, String> variables) {
        this.variables.putAll(variables);
    }

    public Map<String,String> getVariables() {
        return variables;
    }
}
