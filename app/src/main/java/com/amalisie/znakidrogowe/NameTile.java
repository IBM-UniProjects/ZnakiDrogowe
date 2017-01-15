package com.amalisie.znakidrogowe;

import android.content.Context;
import android.widget.Button;

/**
 * Name tile class for Memory game.
 * Extends Button.
 * Implements Tile.
 * Works like a button with assigned road sign.
 * @author Amadeusz Lisiecki
 */
public class NameTile extends Button implements Tile {

    private RoadSign roadSign;
    private boolean covered = true;
    private boolean paired = false;

    /**
     * Constructor.
     * @param context
     */
    public NameTile(Context context) {
        super(context);
    }

    /**
     * Overrides uncover from Tile.
     * Sets object text to road sign name.
     */
    @Override
    public void uncover() {
        this.setText(roadSign.name);
        this.covered = false;
    }

    /**
     * Overrides cover from Tile.
     * Removes object text.
     */
    @Override
    public void cover() {
        this.setText("");
        this.covered = true;
    }

    /**
     * roadSign getter.
     * @return roadSign
     */
    @Override
    public RoadSign getRoadSign() {
        return this.roadSign;
    }

    /**
     * roadSign setter.
     * @param roadSign
     */
    @Override
    public void setRoadSign(RoadSign roadSign) {
        this.roadSign = roadSign;
    }

    /**
     * Checks if object is covered.
     * @return covered
     */
    @Override
    public boolean isCovered() {
        return covered;
    }

    /**
     * Checks if object is paired with another.
     * @return paired
     */
    @Override
    public boolean isPaired() {
        return paired;
    }

    /**
     * Sets object as paired with another.
     */
    @Override
    public void setPaired() {
        this.paired = true;
    }
}
