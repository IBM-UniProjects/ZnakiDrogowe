package com.amalisie.znakidrogowe;

import android.content.Context;
import android.widget.Button;

public class NameTile extends Button {

    public RoadSign roadSign;

    public NameTile(Context context) {
        super(context);
    }

    public void uncover() {
        this.setText(roadSign.name);
    }
}
