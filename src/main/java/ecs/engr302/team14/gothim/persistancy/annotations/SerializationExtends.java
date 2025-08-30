package ecs.engr302.team14.gothim.persistancy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to permit inheritance of
 * {@link SerializedField @SerializedField}s.
 *
 * <p>Inheritance is recursive and this annotation will be inherited such that a
 * subclass will inherit any additional properties from its super until reaching
 * the nearest ancestor class that is \@SerializationExtends annotated then
 * inheriting from the stated class and if that class is annotated as well the
 * cycle will continue.
 *
 * <p>The specified class to inherit from must also be assignable from this
 * class
 *
 * @author MR-Spagetty
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SerializationExtends {
    /**
     * the class to inherit any serialized fields from.
     */
    public Class<?> value();
}
