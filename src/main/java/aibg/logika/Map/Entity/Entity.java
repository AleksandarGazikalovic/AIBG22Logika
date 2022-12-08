package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;

/*@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Blackhole.class, name = "BLACKHOLE"),
        @JsonSubTypes.Type(value = Empty.class, name = "EMPTY"),
        @JsonSubTypes.Type(value = Asteroid.class, name = "FENCE"),
        @JsonSubTypes.Type(value = Wormhole.class, name = "WORMHOLE"),
}
)*/
public interface Entity {

    /** Processes attempt of the player to step on the tile with this entity
     *
     * @param player that is stepping on
     * @param q
     * @param r coordinates of the tile that is being stepped on
     */
    void stepOn(Player player, Game game, int q, int r);

    /**
     * Processes attack on this entity by attacker
     *
     * @param game
     * @param q
     * @param r    coordinates of the tile that is being attacked
     */
    void attacked(Entity attacker, Game game, int q, int r);

    // TODO remove map - map is not changed because players aren't connected to map in any way, they just store their coordinates locally

    //TODO getter and setter for coordinates;maybe make new class for coordinates



}
