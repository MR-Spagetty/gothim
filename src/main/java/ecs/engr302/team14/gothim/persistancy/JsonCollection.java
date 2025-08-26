package ecs.engr302.team14.gothim.persistancy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * a basic interface to lump all JSON collection types (array, object) together
 * for ease of differentiation.
 *
 * @param <K> the "key" type of the collection
 */
public abstract class JsonCollection<K> implements JsonType {
    public abstract Optional<JsonType> get(K position);

    public abstract Optional<JsonType> remove(K position);

    public abstract int size();

    public String prettyPrint() {
        return prettyPrint(0);
    }

    protected abstract String prettyPrint(int indentationLevel);

    /**
     * Prints this Json Collection to file.
     *
     * @param file the file to print this data to
     * @throws FileNotFoundException if the file cannot be written to
     */
    public final void printToFile(File file) throws FileNotFoundException {
        try (var ps = new PrintStream(file)) {
            printToStream(ps);
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    public final void printToStream(PrintStream stream) {
        stream.print(prettyPrint());
    }

    static String indent(int level) {
        return "    ".repeat(level);
    }

    protected static final int parseItemSep(String jsonData) {
        final Pattern pat = Pattern.compile("^\\s*,");
        var matcher = pat.matcher(jsonData);
        if (matcher.find()) {
            return matcher.end();
        } else {
            return -1;
        }
    }

    protected static final Map.Entry<JsonType, Integer> parseItem(String jsonData) {
        JsonType item;
        int off;
        if (JsonValue.isNextNull(jsonData)) {
            var ret = JsonValue.parseNull(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JsonBool.isNext(jsonData)) {
            var ret = JsonBool.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JsonNum.isNext(jsonData)) {
            var ret = JsonNum.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JsonString.isNext(jsonData)) {
            var ret = JsonString.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JsonObject.isNext(jsonData)) {
            var ret = JsonObject.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JsonArray.isNext(jsonData)) {
            var ret = JsonArray.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else {
            throw new IllegalArgumentException(
                    "Could not parse next item in JSON collection: \"%s\"".formatted(jsonData));
        }
        return Map.entry(item, off);
    }

    protected abstract boolean containsExactly(JsonType jsonItem);
}
