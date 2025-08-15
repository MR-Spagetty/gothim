package ecs.engr302.team14.gothim.persistancy;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class Serialization {
    private Serialization() {
    }

    public static JsonType toJson(Object thing) {
        return switch (thing) {
            case JsonType t -> t;
            case null -> JsonValue.NULL;
            case String s -> new JsonString(s);
            case Number num -> new JsonNum(num.doubleValue());
            case Boolean bool -> JsonBool.of(bool);
            case Collection<?> col -> {
                JsonArray arr = new JsonArray();
                col.stream().map(Serialization::toJson).forEach(arr::add);
                if (!(col instanceof ArrayList<?>)) {
                    JsonObject obj = new JsonObject();
                    obj.put("type", new JsonString(col.getClass().getName()));
                    obj.put("values", arr);
                    yield obj;
                }
                yield arr;
            }
            case Map<?, ?> map -> mapMapToJson(map);
            default -> serializeObject(thing);
        };
    }

    private static JsonObject mapMapToJson(Map<?, ?> map) {
        JsonObject ret = new JsonObject();
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mapToJson'");
    }

    private static JsonObject serializeObject(Object thing) {
        JsonObject ret = new JsonObject();
        JsonArray failedFields = new JsonArray();
        var fields = new JsonObject();
        ret.put("failedFields", failedFields);
        ret.put("fields", fields);
        Class<?> thingClass = thing.getClass();
        if (Stream.of(thingClass.getMethods()).parallel()
                .noneMatch(m -> m.isAnnotationPresent(DeserializationMethod.class))) {
            throw new IllegalArgumentException("Class: %s (of supplied object is not serializable"
                    .formatted(thingClass.getName()));
        }
        ret.put("class", new JsonString(thingClass.getName()));
        List<Field> fieldsToSerial = Stream.of(thingClass.getFields())
                .filter(f -> f.isAnnotationPresent(SerializedField.class)).toList();
        fieldsToSerial.stream().forEach(f -> {
            try {
                f.setAccessible(true);
                try {
                    ret.put(f.getName(), toJson(f.get(thing)));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error, object lied about its type");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (InaccessibleObjectException ioe) {
                return; // checkstyle doesn't like an empty catch
            }
        });
        return ret;
    }
}