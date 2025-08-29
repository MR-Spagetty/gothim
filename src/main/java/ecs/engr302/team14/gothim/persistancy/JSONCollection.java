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
public abstract class JSONCollection<K> implements JSONType {
    public abstract Optional<JSONType> get(K position);

    public abstract Optional<JSONType> remove(K position);

    public abstract int size();

    public String prettyPrint() {
        return prettyPrint(0);
    }

    protected abstract String prettyPrint(int indentationLevel);

    /**
     * Prints this JSON Collection to file.
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

    protected static final Map.Entry<JSONType, Integer> parseItem(String jsonData) {
        JSONType item;
        int off;
        if (JSONValue.isNextNull(jsonData)) {
            var ret = JSONValue.parseNull(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JSONBool.isNext(jsonData)) {
            var ret = JSONBool.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JSONNum.isNext(jsonData)) {
            var ret = JSONNum.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JSONString.isNext(jsonData)) {
            var ret = JSONString.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JSONObject.isNext(jsonData)) {
            var ret = JSONObject.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else if (JSONArray.isNext(jsonData)) {
            var ret = JSONArray.parse(jsonData);
            item = ret.getKey();
            off = ret.getValue();
        } else {
            throw new IllegalArgumentException(
                    "Could not parse next item in JSON collection: \"%s\"".formatted(jsonData));
        }
        return Map.entry(item, off);
    }

    protected abstract boolean containsExactly(JSONType jsonItem);
}
