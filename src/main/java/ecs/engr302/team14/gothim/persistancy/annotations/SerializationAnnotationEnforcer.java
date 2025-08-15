package ecs.engr302.team14.gothim.persistancy.annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

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
            int deserializationMethodCount = 0;
            ExecutableElement deserializationMethod = null;
            List<Entry<String, TypeMirror>> fields = new ArrayList<>();

            for (Element enclosed : element.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.FIELD
                        && enclosed.getAnnotation(SerializedField.class) != null) {
                    String specifiedName = enclosed.getAnnotation(SerializedField.class)
                            .deserialParamName();
                    String name = specifiedName.isEmpty() ? enclosed.getSimpleName().toString()
                            : specifiedName;
                    fields.add(Map.entry(name, enclosed.asType()));
                }
                if (enclosed.getKind() == ElementKind.METHOD
                        && enclosed.getAnnotation(DeserializationMethod.class) != null) {
                    deserializationMethodCount++;
                    deserializationMethod = (ExecutableElement) enclosed;
                }
            }

            BiConsumer<String, Element> error = processingEnv.getMessager()::printError;
            if (deserializationMethodCount > 1) {
                error.accept("Class " + element.getSimpleName()
                        + " has more than one @DeserializationMethod", element);
            } else if (fields.size() > 0 && deserializationMethodCount != 1) {
                processingEnv.getMessager()
                        .printError("Class " + element.getSimpleName()
                                + " has at least one @SerializedField but must "
                                + "have exactly one @DeserializationMethod (found "
                                + deserializationMethodCount + ")", element);
            } else if (fields.size() > 0) {
                Map<String, TypeMirror> params = new HashMap<>();
                deserializationMethod.getParameters().stream()
                        .forEach(p -> params.put(p.getSimpleName().toString(), p.asType()));
                if (fields.size() != params.size()) {
                    error.accept(
                            "Number of parameters for method "
                                    + deserializationMethod.getSimpleName()
                                    + " does not match the required number: " + fields.size(),
                            deserializationMethod);
                }
                List<String> missingNames = fields.parallelStream().map(Entry::getKey)
                        .filter(n -> params.keySet().parallelStream().noneMatch(p -> p.equals(n)))
                        .toList();
                if (missingNames.size() > 0) {
                    error.accept("There are some parameters that are not present by "
                            + "name in the @DeserializationMethod that should be, they are: \n"
                            + missingNames, deserializationMethod);
                }
                fields.removeIf(f -> missingNames.contains(f.getKey()));
                List<String> typeMismatchErrors = fields.parallelStream()
                        .filter(f -> !params.get(f.getKey()).equals(f.getValue()))
                        .map(f -> "parameter %s should be of type %s but was %s"
                                .formatted(f.getKey(), f.getValue(), params.get(f.getKey())))
                        .toList();
                if (!typeMismatchErrors.isEmpty()) {
                    error.accept(
                            typeMismatchErrors.size()
                                    + " parameter types do not match their fields for "
                                    + "the @DeserializationMethod:\n"
                                    + typeMismatchErrors.stream().collect(Collectors.joining("\n")),
                            deserializationMethod);
                }

            }
        }
        return true;
    }
}
