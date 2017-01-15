package com.amalisie.znakidrogowe;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;

/**
 * Class for flying objects in Arcade Game.
 * Extends Button.
 * @author Amadeusz Lisiecki
 */
public class FlyingRoadSign extends Button {

    private RoadSign roadSign;
    private int angle;
    private float sina;
    private float cosa;
    private float speed;
    private float dX;
    private float dY;

    /**
     * RoadSign getter.
     * @return roadSign
     */
    public RoadSign getRoadSign() {
        return this.roadSign;
    }

    /**
     * RoadSign setter.
     * @param roadSign
     */
    public void setRoadSign(RoadSign roadSign) {
        this.roadSign = roadSign;
    }

    /**
     * Constructor.
     * Takes Context.
     * Sets random movement angle.
     * Calculates shift in X and Y for angle.
     * @param context
     */
    public FlyingRoadSign(Context context) {
        super(context);

        Random random = new Random();

        this.angle = random.nextInt(360);
        this.speed = 40.0f;

        this.sina = (float) Math.sin(Math.toRadians(this.angle));
        this.cosa = (float) Math.cos(Math.toRadians(this.angle));

        this.dX = this.speed * this.cosa;
        this.dY = this.speed * this.sina;
    }

    /**
     * Moves object by calculated shift in X and Y.
     * Object leaving the display field returns on the other side.
     */
    public void fly()
    {
        ViewGroup parent = (ViewGroup) getParent();
        float newX = this.getX() + this.dX;
        float newY = this.getY() + this.dY;
        if (newX > parent.getWidth())
            newX = 0 - this.getWidth();
        if ((newX + this.getWidth()) < 0)
            newX = parent.getWidth();
        if (newY > parent.getHeight())
            newY = 0 - this.getHeight();
        if ((newY + this.getHeight()) < 0)
            newY = parent.getHeight();
        this.setX(newX);
        this.setY(newY);

    }

    /**
     * Removes caught object from the game.
     */
    public void vanish() {
        ViewGroup parent = (ViewGroup) getParent();
        ArcadeGameActivity arcadeGameActivity = (ArcadeGameActivity) this.getContext();
        parent.removeView(this);
        arcadeGameActivity.flyingRoadSigns.remove(this);
    }
}
