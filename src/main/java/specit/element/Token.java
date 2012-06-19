package specit.element;

/**
 *
 */
public class Token {
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

}
