package specit.parser;

import org.junit.Test;
import specit.element.Comment;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 *
 */
public class CommentParserTest {

    private static final String NL = "\n";

    @Test
    public void parse_multipleComments() {
        String content = "Hey ho! # salutation" + NL +
                "What's happen?" + NL +
                "// a line fully commented";

        CommentParser parser = new CommentParser();
        List<Comment> comments = parser.parseComments(17, content);

        assertThat(comments.size(), is(2));
        assertThat(comments.get(0), equalTo(new Comment(25, "#", " salutation")));
        assertThat(comments.get(1), equalTo(new Comment(53, "//", " a line fully commented")));
    }

    @Test
    public void contentWihoutComments() {
        String content = "Hey ho! # salutation" + NL +
                "What's happen?" + NL +
                "// a line fully commented";

        CommentParser parser = new CommentParser();
        String cleaned = parser.contentWithoutComment(content);

        assertThat(cleaned, notNullValue());
        assertThat(cleaned, equalTo("Hey ho! \nWhat's happen?\n"));
    }
}
