package specit.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

/**
 *
 */
public class TemplateEngineTest {

    private TemplateEngine templateEngine;
    private HashMap<String, String> variables;

    @Before
    public void setUp() {
        templateEngine = new TemplateEngine();
        variables = New.hashMap("who", "World", "when", "tomorrow");
    }

    @Test
    public void resolve_basic() {
        StringBuilder resolved = templateEngine.resolve("<who><when>", variables);
        assertThat(resolved.toString(), equalTo("Worldtomorrow"));
    }

    @Test
    public void resolve() {
        StringBuilder resolved = templateEngine.resolve("Hello <who>!!", variables);
        assertThat(resolved.toString(), equalTo("Hello World!!"));
    }

    @Test
    public void resolve_unknownVariableIsLeftAsIs() {
        StringBuilder resolved = templateEngine.resolve("Hello <whom> <when>!!", variables);
        assertThat(resolved.toString(), equalTo("Hello <whom> tomorrow!!"));
    }
}
