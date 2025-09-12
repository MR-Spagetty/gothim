package ecs.engr302.team14.gothim.logic;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Clue holder class for storing the clues and found clues for the level.
 *
 * @author MR-Spagetty
 */
public class ClueHolder {
    @SerializedField
    private Set<Clue> clues = new HashSet<>();
    @SerializedField
    private Set<String> foundClues = new HashSet<>();

    Consumer<String> clueObserver = unused -> {
    };

    /**
     * Creates a new holder with the given clues and no clues found already.
     *
     * @param clues the clues to store in this holder
     * @see #ClueHolder(List, List)
     */
    public ClueHolder(Set<Clue> clues) {
        this(clues, Set.of());
    }

    /**
     * Creates a new Clue holder with the given clues and found clues.
     *
     * @param clues      the clues to hold
     * @param foundClues the clue ids that have already been found
     * @throws IllegalArgumentException if the found clues contains any ids that are
     *                                  not present in the clues
     */
    @DeserializationMethod(serialFieldNames = { "clues", "foundClues" })
    public ClueHolder(Set<Clue> clues, Set<String> foundClues) {
        this.clues.addAll(clues);
        List<String> invalidFound = foundClues.stream()
                .filter(id -> clues.parallelStream().anyMatch(c -> !id.equals(c.id()))).toList();
        if (!invalidFound.isEmpty()) {
            throw new IllegalArgumentException(
                    "Some clues stated ot be found do not exist:\n" + invalidFound.toString());
        }
        this.foundClues.addAll(foundClues);
    }

    /**
     * Marks the specified clue id as found.
     *
     * @param id the id of the clue to mark as found
     * @throws IllegalArgumentException if the clue id does not correspond to a clue
     */
    public void findClue(String id) {
        if (foundClues.contains(id)) {
            return;
        }
        if (clues.parallelStream().noneMatch(c -> c.id().equals(id))) {
            throw new IllegalArgumentException("Unknown clue: " + id);
        }
        foundClues.add(id);
        clueObserver.accept(id);
    }

    /**
     * sets the observer on this holder to the given observer.
     *
     * <p>Effectively equivalent to clearing then adding but slightly different.
     *
     * @param cons the new observer
     */
    public void setObserver(Consumer<String> cons) {
        this.clueObserver = Objects.requireNonNull(cons);
    }

    /**
     * Adds an additional observer to this holder.
     *
     * @param cons the new observer to add
     */
    public void addObserver(Consumer<String> cons) {
        Consumer<String> oldCons = this.clueObserver;
        this.clueObserver = c -> {
            cons.accept(c);
            oldCons.accept(c);
        };
    }

    /**
     * Clears all observers from this holder.
     */
    public void clearObservers() {
        this.clueObserver = unused -> {
        };
    }

    /**
     * Have all the clues in this holder been found.
     *
     * @return whether they have all been found
     */
    public boolean allCluesFound() {
        return clues.size() == foundClues.size();
    }

    /**
     * the number of Clues that exist in this holder that are of the Public access
     * modifier.
     *
     * @return the number of Public clues
     */
    public int totalPublic() {
        return (int) clues.parallelStream()
                .filter(c -> c.modifier() == AccessModifier.Public).count();
    }

    /**
     * the number of Clues that have been found that are of the Public access
     * modifier.
     *
     * @return the number of found Public clues
     */
    public int foundPublic() {
        return (int) clues.parallelStream()
                .filter(
                        c -> c.modifier() == AccessModifier.Public && foundClues.contains(c.id()))
                .count();
    }

    /**
     * the number of Clues that are yet to be found that are of the Public access
     * modifier.
     *
     * @return the number of yet to be found Public clues
     */
    public int remainingPublic() {
        return totalPublic() - foundPublic();
    }

    /**
     * the number of Clues that exist in this holder that are of the Private access
     * modifier.
     *
     * @return the number of Private clues
     */
    public int totalPrivate() {
        return (int) clues.parallelStream()
                .filter(c -> c.modifier() == AccessModifier.Private).count();
    }

    /**
     * the number of Clues that have been found that are of the Private access
     * modifier.
     *
     * @return the number of found Private clues
     */
    public int foundPrivate() {
        return (int) clues.parallelStream()
                .filter(
                        c -> c.modifier() == AccessModifier.Private && foundClues.contains(c.id()))
                .count();
    }

    /**
     * the number of Clues that are yet to be found that are of the Private access
     * modifier.
     *
     * @return the number of yet to be found Private clues
     */
    public int remainingPrivate() {
        return totalPrivate() - foundPrivate();
    }

    /**
     * the number of Clues that exist in this holder that are of the Static access
     * modifier.
     *
     * @return the number of Static clues
     */

    public int totalStatic() {
        return (int) clues.parallelStream()
                .filter(c -> c.modifier() == AccessModifier.Static).count();
    }

    /**
     * the number of Clues that have been found that are of the Static access
     * modifier.
     *
     * @return the number of found Static clues
     */
    public int foundStatic() {
        return (int) clues.parallelStream()
                .filter(
                        c -> c.modifier() == AccessModifier.Static && foundClues.contains(c.id()))
                .count();
    }

    /**
     * the number of Clues that are yet to be found that are of the Static access
     * modifier.
     *
     * @return the number of yet to be found Static clues
     */
    public int remainingStatic() {
        return totalStatic() - foundStatic();
    }

}
