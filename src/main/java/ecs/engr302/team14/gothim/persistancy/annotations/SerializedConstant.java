package ecs.engr302.team14.gothim.persistancy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Simple annotation to mark Constants to be used when a class is annotated with
 * {@link HasSerializedConstants @HasSerializedConstants}.
 *
 * @author MR-Spagetty
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializedConstant {
}
