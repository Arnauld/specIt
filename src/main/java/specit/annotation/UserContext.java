package specit.annotation;

/**
 *
 *
 */
public @interface UserContext {
    /**
     * (Optional) Name of the context to inject.
     */
    String value() default "";
}
