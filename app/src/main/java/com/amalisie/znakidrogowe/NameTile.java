package com.amalisie.znakidrogowe;

import android.content.Context;
import android.widget.Button;

public class NameTile extends Button implements Tile {

    private RoadSign roadSign;
    private boolean covered = true;
    private boolean paired = false;

    public NameTile(Context context) {
        super(context);
    }

    @Override
    public void uncover() {
        this.setText(roadSign.name);
        this.covered = false;
    }

    @Override
    public void cover() {
        this.setText("");
        this.covered = true;
    }

    @Override
    public RoadSign getRoadSign() {
        return this.roadSign;
    }

    @Override
    public void setRoadSign(RoadSign roadSign) {
        this.roadSign = roadSign;
    }

    @Override
    public boolean isCovered() {
        return covered;
    }

    @Override
    public boolean isPaired() {
        return paired;
    }

    @Override
    public void setPaired() {
        this.paired = true;
    }
}
