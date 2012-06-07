package specit;

import specit.util.Equals;

public class Part {
    
    private final int offset;
    private final Keyword keyword;
    private final String rawContent;
    private final String keywordAlias;
    
    public Part(int offset, Keyword keyword, String rawContent, String keywordAlias) {
        super();
        this.offset = offset;
        this.keyword = keyword;
        this.rawContent = rawContent;
        this.keywordAlias = keywordAlias;
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
        return (offset == other.offset) && Equals.areEquals(rawContent, other.rawContent);
    }

    @Override
    public String toString() {
        return "Part{" +
                "offset=" + offset +
                ", rawContent='" + rawContent + '\'' +
                '}';
    }
}
