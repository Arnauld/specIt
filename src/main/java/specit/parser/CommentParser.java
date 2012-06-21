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

    private Pattern pattern;

    public String commentsRegex() {
        return "(?:" + "(#)(.*)$" + ")|(?:" + "(//)(.*)$" + ")";
    }

    protected void invalidatePattern() {
        pattern = null;
    }

    private Pattern commentsPattern() {
        if (pattern == null)
            pattern = Pattern.compile(commentsRegex(), Pattern.MULTILINE);
        return pattern;
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
            for (int i = 1; i < groupCount; i++) {
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

}
