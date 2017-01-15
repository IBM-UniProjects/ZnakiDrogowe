package com.amalisie.znakidrogowe;

import android.content.Context;
import android.widget.Button;

/**
 * Image tile class for Memory game.
 * Extends Button.
 * Implements Tile.
 * Works like a button with assigned road sign.
 * @author Amadeusz Lisiecki
 */
public class ImgTile extends Button implements Tile {

    private RoadSign roadSign;
    private boolean covered = true;
    private boolean paired = false;

    /**
     * Constructor.
     * @param context
     */
    public ImgTile(Context context) {
        super(context);
    }

    /**
     * Overrides uncover from Tile.
     * Sets background to road sign image.
     */
    @Override
    public void uncover() {
        this.setBackground(roadSign.img);
        this.covered = false;
    }

    /**
     * Overrides cover from Tile.
     * Sets background to default button background.
     */
    @Override
    public void cover() {
        this.setBackgroundResource(android.R.drawable.btn_default);
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
