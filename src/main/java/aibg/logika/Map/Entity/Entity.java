package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;

public interface Entity {

    /** Processes attempt of the player to step on the tile with this entity
     *
     * @param player that is stepping on
     * @param q
     * @param r coordinates of the tile that is being stepped on
     */
    void stepOn(Player player, Map map, int q, int r);

    /**Processes attack on this entity by attacker
     *
     * @param q
     * @param r coordinates of the tile that is being attacked
     */
    void attacked(LiveEntity attacker, Map map, int q, int r);

    // TODO remove map - map is not changed because players aren't connected to map in any way, they just store their coordinates locally

    //TODO getter and setter for coordinates;maybe make new class for coordinates



}
