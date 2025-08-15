package ecs.engr302.team14.gothim.persistancy.annotations;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * enforcing that a class with any @SerializedField s has exactly
 * one @DeserializationMethod.
 *
 * @author MR-Spagetty
 */
@SupportedAnnotationTypes({ "ecs.engr302.team14.gothim.persistancy.annotations.SerializedField",
        "ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod" }) 
@SupportedSourceVersion(SourceVersion.RELEASE_23)
public class SerializationAnnotationEnforcer extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Map from enclosing class -> hasAField / countBMethods
        for (Element element : roundEnv.getRootElements()) {
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }

            boolean hasSerializedField = false;
            int deserializationMethodCount = 0;

            for (Element enclosed : element.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.FIELD
                        && enclosed.getAnnotation(SerializedField.class) != null) {
                    hasSerializedField = true;
                }
                if (enclosed.getKind() == ElementKind.METHOD
                        && enclosed.getAnnotation(SerializedField.class) != null) {
                    deserializationMethodCount++;
                }
            }

            if (hasSerializedField && deserializationMethodCount != 1) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Class " + element.getSimpleName()
                                + " has atleast one @SerializedField but must "
                                + "have exactly one @DeserializationMethod (found "
                                + deserializationMethodCount + ")",
                        element);
            }
        }
        return true;
    }
}
