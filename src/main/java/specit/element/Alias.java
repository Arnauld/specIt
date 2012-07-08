package specit.element;

import static specit.util.Hashcodes.PRIME;

/**
 *
 */
public class Alias {

    private final Keyword keyword;
    private final String keywordAlias;

    public Alias(Keyword keyword, String keywordAlias) {
        if (keyword == null) {
            throw new InvalidElementDefinitionException("Keyword must be defined");
        }
        if (keywordAlias == null) {
            throw new InvalidElementDefinitionException("Keyword alias must be defined");
        }
        this.keyword = keyword;
        this.keywordAlias = keywordAlias;
    }

    public final Keyword getKeyword() {
        return keyword;
    }

    public final String getKeywordAlias() {
        return keywordAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Alias alias = (Alias) o;

        if (keyword != alias.keyword) {
            return false;
        }
        if (!keywordAlias.equals(alias.keywordAlias)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = keyword.hashCode();
        result = PRIME * result + keywordAlias.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Alias{"
                + "keyword=" + keyword
                + ", keywordAlias='" + keywordAlias + '\''
                + '}';
    }
}
