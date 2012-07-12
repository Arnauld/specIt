package specit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated parameter is a user defined context.
 * A user context is usually defined through the {@link Factory} annotation.
 * A name can be added if several instance of the same class can exists at the same time.
 * <p/>
 * <pre>
 *     @UserContext.Factory(scope=UserContext.Scope.Story)
 *     public Calculator newCalculatator() {
 *         return new Calculator();
 *     }
 *     ...
 *     @Given("a variable $x initialized with value $value")
 *     public void defineVariable(@UserContext Calculator calculate, String x, int value) {
 *         calculator.defineVariable(x, value);
 *     }
 * </pre>
 *
 * @see specit.annotation.UserContext.Factory
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UserContext {

    /**
     * (Optional) Name of the context to inject.
     *
     * @see specit.annotation.UserContext.Factory#name()
     */
    String value() default "";

    /**
     * Scope in which a context can belongs to.
     */
    public enum Scope {
        Story,
        Scenario
    }

    /**
     * Indicates that the annotated method creates an instance for the specified scope ({@link #scope()}).
     * This instance will then be available within the corresponding scope by marking corresponding method
     * parameter with {@link specit.annotation.UserContext} annotation.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Factory {

        /**
         * (Optional) Name of the context created.
         *
         * @return
         * @see specit.annotation.UserContext#value()
         */
        String name() default "";

        /**
         * Scope of the context created.
         */
        Scope scope();
    }
}
