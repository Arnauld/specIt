package specit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the name of the variable the parameter is matching.
 * The value must then match the variable name within the step definition.
 * <p/>
 * <pre>
 *     @ Given("a variable $<b>var</b> with a value $<b>value</b>")
 *     public void define(@Variable("<b>value</b>") int val, @Variable("<b>var</b>") String name) {
 *         ...
 *     }
 * </pre>
 *
 * @see Given
 * @see When
 * @see Then
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Variable {
    String value();
}
