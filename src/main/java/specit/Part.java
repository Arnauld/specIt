package specit;

import specit.util.Equals;

public class Part {
    
    private final int offset;
    private final Keyword keyword;
    private final String rawContent;
    private final String keywordAlias;
    
    public Part(int offset, Keyword keyword, String rawContent, String keywordAlias) {
        super();
        integrityCheck(rawContent, keywordAlias);
        this.offset = offset;
        this.keyword = keyword;
        this.rawContent = rawContent;
        this.keywordAlias = keywordAlias;
    }

    private static void integrityCheck(String rawContent, String keywordAlias) {
        int indexOf = rawContent.indexOf(keywordAlias);
        if(indexOf<0)
            throw new IllegalArgumentException("Keyword alias is not in raw content!");
    }

    public int getOffset() {
        return offset;
    }

    public String getRawContent() {
        return rawContent;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public String getKeywordAlias() {
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Part other = (Part) obj;
        return (offset == other.offset)
                && Equals.areEquals(rawContent, other.rawContent)
                // noT required but they both contribute to the object's state
                && keyword == other.keyword
                && Equals.areEquals(keywordAlias, other.keywordAlias);
    }

    @Override
    public String toString() {
        return "Part{" +
                "offset=" + offset +
                ", rawContent='" + rawContent + '\'' +
                '}';
    }

    public String contentAfterAlias() {
        int startingIndex = rawContent.indexOf(keywordAlias)+keywordAlias.length();
        return rawContent.substring(startingIndex);
    }
}
