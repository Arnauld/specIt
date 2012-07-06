package specit.annotation;

import java.lang.annotation.*;

/**
 *
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UserContext {

    /**
     * (Optional) Name of the context to inject.
     */
    String value() default "";
}
