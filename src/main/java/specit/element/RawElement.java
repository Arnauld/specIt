package specit.element;

import java.util.List;

/**
 *
 */
public interface RawElement {
    /**
     * Returns the offset of this part in the original story text.
     *
     * @return
     */
    int getOffset();

    /**
     * Returns the raw content of this part. It contains the keyword alias, comments,
     * trailing or ending newlines...
     *
     * @return
     */
    String getRawContent();

    /**
     * Return the keyword that part belongs to. The keyword is ususally defined from the
     * keyword alias based on the aliases definitions.
     *
     * @return
     * @see #getKeywordAlias()
     */
    Keyword getKeyword();

    /**
     * Return the keyword alias that has been identified for this part. The alias is usually the first
     * words of the raw part. Depending on the aliases definition, it is mapped to a {@link Keyword}.
     *
     * @return
     * @see #getKeyword()
     * @see Keyword
     */
    String getKeywordAlias();

    /**
     * Returns the part content after the Keyword alias. Since the Keyword alias depends on the configuration,
     * it is useful to retrieve the content afterwards for code mapping.
     *
     * @return
     * @see #getRawContent()
     * @see #getKeywordAlias()
     */
    String contentAfterAlias();

    /**
     * Return the {@link Table} defined within the {@link #contentAfterAlias()}.
     * This method is available only if this part belongs to a {@link Keyword#Example} keyword.
     *
     * @return
     * @see Keyword#Example
     */
    Table getExampleTable();

    /**
     * Return the {@link RepeatParameters} defined within the {@link #contentAfterAlias()}.
     * This method is available only if this part belongs to a {@link Keyword#Repeat} keyword.
     *
     * @return
     * @see Keyword#Repeat
     */
    RepeatParameters getRepeatParameters();

    /**
     * Returns the comments that may be contained within this part.
     *
     * @return
     */
    List<Comment> getNestedComments();

    /**
     * Indicates if this part ends with at least one blank line. A line is considered as blank if
     * it contains only spaces, tabulations or newline characters...
     * This is usefull when blank lines are used to delimit scope context such as for {@link Fragment}
     * definition.
     *
     * @return
     * @see String#trim()
     */
    boolean endsWithBlankLine();

    /**
     * @see Token
     */
    Iterable<Token> tokenize();
}
