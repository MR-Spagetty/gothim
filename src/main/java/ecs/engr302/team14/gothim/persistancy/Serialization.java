package ecs.engr302.team14.gothim.persistancy;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Serialization class containting various methods to handle serialization and
 * deserialization of objects.
 *
 * @author MR-Spagetty
 */
public final class Serialization {
    private Serialization() {
    }

    /**
     * Serializes the given object into json data.
     *
     * @param thing the object to serialize
     * @return the json data
     */
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
                    if (col instanceof Set) {
                        obj.put("kind", new JsonString("set"));
                    }
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
        List<Class<?>> keyTypes = new ArrayList<>(
                map.keySet().parallelStream().map(Object::getClass).distinct().toList());
        for (int i = 0; i < keyTypes.size() - 1;) {
            if (keyTypes.get(i).isAssignableFrom(keyTypes.get(i + 1))) {
                keyTypes.remove(i + 1);
            } else if (keyTypes.get(i + 1).isAssignableFrom(keyTypes.get(i))) {
                keyTypes.remove(i);
            } else {
                i++;
            }
        }
        List<Class<?>> valueTypes = new ArrayList<>(
                map.values().parallelStream().map(Object::getClass).distinct().toList());
        for (int i = 0; i < valueTypes.size() - 1;) {
            if (valueTypes.get(i).isAssignableFrom(valueTypes.get(i + 1))) {
                valueTypes.remove(i + 1);
            } else if (valueTypes.get(i + 1).isAssignableFrom(valueTypes.get(i))) {
                valueTypes.remove(i);
            } else {
                i++;
            }
        }
        boolean stringKey = false;
        if (keyTypes.size() == 1) {
            if (!keyTypes.getFirst().equals(String.class)) {
                ret.put("keyType", new JsonString(keyTypes.getFirst().getName()));
            } else {
                stringKey = true;
            }
        } else {
            ret.put("keyType", new JsonString(Object.class.getName()));
        }
        if (valueTypes.size() == 1) {
            ret.put("valueType", new JsonString(mapValueTypeSel(valueTypes.getFirst())));
        } else {
            ret.put("keyType", new JsonString(Object.class.getName()));
        }

        if (stringKey) {
            JsonObject values = new JsonObject();
            map.keySet().forEach(k -> values.put((String) k, toJson(map.get(k))));
            ret.put("entries", values);
        } else {
            JsonArray values = new JsonArray();
            map.entrySet().parallelStream().map(e -> {
                JsonObject entry = new JsonObject();
                entry.put("key", toJson(e.getKey()));
                entry.put("value", toJson(e.getValue()));
                return entry;
            }).forEachOrdered(values::add);
            ret.put("entries", values);
        }

        ret.put("kind", new JsonString("map"));
        ret.put("type", new JsonString(map.getClass().getName()));

        return ret;
    }

    private static String mapValueTypeSel(Class<?> valueClass) {
        if (valueClass.equals(String.class)) {
            return "String";
        } else if (valueClass.equals(Double.class)) {
            return "double";
        } else if (valueClass.equals(Long.class)) {
            return "long";
        } else if (valueClass.equals(Float.class)) {
            return "float";
        } else if (valueClass.equals(Integer.class)) {
            return "int";
        }
        return valueClass.getName();
    }

    private static JsonObject serializeObject(Object thing) {
        JsonObject ret = new JsonObject();
        JsonArray failedFields = new JsonArray();
        var fields = new JsonObject();
        ret.put("failedFields", failedFields);
        ret.put("fields", fields);
        Class<?> thingClass = thing.getClass();
        if (Stream
                .concat(Stream.of(thingClass.getMethods()),
                        Stream.of(thing.getClass().getConstructors()))
                .parallel().noneMatch(m -> m.isAnnotationPresent(DeserializationMethod.class))) {
            throw new IllegalArgumentException("Class: %s (of supplied object is not serializable"
                    .formatted(thingClass.getName()));
        }
        ret.put("class", new JsonString(thingClass.getName()));
        List<Field> fieldsToSerial = Stream.of(thingClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(SerializedField.class)).toList();
        fieldsToSerial.stream().forEach(f -> {
            try {
                f.setAccessible(true);
                try {
                    fields.put(f.getName(), toJson(f.get(thing)));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error, object lied about its type");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (InaccessibleObjectException ioe) {
                return; // checkstyle doesn't like an empty catch
            }
        });
        if (failedFields.size() == 0) {
            ret.remove("failedFields");
        }
        return ret;
    }
}