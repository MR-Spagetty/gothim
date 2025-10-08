package ecs.engr302.team14.gothim.map;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Class for holding the map data for the game.
 */
public class Board {

    Map<Point, PrimitiveTile> board;

    Board(Map<Point, PrimitiveTile> board) {
        this.board = new HashMap<>(Objects.requireNonNull(board));
        this.board.values().parallelStream().forEach(t -> t.linkMap(this));
    }

    /**
     * Gets the tile at the given point.
     *
     * @param p the point to get the tile at
     * @return the tile at that point
     */
    public PrimitiveTile getTile(Point p) {
        return board.get(p);
    }

    /**
     * Gets all tiles within the given rectangle.
     *
     * @param topleft the top left Point of the rectangle
     * @param bottomRight the bottom right Point of the rectangle
     * @return a list of the tiles within that rectangle
     */
    public List<PrimitiveTile> getTiles(Point topleft, Point bottomRight) {
        return IntStream.range((int) topleft.x(), (int) bottomRight.x() + 1).boxed().parallel()
                .flatMap(x -> IntStream.range((int) bottomRight.y(), (int) topleft.y()).boxed()
                        .parallel().map(y -> {
                            Point pos = new Point(x, y);
                            return Optional.ofNullable(getTile(pos)).orElse(new FogTile(pos));
                        }))
                .sorted((a, b) -> a.pos().compareTo(b.pos())).toList();
    }

    /**
     * Gets all the tiles within the square of 2*range + 1 centred on center.
     *
     * @param center the center point of the square
     * @param range the "radius" of the square
     * @return the tiles within the square
     */
    public List<PrimitiveTile> getTiles(Point center, int range) {
        Point p = new Point(-range, range);
        return getTiles(center.add(p), center.sub(p));
    }

    public Collection<PrimitiveTile> getAllTiles() {
        return board.values();
    }

    private class FogTile extends PrimitiveTile {

        public FogTile(Point pos) {
            super(pos, "fog");
        }

        @Override
        public void enter(Entity e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setOccupant(Entity ocupant) {
            throw new UnsupportedOperationException();
        }

    }

}
