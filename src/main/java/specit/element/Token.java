package specit.element;

import static specit.util.Hashcodes.PRIME;

/**
 *
 */
public class Token {

    /**
     * Different kind of token.
     */
    public enum Kind {
        Keyword,
        DefaultContent,
        // --- comment
        CommentDelimiter,
        CommentContent,
        // --- step
        ParameterContent,
        // --- Table
        TableCellDelimiter,
        TableCellContent,
        // --- Repeat & Fragment
        RepeatIdentifierDelimiter,
        RepeatIdentifier,
        FragmentReferenceDelimiter,
        FragmentReference,
    }

    private final int offset;
    private final int length;
    private final Kind kind;

    public Token(Kind kind, int offset, int length) {
        this.kind = kind;
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public Kind getKind() {
        return kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Token token = (Token) o;

        if (length != token.length) {
            return false;
        }
        if (offset != token.offset) {
            return false;
        }
        if (kind != token.kind) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = offset;
        result = PRIME * result + length;
        result = PRIME * result + (kind != null ? kind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Token{"
                + "offset=" + offset
                + ", length=" + length
                + ", kind=" + kind
                + '}';
    }
}
