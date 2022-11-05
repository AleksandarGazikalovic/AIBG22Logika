package aibg.logika.Map.Entity;

import aibg.logika.Map.Map;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/*@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Blackhole.class, name = "BLACKHOLE"),
        @JsonSubTypes.Type(value = Empty.class, name = "EMPTY"),
        @JsonSubTypes.Type(value = Fence.class, name = "FENCE"),
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
    void stepOn(Player player, Map map, int q, int r);

    /**Processes attack on this entity by attacker
     *
     * @param q
     * @param r coordinates of the tile that is being attacked
     */
    void attacked(Entity attacker, Map map, int q, int r);

    // TODO remove map - map is not changed because players aren't connected to map in any way, they just store their coordinates locally

    //TODO getter and setter for coordinates;maybe make new class for coordinates



}
