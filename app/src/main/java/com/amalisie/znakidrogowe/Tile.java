package com.amalisie.znakidrogowe;

/**
 * Tile interface for name and image tiles.
 * @author Amadeusz Lisiecki
 */
public interface Tile {

    /**
     * Uncovers tile.
     */
    public void uncover();

    /**
     * Covers tile.
     */
    public void cover();

    /**
     * roadSign getter.
     * @return roadSign.
     */
    public RoadSign getRoadSign();

    /**
     * roadSign setter.
     * @param roadSign
     */
    public void setRoadSign(RoadSign roadSign);

    /**
     * Checks if tile is covered.
     * @return covered
     */
    public boolean isCovered();

    /**
     * Checks if tile is paired with another.
     * @return paired
     */
    public boolean isPaired();

    /**
     * Sets tile as paired with another.
     */
    public void setPaired();
}
