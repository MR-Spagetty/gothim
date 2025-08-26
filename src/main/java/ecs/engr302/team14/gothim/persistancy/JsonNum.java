package ecs.engr302.team14.gothim.persistancy;

/**
 * Record for storing numbers in JSON.
 *
 * @param value the numeric value of this object
 * @author MR-Spagetty
 */
public record JsonNum(Double value) implements JsonValue<Double> {

    @Override
    public final String toString() {
        return "" + value;
    }

}
