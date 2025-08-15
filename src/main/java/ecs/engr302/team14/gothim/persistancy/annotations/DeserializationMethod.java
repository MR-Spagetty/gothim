package ecs.engr302.team14.gothim.persistancy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The method or constructor to use to reassemble object from the deserialized
 * data.
 *
 * <p>The number, names, and types of the parameters must match those of the
 * fields annotated with {@link SerializedField @SerializedField} where if the
 * name is specified in that annotation that is the name that must match
 *
 * @author MR-Spagetty
 */
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface DeserializationMethod {
}
