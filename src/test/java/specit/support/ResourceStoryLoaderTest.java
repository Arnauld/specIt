package specit.support;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import specit.SpecItRuntimeException;
import specit.util.IO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 */
public class ResourceStoryLoaderTest {

    private ResourceStoryLoader storyLoader;

    @Before
    public void setUp () {
        storyLoader = new ResourceStoryLoader("specit/support/");
    }

    @Test
    public void nominal_usecase() {
        String content = storyLoader.loadStoryAsText("/woot.story");
        assertThat(content).isEqualTo("Hey!");
    }

    @Test
    public void trailing_slash_isNotRelevant() {
        storyLoader = new ResourceStoryLoader("/specit/support/");
        String content = storyLoader.loadStoryAsText("/woot.story");
        assertThat(content).isEqualTo("Hey!");
    }

    @Test(expected = SpecItRuntimeException.class)
    public void missing_story() {
        storyLoader.loadStoryAsText("/wooty.story");
    }

    @Test
    public void utf8_isUsedByDefault() throws IOException {
        IO io = mock(IO.class);
        storyLoader = new ResourceStoryLoader("specit/support/", io);

        when(io.toString(any(InputStream.class), anyString())).thenReturn("Woot");

        String content = storyLoader.loadStoryAsText("/woot.story");
        assertThat(content).isEqualTo("Woot");

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(io).toString(any(InputStream.class), argument.capture());

        assertThat(argument.getValue()).isEqualToIgnoringCase("utf-8");
    }

    @Test
    public void charset_can_be_specified() throws IOException {
        IO io = mock(IO.class);
        storyLoader = new ResourceStoryLoader("specit/support/", io).withCharset("iso-8859-1");

        when(io.toString(any(InputStream.class), anyString())).thenReturn("Woot");

        String content = storyLoader.loadStoryAsText("/woot.story");
        assertThat(content).isEqualTo("Woot");

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(io).toString(any(InputStream.class), argument.capture());

        assertThat(argument.getValue()).isEqualToIgnoringCase("iso-8859-1");
    }

    @Test(expected = SpecItRuntimeException.class)
    public void error_while_reading() throws IOException {
        IO io = mock(IO.class);
        storyLoader = new ResourceStoryLoader("specit/support/", io);

        when(io.toString(any(InputStream.class), anyString())).thenThrow(new IOException());
        storyLoader.loadStoryAsText("/woot.story");
    }
}
