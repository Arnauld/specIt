package specit.parser;

import specit.element.Comment;
import specit.util.New;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 */
public class CommentParser {

    public interface Callback {
        void comment(int offset, String delimiter, String content);
        void data(int offset, CharSequence content);
    }

    private Pattern commentsPattern;

    /**
     * The default comment regex, that supports the following <strong>single line comment</strong> format
     * <pre>
     *   // This is a C-style single line comment
     *
     *   # This is a bash-style single line comment
     * </pre>
     */
    public String commentsRegex() {
        return "(?:" + "(#)(.*)$" + ")|(?:" + "(//)(.*)$" + ")|(?:(?s)" + "(/\\*)(.*?)\\*/" + ")";
    }

    private Pattern commentsPattern() {
        if (commentsPattern == null) {
            commentsPattern = Pattern.compile(commentsRegex(), Pattern.MULTILINE);
        }
        return commentsPattern;
    }

    public String contentWithoutComment(String rawContent) {
        return commentsPattern().matcher(rawContent).replaceAll("");
    }

    public List<Comment> parseComments(int baseOffset, String rawContent) {
        List<Comment> comments = New.arrayList();

        Pattern pattern = commentsPattern();
        Matcher matcher = pattern.matcher(rawContent);
        while (matcher.find()) {
            int groupCount = matcher.groupCount();
            for (int i = 1; i < groupCount; i+=2) {
                String delimiter = matcher.group(i);
                int delimiterStart = matcher.start(i);
                if (delimiter != null) {
                    String content = matcher.group(i + 1);
                    comments.add(new Comment(baseOffset + delimiterStart, delimiter, content));
                    break;
                }
            }
        }

        return comments;
    }

    public void tokenize(int baseOffset, CharSequence rawContent, Callback callback) {
        Matcher matcher = commentsPattern().matcher(rawContent);

        int prev = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start > prev) {
                callback.data(baseOffset + prev, rawContent.subSequence(prev, start));
            }

            int groupCount = matcher.groupCount();
            groupLoop: for (int i = 1; i < groupCount; i+=2) {
                String delimiter = matcher.group(i);
                int delimiterStart = matcher.start(i);
                if (delimiter != null) {
                    String content = matcher.group(i + 1);
                    callback.comment(baseOffset + start, delimiter, content);
                    break groupLoop;
                }
            }

            prev = end;
        }
        if (prev < rawContent.length()) {
            callback.data(baseOffset + prev, rawContent.subSequence(prev, rawContent.length()));
        }
    }
}
