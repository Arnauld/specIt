package specit.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the converter for the corresponding parameter.
 * The converter is responsible for converting the {@ling java.lang.String} extracted from the Step into the
 * corresponding parameter type.
 *
 * @see specit.invocation.Converter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Converter {
    Class<? extends specit.invocation.Converter> value();
}
