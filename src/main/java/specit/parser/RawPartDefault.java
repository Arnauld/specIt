package specit.parser;

import specit.element.*;
import specit.util.CharSequences;
import specit.util.Equals;

import java.util.List;

public class RawPartDefault implements RawPart {

    private final int offset;
    private final Keyword keyword;
    private final String rawContent;
    private final String keywordAlias;
    private final ParserConf parserConf;
    //
    private List<Comment> nestedComments;
    private Table exampleTable;
    private RepeatParameters repeatParameters;

    public RawPartDefault(int offset,
                          Keyword keyword,
                          String rawContent,
                          String keywordAlias) {
        this(offset, keyword, rawContent, keywordAlias, null);
    }

    public RawPartDefault(int offset,
                          Keyword keyword,
                          String rawContent,
                          String keywordAlias,
                          ParserConf parserConf) {
        super();
        integrityCheck(keyword, rawContent, keywordAlias);
        this.offset = offset;
        this.keyword = keyword;
        this.rawContent = rawContent;
        this.keywordAlias = keywordAlias;
        this.parserConf = parserConf;
    }

    private static void integrityCheck(Keyword keyword, String rawContent, String keywordAlias) {
        if (rawContent == null) {
            throw new IllegalArgumentException("Raw content must be defined");
        }
        if (keywordAlias == null) {
            if (keyword != Keyword.Unknown) {
                throw new IllegalArgumentException("Undefined alias requires the Unknown keyword");
            }
            return;
        }
        int indexOf = rawContent.indexOf(keywordAlias);
        if (indexOf < 0) {
            throw new IllegalArgumentException("Keyword alias is not in raw content!");
        }
    }

    @Override
    public final int getOffset() {
        return offset;
    }

    @Override
    public final String getRawContent() {
        return rawContent;
    }

    @Override
    public final Keyword getKeyword() {
        return keyword;
    }

    @Override
    public final String getKeywordAlias() {
        return keywordAlias;
    }

    /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + offset;
        result = prime * result + ((rawContent == null) ? 0 : rawContent.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RawPartDefault other = (RawPartDefault) obj;
        return (offset == other.offset)
                && Equals.areEquals(rawContent, other.rawContent)
                // noT required but they both contribute to the object's state
                && keyword == other.keyword
                && Equals.areEquals(keywordAlias, other.keywordAlias);
    }

    @Override
    public String toString() {
        return "RawPart{" +
                "offset=" + offset +
                ", rawContent='" + rawContent + '\'' +
                '}';
    }

    @Override
    public String contentAfterAlias() {
        if (keywordAlias == null) {
            return rawContent;
        }
        int startingIndex = rawContent.indexOf(keywordAlias) + keywordAlias.length();
        return rawContent.substring(startingIndex);
    }

    @Override
    public Table getExampleTable() {
        if(keyword!=Keyword.Example)
            throw new IllegalStateException("Cannot retrieve an ExampleTable from a " + keyword + " part!");
        ensureParserConfIsDefined();
        if(exampleTable==null) {
            exampleTable = parserConf.tableParser().parse(rawContent);
        }
        return exampleTable;
    }

    @Override
    public RepeatParameters getRepeatParameters() {
        if(keyword!=Keyword.Repeat)
            throw new IllegalStateException("Cannot retrieve RepeatParameters from a " + keyword + " part!");
        if(repeatParameters==null)
            repeatParameters = parserConf.repeatParametersParser().parse(rawContent);
        return repeatParameters;
    }

    @Override
    public List<Comment> getNestedComments() {
        if(nestedComments==null)
            nestedComments = parserConf.commentParser().parseComments(offset, rawContent);
        return nestedComments;
    }

    @Override
    public boolean endsWithBlankLine() {
        return CharSequences.endsWithBlankLine(rawContent);
    }

    private void ensureParserConfIsDefined() {
        if(parserConf==null)
            throw new IllegalStateException("ParserConf is not set!");
    }

    @Override
    public Iterable<Token> tokenize() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
