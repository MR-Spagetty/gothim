package ecs.engr302.team14.gothim.persistancy;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.HasSerializedConstants;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public static JSONType toJSON(Object thing) {
        return switch (thing) {
            case JSONType t -> t;
            case null -> JSONValue.NULL;
            case String s -> new JSONString(s);
            case Number num -> new JSONNum(num.doubleValue());
            case Boolean bool -> JSONBool.of(bool);
            case Collection<?> col -> {
                JSONArray arr = new JSONArray();
                col.stream().map(Serialization::toJSON).forEach(arr::add);
                if (!(col instanceof ArrayList<?>)) {
                    JSONObject obj = new JSONObject();
                    obj.put("kind", new JSONString("collection"));
                    obj.put("class", new JSONString(col.getClass().getName()));
                    obj.put("values", arr);
                    yield obj;
                }
                yield arr;
            }
            case Map<?, ?> map -> mapMapToJSON(map);
            default -> serializeObject(thing);
        };
    }

    private static JSONObject mapMapToJSON(Map<?, ?> map) {
        JSONObject ret = new JSONObject();

        ret.put("kind", new JSONString("map"));
        ret.put("class", new JSONString(map.getClass().getName()));
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
                ret.put("keyType", new JSONString(keyTypes.getFirst().getName()));
            } else {
                stringKey = true;
            }
        } else {
            ret.put("keyType", new JSONString(Object.class.getName()));
        }
        if (valueTypes.size() == 1) {
            ret.put("valueType", new JSONString(mapValueTypeSel(valueTypes.getFirst())));
        } else {
            ret.put("keyType", new JSONString(Object.class.getName()));
        }

        if (stringKey) {
            JSONObject values = new JSONObject();
            map.keySet().forEach(k -> values.put((String) k, toJSON(map.get(k))));
            ret.put("entries", values);
        } else {
            JSONArray values = new JSONArray();
            map.entrySet().parallelStream().map(e -> {
                JSONObject entry = new JSONObject();
                entry.put("key", toJSON(e.getKey()));
                entry.put("value", toJSON(e.getValue()));
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

    @SuppressWarnings("unchecked")
    private static <V> List<Field> fieldsToSerial(Class<V> from) {
        return Stream.of(from).parallel().<Class<? super V>>mapMulti((clazz, cons) -> {
            Class<? super V> curr = clazz;
            cons.accept(curr);
            while (curr.isAnnotationPresent(SerializationExtends.class)) {
                SerializationExtends annotation = curr.getAnnotation(SerializationExtends.class);
                if (annotation == null) {
                    curr = curr.getSuperclass();
                } else {
                    if (!annotation.value().isAssignableFrom(curr)) {
                        throw new AnnotationFormatError("Class A (" + curr.getName()
                                + ") is declared to inherit serializable fields from Class B ("
                                + annotation.value().getName()
                                + ") but Class B is not assignable from Class A");
                    }
                    if (curr.equals(annotation.value())) {
                        break;
                    }
                    curr = (Class<? super V>) annotation.value();
                }
                cons.accept(curr);
            }
        }).flatMap(clazz -> Stream.of(clazz.getDeclaredFields()))
                .filter(f -> f.isAnnotationPresent(SerializedField.class)).toList();
    }

    private static JSONObject serializeObject(Object thing) {
        Class<?> thingClass = thing.getClass();
        Optional<JSONObject> constant = serializeByConstant(thing, thingClass);
        if (constant.isPresent()) {
            return constant.get();
        }
        JSONObject ret = new JSONObject();
        JSONArray failedFields = new JSONArray();
        var fields = new JSONObject();
        ret.put("class", new JSONString(thingClass.getName()));
        ret.put("failedFields", failedFields);
        ret.put("fields", fields);
        if (Stream
                .concat(Stream.of(thingClass.getMethods()),
                        Stream.of(thing.getClass().getConstructors()))
                .parallel().noneMatch(m -> m.isAnnotationPresent(DeserializationMethod.class))) {
            throw new IllegalArgumentException("Class: %s (of supplied object is not serializable"
                    .formatted(thingClass.getName()));
        }
        List<Field> fieldsToSerial = fieldsToSerial(thingClass);
        fieldsToSerial.stream().forEach(f -> {
            try {
                f.setAccessible(true);
                String annotatedName = f.getAnnotation(SerializedField.class).deserialParamName();
                try {
                    fields.put(annotatedName.isBlank() ? f.getName() : annotatedName,
                            toJSON(f.get(thing)));
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

    private static Optional<JSONObject> serializeByConstant(Object thing, Class<?> thingClass) {
        final int staticFinal = (Modifier.STATIC | Modifier.FINAL);
        if (!thingClass.isAnnotationPresent(HasSerializedConstants.class)) {
            return Optional.empty();
        }
        JSONObject ret = new JSONObject();
        ret.put("kind", new JSONString("constant"));
        ret.put("class", new JSONString(thingClass.getName()));
        Optional<Field> constant = Stream.of(thingClass.getDeclaredFields()).parallel()
                .filter(f -> ((f.getModifiers() & staticFinal) == staticFinal)
                        && thingClass.isAssignableFrom(f.getType()))
                .filter(f -> {
                    f.setAccessible(true);
                    try {
                        return f.get(null).equals(thing);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        return false;
                    }
                }).findFirst();
        if (constant.isEmpty()) {
            return Optional.empty();
        }
        ret.put("name", new JSONString(constant.get().getName()));
        return Optional.of(ret);

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
    public static Object fromJSON(JSONType json) {
        if (json == JSONValue.NULL) {
            return null;
        }
        return switch (json) {
            case JSONString s -> s.value();
            case JSONNum n -> n.value();
            case JSONBool b -> b.value();
            case JSONArray a -> {
                List<Object> list = new ArrayList<>();
                IntStream.range(0, a.size()).<Object>mapToObj(i -> fromJSON(a.get(i).get()))
                        .forEachOrdered(list::add);
                yield list;
            }
            case JSONObject o -> deserializeFromObject(o);
            default -> throw new RuntimeException(
                    "Should not have reached here, given value was: %s".formatted(json));
        };
    }

    private static <K, V> Map<K, V> deserializeMap(JSONObject o, Class<K> keyType,
            Class<V> valueType) {
        @SuppressWarnings("unchecked")
        Class<? extends Map<K, V>> mapClass = o.get("class")
                .map(v -> v instanceof JSONString js ? js.value() : null).map(className -> {
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

        JSONCollection<?> entries = o.get("entries")
                .map(v -> v instanceof JSONCollection jc ? jc : null).orElseThrow();

        if (entries instanceof JSONArray arr) {
            IntStream.range(0, arr.size()).forEach(i -> {
                JSONObject entry = arr.get(i).map(v -> v instanceof JSONObject jo ? jo : null)
                        .orElseThrow();
                K key = cast(fromJSON(entry.get("key").orElseThrow()), keyType);
                V value = cast(fromJSON(entry.get("value").orElseThrow()), valueType);
                ret.put(key, value);
            });
        } else if (entries instanceof JSONObject obj) {
            for (String key : obj.keySet()) {
                K k = cast(key, keyType);
                V v = cast(fromJSON(obj.get(key).orElseThrow()), valueType);
                ret.put(k, v);
            }
        }

        return ret;
    }

    private static Collection<?> deserializeCollection(JSONObject o) {
        JSONArray valuesDat = o.get("values").map(v -> v instanceof JSONArray ja ? ja : null)
                .orElseThrow();
        List<Object> values = IntStream.range(0, valuesDat.size())
                .mapToObj(i -> fromJSON(valuesDat.get(i).get())).toList();
        @SuppressWarnings("unchecked")
        Class<? extends Collection<?>> colClass = o.get("class")
                .<String>map(v -> v instanceof JSONString js ? js.value() : null).map(className -> {
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

    private static Object deserializeFromObject(JSONObject o) {
        String kind = o.get("kind").map(v -> v instanceof JSONString js ? js.value() : null)
                .orElse(null);
        switch (kind) {
            case "map" -> {
                Class<?> keyType = o.get("keyType").map(v -> {
                    if (v instanceof JSONString js) {
                        return js.value();
                    }
                    throw new IllegalArgumentException(
                            "\"keyType\" field in map was not a JSONString "
                                    + "but instead a %s with value:\n%s"
                                            .formatted(o.getClass().getSimpleName(), o.toString()));
                }).map(Serialization::mapValueTypeDet).orElse(null);
                if (keyType == null) {
                    keyType = String.class;
                }
                Class<?> valueType = o.get("valueType").map(v -> {
                    if (v instanceof JSONString js) {
                        return js.value();
                    }
                    throw new IllegalArgumentException(
                            "\"valueType\" field in map was not a JSONString "
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
            case "constant" -> {
                return deserializeConstant(o);
            }

            case null -> {
                return deserializeObject(o);
            }

            default -> throw new RuntimeException("Unexpcted value of kind: %s".formatted(kind));
        }
    }

    private static Object deserializeObject(JSONObject o) {
        Class<?> thingClass = o.get("class").map(v -> {
            if (v instanceof JSONString js) {
                return js.value();
            }
            throw new IllegalArgumentException("\"class\" field in object was not a JSONString but "
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
        JSONObject fields = o.get("fields").map(v -> v instanceof JSONObject jo ? jo : null)
                .orElse(new JSONObject());
        JSONArray failed = o.get("failedFields").map(v -> v instanceof JSONArray arr ? arr : null)
                .orElse(new JSONArray());
        IntStream.range(0, failed.size()).mapToObj(failed::get)
                .flatMap(op -> op.map(v -> v instanceof JSONString js ? js.value() : null).stream())
                .forEach(v -> fields.put(v, JSONValue.NULL));
        if (args.length != fields.size()) {
            throw new AnnotationFormatError("Expected same number of args and stored fields,"
                    + " got %d, expcted %d".formatted(args.length, fields.size()));
        }
        String[] paramNames = deserialMeth.getAnnotation(DeserializationMethod.class)
                .serialFieldNames();
        for (int argInd = 0; argInd < args.length; argInd++) {
            Class<?> argType = deserialMeth.getParameterTypes()[argInd];
            String argName = paramNames[argInd];
            args[argInd] = cast(
                    fromJSON(fields.get(argName).orElseThrow(
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

    private static Object deserializeConstant(JSONObject o) {
        Class<?> thingClass = o.get("class").map(v -> {
            if (v instanceof JSONString js) {
                return js.value();
            }
            throw new IllegalArgumentException("\"class\" field in object was not a JSONString but "
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
        Field field;
        try {
            field = thingClass.getDeclaredField(((JSONString) o.get("name").get()).value());
        } catch (NoSuchFieldException e) {
            throw new AnnotationFormatError("The specified constant does not exist", e);
        }
        field.setAccessible(true);
        try {
            return field.get(null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access constant: %s.%s"
                    .formatted(thingClass.getName(), field.getName()), e);
        } catch (NullPointerException npe) {
            throw new AnnotationFormatError("Specified constant is not a constant", npe);
        }
    }
}