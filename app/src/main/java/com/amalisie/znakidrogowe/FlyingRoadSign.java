package com.amalisie.znakidrogowe;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Random;

public class FlyingRoadSign extends Button {

    private RoadSign roadSign;
    private int angle;
    private float sina;
    private float cosa;
    private float speed;
    private float dX;
    private float dY;

    public RoadSign getRoadSign() {
        return this.roadSign;
    }

    public void setRoadSign(RoadSign roadSign) {
        this.roadSign = roadSign;
    }

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

    public void fly()
    {
        ViewGroup parent = (ViewGroup) getParent();
        System.out.println("Angle: " + this.angle);
        float newX = this.getX() + this.dX;
        float newY = this.getY() + this.dY;
        if (newX > parent.getWidth())
            newX = 0 - this.getWidth();
        if ((newX + this.getWidth()) < 0)
            newX = parent.getWidth();
        if (newY > parent.getHeight())
            newY = 0;
        if (newY < 0)
            newY = parent.getHeight();
        this.setX(newX);
        this.setY(newY);

    }
}
