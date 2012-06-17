package specit.parser;

import specit.element.Alias;

/**
 *
 *
 */
public interface ParserConf {
    Iterable<Alias> aliases();

    String ignoredCharactersOnPartStart();

    CommentParser commentParser();

    TableParser tableParser();

    RepeatParametersParser repeatParametersParser();

    String tableCellSeparator();
}
