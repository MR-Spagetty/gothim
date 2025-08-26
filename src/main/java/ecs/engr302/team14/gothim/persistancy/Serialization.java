package ecs.engr302.team14.gothim.persistancy;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;
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
     * Method for finding the lowest common ancestor of two classes.
     *
     * @param a the first class
     * @param b the second class
     * @return the most specific common super class of both classes
     */
    public static Class<?> lowestCommonAncestor(Class<?> a, Class<?> b) {
        if (a.equals(b)) {
            return a;
        }
        if (b.isAssignableFrom(a)) {
            return b;
        }
        Class<?> curr = a;
        while (curr != null) {
            if (curr.isAssignableFrom(b)) {
                return curr;
            }
            curr = curr.getSuperclass();
        }
        return Object.class;
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
                    obj.put("kind", new JsonString("collection"));
                    obj.put("class", new JsonString(col.getClass().getName()));
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

        ret.put("kind", new JsonString("map"));
        ret.put("class", new JsonString(map.getClass().getName()));
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
        while (valueTypes.size() > 1) {
            valueTypes
                    .add(lowestCommonAncestor(valueTypes.removeFirst(), valueTypes.removeFirst()));
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

    private static Class<?> mapValueTypeDet(String className) {
        return switch (className) {
            case "String" -> String.class;
            case "double" -> Double.class;
            case "long" -> Long.class;
            case "float" -> Float.class;
            case "int" -> Integer.class;
            default -> {
                try {
                    yield Class.forName(className);
                } catch (ClassNotFoundException e) {
                    yield Object.class;
                }
            }
        };
    }

    private static JsonObject serializeObject(Object thing) {
        JsonObject ret = new JsonObject();
        JsonArray failedFields = new JsonArray();
        var fields = new JsonObject();
        Class<?> thingClass = thing.getClass();
        ret.put("class", new JsonString(thingClass.getName()));
        ret.put("failedFields", failedFields);
        ret.put("fields", fields);
        if (Stream
                .concat(Stream.of(thingClass.getMethods()),
                        Stream.of(thing.getClass().getConstructors()))
                .parallel().noneMatch(m -> m.isAnnotationPresent(DeserializationMethod.class))) {
            throw new IllegalArgumentException("Class: %s (of supplied object is not serializable"
                    .formatted(thingClass.getName()));
        }
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

    private static <O, T> T cast(O obj, Class<T> to) {
        if (to.isPrimitive()) {
            @SuppressWarnings("unchecked")
            T unboxed = (T) obj;
            return unboxed;
        }
        if (obj instanceof Number num) {
            if (to.equals(Double.class)) {
                return to.cast(num.doubleValue());
            } else if (to.equals(Float.class)) {
                return to.cast(num.floatValue());
            } else if (to.equals(Long.class)) {
                return to.cast(num.longValue());
            } else if (to.equals(Integer.class)) {
                return to.cast(num.intValue());
            } else if (to.equals(Short.class)) {
                return to.cast(num.shortValue());
            } else if (to.equals(Byte.class)) {
                return to.cast(num.byteValue());
            }
        }
        return to.cast(obj);
    }

    /**
     * Deserializes the given JSON data to the appropriate Objects where
     * possible.
     *
     * @param json the JSON data
     * @return the deserialized object
     */
    public static Object fromJson(JsonType json) {
        if (json == JsonValue.NULL) {
            return null;
        }
        return switch (json) {
            case JsonString s -> s.value();
            case JsonNum n -> n.value();
            case JsonBool b -> b.value();
            case JsonArray a -> {
                List<Object> list = new ArrayList<>();
                IntStream.range(0, a.size()).<Object>mapToObj(i -> fromJson(a.get(i).get()))
                        .forEachOrdered(list::add);
                yield list;
            }
            case JsonObject o -> deserializeFromObject(o);
            default -> throw new RuntimeException(
                    "Should not have reached here, given value was: %s".formatted(json));
        };
    }

    private static <K, V> Map<K, V> deserializeMap(JsonObject o, Class<K> keyType,
            Class<V> valueType) {
        @SuppressWarnings("unchecked")
        Class<? extends Map<K, V>> mapClass = o.get("class")
                .map(v -> v instanceof JsonString js ? js.value() : null).map(className -> {
                    try {
                        return (Class<? extends Map<K, V>>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }).orElseThrow();

        Supplier<Map<K, V>> mapSupp = () -> {
            try {
                return mapClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return new HashMap<>(); // Fallback if instantiation fails
            }
        };
        Map<K, V> ret = mapSupp.get();

        JsonCollection<?> entries = o.get("entries")
                .map(v -> v instanceof JsonCollection jc ? jc : null).orElseThrow();

        if (entries instanceof JsonArray arr) {
            IntStream.range(0, arr.size()).forEach(i -> {
                JsonObject entry = arr.get(i).map(v -> v instanceof JsonObject jo ? jo : null)
                        .orElseThrow();
                K key = cast(fromJson(entry.get("key").orElseThrow()), keyType);
                V value = cast(fromJson(entry.get("value").orElseThrow()), valueType);
                ret.put(key, value);
            });
        } else if (entries instanceof JsonObject obj) {
            for (String key : obj.keySet()) {
                K k = cast(key, keyType);
                V v = cast(fromJson(obj.get(key).orElseThrow()), valueType);
                ret.put(k, v);
            }
        }

        return ret;
    }

    private static Collection<?> deserializeCollection(JsonObject o) {
        JsonArray valuesDat = o.get("values").map(v -> v instanceof JsonArray ja ? ja : null)
                .orElseThrow();
        List<Object> values = IntStream.range(0, valuesDat.size())
                .mapToObj(i -> fromJson(valuesDat.get(i).get())).toList();
        @SuppressWarnings("unchecked")
        Class<? extends Collection<?>> colClass = o.get("class")
                .<String>map(v -> v instanceof JsonString js ? js.value() : null).map(className -> {
                    try {
                        return (Class<? extends Collection<?>>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }).orElseThrow();
        Collection<?> ret;
        try {
            ret = colClass.getDeclaredConstructor().newInstance(values);
        } catch (Exception e) {
            ret = new ArrayList<>(values); // Fallback if instantiation fails
        }

        return ret;
    }

    private static Object deserializeFromObject(JsonObject o) {
        String kind = o.get("kind").map(v -> v instanceof JsonString js ? js.value() : null)
                .orElse(null);
        switch (kind) {
            case "map" -> {
                Class<?> keyType = o.get("keyType").map(v -> {
                    if (v instanceof JsonString js) {
                        return js.value();
                    }
                    throw new IllegalArgumentException(
                            "\"keyType\" field in map was not a JsonString "
                                    + "but instead a %s with value:\n%s"
                                            .formatted(o.getClass().getSimpleName(), o.toString()));
                }).map(Serialization::mapValueTypeDet).orElse(null);
                if (keyType == null) {
                    keyType = String.class;
                }
                Class<?> valueType = o.get("valueType").map(v -> {
                    if (v instanceof JsonString js) {
                        return js.value();
                    }
                    throw new IllegalArgumentException(
                            "\"valueType\" field in map was not a JsonString "
                                    + "but instead a %s with value:\n%s"
                                            .formatted(o.getClass().getSimpleName(), o.toString()));
                }).map(Serialization::mapValueTypeDet)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Could not determine the value type of the map in object:\n%s"
                                        .formatted(o.prettyPrint())));
                return deserializeMap(o, keyType, valueType);
            }
            case "collection" -> {
                return deserializeCollection(o);
            }

            case null -> {
                return deserializeObject(o);
            }

            default -> throw new RuntimeException("Unexpcted value of kind: %s".formatted(kind));
        }
    }

    private static Object deserializeObject(JsonObject o) {
        Class<?> thingClass = o.get("class").map(v -> {
            if (v instanceof JsonString js) {
                return js.value();
            }
            throw new IllegalArgumentException("\"class\" field in object was not a JsonString but "
                    + "instead a %s with value:\n%s".formatted(o.getClass().getSimpleName(),
                            o.toString()));
        }).map(className -> {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(
                        "Could not find class named %s specified in object:\n%s"
                                .formatted(className, o.prettyPrint()));
            }
        }).orElseThrow(() -> new IllegalArgumentException(
                "Could not determine the class of the object from json:\n%s"
                        .formatted(o.prettyPrint())));

        Executable deserialMeth = Stream
                .concat(Stream.of(thingClass.getDeclaredMethods()),
                        Stream.of(thingClass.getDeclaredConstructors()))
                .parallel().filter(m -> m.isAnnotationPresent(DeserializationMethod.class))
                .reduce((a, b) -> {
                    throw new AnnotationFormatError(
                            "Expected only one DeserializationMethod found at least: %s and %s"
                                    .formatted(a, b));
                })
                .orElseThrow(() -> new AnnotationFormatError("Expected one DeserializationMethod"));
        deserialMeth.setAccessible(true);
        Object[] args = new Object[deserialMeth.getParameterCount()];
        JsonObject fields = o.get("fields").map(v -> v instanceof JsonObject jo ? jo : null)
                .orElse(new JsonObject());
        JsonArray failed = o.get("failedFields").map(v -> v instanceof JsonArray arr ? arr : null)
                .orElse(new JsonArray());
        IntStream.range(0, failed.size()).mapToObj(failed::get)
                .flatMap(op -> op.map(v -> v instanceof JsonString js ? js.value() : null).stream())
                .forEach(v -> fields.put(v, JsonValue.NULL));
        if (args.length != fields.size()) {
            throw new AnnotationFormatError("Expected same number of args and stored fields");
        }
        for (int argInd = 0; argInd < args.length; argInd++) {
            Class<?> argType = deserialMeth.getParameterTypes()[argInd];
            String argName = deserialMeth.getParameters()[argInd].getName();
            args[argInd] = cast(
                    fromJson(fields.get(argName).orElseThrow(
                            () -> new IllegalArgumentException("Could not find field:" + argName))),
                    argType);
        }
        if (deserialMeth instanceof Constructor cons) {
            try {
                return cons.newInstance(args);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (deserialMeth instanceof Method meth) {
            try {
                return meth.invoke(null, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                throw new AnnotationFormatError(
                        "Deserialization method should be static or a constructor");
            }
        }
        return null;
    }
}