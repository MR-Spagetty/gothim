package ecs.engr302.team14.gothim.persistancy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The method or constructor to use to reassemble object from the deserialized
 * data.
 *
 * <p>The number and types of the parameters must match those of the fields
 * annotated with {@link SerializedField @SerializedField} and the array of
 * specified names must math the names as specified in the annotation or if not
 * specified/empty the name of the field itself
 *
 * @author MR-Spagetty
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface DeserializationMethod {
    /**
     * the names of the fields in the serialized data to be used for the
     * deserialization in the order they are to be passed.
     */
    String[] serialFieldNames();
}
