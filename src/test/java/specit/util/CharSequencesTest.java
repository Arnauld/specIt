package specit.util;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

}
