package specit.element;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class ElementVisitorTest {

    private ElementVisitor delegateMock;

    @Before
    public void setUp() {
        delegateMock = mock(ElementVisitor.class);
    }

    @Test
    public void beginDefaultFallback_returnsTrue() {
        assertThat(new ElementVisitor().beginDefaultFallback(null)).isTrue();
    }

    @Test
    public void endDefaultFallback_doesNotThrowAnything() {
        new ElementVisitor().endDefaultFallback(null);

        Element element = mock(Element.class);
        new ElementVisitor().endDefaultFallback(element);
        verifyNoMoreInteractions(element);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginStep() {
        Step step = mock(Step.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginStep(step);

        verify(delegateMock).beginDefaultFallback(same(step));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndStep() {
        Step step = mock(Step.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endStep(step);

        verify(delegateMock).endDefaultFallback(same(step));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginScenario() {
        Scenario scenario = mock(Scenario.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginScenario(scenario);

        verify(delegateMock).beginDefaultFallback(same(scenario));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndScenario() {
        Scenario scenario = mock(Scenario.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endScenario(scenario);

        verify(delegateMock).endDefaultFallback(same(scenario));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginBackground() {
        Background scenario = mock(Background.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginBackground(scenario);

        verify(delegateMock).beginDefaultFallback(same(scenario));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndBackground() {
        Background scenario = mock(Background.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endBackground(scenario);

        verify(delegateMock).endDefaultFallback(same(scenario));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginExample() {
        Example example = mock(Example.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginExample(example);

        verify(delegateMock).beginDefaultFallback(same(example));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndExample() {
        Example example = mock(Example.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endExample(example);

        verify(delegateMock).endDefaultFallback(same(example));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginNarrative() {
        Narrative narrative = mock(Narrative.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginNarrative(narrative);

        verify(delegateMock).beginDefaultFallback(same(narrative));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndNarrative() {
        Narrative narrative = mock(Narrative.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endNarrative(narrative);

        verify(delegateMock).endDefaultFallback(same(narrative));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginRequire() {
        Require require = mock(Require.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginRequire(require);

        verify(delegateMock).beginDefaultFallback(same(require));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndRequire() {
        Require require = mock(Require.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endRequire(require);

        verify(delegateMock).endDefaultFallback(same(require));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginStory() {
        Story story = mock(Story.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginStory(story);

        verify(delegateMock).beginDefaultFallback(same(story));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndStory() {
        Story story = mock(Story.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endStory(story);

        verify(delegateMock).endDefaultFallback(same(story));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginDefaultExecutablePart() {
        DefaultExecutablePart story = mock(DefaultExecutablePart.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginDefaultExecutablePart(story);

        verify(delegateMock).beginDefaultFallback(same(story));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndDefaultExecutablePart() {
        DefaultExecutablePart defaultExecutablePart = mock(DefaultExecutablePart.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endDefaultExecutablePart(defaultExecutablePart);

        verify(delegateMock).endDefaultFallback(same(defaultExecutablePart));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginRepeat() {
        Repeat repeat = mock(Repeat.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginRepeat(repeat);

        verify(delegateMock).beginDefaultFallback(same(repeat));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndRepeat() {
        Repeat repeat = mock(Repeat.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endRepeat(repeat);

        verify(delegateMock).endDefaultFallback(same(repeat));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onBeginFragment() {
        Fragment fragment = mock(Fragment.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.beginFragment(fragment);

        verify(delegateMock).beginDefaultFallback(same(fragment));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void ensureFallbackIsCalledByDefault_onEndFragment() {
        Fragment fragment = mock(Fragment.class);

        ElementVisitor visitor = createElementVisitorWithMockOnFallbackMethods();
        visitor.endFragment(fragment);

        verify(delegateMock).endDefaultFallback(same(fragment));
        verifyNoMoreInteractions(delegateMock);
    }


    private ElementVisitor createElementVisitorWithMockOnFallbackMethods() {
        return new ElementVisitor() {
            @Override
            protected boolean beginDefaultFallback(Element element) {
                return delegateMock.beginDefaultFallback(element);
            }

            @Override
            protected void endDefaultFallback(Element element) {
                delegateMock.endDefaultFallback(element);
            }
        };
    }
}
