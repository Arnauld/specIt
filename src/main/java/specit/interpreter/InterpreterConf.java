package specit.interpreter;

import specit.util.TemplateEngine;

/**
 *
 *
 */
public interface InterpreterConf {

    TemplateEngine templateEngine();

    InterpreterContext createInterpreterContext();
}
