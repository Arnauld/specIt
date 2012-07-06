package specit.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserContextFactory {
    /**
     * (Optional) Name of the context created.
     * @return
     */
    String name() default "";

    /**
     * Scope of the context created.
     */
    UserContextScope scope();
}
