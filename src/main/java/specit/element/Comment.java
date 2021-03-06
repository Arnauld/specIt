package specit.element;

import static specit.util.Hashcodes.PRIME;

/**
 *
 *
 */
public class Comment {
    private final int offset;
    private final String delimiter;
    private final String content;


    public Comment(int offset, String delimiter, String content) {
        this.offset = offset;
        this.delimiter = delimiter;
        this.content = content;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public int getOffset() {
        return offset;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Comment comment = (Comment) o;

        if (offset != comment.offset) {
            return false;
        }
        else if (content != null ? !content.equals(comment.content) : comment.content != null) {
            return false;
        }
        else if (delimiter != null ? !delimiter.equals(comment.delimiter) : comment.delimiter != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = offset;
        result = PRIME * result + (delimiter != null ? delimiter.hashCode() : 0);
        result = PRIME * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{"
                + "offset=" + offset
                + ", delimiter='" + delimiter + '\''
                + ", content='" + content + '\''
                + '}';
    }
}
