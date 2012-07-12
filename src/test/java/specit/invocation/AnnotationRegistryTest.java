package specit.invocation;

import static org.fest.assertions.api.Assertions.assertThat;

import specit.annotation.UserContext;
import specit.annotation.lifecycle.AfterScenario;
import specit.annotation.lifecycle.AfterStory;
import specit.annotation.lifecycle.BeforeScenario;
import specit.annotation.lifecycle.BeforeStory;
import specit.util.New;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class AnnotationRegistryTest {


    private AnnotationRegistry annotationRegistry;

    @Before
    public void setUp() {
        annotationRegistry = new AnnotationRegistry();
    }

    @Test
    public void scan_null() throws ParameterMappingException {
        annotationRegistry.scan(null);
    }

    @Test
    public void scan_case1_userContextFactories() throws Exception {
        // When
        annotationRegistry.scan(Case1.class);

        // Then
        assertThat(annotationRegistry.getUserContextFactories(UserContext.Scope.Story))
                .isNotNull()
                .isEmpty();

        List<UserContextFactorySupport> userContextFactories =
                annotationRegistry.getUserContextFactories(UserContext.Scope.Scenario);
        assertThat(userContextFactories)
                .isNotNull()
                .hasSize(1);
        UserContextFactorySupport userContextFactorySupport = userContextFactories.get(0);
        assertThat(userContextFactorySupport.getOwningType()).isEqualTo((Class) Case1.class);
        assertThat(userContextFactorySupport.getMethod().getName()).isEqualTo("context");
        assertThat(userContextFactorySupport.scope()).isEqualTo(UserContext.Scope.Scenario);
    }

    @Test
    public void scan_case1_beforeStory() throws Exception {
        // When
        annotationRegistry.scan(Case1.class);

        // Then
        List<Lifecycle> lifecycles = annotationRegistry.getLifecycles(BeforeStory.class);
        assertThat(lifecycles).isNotNull().hasSize(3);
    }

    @Test
    public void scan_case1_afterStory() throws Exception {
        // When
        annotationRegistry.scan(Case1.class);

        // Then
        List<Lifecycle> lifecycles = annotationRegistry.getLifecycles(AfterStory.class);
        assertThat(lifecycles).isNotNull().hasSize(1);
        assertThat(lifecycles.get(0).getLifecycleType()).isEqualTo((Class) AfterStory.class);
        assertThat(lifecycles.get(0).getOwningType()).isEqualTo((Class) Case1.class);
        assertThat(lifecycles.get(0).getMethod().getName()).isEqualTo("afterStory");
    }

    @Test
    public void scan_case1_beforeScenario() throws Exception {
        // When
        annotationRegistry.scan(Case1.class);

        // Then
        List<Lifecycle> lifecycles = annotationRegistry.getLifecycles(BeforeScenario.class);
        assertThat(lifecycles).isNotNull().hasSize(2);
    }

    @Test
    public void scan_case1_afterScenario() throws Exception {
        // When
        annotationRegistry.scan(Case1.class);

        // Then
        List<Lifecycle> lifecycles = annotationRegistry.getLifecycles(AfterScenario.class);
        assertThat(lifecycles).isNotNull().hasSize(1);
        assertThat(lifecycles.get(0).getLifecycleType()).isEqualTo((Class) AfterScenario.class);
        assertThat(lifecycles.get(0).getOwningType()).isEqualTo((Class) Case1.class);
        assertThat(lifecycles.get(0).getMethod().getName()).isEqualTo("afterScenario");
    }

    private static class Case1 {
        @BeforeStory
        public void beforeStory1() {
        }

        @BeforeStory
        public void beforeStory2() {
        }

        @BeforeStory
        public static void beforeStoryStatic() {
        }

        @AfterStory
        public void afterStory() {
        }

        @BeforeScenario
        public void beforeScenario1() {
        }

        @BeforeScenario
        public void beforeScenario2(@UserContext Map<String, String> context) {
        }

        @AfterScenario
        public void afterScenario() {
        }

        @UserContext.Factory(scope = UserContext.Scope.Scenario)
        public Map<String, String> context() {
            return New.hashMap();
        }
    }
}
