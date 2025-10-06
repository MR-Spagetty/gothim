package ecs.engr302.team14.gothim.map;

import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;

import java.util.*;

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
        List<PrimitiveTile> ls = new ArrayList<>();
        for (double x = topleft.x(); x <= bottomRight.x(); x++) {
            for (double y = topleft.y(); y <= bottomRight.y(); y++) {
                //get an option from the board and add to list, otherwise do nothing
                Optional.ofNullable(board.get(new Point(x, y))).ifPresent(ls::add);
            }
        }
        return ls;
    }

    public Collection<PrimitiveTile> getAllTiles() {
        return board.values();
    }

}
