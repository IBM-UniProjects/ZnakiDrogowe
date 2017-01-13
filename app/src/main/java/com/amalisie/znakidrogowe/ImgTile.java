package com.amalisie.znakidrogowe;

import android.content.Context;
import android.widget.Button;

public class ImgTile extends Button {

    public RoadSign roadSign;

    public ImgTile(Context context) {
        super(context);
    }

    public void uncover() {
        this.setBackground(roadSign.img);
    }
}
