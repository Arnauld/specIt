package specit.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class CharSequencesTest {

    @Test
    public void startsWith() {
        assertThat(CharSequences.startsWith("youpla boom", "youp"), is(true));
        assertThat(CharSequences.startsWith("yOupla boom", "youp"), is(false));
        assertThat(CharSequences.startsWith(" youpla boom", "youp"), is(false));
        assertThat(CharSequences.startsWith("youpla boom", "yop"), is(false));
    }

    @Test
    public void startsWithIgnoringChars() {
        assertThat(CharSequences.startsWithIgnoringChars("    youpla boom", "youp", " \t"), is(true));
        assertThat(CharSequences.startsWithIgnoringChars(" \t youpla boom", "youp", " \t"), is(true));
        assertThat(CharSequences.startsWithIgnoringChars(" _ youpla boom", "youp", " \t"), is(false));
    }

    @Test
    public void endsWithBlankLine() {
        assertThat(CharSequences.endsWithBlankLine("    youpla boom"), is(false));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n"), is(false));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n\n"), is(true));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n   \n"), is(true));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n  \t \n"), is(true));
    }

    @Test
    public void endsWithBlankLine_trailingWhitespaces() {
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n\n "), is(true));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n\n\t"), is(true));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n\n\t "), is(true));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n  \n "), is(true));
        assertThat(CharSequences.endsWithBlankLine("    youpla boom\n\t\n\t"), is(true));
    }
}
