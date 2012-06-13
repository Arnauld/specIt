package specit.element;

import specit.util.Equals;
import specit.util.New;

import java.util.List;

public class RawPart {
    
    private final int offset;
    private final Keyword keyword;
    private final String rawContent;
    private final String keywordAlias;
    private final List<Comment> nestedComments;
    private final Table exampleTable;

    public RawPart(int offset, Keyword keyword, String rawContent, String keywordAlias) {
        this(offset, keyword, rawContent, keywordAlias, New.<Comment>arrayList(), Table.empty());
    }

    public RawPart(int offset,
                   Keyword keyword,
                   String rawContent,
                   String keywordAlias,
                   List<Comment> nestedComments,
                   Table exampleTable) {
        super();
        integrityCheck(keyword, rawContent, keywordAlias);
        this.offset = offset;
        this.keyword = keyword;
        this.rawContent = rawContent;
        this.keywordAlias = keywordAlias;
        this.nestedComments = nestedComments;
        this.exampleTable = exampleTable;
    }

    private static void integrityCheck(Keyword keyword, String rawContent, String keywordAlias) {
        if(rawContent==null) {
            throw new IllegalArgumentException("Raw content must be defined");
        }
        if(keywordAlias==null) {
            if(keyword!=Keyword.Unknown) {
                throw new IllegalArgumentException("Undefined alias requires the Unknown keyword");
            }
            return;
        }
        int indexOf = rawContent.indexOf(keywordAlias);
        if(indexOf<0) {
            throw new IllegalArgumentException("Keyword alias is not in raw content!");
        }
    }

    public final int getOffset() {
        return offset;
    }

    public final String getRawContent() {
        return rawContent;
    }

    public final Keyword getKeyword() {
        return keyword;
    }

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
        RawPart other = (RawPart) obj;
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

    public String contentAfterAlias() {
        if(keywordAlias==null) {
            return rawContent;
        }
        int startingIndex = rawContent.indexOf(keywordAlias)+keywordAlias.length();
        return rawContent.substring(startingIndex);
    }

    public Table getExampleTable() {
        return exampleTable;
    }

    public List<Comment> getNestedComments() {
        return nestedComments;
    }
}
