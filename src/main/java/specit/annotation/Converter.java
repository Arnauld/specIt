package specit.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Converter {
    Class<? extends specit.mapping.Converter> value();
}
