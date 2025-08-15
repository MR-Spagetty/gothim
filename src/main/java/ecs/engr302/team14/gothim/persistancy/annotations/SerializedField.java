package ecs.engr302.team14.gothim.persistancy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field to be used when serializing objects.
 *
 * @author MR-Spagetty
 */
@Target(ElementType.FIELD)
public @interface SerializedField {
    /**
     * the name of the parameter to pass this value into in the deserialization
     * method.
     *
     * <p>if unspecified will use the field name
     */
    public String deserialParamName() default "";
}