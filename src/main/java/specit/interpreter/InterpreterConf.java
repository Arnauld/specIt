package specit.interpreter;

import specit.parser.ParserConf;
import specit.util.TemplateEngine;

/**
 *
 *
 */
public interface InterpreterConf {

    TemplateEngine templateEngine();

    ExecutionContext createExecutionContext();
}
