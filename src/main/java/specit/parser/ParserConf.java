package specit.parser;

import specit.element.Alias;

import java.util.List;

/**
 *
 *
 */
public interface ParserConf {
    Iterable<Alias> aliases();

    String ignoredCharactersOnPartStart();
    CommentParser commentParser();
    ExampleVariablesParser exampleVariablesParser();

    String cellSeparator();
}
