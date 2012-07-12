package specit.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

/**
 *
 *
 */
public class CharIteratorsTest {

    @Test(expected = IllegalArgumentException.class)
    public void createFrom_nullString() {
        CharIterators.createFrom(null);
    }

    @Test
    public void createFrom_emptyString() {
        CharIterator iterator = CharIterators.createFrom("");
        assertThat(iterator.read()).isEqualTo(CharIterator.EOF);
    }

    @Test
    public void createFrom_basicString() {
        CharIterator iterator = CharIterators.createFrom("abc");
        assertThat(iterator.read()).isEqualTo((int)'a');
        assertThat(iterator.read()).isEqualTo((int)'b');
        assertThat(iterator.read()).isEqualTo((int)'c');
        assertThat(iterator.read()).isEqualTo(CharIterator.EOF);
    }


}
