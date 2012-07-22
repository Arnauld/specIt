package specit.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import specit.element.Comment;

import org.junit.Test;
import java.util.List;

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
    public void parse_multilineComments() {
        String content = "Hey ho! /* salutation" + NL +
                "What's happen?" + NL +
                " nothing? */Hey?";

        CommentParser parser = new CommentParser();
        List<Comment> comments = parser.parseComments(17, content);

        assertThat(comments.size(), is(1));
        assertThat(comments.get(0), equalTo(new Comment(25, "/*", " salutation" + NL + "What's happen?" + NL + " nothing? ")));
    }

    @Test
    public void contentWihoutComments_singleLineCases() {
        String content = "Hey ho! # salutation" + NL +
                "What's happen?" + NL +
                "// a line fully commented";

        CommentParser parser = new CommentParser();
        String cleaned = parser.contentWithoutComment(content);

        assertThat(cleaned, notNullValue());
        assertThat(cleaned, equalTo("Hey ho! \nWhat's happen?\n"));
    }

    @Test
    public void contentWihoutComments_multilineCase() {
        String content = "Hey ho! /* salutation" + NL +
                "What's happen?" + NL +
                " nothing? */Hey?";

        CommentParser parser = new CommentParser();
        String cleaned = parser.contentWithoutComment(content);

        assertThat(cleaned, notNullValue());
        assertThat(cleaned, equalTo("Hey ho! Hey?"));
    }

    @Test
    public void tokenize_multipleComments() {
        String content = "Hey ho! # salutation" + NL +
                "What's happen?" + NL +
                "// a line fully commented";

        CommentParser.Callback callback = mock(CommentParser.Callback.class);

        new CommentParser().tokenize(17, content, callback);

        verify(callback).data(17, "Hey ho! ");
        verify(callback).comment(25, "#", " salutation");
        verify(callback).data(37, NL + "What's happen?" + NL);
        verify(callback).comment(53, "//", " a line fully commented");
        verifyNoMoreInteractions(callback);
    }
}
