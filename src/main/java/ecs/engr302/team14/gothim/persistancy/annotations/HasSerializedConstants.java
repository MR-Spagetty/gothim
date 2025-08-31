package ecs.engr302.team14.gothim.persistancy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Simple annotation used to mark when objects of a type should be attempted to
 * be serialized into constants.
 *
 * @author MR-Spagetty
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HasSerializedConstants {

}
