package com.amalisie.znakidrogowe;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Road sign class for Memory and Arcade games.
 * Implements Serializable.
 * @author Amadeusz Lisiecki
 */
public class RoadSign implements Serializable {

    public String name;
    public String desc;
    public Drawable img;
    public String group;

    /**
     * Constructor.
     * Sets everything to null.
     */
    public RoadSign(){
        this.name = null;
        this.desc = null;
        this.img = null;
        this.group = null;
    }

    /**
     * Sets object image.
     * @param context
     */
    public void setImg(Context context) {
        String img = name.split("-")[0].toLowerCase()
                + name.split("-")[1].toLowerCase()
                + ".png";
        this.img = Utils.getImageFromAssets(context, img);
    }

    /**
     * Clears object image.
     */
    public void clearImg() {
        img = null;
    }
}
